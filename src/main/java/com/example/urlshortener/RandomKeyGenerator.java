package com.example.urlshortener;

import com.example.urlshortener.entity.UrlRepository;
import org.springframework.stereotype.Component;

@Component
public class RandomKeyGenerator implements UrlKeyGenerator {

  UrlRepository urlRepository;

  RandomKeyGenerator(UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  @Override
  public String generate() {
    String generatedKey = createRandomString();

    // Generate new string until no collision
    while (urlKeyExists(generatedKey)) {
      generatedKey = createRandomString();
    }
    return generatedKey;
  }

  protected String createRandomString() {
    String AlphaNumericString =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < URL_KEY_LENGTH; i++) {
      int index = (int) (AlphaNumericString.length() * Math.random());
      builder.append(AlphaNumericString.charAt(index));
    }
    return builder.toString();
  }

  private boolean urlKeyExists(final String urlKey) {
    return urlRepository.findById(urlKey).isPresent();
  }
}
