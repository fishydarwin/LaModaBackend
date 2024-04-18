package com.github.fishydarwin.LaModaBackend.repository.hibernate;

import com.github.fishydarwin.LaModaBackend.domain.hibernate.HArticleWrapper;
import com.github.fishydarwin.LaModaBackend.domain.hibernate.HCategoryWrapper;
import com.github.fishydarwin.LaModaBackend.domain.hibernate.HUserWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HArticleRepository extends CrudRepository<HArticleWrapper, Integer>,
                                            PagingAndSortingRepository<HArticleWrapper, Integer> {

    List<HArticleWrapper> findAllByAuthor(HUserWrapper author, Pageable pageable);
    Long countByAuthor(HUserWrapper author);

    List<HArticleWrapper> findAllByCategory(HCategoryWrapper author, Pageable pageable);
    Long countByCategory(HCategoryWrapper author);

    List<HArticleWrapper> findByNameContaining(String name, Pageable pageable);
//    Long countByCategory(HCategoryWrapper author);

}
