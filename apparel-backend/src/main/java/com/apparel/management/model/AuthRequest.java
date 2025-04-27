package com.apparel.management.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
