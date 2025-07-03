package com.rasberry.project.Controller;

import com.rasberry.project.Dto.PowerControllRequestDto;
import com.rasberry.project.Service.RemoteControlService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/power")
public class RemoteController {

    private final RemoteControlService remoteControlService;

    public RemoteController(RemoteControlService remoteControlService){
        this.remoteControlService = remoteControlService;
    }

    @PostMapping("/{device}")
    public CompletableFuture<String> controlDevice(@PathVariable String device, @RequestBody PowerControllRequestDto request){
        String command = request.getMessage();
        System.out.println("[" + device + "] 명령 수신: " + command);
//        return "[" + device + "] " + command + " 명령 처리 완료";
        return remoteControlService.controlDevice(device, command);
    }

}
