package top.fsky.crawler.adapter.inbound.rpc.controller;

import top.fsky.crawler.adapter.inbound.rpc.payloads.UserIdentityAvailability;
import top.fsky.crawler.adapter.inbound.rpc.payloads.UserSummary;
import top.fsky.crawler.application.security.CurrentUser;
import top.fsky.crawler.application.security.UserPrincipal;
import top.fsky.crawler.application.service.UserBusinessService;
import top.fsky.crawler.application.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/users")
@Api(value = "user api", tags = "user api")
public class UserController {
    private static final String EMAIL = "'email'";
    private static final String USERNAME = "'username'";
    
    private final UserBusinessService userBusinessService;

    @Autowired
    public UserController(UserBusinessService userBusinessService) {
        this.userBusinessService = userBusinessService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("get current user")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
    }

    @GetMapping("/checkUsernameAvailability")
    @ApiOperation("check user name availability")
    public UserIdentityAvailability checkUsernameAvailability(
            @NotEmpty(message = USERNAME + AppConstants.IS_EMPTY)
            @RequestParam(value = "username") String username) {
        Boolean isAvailable = userBusinessService.isUsernameAvailable(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/checkEmailAvailability")
    @ApiOperation("check email availability")
    public UserIdentityAvailability checkEmailAvailability(
            @NotEmpty(message = EMAIL + AppConstants.IS_EMPTY)
            @Email(message = EMAIL + AppConstants.IS_INVALID)
            @RequestParam(value = "email")
                    String email) {
        Boolean isAvailable = userBusinessService.isEmailAvailable(email);
        return new UserIdentityAvailability(isAvailable);
    }
}
