package me.seongho9.dev.service.server;

public interface ServerPowerService {
    public void powerOn(String ipStr, String macStr);
    public boolean ping(String ipStr);
    public void powerOff(String ipStr);
}
