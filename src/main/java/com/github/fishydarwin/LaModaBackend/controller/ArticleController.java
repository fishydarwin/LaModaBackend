package com.github.fishydarwin.LaModaBackend.controller;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.domain.validator.Validator;
import com.github.fishydarwin.LaModaBackend.manager.UserSessionManager;
import com.github.fishydarwin.LaModaBackend.repository.ArticleRepository;
import com.github.fishydarwin.LaModaBackend.repository.hibernate.*;
import com.github.fishydarwin.LaModaBackend.util.PagedResult;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
public class ArticleController {

    private final ArticleRepository repository;

    public ArticleController(HArticleRepository autowiredArticleRepository,
                             HArticleAttachmentRepository autowiredArticleAttachmentRepository,
                             HUserRepository autowiredUserRepository,
                             HCategoryRepository autowiredCategoryRepository) {
        repository = new JPAArticleRepository(
                autowiredArticleRepository,
                autowiredArticleAttachmentRepository,
                autowiredUserRepository,
                autowiredCategoryRepository
        );

//        try {
//            System.out.println("Generating articles...");
//            ArticleFaker.init();
//            for (int i = 51; i < 10000; i++) {
//                repository.add(ArticleFaker.generateRandomArticle());
//            }
//        } catch (URISyntaxException | IOException | ParseException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

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
                       @PathVariable long id,
                       @RequestParam String sessionId) {
        String validation = Validator.validate(article);
        if (!validation.equals("OK"))
            return -1;
        if (article.id() != id) return -1;

        Article doubleCheckArticle = repository.byId(id);
        User sessionUser = UserSessionManager.bySession(sessionId);
        if (doubleCheckArticle == null || sessionUser == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        if (sessionUser.id() != doubleCheckArticle.idAuthor())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        return repository.update(article);
    }

    @DeleteMapping("/article/delete/{id}")
    public boolean delete(@PathVariable long id,
                          @RequestParam String sessionId) {

        Article doubleCheckArticle = repository.byId(id);
        User sessionUser = UserSessionManager.bySession(sessionId);
        if (doubleCheckArticle == null || sessionUser == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials");
        if (sessionUser.id() != doubleCheckArticle.idAuthor())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials");

        return repository.delete(id);
    }

    @MessageMapping("/article-time")
    @SendTo("/article")
    public long refreshArticles() {
        return repository.getLastUpdateTime();
    }

}
