package com.yuumi.ecommerce.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

  private List<T> content;
  private PageMetadata page;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PageMetadata {
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
  }

  public static <T> PageResponse<T> of(List<T> content, PageQuery query, long totalElements) {
    int totalPages = query.size() == 0 ? 0 : (int) Math.ceil((double) totalElements / query.size());
    return PageResponse.<T>builder()
        .content(content)
        .page(PageMetadata.builder()
            .number(query.page())
            .size(query.size())
            .totalElements(totalElements)
            .totalPages(totalPages)
            .build())
        .build();
  }
}
