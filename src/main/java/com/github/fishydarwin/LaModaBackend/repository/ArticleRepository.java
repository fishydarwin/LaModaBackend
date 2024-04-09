package com.github.fishydarwin.LaModaBackend.repository;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.util.PagedResult;

public interface ArticleRepository {

    PagedResult<Article> all(int page);
    PagedResult<Article> byUser(int page, long idAuthor);
    PagedResult<Article> byCategory(int page, long category);
    PagedResult<Article> byMatchText(int page, String text);
    boolean any(long id);
    Article byId(long id);

    long add(Article article);
    long update(Article article);
    boolean delete(long id);

    long getLastUpdateTime();

}
