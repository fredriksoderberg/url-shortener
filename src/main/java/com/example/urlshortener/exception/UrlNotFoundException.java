package com.example.urlshortener.exception;

public class UrlNotFoundException extends RuntimeException {

  public UrlNotFoundException(String key) {
    super("Url not found for key: " + key);
  }
}
