package com.github.fishydarwin.LaModaBackend.domain.hibernate;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class HArticleWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="idAuthor")
    private HUserWrapper author;

    @ManyToOne
    @JoinColumn(name="idCategory")
    private HCategoryWrapper category;

    private String name;
    private String summary;

    @OneToMany(mappedBy = "article")
    private Set<HArticleAttachmentWrapper> attachmentArray;

    public HArticleWrapper() {}
    public HArticleWrapper(HUserWrapper author, HCategoryWrapper category, String name, String summary) {
        this.author = author;
        this.category = category;
        this.name = name;
        this.summary = summary;
        this.attachmentArray = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HUserWrapper getAuthor() {
        return author;
    }

    public void setAuthor(HUserWrapper author) {
        this.author = author;
    }

    public HCategoryWrapper getCategory() {
        return category;
    }

    public void setCategory(HCategoryWrapper category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Set<HArticleAttachmentWrapper> getAttachmentArray() {
        return attachmentArray;
    }

    public void setAttachmentArray(Set<HArticleAttachmentWrapper> attachmentArray) {
        this.attachmentArray = attachmentArray;
    }

    public void addAttachment(HArticleAttachmentWrapper attachment) {
        attachmentArray.add(attachment);
    }

    public void removeAttachment(HArticleAttachmentWrapper attachment) {
        attachmentArray.remove(attachment);
    }
}
