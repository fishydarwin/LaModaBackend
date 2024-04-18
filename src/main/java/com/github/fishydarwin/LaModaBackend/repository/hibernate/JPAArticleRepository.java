package com.github.fishydarwin.LaModaBackend.repository.hibernate;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.ArticleAttachment;
import com.github.fishydarwin.LaModaBackend.domain.hibernate.HArticleAttachmentWrapper;
import com.github.fishydarwin.LaModaBackend.domain.hibernate.HArticleWrapper;
import com.github.fishydarwin.LaModaBackend.domain.hibernate.HCategoryWrapper;
import com.github.fishydarwin.LaModaBackend.domain.hibernate.HUserWrapper;
import com.github.fishydarwin.LaModaBackend.repository.ArticleRepository;
import com.github.fishydarwin.LaModaBackend.util.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class JPAArticleRepository implements ArticleRepository {

    private final HArticleRepository articleRepository;
    private final HArticleAttachmentRepository articleAttachmentRepository;

    private final HUserRepository userRepository;
    private final HCategoryRepository categoryRepository;

    private long lastUpdate = System.currentTimeMillis();

    public JPAArticleRepository(HArticleRepository autowiredArticleRepository,
                                HArticleAttachmentRepository autowiredArticleAttachmentRepository,
                                HUserRepository autowiredUserRepository,
                                HCategoryRepository autowiredCategoryRepository) {

        articleRepository = autowiredArticleRepository;
        articleAttachmentRepository = autowiredArticleAttachmentRepository;
        userRepository = autowiredUserRepository;
        categoryRepository = autowiredCategoryRepository;
    }

    private Article articleFromWrapper(HArticleWrapper article) {
        return new Article(
                article.getId(),
                article.getAuthor().getId(),
                article.getCategory().getId(),
                article.getName(),
                article.getSummary(),
                article.getAttachmentArray().stream()
                        .map(attachment ->
                        new ArticleAttachment(
                                attachment.getId(),
                                attachment.getArticle().getId(),
                                attachment.getAttachmentUrl())
                        )
                        .toList()
        );
    }

    @Override
    public PagedResult<Article> all(int page) {
        List<Article> result = articleRepository
                .findAll(PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "id")))
                .stream()
                .map(this::articleFromWrapper)
                .toList();
        return new PagedResult<>(result, articleRepository.count());
    }

    @Override
    public PagedResult<Article> byUser(int page, long idAuthor) {
        Optional<HUserWrapper> userMaybe = userRepository.findById((int) idAuthor);
        if (userMaybe.isEmpty()) return new PagedResult<>(new ArrayList<>(), 0);
        HUserWrapper user = userMaybe.get();

        List<Article> result = articleRepository
                .findAllByAuthor(user, PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "id")))
                .stream()
                .map(this::articleFromWrapper)
                .toList();
        return new PagedResult<>(result, articleRepository.countByAuthor(user));
    }

    @Override
    public PagedResult<Article> byCategory(int page, long idCategory) {

        Optional<HCategoryWrapper> categoryMaybe = categoryRepository.findById((int) idCategory);
        if (categoryMaybe.isEmpty()) return new PagedResult<>(new ArrayList<>(), 0);
        HCategoryWrapper category = categoryMaybe.get();

        List<Article> result = articleRepository
                .findAllByCategory(category, PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "id")))
                .stream()
                .map(this::articleFromWrapper)
                .toList();
        return new PagedResult<>(result, articleRepository.countByCategory(category));
    }

    @Override
    public PagedResult<Article> byMatchText(int page, String text) {

        List<Article> result = articleRepository
                .findByNameContaining(text, PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "id")))
                .stream()
                .map(this::articleFromWrapper)
                .toList();
        return new PagedResult<>(result, 9);

    }

    @Override
    public boolean any(long id) {
        return articleRepository.existsById((int) id);
    }

    @Override
    public Article byId(long id) {
        Optional<HArticleWrapper> articleMaybe = articleRepository.findById((int) id);
        if (articleMaybe.isEmpty()) return null;
        return this.articleFromWrapper(articleMaybe.get());
    }

    @Override
    public long add(Article article) {
        Optional<HUserWrapper> userMaybe = userRepository.findById((int) article.idAuthor());
        if (userMaybe.isEmpty()) return -1;

        Optional<HCategoryWrapper> categoryMaybe = categoryRepository.findById((int) article.idCategory());
        if (categoryMaybe.isEmpty()) return -1;

        HUserWrapper user = userMaybe.get();
        HCategoryWrapper category = categoryMaybe.get();

        HArticleWrapper articleWrapper = new HArticleWrapper(user, category, article.name(), article.summary());

        HArticleWrapper result = articleRepository.save(articleWrapper);
        article.attachmentArray().forEach(attachment -> {
            HArticleAttachmentWrapper addedAttachment = articleAttachmentRepository.save(
                    new HArticleAttachmentWrapper(result, attachment.attachmentUrl())
            );
            result.addAttachment(addedAttachment);
        });

        HArticleWrapper thenResult = articleRepository.save(result);
        lastUpdate = System.currentTimeMillis();
        return thenResult.getId();
    }

    @Override
    public long update(Article article) {
        Optional<HArticleWrapper> articleMaybe = articleRepository.findById((int) article.id());
        if (articleMaybe.isEmpty()) return -1;
        HArticleWrapper articleWrapper = articleMaybe.get();

        Optional<HCategoryWrapper> categoryMaybe = categoryRepository.findById((int) article.idCategory());
        if (categoryMaybe.isEmpty()) return -1;
        HCategoryWrapper category = categoryMaybe.get();

        articleWrapper.setName(article.name());
        articleWrapper.setSummary(article.name());
        articleWrapper.setCategory(category);

        for (HArticleAttachmentWrapper attachment : articleWrapper.getAttachmentArray())
            articleAttachmentRepository.delete(attachment);
        articleWrapper.setAttachmentArray(new HashSet<>());

        HArticleWrapper result = articleRepository.save(articleWrapper);

        article.attachmentArray().forEach(attachment -> {
            HArticleAttachmentWrapper addedAttachment = articleAttachmentRepository.save(
                    new HArticleAttachmentWrapper(result, attachment.attachmentUrl())
            );
            result.addAttachment(addedAttachment);
        });

        HArticleWrapper thenResult = articleRepository.save(articleWrapper);
        lastUpdate = System.currentTimeMillis();
        return thenResult.getId();
    }

    @Override
    public boolean delete(long id) {
        Optional<HArticleWrapper> articleMaybe = articleRepository.findById((int) id);
        if (articleMaybe.isEmpty()) return false;
        HArticleWrapper articleWrapper = articleMaybe.get();

        articleRepository.delete(articleWrapper);
        lastUpdate = System.currentTimeMillis();
        return true;
    }

    @Override
    public long getLastUpdateTime() {
        return lastUpdate;
    }

}
