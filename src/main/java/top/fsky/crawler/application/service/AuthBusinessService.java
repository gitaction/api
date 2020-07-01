package top.fsky.crawler.application.service;

import top.fsky.crawler.application.concept.BusinessService;
import top.fsky.crawler.application.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthBusinessService implements BusinessService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthBusinessService(AuthenticationManager authenticationManager,
                               JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }
    
    public Authentication doAuthentication(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
    
    public String getToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication);
    }
}
