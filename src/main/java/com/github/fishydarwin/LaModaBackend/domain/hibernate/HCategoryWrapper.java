package com.github.fishydarwin.LaModaBackend.domain.hibernate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HCategoryWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private boolean systemCategory;

    public HCategoryWrapper() {}

    public HCategoryWrapper(String name, boolean systemCategory) {
        this.name = name;
        this.systemCategory = systemCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSystemCategory() {
        return systemCategory;
    }

    public void setSystemCategory(boolean systemCategory) {
        this.systemCategory = systemCategory;
    }

}
