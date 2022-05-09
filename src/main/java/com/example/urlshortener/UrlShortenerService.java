package com.example.urlshortener;

import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.entity.UrlRepository;
import com.example.urlshortener.exception.UrlNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlShortenerService {

  UrlRepository urlRepository;
  UrlKeyGenerator urlKeyGenerator;

  UrlShortenerService(UrlRepository urlRepository,
      UrlKeyGenerator urlKeyGenerator) {
    this.urlRepository = urlRepository;
    this.urlKeyGenerator = urlKeyGenerator;
  }

  @Cacheable(value="urls", key = "#urlKey")
  public UrlEntity getUrl(final String urlKey) {
    log.info("Getting url from database for: {}", urlKey);
    return urlRepository.findById(urlKey)
        .orElseThrow(() -> new UrlNotFoundException(urlKey));
  }

  public UrlEntity createShortUrl(final UrlEntity url) {
    //Check if url already exists
    Optional<UrlEntity> existingUrlEntity = urlRepository.findByUrl(url.getUrl());
    if(existingUrlEntity.isPresent()) {
      return existingUrlEntity.get();
    }
    
    UrlEntity urlEntityToSave = url.toBuilder()
        .created(LocalDateTime.now())
        .key(urlKeyGenerator.generate())
        .build();
    log.info("Creating in database url entity: {}", urlEntityToSave.toString());
    return urlRepository.save(urlEntityToSave);
  }
}
