package top.fsky.crawler.adapter.inbound.rpc.payloads;

import top.fsky.crawler.application.utils.AppConstants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {
    @NotBlank(message = "'name'" + AppConstants.IS_EMPTY)
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank(message = "'username'" + AppConstants.IS_EMPTY)
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank(message = "'email'" + AppConstants.IS_EMPTY)
    @Size(max = 40)
    @Email(message = "'email'" + AppConstants.IS_INVALID)
    private String email;

    @NotBlank(message = "'password'" + AppConstants.IS_EMPTY)
    @Size(min = 6, max = 20)
    private String password;
}
