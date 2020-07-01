package com.gitaction.api.adapter.inbound.rpc.payloads;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignInRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
