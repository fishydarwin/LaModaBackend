package com.github.fishydarwin.LaModaBackend.domain;

import com.github.fishydarwin.LaModaBackend.domain.validator.Validatable;

import java.util.List;

public record Article(long id, long idAuthor, long idCategory,
                      String name, String summary,
                      List<ArticleAttachment> attachmentArray)
implements Validatable {

    @Override
    public String validate() {
        if (this.name.trim().length() <= 0) {
            return "Vă rugăm să introduceți numele articolului.";
        }
        if (this.summary.trim().length() <= 0) {
            return "Vă rugăm să introduceți descrierea articolului.";
        }
        if (this.attachmentArray.size() <= 0) {
            return "Trebuie să adaugați cel putin o imagine în articolul dvs.";
        }
        return "OK";
    }

}
