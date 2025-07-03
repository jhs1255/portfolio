package com.rasberry.project.Config;

import com.rasberry.project.Handler.RaspberryWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(raspberryWebSocketHandler(),"/raspberry").setAllowedOrigins("*");
    }

    @Bean
    public RaspberryWebSocketHandler raspberryWebSocketHandler(){
        return new RaspberryWebSocketHandler();
    }

    // ✅ 여기에 추가 가능
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
