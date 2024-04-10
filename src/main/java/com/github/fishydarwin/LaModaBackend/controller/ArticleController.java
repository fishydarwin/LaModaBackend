package com.github.fishydarwin.LaModaBackend.controller;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.validator.Validator;
import com.github.fishydarwin.LaModaBackend.repository.ArticleRepository;
import com.github.fishydarwin.LaModaBackend.repository.faker.ArticleFaker;
import com.github.fishydarwin.LaModaBackend.repository.memory.InMemoryArticleRepository;
import com.github.fishydarwin.LaModaBackend.util.PagedResult;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.net.URISyntaxException;

@CrossOrigin
@RestController
public class ArticleController {

    private final ArticleRepository repository = new InMemoryArticleRepository();

    @GetMapping("/article/all")
    public PagedResult<Article> all(@RequestParam(value="page") int page) {
        return repository.all(page);
    }

    @GetMapping("/article/byId")
    public Article byId(@RequestParam(value="id") long id) {
        return repository.byId(id);
    }

    @GetMapping("/article/any")
    public boolean any(@RequestParam(value="id") long id) {
        return repository.any(id);
    }

    @GetMapping("/article/byUser")
    public PagedResult<Article> byUser(@RequestParam(value="author") int author,
                                       @RequestParam(value="page") int page) {
        return repository.byUser(page, author);
    }

    @GetMapping("/article/byCategory")
    public PagedResult<Article> byCategory(@RequestParam(value="category") int category,
                                           @RequestParam(value="page") int page) {
        return repository.byCategory(page, category);
    }

    @GetMapping("/article/byMatchText")
    public PagedResult<Article> byMatchText(@RequestParam(value="text") String text,
                                            @RequestParam(value="page") int page) {
        return repository.byMatchText(page, text);
    }

    @PostMapping("/article/add")
    public long add(@RequestBody Article article) {
        String validation = Validator.validate(article);
        if (!validation.equals("OK"))
            return -1;
        return repository.add(article);
    }

    @PutMapping("/article/update/{id}")
    public long update(@RequestBody Article article,
                       @PathVariable long id) {
        String validation = Validator.validate(article);
        if (!validation.equals("OK"))
            return -1;
        if (article.id() != id) return -1;
        return repository.update(article);
    }

    @DeleteMapping("/article/delete/{id}")
    public boolean delete(@PathVariable long id) {
        //TODO: authenticate author!
        return repository.delete(id);
    }

    @MessageMapping("/article-time")
    @SendTo("/article")
    public long refreshArticles() {
        return repository.getLastUpdateTime();
    }

}
