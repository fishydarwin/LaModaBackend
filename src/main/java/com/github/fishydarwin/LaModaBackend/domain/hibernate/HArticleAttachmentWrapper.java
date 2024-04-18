package com.github.fishydarwin.LaModaBackend.domain.hibernate;

import jakarta.persistence.*;

@Entity
public class HArticleAttachmentWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="idArticle")
    private HArticleWrapper article;

    private String attachmentUrl;

    public HArticleAttachmentWrapper() {}
    public HArticleAttachmentWrapper(HArticleWrapper article, String attachmentUrl) {
        this.article = article;
        this.attachmentUrl = attachmentUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HArticleWrapper getArticle() {
        return article;
    }

    public void setArticle(HArticleWrapper article) {
        this.article = article;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
