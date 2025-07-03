package com.rasberry.project.Handler;


import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/flutter")
@Component
public class FlutterWebSocketHandler {
    private static final Set<Session> flutterSession = ConcurrentHashMap.newKeySet();

    @OnOpen
    public void onOpen(Session session){
        flutterSession.add(session);
        System.out.println("flutter와 연결됨: " + session.getId());
    }

    @OnClose
    public void onClose(Session session){
        flutterSession.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket 오류 발생: " + throwable.getMessage());
    }

    public static void broadcast(String json){
        for(Session session : flutterSession){
            try {
                session.getAsyncRemote().sendText(json);
            } catch (Exception e) {
                System.out.println("⚠️ Flutter 전송 중 오류: " + e.getMessage());
            }
        }
    }

}
