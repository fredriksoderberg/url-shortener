package com.example.urlshortener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.entity.UrlRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = UrlShortenerService.class)
@EnableJpaRepositories(basePackageClasses = UrlRepository.class)
@EntityScan(basePackageClasses = UrlEntity.class)
@ContextConfiguration(
    initializers = UrlShortenerServiceTest.Initializer.class,
    classes = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    })
public class UrlShortenerServiceTest {

  @Autowired UrlShortenerService urlShortenerService;
  @Autowired UrlRepository urlRepository;
  @MockBean
  RandomKeyGenerator randomKeyGenerator;

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
      "postgres:12").withDatabaseName("postgres").withUsername("user")
      .withPassword("pass").withExposedPorts(5432);

  static class Initializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of("spring.datasource.url=" + postgres.getJdbcUrl(),
              "spring.datasource.username=" + postgres.getUsername(),
              "spring.datasource.password=" + postgres.getPassword(),
              "spring.datasource.driver-class-name=" + "org.postgresql.Driver")
          .applyTo(configurableApplicationContext.getEnvironment());
    }
  }

  @BeforeEach
  void clean() {
    urlRepository.deleteAll();
  }

  @Test
  public void shouldGetUrl() {
    urlRepository.save(buildUrlEntity());
    UrlEntity urlEntity = urlShortenerService.getUrl("qks5o");
    assertThat(urlEntity.getUrl(), is("https://www.dice.se"));
  }

  @Test void shouldCreateNewUrlEntity() {
    when(randomKeyGenerator.generate()).thenReturn("qks5o");
    UrlEntity requestUrlEntity = UrlEntity.builder().url("https://www.dice.se").build();
    UrlEntity createdUrlEntity = urlShortenerService.createShortUrl(requestUrlEntity); 
    assertThat(createdUrlEntity.getUrl(), is("https://www.dice.se"));
    assertThat(createdUrlEntity.getKey(), is("qks5o"));
  }

  @Test void shouldReturnExistingUrlEntity() {
    urlRepository.save(buildUrlEntity());
    UrlEntity requestUrlEntity = UrlEntity.builder().url("https://www.dice.se").build();
    UrlEntity createdUrlEntity = urlShortenerService.createShortUrl(requestUrlEntity);
    assertThat(createdUrlEntity.getUrl(), is("https://www.dice.se"));
    assertThat(createdUrlEntity.getKey(), is("qks5o"));
    verify(randomKeyGenerator, never()).generate();
  }
  
  private UrlEntity buildUrlEntity() {
    return UrlEntity.builder()
        .key("qks5o")
        .url("https://www.dice.se")
        .created(LocalDateTime.parse("2022-01-01T11:00:00"))
        .build();
  }
}
