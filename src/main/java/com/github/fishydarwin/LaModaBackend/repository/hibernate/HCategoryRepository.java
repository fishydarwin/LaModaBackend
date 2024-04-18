package com.github.fishydarwin.LaModaBackend.repository.hibernate;

import com.github.fishydarwin.LaModaBackend.domain.hibernate.HCategoryWrapper;
import org.springframework.data.repository.CrudRepository;

public interface HCategoryRepository extends CrudRepository<HCategoryWrapper, Integer> {
}
