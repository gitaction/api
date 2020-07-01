package top.fsky.crawler.integration.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithJWTUserSecurityContextFactory.class)
public @interface WithMockJwtUser {

    String value();
}
