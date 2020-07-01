package top.fsky.crawler.adapter.inbound.rest;

import top.fsky.crawler.application.model.UserProfile;
import top.fsky.crawler.application.service.UserBusinessService;
import top.fsky.crawler.application.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/users")
@Api(value = "user api", tags = "user api")
public class UserResource {
    private static final String USERNAME = "'username'";
    private final UserBusinessService userBusinessService;

    @Autowired
    public UserResource(UserBusinessService userBusinessService) {
        this.userBusinessService = userBusinessService;
    }
    
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("get user profile")
    public UserProfile getUserProfile(
            @NotEmpty(message = USERNAME + AppConstants.IS_EMPTY) 
            @PathVariable(value = "username") String username) {
        return userBusinessService.getUserProfileByUsername(username);
    }
}
