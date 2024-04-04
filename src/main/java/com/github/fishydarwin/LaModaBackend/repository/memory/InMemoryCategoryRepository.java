package com.github.fishydarwin.LaModaBackend.repository.memory;

import com.github.fishydarwin.LaModaBackend.domain.Category;
import com.github.fishydarwin.LaModaBackend.repository.CategoryRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> contents = new HashMap<>();

    public InMemoryCategoryRepository() {
        contents.put(1L,
                new Category(1, "Îmbrăcăminte", true
        ));
        contents.put(2L,
                new Category(2, "Pantofi", true
        ));
        contents.put(3L,
                new Category(3, "Coafuri", true
        ));
        contents.put(4L,
                new Category(4, "Machiaj", true
        ));
        contents.put(5L,
                new Category(5, "Manichiură", true
        ));
    }

    @Override
    public Collection<Category> all() {
        return contents.values();
    }

    @Override
    public boolean any(long id) {
        return contents.containsKey(id);
    }

    @Override
    public Category byId(long id) {
        if (contents.containsKey(id)) return contents.get(id);
        return null;
    }

}
