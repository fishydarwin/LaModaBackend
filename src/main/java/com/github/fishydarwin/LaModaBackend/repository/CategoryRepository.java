package com.github.fishydarwin.LaModaBackend.repository;

import com.github.fishydarwin.LaModaBackend.domain.Category;

import java.util.Collection;

public interface CategoryRepository {

    Collection<Category> all();
    boolean any(long id);
    Category byId(long id);

}
