package com.msieiro.SeleniumArticlesScraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
class SeleniumArticlesScraperApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeleniumArticlesScraperApplication.class, args);
    }
}
