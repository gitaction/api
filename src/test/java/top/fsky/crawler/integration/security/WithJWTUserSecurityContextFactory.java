package top.fsky.crawler.integration.security;

import top.fsky.crawler.application.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;

final public class WithJWTUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtUser> {
    @Autowired
    private UserService userService;

    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser withUser) {
        String userName = withUser.value();
        Assert.hasLength(userName, "value() must be non empty String");
        UserPrincipal jwtUser = userService.loadUserByUsername(userName);
        String token = "FAKE_TOKEN";

        Authentication authentication = new PreAuthenticatedAuthenticationToken(jwtUser, token, jwtUser.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
