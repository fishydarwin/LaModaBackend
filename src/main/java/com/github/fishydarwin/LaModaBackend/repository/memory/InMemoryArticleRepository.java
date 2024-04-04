package com.github.fishydarwin.LaModaBackend.repository.memory;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.ArticleAttachment;
import com.github.fishydarwin.LaModaBackend.repository.ArticleRepository;
import com.github.fishydarwin.LaModaBackend.util.PagedResult;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryArticleRepository implements ArticleRepository {

    private static long LAST_ID = 0;
    private final Map<Long, Article> contents = new HashMap<>();
    private final List<Long> articleIdsOrdered = new ArrayList<>();

    private static long LAST_ID_ATTACHMENT = 0;
    private final Map<Long, ArticleAttachment> attachments = new HashMap<>();

    public InMemoryArticleRepository() {
        
        add(new Article(
                1, 1, 1, "Pullover stradă",
                "Descoperă modelele de pullover pentru primăvară în cea mai nouă colecție a noastră",
                List.of(
                        new ArticleAttachment(1, 1,
                                "/assets/mock-data-images/pullover1.jpg"),
                        new ArticleAttachment(2, 1,
                                "/assets/mock-data-images/pullover2.jpg"),
                        new ArticleAttachment(3, 1,
                                "/assets/mock-data-images/pullover3.jpg")
                )
        ));
        add(new Article(
                2, 2, 2, "Vanși modele noi",
                "Ultimul model de Vanși, cu un design nou și inovator, ce redefinesc standardele stilului",
                List.of(
                        new ArticleAttachment(4, 2,
                                "/assets/mock-data-images/vans1.webp"),
                        new ArticleAttachment(5, 2,
                                "/assets/mock-data-images/vans2.jpg"),
                        new ArticleAttachment(6, 2,
                                "/assets/mock-data-images/vans3.webp")
                )
        ));
        add(new Article(
                3, 2, 3, "Păr scurt primăvară",
                "Împrospătează-ți look-ul cu o tunsoare modernă pentru păr scurt deosebit",
                List.of(
                        new ArticleAttachment(7, 3,
                                "/assets/mock-data-images/shorthair1.jpg"),
                        new ArticleAttachment(8, 3,
                                "/assets/mock-data-images/shorthair2.jpg"),
                        new ArticleAttachment(9, 3,
                                "/assets/mock-data-images/shorthair3.jpeg")
                )
        ));
        add(new Article(
                4, 3, 4, "Eyeliner 2024",
                "Explorează trendurile în moda eyeliner pentru anul 2024 și descoperă cele mai " +
                        "recente tehnici și stiluri",
                List.of(
                        new ArticleAttachment(10, 4,
                                "/assets/mock-data-images/eyeliner1.webp"),
                        new ArticleAttachment(11, 4,
                                "/assets/mock-data-images/eyeliner2.jpeg"),
                        new ArticleAttachment(12, 4,
                                "/assets/mock-data-images/eyeliner3.jpg")
                )
        ));
        add(new Article(
                5, 4, 5, "Albastru stiletto",
                "Încântă-ți simțurile cu unghii albastre stiletto, adăugând o notă de eleganță " +
                        "și stil la fiecare pas",
                List.of(
                        new ArticleAttachment(13, 5,
                                "/assets/mock-data-images/stiletto1.jpg"),
                        new ArticleAttachment(14, 5,
                                "/assets/mock-data-images/stiletto2.jpg.avif"),
                        new ArticleAttachment(15, 5,
                                "/assets/mock-data-images/stiletto3.webp.jpeg")
                )
        ));
    }

    @Override
    public PagedResult<Article> all(int page) {
        List<Long> slicedStackArray = new ArrayList<>();
        for (int i = page * 9; i < (page + 1) * 9 && i < articleIdsOrdered.size(); i++)
            slicedStackArray.add(articleIdsOrdered.get(i));
        return new PagedResult<>
                (slicedStackArray.stream().map(contents::get).collect(Collectors.toList()),
                        contents.size());
    }

    @Override
    public PagedResult<Article> byUser(int page, long idAuthor) {
        List<Long> slicedStackArray = new ArrayList<>();
        int i = 0;
        long sizeFilter = 0;
        while (i < articleIdsOrdered.size()) {
            long id = articleIdsOrdered.get(i);
            if (contents.get(id).idAuthor() == idAuthor) {
                if (sizeFilter >= page * 9L && sizeFilter < (page + 1) * 9L)
                    slicedStackArray.add(id);
                sizeFilter++;
            }
            i++;
        }
        return new PagedResult<>
                (slicedStackArray.stream().map(contents::get).collect(Collectors.toList()),
                        sizeFilter);
    }

    @Override
    public PagedResult<Article> byCategory(int page, long category) {
        List<Long> slicedStackArray = new ArrayList<>();
        int i = 0;
        long sizeFilter = 0;
        while (i < articleIdsOrdered.size()) {
            long id = articleIdsOrdered.get(i);
            if (contents.get(id).idCategory() == category) {
                if (sizeFilter >= page * 9L && sizeFilter < (page + 1) * 9L)
                    slicedStackArray.add(id);
                sizeFilter++;
            }
            i++;
        }
        return new PagedResult<>
                (slicedStackArray.stream().map(contents::get).collect(Collectors.toList()),
                        sizeFilter);
    }

    @Override
    public PagedResult<Article> byMatchText(int page, String text) {
        List<Long> slicedStackArray = new ArrayList<>();
        int i = 0;
        long sizeFilter = 0;
        while (i < articleIdsOrdered.size()) {
            long id = articleIdsOrdered.get(i);
            if (contents.get(id).name().toLowerCase().contains(text) ||
                contents.get(id).summary().toLowerCase().contains(text)) {
                if (sizeFilter >= page * 9L && sizeFilter < (page + 1) * 9L)
                    slicedStackArray.add(id);
                sizeFilter++;
            }
            i++;
        }
        return new PagedResult<>
                (slicedStackArray.stream().map(contents::get).collect(Collectors.toList()),
                        sizeFilter);
    }

    @Override
    public boolean any(long id) {
        return contents.containsKey(id);
    }

    @Override
    public Article byId(long id) {
        if (contents.containsKey(id)) return contents.get(id);
        return null;
    }

    @Override
    public long add(Article article) {
        LAST_ID++;

        List<ArticleAttachment> attachmentArray = new ArrayList<>();
        for (ArticleAttachment attachment : article.attachmentArray()) {
            ArticleAttachment tempAttachment = new ArticleAttachment(
                    LAST_ID, ++LAST_ID_ATTACHMENT, attachment.attachmentUrl()
            );
            attachmentArray.add(tempAttachment);
            attachments.put(LAST_ID_ATTACHMENT, tempAttachment);
        }

        Article added = new Article(
                LAST_ID, article.idAuthor(), article.idCategory(),
                article.name(), article.summary(),
                attachmentArray
        );

        contents.put(LAST_ID, added);
        articleIdsOrdered.add(0, LAST_ID);
        return LAST_ID;
    }

    @Override
    public long update(Article article) {

        contents.get(article.id()).attachmentArray().forEach(attachment -> attachments.remove((attachment.id())));

        List<ArticleAttachment> attachmentArray = new ArrayList<>();
        for (ArticleAttachment attachment : article.attachmentArray()) {
            ArticleAttachment tempAttachment = new ArticleAttachment(
                    article.id(), ++LAST_ID_ATTACHMENT, attachment.attachmentUrl()
            );
            attachmentArray.add(tempAttachment);
            attachments.put(LAST_ID_ATTACHMENT, tempAttachment);
        }

        Article updated = new Article(
                article.id(), article.idAuthor(), article.idCategory(),
                article.name(), article.summary(),
                attachmentArray
        );

        contents.put(article.id(), updated);
        return article.id();
    }

    @Override
    public boolean delete(long id) {
        if (contents.containsKey(id)) {
            contents.get(id).attachmentArray().forEach((attachment) -> attachments.remove(attachment.id()));
            contents.remove(id);
        }
        return false;
    }
}
