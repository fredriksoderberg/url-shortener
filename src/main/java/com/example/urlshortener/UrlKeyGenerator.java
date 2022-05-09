package com.example.urlshortener;

public interface UrlKeyGenerator {

  int URL_KEY_LENGTH = 5;
  
  String generate();
}
