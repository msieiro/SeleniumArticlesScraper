package com.msieiro.SeleniumArticlesScraper.application.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msieiro.SeleniumArticlesScraper.application.ArticleService;
import com.msieiro.SeleniumArticlesScraper.domain.Article;
import com.msieiro.SeleniumArticlesScraper.domain.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = false)
    public void saveAllArticles(final List<Article> articles) {
        articleRepository.saveAll(articles);
    }

}
