package me.seongho9.dev.service.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.excepction.container.ContainerConflictException;
import me.seongho9.dev.excepction.container.ContainerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ContainerServiceDocker implements ContainerService {

    @Autowired
    private final DockerClient dockerClient;

    @Override
    public String createContainer(String name, String imageName, Integer port, String volPath) throws ConflictException {

        HostConfig hostConfig = HostConfig.newHostConfig()
                .withPortBindings(PortBinding.parse(port.toString() + ":8888"))
                .withBinds(Bind.parse(volPath + ":/src"));

        String id = dockerClient.createContainerCmd(imageName)
                .withName(name)
                .withHostConfig(hostConfig).exec()
                .getId();
        return id;
    }
    @Override
    public void deleteContainer(String containerId) throws NotFoundException, ConflictException {
        dockerClient.removeContainerCmd(containerId).exec();
    }
    @Override
    public void startContainer(String containerId) {
        try {
            dockerClient.startContainerCmd(containerId).exec();
        } catch (NotFoundException e) {
            throw new ContainerNotFoundException("Not Found Container " + containerId, e.getCause());
        } catch (NotModifiedException e){
            throw new ContainerConflictException("Container is already started", e.getCause());
        }
    }
    @Override
    public void stopContainer(String containerId) {
        try {
            dockerClient.stopContainerCmd(containerId).exec();
        } catch (NotFoundException e) {
            throw new ContainerNotFoundException("Not Found Container " + containerId, e.getCause());
        } catch (NotModifiedException e){
            throw new ContainerConflictException("Container is not Running", e.getCause());
        }
    }

    @Override
    public boolean healthContainer(String containerId) {
        InspectContainerResponse inspect = dockerClient.inspectContainerCmd(containerId).exec();
        log.info("inspect info {}", inspect.toString());
        if(!inspect.equals(null)){
            return inspect.getState().getRunning();
        }
        else{
            throw new ContainerNotFoundException("Not Found Container "+containerId);
        }
    }

    @Override
    public List<String> getImageList() {
        List<String> imageList = new ArrayList<>();
        List<Image> execImages = dockerClient.listImagesCmd().exec();

        for (Image execImage : execImages) {
            for (String imageName : Arrays.stream(execImage.getRepoTags()).toList()) {
                Integer splitIdx = imageName.indexOf("/");
                if(splitIdx.equals(-1)){
                    continue;
                }
                if(imageName.substring(0, splitIdx).equals("seongho9")){
                    imageList.add(imageName.substring(splitIdx+1));
                }
            }
        }
        return imageList;
    }
}
