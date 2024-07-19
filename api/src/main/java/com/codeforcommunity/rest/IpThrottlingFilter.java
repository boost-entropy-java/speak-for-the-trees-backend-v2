package com.codeforcommunity.rest;

import static com.codeforcommunity.rest.ApiRouter.end;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.TokensInheritanceStrategy;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;

public class IpThrottlingFilter implements Handler<RoutingContext> {
  private final BucketConfiguration config;
  private Map<String, Bucket> buckets;

  public IpThrottlingFilter(BucketConfiguration config) {
    this.config = config;
    this.buckets = new HashMap<>();
  }

  @Override
  public void handle(RoutingContext ctx) {
    String ip = ctx.request().remoteAddress().toString();
    System.out.println(buckets);
    Bucket bucket = this.buckets.get(ip);
    System.out.println(bucket);

    if (bucket == null) {
      bucket = Bucket.builder().build();
      bucket.replaceConfiguration(this.config, TokensInheritanceStrategy.RESET);
      this.buckets.put(ip, bucket);
    }

    if (bucket.tryConsume(1)) {
      ctx.next();
    } else {
      end(ctx.response(), 429, "Too many requests", "text/plain");
    }
  }
}
