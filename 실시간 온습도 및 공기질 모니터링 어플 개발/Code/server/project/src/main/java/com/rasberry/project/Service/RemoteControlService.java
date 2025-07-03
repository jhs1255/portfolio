package com.rasberry.project.Service;

import com.rasberry.project.Handler.RaspberryWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class RemoteControlService {

    @Autowired
    private RaspberryWebSocketHandler raspberryWebSocketHandler;

    public CompletableFuture<String> controlDevice(String device, String command){
        return raspberryWebSocketHandler.sendWithResponse(device, command);
    }

}
