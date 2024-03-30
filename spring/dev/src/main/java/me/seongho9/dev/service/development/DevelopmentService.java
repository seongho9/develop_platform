package me.seongho9.dev.service.development;

import me.seongho9.dev.domain.container.Container;
import me.seongho9.dev.domain.development.dto.CreateDevDTO;

import java.util.List;

public interface DevelopmentService {

    public void createEnvironment(CreateDevDTO createDTO);
    public void deleteEnvironment(String containerId, String imageName);
    public void startEnvironment(String containerId);
    public void stopEnvironment(String containerId);
    public List<Container> getEnvironmentList(String userId);
    public List<String> getImageList();
}
