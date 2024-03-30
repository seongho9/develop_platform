package me.seongho9.dev.service.server;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import me.seongho9.dev.service.server.session.ServerOperation;
import me.seongho9.dev.service.server.session.ServerSessionAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerServiceJSch implements ServerService{

    @Autowired
    private final ServerSessionAspect sessionAspect;

    @Override
    @ServerOperation
    public void mkdir(String path) {
        String command = "mkdir " + path;
        exec(command);
    }
    @Override
    @ServerOperation
    public void rm(String path) {
        if(path.equals("/")){
            throw new RuntimeException("root directory cannot me delete");
        }
        String command = "rm -r " + path;
        exec(command);
    }
    @Override
    @ServerOperation
    public List<String> ls(String path) {
        String command = "ls " + path;
        try {
            Session session = sessionAspect.getSession();
            ChannelExec ch = (ChannelExec) session.openChannel("exec");
            ch.setCommand(command);
            ch.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ch.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(ch.getErrStream()));
            String errResponse = err.readLine();
            if(errResponse != null){
                throw new RuntimeException(errResponse);
            }

            List<String> result = new ArrayList<>();
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.add(buffer);
            }
            return result;

        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void exec(String command){
        try {
            Session session = sessionAspect.getSession();
            ChannelExec ch = (ChannelExec) session.openChannel("exec");
            ch.setCommand(command);
            ch.connect();

            BufferedReader err = new BufferedReader(new InputStreamReader(ch.getErrStream()));
            String response = err.readLine();
            if (response != null) {
                throw new RuntimeException(response);
            }
        } catch (JSchException e){
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }


}
