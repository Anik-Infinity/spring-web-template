package com.anik.app.dto.auth;

import com.anik.app.dto.BaseDto;
import com.anik.app.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponseDto extends BaseDto {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
