package me.seongho9.dev.service.server.session;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Aspect
@Slf4j
@Component
public class ServerSessionAspect {

    private Session sshSession;
    private final String serverAddress;
    private final String serverPort;
    private final String serverUser;
    private final String serverKey;

    public ServerSessionAspect(
            @Value("${host.ip}") String serverAddress,
            @Value("${host.port}") String serverPort,
            @Value("${host.user}") String serverUser,
            @Value("${host.key}") String serverKey
    ) {
        log.info("Server Session : {}:{}", serverAddress, serverPort);
        log.info("Server key: {}", serverKey);
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.serverUser = serverUser;
        this.serverKey = serverKey;
    }

    public Session getSession() {
        if (sshSession != null||sshSession.isConnected()) {
            return sshSession;
        } else {
            throw new RuntimeException("not connected to session");
        }
    }
    @Before("@annotation(ServerOperation)")
    public void establishSession() {

        try {
            JSch jSch = new JSch();
            jSch.addIdentity(serverKey);
            log.info("server key {} ", serverKey);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            sshSession = jSch.getSession(serverUser, serverAddress, Integer.parseInt(serverPort));
            sshSession.setConfig(properties);
            sshSession.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }
    @AfterReturning("@annotation(ServerOperation)")
    public void closeSession(){
        if(sshSession.isConnected()){
            sshSession.disconnect();
        }
    }
}
