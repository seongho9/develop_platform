package me.seongho9.dev.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigDelegate;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Configuration
public class DockerConfig {

    private final String host;
    private final boolean isVerify;

    public DockerConfig(
            @Value("${docker.host}") String host,
            @Value("${docker.tls.verify}") boolean isVerify
    ) {
        this.host = host;
        this.isVerify = isVerify;
    }

    @Bean
    public DockerClient dockerClient(){
        log.info("Docker client");
        log.info("host={}", this.host);
        DefaultDockerClientConfig config = getDockerClientConfig();
        DockerHttpClient httpClient = getHttpClient(config);
        return DockerClientImpl.getInstance(config, httpClient);
    }
    private DockerHttpClient getHttpClient(DefaultDockerClientConfig config) {
        DockerClientConfigDelegate clientConfig = new DockerClientConfigDelegate(config);
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(clientConfig.getDockerHost())
                .sslConfig(clientConfig.getSSLConfig())
                .build();
    }
    private DefaultDockerClientConfig getDockerClientConfig(){
        return new DefaultDockerClientConfig.Builder()
                .withDockerHost(this.host)
                .withDockerTlsVerify(this.isVerify)
                .build();
    }
}
