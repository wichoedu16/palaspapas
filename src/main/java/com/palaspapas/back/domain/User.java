package com.palaspapas.back.domain;

import com.palaspapas.back.domain.common.BaseDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends BaseDomain {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}