package com.github.fishydarwin.LaModaBackend.runtime.tester;

import com.github.fishydarwin.LaModaBackend.controller.ArticleController;
import com.github.fishydarwin.LaModaBackend.repository.faker.ArticleFaker;
import org.json.simple.parser.ParseException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class ArticleWebsocketTester implements ApplicationListener<ApplicationReadyEvent> {

    private static final boolean IS_TESTER_ENABLED = false;

    private final ArticleController articleController;
    public ArticleWebsocketTester(ArticleController articleController) {
        this.articleController = articleController;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (IS_TESTER_ENABLED) {
            WebSocketClient client = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(client);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            StompSessionHandler sessionHandler = new WebsocketTestHandler(articleController);
            stompClient.connectAsync("ws://localhost:8080/ws", sessionHandler);
        }
    }

    private static class WebsocketTestHandler extends StompSessionHandlerAdapter {

        private final ArticleController articleController;
        public WebsocketTestHandler(ArticleController articleController) {
            this.articleController = articleController;
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("Socket tester client is ready.");
            new Thread(() -> {

                for (;;) {

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        articleController.add(ArticleFaker.generateRandomArticle());
                        session.send("/app/article-time", "");
                        System.out.println("Added another random article...");
                    } catch (URISyntaxException | IOException | ParseException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

            }).start();
        }
    }

}
