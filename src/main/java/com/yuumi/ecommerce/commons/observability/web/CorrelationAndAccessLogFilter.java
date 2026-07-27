package com.yuumi.ecommerce.commons.observability.web;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Order(1)
public class CorrelationAndAccessLogFilter extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(CorrelationAndAccessLogFilter.class);

  public static final String CID = "cid";
  public static final String TRACE_ID = "traceId";
  public static final String SPAN_ID = "spanId";

  private final Tracer tracer;

  public CorrelationAndAccessLogFilter(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    String cid = req.getHeader("X-Request-Id");
    if (cid == null || cid.trim().isEmpty()) {
      cid = UUID.randomUUID().toString();
    }

    long startNs = System.nanoTime();

    MDC.put(CID, cid);
    MDC.put("method", req.getMethod());
    MDC.put("path", req.getRequestURI());
    putTracingContextInMdc();

    res.setHeader("X-Request-Id", cid);

    try {
      LOG.info("ENTER controller cid={} method={} path={}", cid, MDC.get("method"), MDC.get("path"));
      chain.doFilter(req, res);
    } finally {
      long durMs = (System.nanoTime() - startNs) / 1_000_000L;

      MDC.put("status", Integer.toString(res.getStatus()));
      MDC.put("durMs", Long.toString(durMs));
      putTracingContextInMdc();

      res.setHeader("X-Request-Id", cid);

      LOG.info(
          "Exit controller cid={} method={} path={} status={} durMs={}",
          cid,
          MDC.get("method"),
          MDC.get("path"),
          MDC.get("status"),
          MDC.get("durMs")
      );

      MDC.clear();
    }
  }

  private void putTracingContextInMdc() {
    if (tracer == null) {
      return;
    }
    Span currentSpan = tracer.currentSpan();
    if (currentSpan == null) {
      return;
    }
    MDC.put(TRACE_ID, currentSpan.context().traceId());
    MDC.put(SPAN_ID, currentSpan.context().spanId());
  }
}
