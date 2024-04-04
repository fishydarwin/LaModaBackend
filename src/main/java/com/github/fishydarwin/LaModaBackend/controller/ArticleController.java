package com.github.fishydarwin.LaModaBackend.controller;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.repository.ArticleRepository;
import com.github.fishydarwin.LaModaBackend.repository.memory.InMemoryArticleRepository;
import com.github.fishydarwin.LaModaBackend.util.PagedResult;
import org.springframework.web.bind.annotation.*;

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
        //TODO: Validate server-side here: call Validator interface class
        return repository.add(article);
    }

    @PutMapping("/article/update/{id}")
    public long update(@RequestBody Article article,
                       @PathVariable long id) {
        //TODO: Validate server-side here: call Validator interface class
        //TODO: authenticate author!
        if (article.id() != id) return -1;
        return repository.update(article);
    }

    @DeleteMapping("/article/delete")
    public boolean delete(@PathVariable long id) {
        //TODO: Validate server-side here: call Validator interface class
        //TODO: authenticate author!
        return repository.delete(id);
    }

}
