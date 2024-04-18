package com.github.fishydarwin.LaModaBackend.repository.hibernate;

import com.github.fishydarwin.LaModaBackend.domain.Category;
import com.github.fishydarwin.LaModaBackend.domain.hibernate.HCategoryWrapper;
import com.github.fishydarwin.LaModaBackend.repository.CategoryRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JPACategoryRepository implements CategoryRepository {

    private final HCategoryRepository categoryRepository;

    public JPACategoryRepository(HCategoryRepository autowiredRepository) {
        categoryRepository = autowiredRepository;
    }

    @Override
    public Collection<Category> all() {
        return StreamSupport
                .stream(categoryRepository.findAll().spliterator(), false)
                .map(category -> new Category(category.getId(), category.getName(), category.isSystemCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean any(long id) {
        return categoryRepository.existsById((int) id);
    }

    @Override
    public Category byId(long id) {
        Optional<HCategoryWrapper> categoryMaybe = categoryRepository.findById((int) id);
        if (categoryMaybe.isEmpty()) return null;
        HCategoryWrapper category = categoryMaybe.get();
        return new Category(category.getId(), category.getName(), category.isSystemCategory());
    }

}
