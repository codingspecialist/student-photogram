package com.yonghyeon.ingram.web.api;

import com.yonghyeon.ingram.config.auth.PrincipalDetails;
import com.yonghyeon.ingram.domain.user.User;
import com.yonghyeon.ingram.service.FollowService;
import com.yonghyeon.ingram.service.UserService;
import com.yonghyeon.ingram.web.dto.CMResponseDto;
import com.yonghyeon.ingram.web.dto.follow.FollowDto;
import com.yonghyeon.ingram.web.dto.user.UserPasswordUpdateDto;
import com.yonghyeon.ingram.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/api/user/{pageUserId}/following")
    public ResponseEntity<?> followingList(@PathVariable Long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<FollowDto> followDtos = followService.followingList(principalDetails.getUser().getId(), pageUserId);

        return new ResponseEntity<>(new CMResponseDto<>(1L, "팔로잉 리스트 가져오기 성공", followDtos), HttpStatus.OK);
    }

    @GetMapping("/api/user/{pageUserId}/follower")
    public ResponseEntity<?> followerList(@PathVariable Long pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<FollowDto> followDtos = followService.followerList(principalDetails.getUser().getId(), pageUserId);

        return new ResponseEntity<>(new CMResponseDto<>(1L, "팔로워 리스트 가져오기 성공", followDtos), HttpStatus.OK);
    }

    @PutMapping("/api/user/{id}")
    public CMResponseDto<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto updateDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

            User user = userService.userUpdate(id, updateDto);
            principalDetails.setUser(user);

            // 응답시에 user 엔터티의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답
            return new CMResponseDto<>(1L, "회원정보 변경 완료", user);
    }

    @PutMapping("/api/user/{id}/password")
    public CMResponseDto<?> passwordUpdate(
            @PathVariable Long id,
            @Valid UserPasswordUpdateDto updateDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User user = userService.userPasswordUpdate(id, updateDto);
        principalDetails.setUser(user);

        return new CMResponseDto<>(1L, "비밀번호 변경 완료", null);
    }

    @PutMapping("/api/user/{principalId}/profileImageUrl")
    public ResponseEntity<?> profileImageUpdate(
            @PathVariable Long principalId,
            MultipartFile profileImageFile,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User user = userService.userProfileUpdate(principalId, profileImageFile);

        principalDetails.setUser(user); // 세션 변경

        return new ResponseEntity<>(new CMResponseDto<>(1L,"프로필 사진 변경 완료",null), HttpStatus.OK);
    }
}