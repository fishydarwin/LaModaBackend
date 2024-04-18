package com.github.fishydarwin.LaModaBackend.repository.hibernate;

import com.github.fishydarwin.LaModaBackend.domain.hibernate.HArticleAttachmentWrapper;
import org.springframework.data.repository.CrudRepository;

public interface HArticleAttachmentRepository extends CrudRepository<HArticleAttachmentWrapper, Integer> {
}
