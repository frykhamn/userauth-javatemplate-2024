package com.auth.userserver.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

private String status;
private String message;
private UserDto user;

}
