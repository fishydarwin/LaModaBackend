package com.github.fishydarwin.LaModaBackend.controller;

import com.github.fishydarwin.LaModaBackend.domain.Category;
import com.github.fishydarwin.LaModaBackend.repository.CategoryRepository;
import com.github.fishydarwin.LaModaBackend.repository.memory.InMemoryCategoryRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@CrossOrigin
@RestController
public class CategoryController {

    private final CategoryRepository repository = new InMemoryCategoryRepository();

    @GetMapping("/category/all")
    public Collection<Category> all() {
        return repository.all();
    }

    @GetMapping("/category/any")
    public boolean any(@RequestParam(value="id") long id) {
        return repository.any(id);
    }

    @GetMapping("/category/byId")
    public Category byId(@RequestParam(value="id") long id) {
        return repository.byId(id);
    }

}
