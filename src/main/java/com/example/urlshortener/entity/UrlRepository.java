package com.example.urlshortener.entity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<UrlEntity, String> {
  Optional<UrlEntity> findByUrl(String url);
}
