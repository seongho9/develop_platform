package me.seongho9.dev.service.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.domain.member.dto.SignupDTO;
import me.seongho9.dev.excepction.container.ContainerConflictException;
import me.seongho9.dev.repository.ContainerRepository;
import me.seongho9.dev.repository.MemberRepository;
import me.seongho9.dev.repository.PortRepository;
import me.seongho9.dev.service.member.MemberService;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ContainerRepository containerRepository;
    @Autowired
    PortRepository portRepository;
    @BeforeEach
    void beforeEach(){
        //member insert
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setName("seongho_jang");
        signupDTO.setPasswd("1234");
        signupDTO.setUserId("seongho9");
        signupDTO.setMail("seongho9@gmail.com");
        memberService.signup(signupDTO);
    }
    @AfterEach
    void AfterEach(){
        //delete all
        memberRepository.deleteAll();
        portRepository.deleteAll();
        containerRepository.deleteAll();
    }
    @Test
    void createContainer() {
        //given
        String imageName = "seongho9/cpp:latest";
        //when
        String id = containerService.createContainer(
                "test1",imageName, 8080, "/home/seongho/test"
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
        String imageCpp = "seongho9/cpp:latest";
        String imageJava = "seongho9/java:17";
        String name = "test2";
        Integer port = 8080;
        String vol = "/home/seongho/test";
        //when
        String id = containerService.createContainer(
                name, imageCpp, 8080, vol + "/cpp"
        );
        //then
        Assertions.assertThatThrownBy(()->containerService.createContainer(name, imageJava, 8081, vol+"/java"))
                .isInstanceOf(ConflictException.class);
        //after
        dockerClient.removeContainerCmd(id);
    }
    @Test
    void deleteContainer() {
        //given
        String imageName = "seongho9/cpp:latest";
        String id = containerService.createContainer(
                "test3",imageName, 8080, "/home/seongho/test"
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
        String imageCpp = "seongho9/cpp:latest";
        String name = "test4";
        Integer port = 8080;
        String vol = "/home/seongho/test";
        String id = containerService.createContainer(name, imageCpp, port, vol);
        containerService.startContainer(id);

        //then
        Assertions.assertThatThrownBy(() -> containerService.deleteContainer(id))
                .isInstanceOf(ConflictException.class);

        //after
        containerService.stopContainer(id);
        containerService.deleteContainer(id);
    }
    @Test
    void startAndStopContainer(){
        //given
        String imageName = "seongho9/cpp:latest";
        String id = containerService.createContainer(
                "test5",imageName, 8080, "/home/seongho/test"
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
        String imageCpp = "seongho9/cpp:latest";
        String name = "test6";
        Integer port = 8080;
        String vol = "/home/seongho/test";
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
        String imageCpp = "seongho9/cpp:latest";
        String name = "test7";
        Integer port = 8080;
        String vol = "/home/seongho/test";
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
        String imageName = "seongho9/cpp:latest";
        String name = "test8";
        Integer port = 8080;
        String vol = "/home/seongho/test";

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
        String imageCpp = "seongho9/cpp:latest";
        String name = "test9";
        Integer port = 8080;
        String vol = "/home/seongho/test";
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
        String imageName = "seongho9/cpp:latest";
        String name = "test10";
        Integer port = 8080;
        String vol = "/home/seongho/test";

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