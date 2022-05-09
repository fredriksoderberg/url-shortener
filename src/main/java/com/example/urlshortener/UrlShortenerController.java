package com.example.urlshortener;

import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.exception.BadRequestException;
import com.example.urlshortener.exception.UrlNotFoundException;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UrlShortenerController {

  UrlShortenerService urlShortenerService;

  UrlShortenerController(UrlShortenerService urlShortenerService) {
    this.urlShortenerService = urlShortenerService;
  }

  @GetMapping("/{urlKey}")
  public ResponseEntity<UrlEntity> redirectLongUrl(@PathVariable String urlKey) {
    try {
      validateUrlKeyLength(urlKey);
      UrlEntity urlEntity = urlShortenerService.getUrl(urlKey);
      return ResponseEntity
          .status(HttpStatus.TEMPORARY_REDIRECT)
          .header(HttpHeaders.LOCATION, urlEntity.getUrl())
          .body(urlEntity);
    } catch (UrlNotFoundException unfe) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", unfe);
    } catch (BadRequestException bre) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", bre);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error", e);
    }
  }

  @PostMapping("/url_shortener")
  public ResponseEntity<UrlEntity> createShortUrl(@RequestBody UrlEntity url) {
    try {
      validateUrlFormat(url.getUrl());
      UrlEntity urlEntity = urlShortenerService.createShortUrl(url);
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(urlEntity);
    } catch (BadRequestException bre) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request", bre);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error", e);
    }
  }

  private void validateUrlKeyLength(final String key) {
    if (key.length() != RandomKeyGenerator.URL_KEY_LENGTH) {
      throw new BadRequestException(
          "Wrong short url key length. Should be length: " + UrlKeyGenerator.URL_KEY_LENGTH);
    }
  }

  private void validateUrlFormat(final String url) {
    UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES);
    if (!urlValidator.isValid(url)) {
      throw new BadRequestException(
          "Wrong url format: " + url);
    }
  }
}
