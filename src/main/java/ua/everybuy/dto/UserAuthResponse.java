package ua.everybuy.dto;

import lombok.Getter;

@Getter
public class UserAuthResponse {
    private int status;
    private UserDto data;

}