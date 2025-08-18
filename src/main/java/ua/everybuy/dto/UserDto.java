package ua.everybuy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private Boolean isValid;
    private Long userId;
    private String email;
    private String phoneNumber;
    private List<String> roles;
}
