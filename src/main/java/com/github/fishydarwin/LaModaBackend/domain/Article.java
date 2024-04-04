package com.github.fishydarwin.LaModaBackend.domain;

import java.util.List;

public record Article(long id, long idAuthor, long idCategory,
                      String name, String summary,
                      List<ArticleAttachment> attachmentArray) {}
//                      Date creationDate) {}
