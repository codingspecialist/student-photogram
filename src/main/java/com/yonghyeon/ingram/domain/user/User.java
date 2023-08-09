package com.yonghyeon.ingram.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yonghyeon.ingram.domain.image.Image;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private String phonenum;

    private String gender;

    private String website;

    private String bio;

    private String profileImageUrl;

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private String role; // 권한 -> ROLE_USER, ROLE_GOOGLE_USER

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user"}) // Image의 user필드는 무시하고 파싱(무한 참조 방지)
    private List<Image> images;

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

    @Builder
    public User(String username, String password, String email, String name, String phonenum, String gender, String website, String bio) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phonenum = phonenum;
        this.gender = gender;
        this.website = website;
        this.bio = bio;
    }

    public void update(String name, String username, String email, String phonenum, String gender, String website, String bio) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phonenum = phonenum;
        this.gender = gender;
        this.website = website;
        this.bio = bio;
    }

    public void passwordUpdate(String password) {
        this.password = password;
    }

}
