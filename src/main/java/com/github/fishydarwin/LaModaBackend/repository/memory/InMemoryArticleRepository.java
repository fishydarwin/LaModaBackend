package com.github.fishydarwin.LaModaBackend.repository.memory;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.ArticleAttachment;
import com.github.fishydarwin.LaModaBackend.repository.ArticleRepository;
import com.github.fishydarwin.LaModaBackend.repository.faker.ArticleFaker;
import com.github.fishydarwin.LaModaBackend.util.PagedResult;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryArticleRepository implements ArticleRepository {

    private static long LAST_ID = 0;
    private final Map<Long, Article> contents = new HashMap<>();
    private final List<Long> articleIdsOrdered = new ArrayList<>();

    private static long LAST_ID_ATTACHMENT = 0;
    private final Map<Long, ArticleAttachment> attachments = new HashMap<>();

    private long lastUpdate = System.currentTimeMillis();

    public InMemoryArticleRepository() {
        try {
            ArticleFaker.init();
            for (int i = 0; i < 50; i++) {
                add(ArticleFaker.generateRandomArticle());
            }
        } catch (URISyntaxException | IOException | ParseException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PagedResult<Article> all(int page) {
        if (page < 0) page = 0;
        List<Long> slicedStackArray = new ArrayList<>();
        for (int i = page * 9; i < (page + 1) * 9 && i < articleIdsOrdered.size(); i++)
            slicedStackArray.add(articleIdsOrdered.get(i));
        return new PagedResult<>
                (slicedStackArray.stream().map(contents::get).collect(Collectors.toList()),
                        contents.size());
    }

    @Override
    public PagedResult<Article> byUser(int page, long idAuthor) {
        if (page < 0) page = 0;
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
        if (page < 0) page = 0;
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
        if (page < 0) page = 0;
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
        lastUpdate = System.currentTimeMillis();

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
        lastUpdate = System.currentTimeMillis();

        return article.id();
    }

    @Override
    public boolean delete(long id) {
        if (contents.containsKey(id)) {
            contents.get(id).attachmentArray().forEach((attachment) -> attachments.remove(attachment.id()));
            articleIdsOrdered.remove(id);
            return contents.remove(id) != null;
        }
        lastUpdate = System.currentTimeMillis();

        return false;
    }

    @Override
    public long getLastUpdateTime() {
        return lastUpdate;
    }
}
