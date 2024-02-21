package com.anik.app.dto.user;

import com.anik.app.dto.BaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends BaseDto {
    @NotNull
    private String firstName;
    private String lastName;
    @NotNull
    private String phone;
    @Email
    private String email;
    private String token;
}
