package com.msieiro.SeleniumArticlesScraper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.msieiro.SeleniumArticlesScraper.application.ArticleService;
import com.msieiro.SeleniumArticlesScraper.application.PersonaService;
import com.msieiro.SeleniumArticlesScraper.domain.Article;
import com.msieiro.SeleniumArticlesScraper.domain.Person;
import com.msieiro.SeleniumArticlesScraper.domain.utils.DateUtils;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class SeleniumExecutor {

    private final PersonaService personaService;
    private final ArticleService articleService;

    @EventListener(ApplicationReadyEvent.class)
    protected void loadDB() {
        WebDriverManager.chromedriver().setup();
        final ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
        options.addArguments("--headless");
        final WebDriver driver = new ChromeDriver(options);

        loadPersonasInDB();

        loadMidudevArticles(driver);
        loadJamesSinclairArticles(driver);
        loadBaeldungArticles(driver);
        loadNicolasSchurmannArticles(driver);
        loadSmashingMagazineArticles(driver);
        loadWebDevArticles(driver);
        loadFreeCodeCampArticles(driver);
        loadReflectoringArticles(driver);
        loadAhmadShadeedArticles(driver);
        loadCSSTricksArticles(driver);

        driver.quit();
    }

    private void loadPersonasInDB() {
        final List<Person> personas = new ArrayList<>() {
            {
                add(Person.builder()
                        .name("midudev")
                        .website("https://midu.dev/")
                        .logo("https://midu.dev/logo.png")
                        .build());
                add(Person.builder()
                        .name("James Sinclair")
                        .website("https://jrsinclair.com/")
                        .logo("https://jrsinclair.com/assets/owl.svg")
                        .build());
                add(Person.builder()
                        .name("Baeldung")
                        .website("https://www.baeldung.com/full_archive")
                        .logo("https://media-exp1.licdn.com/dms/image/C561BAQE-eTcygnAyIA/company-background_10000/0/1555304127962?e=2147483647&v=beta&t=OrfN0EC9zz5hnJhyw9scYew49uVFqcAG1d7zC43tgXc")
                        .build());
                add(Person.builder()
                        .name("Nicolas Schurmann")
                        .website("https://www.nicolas-schurmann.com/")
                        .logo("https://i.ibb.co/D79cDNy/Z69fh-RL9-Oa-Xs-Dz-Xs-CUe2s-GIq-U7-G1-F5-Mwl0-Pwl-Bsx-ll13-K0n-Lb47q7-RMen7-NHvz-MVDgd2-s88-c-k-c0x0.jpg")
                        .build());
                add(Person.builder()
                        .name("Smashing Magazine")
                        .website("https://www.smashingmagazine.com/articles/")
                        .logo("https://inpsyde.com/wp-content/uploads/sites/2/2020/04/smashing-magazine_logo-cover_big@2x.jpg")
                        .build());
                add(Person.builder()
                        .name("web.dev")
                        .website("https://web.dev/blog/")
                        .logo("https://web-dev.imgix.net/image/FNkVSAX8UDTTQWQkKftSgGe9clO2/uZ3hQS2EPrA9csOgkoXI.png?auto=format&fit=max&w=1200&fm=auto")
                        .build());
                add(Person.builder()
                        .name("freeCodeCamp")
                        .website("https://www.freecodecamp.org/news/")
                        .logo("https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/FreeCodeCamp_logo.png/800px-FreeCodeCamp_logo.png")
                        .build());
                add(Person.builder()
                        .name("Reflectoring")
                        .website("https://reflectoring.io/")
                        .logo("https://reflectoring.io/images/logo_hu354e18ffd6bdfee5be8e3f497e7fb612_22876_150x0_resize_q90_h2_box_3.webp")
                        .build());
                add(Person.builder()
                        .name("Ahmad Shadeed")
                        .website("https://ishadeed.com/articles/")
                        .logo("https://ishadeed.com/assets/shadeed.jpg")
                        .build());
                add(Person.builder()
                        .name("CSS-Tricks")
                        .website("https://css-tricks.com/archives/")
                        .logo("https://css-tricks.com/wp-content/uploads/2019/06/akqRGyta_400x400.jpg")
                        .build());
                add(Person.builder()
                        .name("Digitalocean")
                        .website("https://www.digitalocean.com/community/tutorials")
                        .logo("https://upload.wikimedia.org/wikipedia/commons/7/79/DigitalOcean_logo.png")
                        .build());
                add(Person.builder()
                        .name("spring.io")
                        .website("https://spring.io/blog")
                        .logo("https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Spring_Framework_Logo_2018.svg/2560px-Spring_Framework_Logo_2018.svg.png")
                        .build());
                add(Person.builder()
                        .name("dev.to")
                        .website("https://dev.to/t/")
                        .logo("https://dev-to-uploads.s3.amazonaws.com/uploads/logos/resized_logo_UQww2soKuUsjaOGNB38o.png")
                        .build());
            }
        };
        personaService.savePersons(personas);
        log.info("ADDED ALL PERSONS TO DATABASE");
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadMidudevArticles(final WebDriver driver) {
        final Person midudev = personaService.getPersonByName("midudev");
        final List<Article> miduArticles = new ArrayList<>();

        try {
            driver.get(midudev.getWebsite());

            final List<WebElement> miduWebArticles = driver.findElements(By.className("home-article"));

            for (int i = 0; i < miduWebArticles.size(); i++) {
                final WebElement article = miduWebArticles.get(i);
                miduArticles.add(Article.builder()
                        .title(article.findElement(By.tagName("h2"))
                                .getText())
                        .date(DateUtils.parseStringDateToLocalDate(article.findElement(By.tagName("time"))
                                .getText()))
                        .url(article.getAttribute("href"))
                        .owner(midudev)
                        .build());
            }

            articleService.saveAllArticles(miduArticles);
            log.info("ADDED ALL midudev ARTICLES TO DATABASE");
        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadMidudevArticles: {}", e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadJamesSinclairArticles(final WebDriver driver) {
        final Person jSinclair = personaService.getPersonByName("James Sinclair");
        final List<Article> jSinclairArticles = new ArrayList<>();
        try {
            driver.get(jSinclair.getWebsite());

            final List<WebElement> jSinclairWebArticles = driver
                    .findElements(By.className("ArticleList-item"));

            for (int i = 0; i < 6; i++) {
                final WebElement article = jSinclairWebArticles.get(i);
                jSinclairArticles.add(Article.builder()
                        .title(article.findElement(By.tagName("h2")).findElement((By.tagName("a")))
                                .getText())
                        .date(DateUtils.parseStringDateToLocalDate2(article.findElement(By.tagName("time"))
                                .getText()))
                        .url(article.findElement(By.tagName("h2")).findElement((By.tagName("a"))).getAttribute("href"))
                        .owner(jSinclair)
                        .build());
            }

            articleService.saveAllArticles(jSinclairArticles);
            log.info("ADDED ALL James Sinclair ARTICLES TO DATABASE");
        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadJamesSinclairArticles: {}",
                    e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadBaeldungArticles(final WebDriver driver) {
        final Person baeldung = personaService.getPersonByName("Baeldung");
        final List<Article> baeldungArticles = new ArrayList<>();

        try {
            driver.get(baeldung.getWebsite());

            final List<WebElement> baeldungArchiveList = driver
                    .findElement(By.className("bca-archive__monthlisting")).findElements(By.tagName("li"));

            for (int i = 0; i < 10; i++) {
                final WebElement article = baeldungArchiveList.get(i);

                baeldungArticles.add(Article.builder()
                        .title(article.findElement(By.tagName("a"))
                                .getText())
                        .date(LocalDate.now())
                        .url(article.findElement(By.tagName("a")).getAttribute("href"))
                        .owner(baeldung)
                        .build());
            }

            baeldungArticles.forEach(article -> {
                driver.get(article.getUrl());
                article.setDate(
                        DateUtils.parseStringDateToLocalDate3(driver.findElement(By.className("updated")).getText()));
            });

            articleService.saveAllArticles(baeldungArticles);

            log.info("ADDED ALL Baeldung ARTICLES TO DATABASE");

        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadBaeldungArticles: {}",
                    e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadNicolasSchurmannArticles(final WebDriver driver) {
        final Person nicolas = personaService.getPersonByName("Nicolas Schurmann");
        final List<Article> nicolasArticles = new ArrayList<>();

        try {
            driver.get(nicolas.getWebsite());

            final List<WebElement> nicolasColumnsList = driver
                    .findElements(By.cssSelector("a[class*='postItem__Article']"));

            for (int i = 0; i < 11; i++) {
                final WebElement article = nicolasColumnsList.get(i);

                nicolasArticles.add(Article.builder()
                        .title(article.findElement(By.tagName("p"))
                                .getText())
                        .date(LocalDate.now())
                        .url(article.getAttribute("href"))
                        .owner(nicolas)
                        .build());
            }

            nicolasArticles.forEach(article -> {
                driver.get(article.getUrl());
                article.setDate(
                        DateUtils.parseStringDateToLocalDate3(
                                driver.findElement(By.cssSelector("p[class*='blog-post__']")).getText()));
            });

            articleService.saveAllArticles(nicolasArticles);

            log.info("ADDED ALL Nicolas Schurmann ARTICLES TO DATABASE");

        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadNicolasSchurmannArticles: {}",
                    e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadSmashingMagazineArticles(final WebDriver driver) {
        final Person smashingMagazine = personaService.getPersonByName("Smashing Magazine");
        final List<Article> smashingMagazineArticles = new ArrayList<>();

        try {
            driver.get(smashingMagazine.getWebsite());

            final List<WebElement> smashingMagazineArticleList = driver
                    .findElements(By.className("article--post"));

            for (int i = 0; i < 9; i++) {
                final WebElement article = smashingMagazineArticleList.get(i);

                smashingMagazineArticles.add(Article.builder()
                        .title(article.findElement(By.tagName("h2"))
                                .findElement(By.tagName("a"))
                                .getText())
                        .date(DateUtils
                                .parseStringDateToLocalDate3(article.findElement(By.className("article--post__content"))
                                        .findElement(By.className("article--post__teaser"))
                                        .findElement(By.tagName("time"))
                                        .getText()))
                        .url((article.findElement(By.tagName("h2"))
                                .findElement(By.tagName("a"))
                                .getAttribute("href")))
                        .owner(smashingMagazine)
                        .build());
            }

            articleService.saveAllArticles(smashingMagazineArticles);

            log.info("ADDED ALL Smashing Magazine ARTICLES TO DATABASE");

        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadSmashingMagazineArticles: {}",
                    e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadWebDevArticles(final WebDriver driver) {
        final Person webDev = personaService.getPersonByName("web.dev");
        final List<Article> webDevArticles = new ArrayList<>();

        try {
            driver.get(webDev.getWebsite());

            final List<WebElement> webDevArticleList = driver
                    .findElements(By.className("card"));

            for (int i = 0; i < 7; i++) {
                final WebElement article = webDevArticleList.get(i);

                webDevArticles.add(Article.builder()
                        .title(article.findElement(By.className("card__content"))
                                .findElement(By.tagName("h2")).findElement(By.tagName("a"))
                                .getText())
                        .date(DateUtils.parseStringDateToLocalDate3(article.findElement(By.className("gap-top-size-1"))
                                .findElement(By.className("card__avatars"))
                                .findElement(By.className("flow"))
                                .findElement(By.tagName("time"))
                                .getText()))
                        .url(article
                                .findElement(By.tagName("a"))
                                .getAttribute("href"))
                        .owner(webDev)
                        .build());
            }

            articleService.saveAllArticles(webDevArticles);

            log.info("ADDED ALL web.dev ARTICLES TO DATABASE");

        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadWebDevArticles: {}",
                    e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadFreeCodeCampArticles(final WebDriver driver) {
        final Person freeCodeCamp = personaService.getPersonByName("freeCodeCamp");
        final List<Article> freeCodeCampArticles = new ArrayList<>();

        try {
            driver.get(freeCodeCamp.getWebsite());

            final List<WebElement> freeCodeCampArticleList = driver
                    .findElements(By.className("post-card"));

            for (int i = 0; i < 7; i++) {
                final WebElement article = freeCodeCampArticleList.get(i);

                freeCodeCampArticles.add(Article.builder()
                        .title(article.findElement(By.className("post-card-title")).getText())
                        .date(LocalDate.now())
                        .url(article
                                .findElement(By.tagName("a"))
                                .getAttribute("href"))
                        .owner(freeCodeCamp)
                        .build());
            }

            freeCodeCampArticles.forEach(article -> {
                driver.get(article.getUrl());
                article.setDate(
                        DateUtils.parseStringDateToLocalDate3(
                                driver.findElement(By.className("post-full-meta-date")).getText()));
            });

            articleService.saveAllArticles(freeCodeCampArticles);

            log.info("ADDED ALL freeCodeCamp ARTICLES TO DATABASE");

        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadFreeCodeCampArticles: {}",
                    e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadReflectoringArticles(final WebDriver driver) {
        final Person reflectoring = personaService.getPersonByName("Reflectoring");
        final List<Article> reflectoringArticles = new ArrayList<>();

        try {
            driver.get(reflectoring.getWebsite());

            final List<WebElement> reflectoringArticleList = driver
                    .findElements(By.className("card-body"));

            for (int i = 0; i < reflectoringArticleList.size(); i++) {
                final WebElement article = reflectoringArticleList.get(i);

                reflectoringArticles.add(Article.builder()
                        .title(article.findElement(By.tagName("h3")).findElement(By.tagName("a")).getText())
                        .date(DateUtils.parseStringDateToLocalDate3(article.findElement(By.className(
                                "card-meta")).findElements(By.tagName(
                                        "li"))
                                .get(1).getText()))
                        .url(article.findElement(By.tagName("h3")).findElement(By.tagName("a")).getAttribute("href"))
                        .owner(reflectoring)
                        .build());
            }

            articleService.saveAllArticles(reflectoringArticles);

            log.info("ADDED ALL Reflectoring ARTICLES TO DATABASE");

        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadReflectoringArticles: {}",
                    e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadAhmadShadeedArticles(final WebDriver driver) {
        final Person ahmadShadeed = personaService.getPersonByName("Ahmad Shadeed");
        final List<Article> ahmadShadeedArticles = new ArrayList<>();

        try {
            driver.get(ahmadShadeed.getWebsite());

            final List<WebElement> ahmadShadeedArticleList = driver
                    .findElements(By.className("c-article"));

            for (int i = 0; i < 9; i++) {
                final WebElement article = ahmadShadeedArticleList.get(i);

                ahmadShadeedArticles.add(Article.builder()
                        .title(article.findElement(By.tagName("a")).findElement(By.tagName("h3")).getText())
                        .date(DateUtils.parseStringDateToLocalDate(
                                article.findElement(By.tagName("a")).findElement(By.tagName("time")).getText()))
                        .url(article.findElement(By.tagName("a")).getAttribute("href"))
                        .owner(ahmadShadeed)
                        .build());
            }

            articleService.saveAllArticles(ahmadShadeedArticles);

            log.info("ADDED ALL Ahmad Shadeed ARTICLES TO DATABASE");

        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadAhmadShadeedArticles: {}",
                    e.getMessage());
        }
    }

    @Retryable(value = Exception.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
    private void loadCSSTricksArticles(final WebDriver driver) {
        final Person cssTricks = personaService.getPersonByName("CSS-Tricks");
        final List<Article> cssTricksArticles = new ArrayList<>();

        try {
            driver.get(cssTricks.getWebsite());

            final List<WebElement> cssTricksArticleList = driver
                    .findElements(By.className("article-article"));

            for (int i = 0; i < 9; i++) {
                final WebElement article = cssTricksArticleList.get(i);

                cssTricksArticles.add(Article.builder()
                        .title(article.findElement(By.tagName("h2")).findElement(By.tagName("a")).getText())
                        .date(DateUtils.parseStringDateToLocalDate3(
                                article.findElement(By.className("author-row")).findElement(By.tagName("time"))
                                        .getText()))
                        .url(article.findElement(By.tagName("h2")).findElement(By.tagName("a")).getAttribute("href"))
                        .owner(cssTricks)
                        .build());
            }

            articleService.saveAllArticles(cssTricksArticles);

            log.info("ADDED ALL CSSTricks ARTICLES TO DATABASE");

        } catch (final Exception e) {
            log.error("Error with SeleniumExecutor.loadCSSTricksArticles: {}",
                    e.getMessage());
        }
    }
}
