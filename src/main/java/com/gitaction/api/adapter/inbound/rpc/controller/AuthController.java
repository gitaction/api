package com.gitaction.api.adapter.inbound.rpc.controller;

import com.gitaction.api.adapter.inbound.payloads.ApiResponse;
import com.gitaction.api.adapter.inbound.payloads.JwtAuthenticationResponse;
import com.gitaction.api.adapter.inbound.rpc.payloads.SignInRequest;
import com.gitaction.api.adapter.inbound.rpc.payloads.SignUpRequest;
import com.gitaction.api.application.model.User;
import com.gitaction.api.application.service.AuthBusinessService;
import com.gitaction.api.application.service.UserBusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@Api(value = "auth api", tags = "auth api")
public class AuthController {

    private final AuthBusinessService authBusinessService;
    private final UserBusinessService userBusinessService;
    
    @Autowired
    public AuthController(
            AuthBusinessService authBusinessService,
            UserBusinessService userBusinessService
    ){
        this.authBusinessService = authBusinessService;
        this.userBusinessService = userBusinessService;
    } 
    
    @PostMapping("/sign-in")
    @ApiOperation("user sign in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        Authentication authentication = authBusinessService.doAuthentication(signInRequest.getEmail(), signInRequest.getPassword());
        String jwt = authBusinessService.getToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/sign-up")
    @ApiOperation("new user sign up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (!userBusinessService.isUsernameAvailable(signUpRequest.getUsername())) {
            return new ResponseEntity(
                    new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!userBusinessService.isEmailAvailable(signUpRequest.getEmail())) {
            return new ResponseEntity(
                    new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User result = userBusinessService.createUser(
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(
                new ApiResponse(true, "User registered successfully")
        );
    }
}
