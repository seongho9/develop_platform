package me.seongho9.dev.service.server;

import java.util.List;

public interface ServerService {

    public void mkdir(String path);
    public void rm(String path);
    public List<String> ls(String path);
}
