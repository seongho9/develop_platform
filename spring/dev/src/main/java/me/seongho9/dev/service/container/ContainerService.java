package me.seongho9.dev.service.container;

import me.seongho9.dev.domain.container.dto.InfoDTO;
import me.seongho9.dev.domain.development.dto.CreateDevDTO;

import java.util.List;

public interface ContainerService {

    String createContainer(String name, String imageName, Integer port, String volPath);

    void deleteContainer(String containerId);

    void startContainer(String containerId);

    void stopContainer(String containerId);

    boolean healthContainer(String containerId);
    List<String> getImageList();
}
