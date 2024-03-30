package me.seongho9.dev.service.server;

import com.jcraft.jsch.Session;
import me.seongho9.dev.service.server.session.ServerOperation;
import me.seongho9.dev.service.server.session.ServerSessionAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;

@Service
public class ServerPowerServiceImpl implements ServerPowerService {

    @Autowired
    ServerSessionAspect aspect;

    @Override
    public void powerOn(String ipStr, String macStr) {

        byte[] magicPacket = createMagicPacket(parseMacAddress(macStr));

        try{
            InetAddress ipAddress = InetAddress.getByName(ipStr);
            DatagramPacket packet = new DatagramPacket(magicPacket, magicPacket.length, ipAddress, 9);
            DatagramSocket socket = new DatagramSocket();
            for(int i=0; i<3; i++) {
                socket.send(packet);
                Thread.sleep(500);
            }
            socket.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private byte[] parseMacAddress(String macAddress) {
        String[] hex = macAddress.split("(\\:|\\-)");

        byte[] macBytes =  new byte[6];
        for (int i = 0; i < 6; i++) {
            macBytes[i] = (byte) Integer.parseInt(hex[i], 16);
        }
        return macBytes;
    }
    private byte[] createMagicPacket(byte[] macBytes) {
        byte[] magicPacket = new byte[102];

        for (int i = 0; i < 6; i++) {
            magicPacket[i]  = (byte) 0xFF;
        }
        for (int i = 6; i < magicPacket.length; i += macBytes.length) {
            System.arraycopy(macBytes, 0, magicPacket, i, macBytes.length);
        }

        return magicPacket;
    }

    @Override
    public boolean ping(String ipStr) {
        try{
            InetAddress inet = InetAddress.getByName(ipStr);
            if(inet.isReachable(3000)){
                return true;
            }
            else {
                return false;
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @ServerOperation
    public void powerOff(String ipStr) {
        Session session = aspect.getSession();
    }
}
