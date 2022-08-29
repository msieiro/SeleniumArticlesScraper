package com.msieiro.SeleniumArticlesScraper.application;

import java.util.List;

import com.msieiro.SeleniumArticlesScraper.domain.Article;

public interface ArticleService {

    void saveAllArticles(final List<Article> articles);

}
