package me.seongho9.dev.service.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.excepction.container.ContainerConflictException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class ContainerServiceDockerTest {
    @Autowired
    ContainerService containerService;
    @Autowired
    DockerClient dockerClient;
    @Test
    void createContainer() {
        //given
        String imageName = "seongho9/cpp-code:latest";
        //when
        String id = containerService.createContainer(
                "test",imageName, 8080, "/home/seongho"
        );
        //then
        log.info("id return {}", id);
        Assertions.assertThat(
                dockerClient.inspectContainerCmd(id).exec().getState().getRunning())
                .isEqualTo(false);
        //after
        dockerClient.removeContainerCmd(id).exec();
    }
    @Test
    void createContainerNameIsDuplicated(){
        //given
        String imageCpp = "seongho9/cpp-code:latest";
        String imageJava = "seongho9/openjdk-code:17";
        String name = "test";
        Integer port = 8080;
        String vol = "/home/seongho";
        //when
        String id = containerService.createContainer(
                name, imageCpp, 8080, vol + "/cpp"
        );
        //then
        Assertions.assertThatThrownBy(()->containerService.createContainer(name, imageJava, 8081, vol+"/java"))
                .isInstanceOf(ContainerConflictException.class);
        //after
        dockerClient.removeContainerCmd(id);
    }
    @Test
    void deleteContainer() {
        //given
        String imageName = "seongho9/cpp-code:latest";
        String id = containerService.createContainer(
                "test",imageName, 8080, "/home/seongho"
        );
        //when
        containerService.deleteContainer(id);
        //then
        Assertions.assertThatThrownBy(()->
                        dockerClient.inspectContainerCmd(id).exec()).isInstanceOf(NotFoundException.class);
    }
    @Test
    void deleteInvalidContainer() {
        //given
        String id = "invalidId";
        //then
        Assertions.assertThatThrownBy(()->containerService.deleteContainer(id))
                .isInstanceOf(NotFoundException.class);
    }
    @Test
    void deleteRunningContainer() {
        //given
        String imageCpp = "seongho9/cpp-code:latest";
        String name = "test";
        Integer port = 8080;
        String vol = "/home/seongho";
        String id = containerService.createContainer(name, imageCpp, port, vol);
        containerService.startContainer(id);
        //then
        Assertions.assertThatThrownBy(() -> containerService.deleteContainer(id))
                .isInstanceOf(ContainerConflictException.class);
        //after
        containerService.stopContainer(id);
        containerService.deleteContainer(id);
    }
    @Test
    void startAndStopContainer(){
        //given
        String imageName = "seongho9/cpp-code:latest";
        String id = containerService.createContainer(
                "test",imageName, 8080, "/home/seongho"
        );
        //when
        containerService.startContainer(id);
        //then-start
        Assertions.assertThat(
                dockerClient.inspectContainerCmd(id).exec().getState().getRunning()
        ).isEqualTo(true);
        //then-stop
        containerService.stopContainer(id);
        Assertions.assertThat(
                dockerClient.inspectContainerCmd(id).exec().getState().getRunning()
        ).isEqualTo(false);
        //after
        containerService.deleteContainer(id);
    }
    @Test
    void startContainerDuplicate() {
        //given
        String imageCpp = "seongho9/cpp-code:latest";
        String name = "test";
        Integer port = 8080;
        String vol = "/home/seongho";
        String id = containerService.createContainer(name, imageCpp, port, vol);
        //when
        containerService.startContainer(id);
        //then
        Assertions.assertThatThrownBy(() -> containerService.startContainer(id))
                .isInstanceOf(ContainerConflictException.class);
        //after
        containerService.stopContainer(id);
        containerService.deleteContainer(id);
    }
    @Test
    void stopContainerNotRunning() {
        //given
        String imageCpp = "seongho9/cpp-code:latest";
        String name = "test";
        Integer port = 8080;
        String vol = "/home/seongho";
        String id = containerService.createContainer(name, imageCpp, port, vol);
        //then
        Assertions.assertThatThrownBy(() -> containerService.stopContainer(id))
                .isInstanceOf(ContainerConflictException.class);
        //after
        containerService.deleteContainer(id);
    }
    @Test
    void infoContainer(){
        //given
        String imageName = "seongho9/cpp-code:latest";
        String name = "test";
        Integer port = 8080;
        String vol = "/home/seongho";

        //when
        String id = containerService.createContainer(
                name, imageName, port, vol
        );
        containerService.startContainer(id);
        InspectContainerResponse exec = dockerClient.inspectContainerCmd(id).exec();
        ContainerConfig config = exec.getConfig();

        List<String> ports = new ArrayList<>();
        NetworkSettings networkSettings = exec.getNetworkSettings();
        Ports ports1 = networkSettings.getPorts();
        Map<ExposedPort, Ports.Binding[]> bindings = ports1.getBindings();

        Assertions.assertThat(config.getImage()).isEqualTo(imageName);
        Assertions.assertThat(exec.getName().substring(1)).isEqualTo(name);
        Assertions.assertThat(bindings.get(ExposedPort.parse("8888/tcp"))).contains(
                Ports.Binding.parse("0.0.0.0:8080")
        );

        //after
        containerService.stopContainer(id);
        containerService.deleteContainer(id);
    }
    @Test
    void infoIsNotRunningContainer(){
        //given
        String imageCpp = "seongho9/cpp-code:latest";
        String name = "test";
        Integer port = 8080;
        String vol = "/home/seongho";
        String id = containerService.createContainer(name, imageCpp, port, vol);
        //when
        boolean health = containerService.healthContainer(id);
        //then
        Assertions.assertThat(health).isEqualTo(false);
        //after
        containerService.deleteContainer(id);
    }
    @Test
    void healthContainer(){
        //given
        String imageName = "seongho9/cpp-code:latest";
        String name = "test";
        Integer port = 8080;
        String vol = "/home/seongho";

        //when
        String id = containerService.createContainer(
                name, imageName, port, vol
        );
        containerService.startContainer(id);
        //then
        Assertions.assertThat(containerService.healthContainer(id)).isEqualTo(true);
        //after
        containerService.stopContainer(id);
        containerService.deleteContainer(id);
    }

    @Test
    void getImageList(){
        List<String> imageList = containerService.getImageList();
        for (String id : imageList) {
            log.info("{}", id);
        }
    }
}