package com.rasberry.project.Handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RaspberryWebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession raspSession;
    private final Map<String, CompletableFuture<String>> waitResponse = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        this.raspSession = session;
        System.out.println("라즈베리 파이 연결됨 : "+session.getId());
    }

    public CompletableFuture<String> sendWithResponse(String device, String command){
        if(raspSession == null || !raspSession.isOpen()){
            CompletableFuture<String> faild = new CompletableFuture<>();
            faild.complete("연결안됨");
            return faild;
        }

        try {
            String message = new ObjectMapper().writeValueAsString(Map.of(
                    "device", device,
                    "command", command
            ));
            raspSession.sendMessage(new TextMessage(message));
            System.out.println("라즈베리파이에 전송: " + message);

            CompletableFuture<String> future = new CompletableFuture<>();
            waitResponse.put(device, future);
            return future;
        } catch (Exception e) {
            CompletableFuture<String> failed = new CompletableFuture<>();
            failed.complete("전송 실패: " + e.getMessage());
            return failed;
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws  Exception{
        String payload = message.getPayload();
        System.out.println("요청값 수신 : " + payload);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> response = new ObjectMapper().readValue(payload, new TypeReference<Map<String, String>>() {});
        String device = response.get("device");
        String result = response.get("result");

        //temp가 포함된 센서데이터인 경우 flutter로 브로드캐스트
        if (device == null && result == null && response.containsKey("temp")) {
            FlutterWebSocketHandler.broadcast(payload); // 센서값 그대로 전송
            System.out.println("📡 Flutter로 센서 데이터 푸시: " + payload);
            return;
        }

        if (device == null || result == null) {
            System.out.println("잘못된 응답 형식: 'device' 또는 'result'가 누락됨");
            return;  // 응답이 유효하지 않으면 처리를 중단
        }

        CompletableFuture<String> waiter = waitResponse.remove(device);
        if (waiter != null) {
            waiter.complete(result);
        } else {
            System.out.println("해당 device에 대한 대기 작업이 없음: " + device);
        }
    }

}
