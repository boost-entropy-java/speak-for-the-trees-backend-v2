package com.codeforcommunity.dto.emailer;

public class LoadTemplateResponse {
  String name;
  String template;
  String author;

  public LoadTemplateResponse(String template, String name, String author) {
    this.template = template;
    this.name = name;
    this.author = author;
  }

  public String getName() {
    return name;
  }

  public String getTemplate() {
    return template;
  }

  public String getAuthor() {
    return author;
  }
}
