package com.gitaction.api.adapter.inbound.rpc.payloads;

import lombok.Data;

@Data
public class UserIdentityAvailability {
    private Boolean available;

    public UserIdentityAvailability(Boolean available) {
        this.available = available;
    }
}
