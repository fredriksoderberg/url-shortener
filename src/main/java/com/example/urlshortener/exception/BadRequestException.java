package com.example.urlshortener.exception;

public class BadRequestException extends RuntimeException {
  public BadRequestException(String msg) {
    super("Bad request: " + msg);
  }
}
