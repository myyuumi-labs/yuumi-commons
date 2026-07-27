package com.yuumi.ecommerce.commons.observability.autoconfig;

import com.yuumi.ecommerce.commons.observability.http.CidFeignInterceptor;
import com.yuumi.ecommerce.commons.observability.web.CorrelationAndAccessLogFilter;
import feign.RequestInterceptor;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ObservabilityAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(
      name = "yuumi.observability.filter.enabled",
      havingValue = "true",
      matchIfMissing = true
  )
  public CorrelationAndAccessLogFilter correlationAndAccessLogFilter(ObjectProvider<Tracer> tracer) {
    return new CorrelationAndAccessLogFilter(tracer.getIfAvailable());
  }

  @ConditionalOnClass(RequestInterceptor.class)
  static class FeignCidConfig {

    @Bean(name = "cidFeignInterceptor")
    @ConditionalOnMissingBean(name = "cidFeignInterceptor")
    @ConditionalOnProperty(
        name = "yuumi.observability.feign.enabled",
        havingValue = "true",
        matchIfMissing = true
    )
    public RequestInterceptor cidFeignInterceptor() {
      return new CidFeignInterceptor();
    }
  }
}
