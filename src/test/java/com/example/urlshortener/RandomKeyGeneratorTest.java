package com.example.urlshortener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.entity.UrlRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = RandomKeyGenerator.class)
public class RandomKeyGeneratorTest {

  @Autowired RandomKeyGenerator randomKeyGenerator;
  @MockBean UrlRepository urlRepository;

  @Test
  public void shouldGenerateNewRandomString() {
    when(urlRepository.findById(any())).thenReturn(
        Optional.of(UrlEntity.builder().key("same_key").build()), Optional.empty());
    randomKeyGenerator.generate();
    verify(urlRepository, times(2)).findById(any());
  }
  
  @Test
  public void shouldGenerateCorrectLength() {
    when(urlRepository.findById(any())).thenReturn(Optional.empty());
    String urlKey = randomKeyGenerator.generate();
    assertThat(urlKey.length(), is(UrlKeyGenerator.URL_KEY_LENGTH));
  }
}
