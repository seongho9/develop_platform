package me.seongho9.dev.service.development;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.domain.container.Container;
import me.seongho9.dev.domain.development.ContainerPort;
import me.seongho9.dev.domain.development.dto.CreateDevDTO;
import me.seongho9.dev.domain.development.vo.Environment;
import me.seongho9.dev.excepction.member.MemberNotFoundException;
import me.seongho9.dev.excepction.container.ContainerConflictException;
import me.seongho9.dev.excepction.container.ContainerNotFoundException;
import me.seongho9.dev.repository.ContainerRepository;
import me.seongho9.dev.repository.MemberRepository;
import me.seongho9.dev.service.container.ContainerService;
import me.seongho9.dev.service.development.task.TaskManager;
import me.seongho9.dev.service.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@Slf4j
public class DevelopmentServiceImpl implements DevelopmentService {

    @Autowired
    ContainerService containerService;
    @Autowired
    ServerService serverService;
    @Autowired
    ContainerRepository containerRepository;
    @Autowired
    TaskManager taskManager;
    @Autowired
    MemberRepository memberRepository;
    @Value("${host.volume}")
    String volumePath;

    @Override
    @Transactional
    public void createEnvironment(CreateDevDTO createDTO) {
        String containerName = getContainerName(createDTO.getUserId(), createDTO.getImageName());
        ThreadLocal<Queue<Function<String, String>>> rollback = ThreadLocal.withInitial(LinkedList::new);
        try {
            //make host volume
            serverService.mkdir(volumePath+"/"+containerName);
            Function<String, String> undoMkdir= (String param) -> {
                serverService.rm(volumePath+"/"+containerName);
                return "";
            };
            rollback.get().add(undoMkdir);

            //create container
            Integer port = memberRepository.findById(createDTO.getUserId()).get().getPorts().getStart()
                    + getContainerPort(createDTO.getImageName()).ordinal();
            log.info("ordinal {}", getContainerPort(createDTO.getImageName()).ordinal());
            log.info("port {}", port);
            String id = containerService.createContainer(containerName, "seongho9/" + createDTO.getImageName(),
                    port, volumePath + "/" + containerName);

            Function<String, String> undoContainer = (String param)->{
                containerService.deleteContainer(id);
                return "";
            };
            rollback.get().add(undoContainer);

            //add repository
            Container container = new Container(id, createDTO.getUserId(), containerName, port);
            containerRepository.save(container);

        } catch (NoSuchElementException e){
            taskManager.undo(rollback.get());
            throw new MemberNotFoundException(e);
        } catch (ConflictException e) {
            taskManager.undo(rollback.get());
            throw new ContainerConflictException("Conflict Container " + createDTO.getImageName(), e.getCause());
        } finally {
            rollback.get().clear();
        }
    }

    @Override
    @Transactional
    public void deleteEnvironment(String containerId, String imageName) {

        ThreadLocal<Queue<Function<String, String>>> rollback = ThreadLocal.withInitial(LinkedList::new);
        try {
            //get Domain data
            Container container = containerRepository.findById(containerId).get();
            String containerName = container.getName();

            //delete container
            containerService.deleteContainer(containerId);
            Function<String, String> undoContainer = (String param) ->{
                String id = containerService.createContainer(
                        container.getName(), imageName, container.getPort(), containerName
                );
                return id;
            };
            rollback.get().add(undoContainer);

            //delete directory
            serverService.rm(volumePath + "/" + containerName);

            Function<String, String> undoDir = (String param) ->{
                serverService.mkdir(volumePath+"/"+containerName);
                return volumePath+"/"+containerName;
            };
            rollback.get().add(undoDir);

            containerRepository.delete(container);

        } catch (NotFoundException e) {
            taskManager.undo(rollback.get());
            throw new ContainerNotFoundException("Not Found Container " + containerId, e.getCause());
        } catch (ConflictException e) {
            taskManager.undo(rollback.get());
            throw new ContainerConflictException("Container is Running", e.getCause());
        } catch (NoSuchElementException e){
            taskManager.undo(rollback.get());
            throw new MemberNotFoundException(e);

        } finally {
            rollback.get().clear();
        }
    }

    @Override
    public void startEnvironment(String containerId) {

        containerService.startContainer(containerId);
    }

    @Override
    public void stopEnvironment(String containerId) {
        containerService.stopContainer(containerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Environment> getEnvironmentList(String userId) {
        try {
            Integer cnt = containerRepository.countContainerByUserId(userId);
            log.info("{}", cnt);
            if(cnt==0){
                return new ArrayList<>();
            }

            Optional<List<Container>> optional = containerRepository.findContainerListByUserId(userId);

            List<Container> containers = optional.get();

            List<Environment> envList = new ArrayList<>();
            containers.forEach((container)->{
                boolean isRunning = containerService.healthContainer(container.getId());
                Environment env = new Environment(container.getId(), container.getUserId(), container.getName(), isRunning);
                envList.add(env);
            });
            return envList;
        } catch (NoSuchElementException e){
            throw new MemberNotFoundException(e);
        }
    }

    @Override
    public List<String> getImageList() {
        List<String> imageList = containerService.getImageList();
        return imageList;
    }

    private String getContainerName(String userId, String imageName){
        return userId + "-" +
                imageName.substring(imageName.indexOf("/")+1).replace(":","-");
    }

    private ContainerPort getContainerPort(String imageName){
        log.info("image {}", imageName);
        String image = imageName.substring(0, imageName.indexOf(":"));
        log.info("image Name : {}", image);
        if(image.equals("java")){
            return ContainerPort.JAVA;
        }
        else if(image.equals("cpp") || image.equals("c")){
            return ContainerPort.CPP;
        }
        else if(image.equals("javascript")){
            return ContainerPort.JAVASCRIPT;
        }
        else if(image.equals("rust")){
            return ContainerPort.RUST;
        }
        else if (image.equals("python")) {
            return ContainerPort.PYTHON;
        }
        else{
            return ContainerPort.CODE;
        }
    }
}
