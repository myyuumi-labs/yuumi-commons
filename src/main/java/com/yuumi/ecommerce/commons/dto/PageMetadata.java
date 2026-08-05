package com.yuumi.ecommerce.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageMetadata {

  private int page;
  private int size;

  @JsonProperty("total_elements")
  private long totalElements;

  @JsonProperty("total_pages")
  private int totalPages;

  @JsonProperty("has_next")
  private boolean hasNext;

  @JsonProperty("has_previous")
  private boolean hasPrevious;

  @JsonProperty("paging_ignored")
  private boolean pagingIgnored;

  public static PageMetadata of(Pageable pageable, long totalElements) {
    if (pageable.pagingIgnored()) {
      int itemCount = totalElements > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalElements;
      return PageMetadata.builder()
          .page(0)
          .size(itemCount)
          .totalElements(totalElements)
          .totalPages(1)
          .hasNext(false)
          .hasPrevious(false)
          .pagingIgnored(true)
          .build();
    }

    int totalPages = pageable.size() == 0 ? 0 : (int) Math.ceil((double) totalElements / pageable.size());
    int page = pageable.page();
    return PageMetadata.builder()
        .page(page)
        .size(pageable.size())
        .totalElements(totalElements)
        .totalPages(totalPages)
        .hasNext(totalPages > 0 && page < totalPages - 1)
        .hasPrevious(page > 0)
        .pagingIgnored(false)
        .build();
  }
}
