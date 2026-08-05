package com.yuumi.ecommerce.commons.dto;

public record Pageable(int page, int size, boolean pagingIgnored) {

  private static final int DEFAULT_SIZE = 20;
  private static final int MAX_SIZE = 100;

  public Pageable(int page, int size) {
    this(page, size, false);
  }

  public Pageable {
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
    return pagingIgnored ? 0 : page * size;
  }
}
