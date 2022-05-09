package com.example.urlshortener.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "url_data")
@ToString
public class UrlEntity implements Serializable {

  @Id
  String key;

  @Column(name = "url", nullable = false)
  String url;

  @Column(name = "created", nullable = false)
  LocalDateTime created;
}
