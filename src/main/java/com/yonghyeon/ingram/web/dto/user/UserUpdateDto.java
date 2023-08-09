package com.yonghyeon.ingram.web.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class UserUpdateDto {

    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotBlank
    private String email;

    private String website;
    private String bio;
    private String phonenum;
    private String gender;

    @Builder
    public UserUpdateDto(String name, String username, String email, String website, String bio, String phonenum, String gender) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.website = website;
        this.bio = bio;
        this.phonenum = phonenum;
        this.gender = gender;
    }
}
