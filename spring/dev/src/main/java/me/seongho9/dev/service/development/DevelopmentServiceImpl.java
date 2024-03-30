package me.seongho9.dev.service.development;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import me.seongho9.dev.domain.container.Container;
import me.seongho9.dev.domain.development.ContainerPort;
import me.seongho9.dev.domain.development.dto.CreateDevDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

@Service
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
        Queue<Function<String, String>> q = new LinkedList<>();
        Queue<String> params = new LinkedList<>();
        try {
            //make host volume
            serverService.mkdir(volumePath+"/"+containerName);
            Function<String, String> undoVolume = (String param) ->{
                serverService.rm(volumePath+"/"+param);
                return param;
            };
            params.add(containerName);
            taskManager.registerTask(q, undoVolume);

            //create container
            Integer port = memberRepository.findById(createDTO.getUserId()).get().getPorts().getStart()
                    + getContainerPort(createDTO.getImageName()).ordinal();

            String id = containerService.createContainer(containerName, "seongho9/" + createDTO.getImageName(),
                    port, volumePath + "/" + containerName);

            Function<String, String> undoContainer = (String param) ->{
                containerService.deleteContainer(param);
                return param;
            };
            taskManager.registerTask(q, undoContainer);
            params.add(id);

            //add repository
            Container container = new Container(id, createDTO.getUserId(), containerName, port);
            containerRepository.save(container);

        } catch (NoSuchElementException e){
            taskManager.undo(q, params);
            throw new MemberNotFoundException(e);
        } catch (ConflictException e) {
            taskManager.undo(q, params);
            throw new ContainerConflictException("Conflict Container " + createDTO.getImageName(), e.getCause());
        } finally {
            q.clear();
            params.clear();
        }
    }

    @Override
    @Transactional
    public void deleteEnvironment(String containerId, String imageName) {
        Queue<Function<String, String>> q = new LinkedList<>();
        Queue<String> params = new LinkedList<>();
        try {
            //get Domain data
            Container container = containerRepository.findById(containerId).get();
            String containerName = getContainerName(container.getUserId(), imageName);

            //delete container
            containerService.deleteContainer(containerId);
            Function<String, String> undoContainer = (String param) -> {
                String id = containerService.createContainer(
                        container.getName(), imageName, container.getPort(), containerName
                );
                return id;
            };
            taskManager.registerTask(q, undoContainer);
            params.add("");

            //delete directory
            serverService.rm(volumePath + "/" + containerName);
            Function<String, String> undoDir = (String param) ->{
                serverService.mkdir(param);
                return param;
            };
            taskManager.registerTask(q, undoDir);
            params.add(volumePath + "/" + containerName);

            containerRepository.delete(container);

        } catch (NotFoundException e) {
            taskManager.undo(q, params);
            throw new ContainerNotFoundException("Not Found Container " + containerId, e.getCause());
        } catch (ConflictException e) {
            taskManager.undo(q, params);
            throw new ContainerConflictException("Container is Running", e.getCause());
        } catch (NoSuchElementException e){
            taskManager.undo(q, params);
            throw new MemberNotFoundException(e);

        } finally {
            q.clear();
            params.clear();
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
    public List<Container> getEnvironmentList(String userId) {
        try {
            List<Container> containers = containerRepository.findContainerListByUserId(userId).get();

            return containers;
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
        if(imageName.substring(0,imageName.indexOf(":")).equals("Java")){
            return ContainerPort.JAVA;
        }
        else if(imageName.substring(0,imageName.indexOf(":")).equals("C++") || imageName.substring(0,1).equals("C")){
            return ContainerPort.CPP;
        }
        else if(imageName.substring(0,imageName.indexOf(":")).equals("javascript")){
            return ContainerPort.JAVASCRIPT;
        }
        else if(imageName.substring(0,imageName.indexOf(":")).equals("rust")){
            return ContainerPort.RUST;
        }
        else if (imageName.substring(0,imageName.indexOf(":")).equals("python")) {
            return ContainerPort.PYTHON;
        }
        else{
            return ContainerPort.CODE;
        }
    }
}
