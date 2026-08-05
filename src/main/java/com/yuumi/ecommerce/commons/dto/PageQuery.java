package com.yuumi.ecommerce.commons.dto;

public record PageQuery(int page, int size) {

  private static final int DEFAULT_SIZE = 20;
  private static final int MAX_SIZE = 100;

  public PageQuery {
    if (page < 0) {
      page = 0;
    }
    if (size < 1) {
      size = DEFAULT_SIZE;
    }
    if (size > MAX_SIZE) {
      size = MAX_SIZE;
    }
  }

  public int offset() {
    return page * size;
  }
}
