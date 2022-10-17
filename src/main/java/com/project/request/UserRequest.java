package com.project.request;

import lombok.Data;

@Data
public class UserRequest {
    private Long id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
}
