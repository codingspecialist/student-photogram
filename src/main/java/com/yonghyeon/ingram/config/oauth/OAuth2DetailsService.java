package com.yonghyeon.ingram.config.oauth;

import com.yonghyeon.ingram.config.auth.PrincipalDetails;
import com.yonghyeon.ingram.domain.user.User;
import com.yonghyeon.ingram.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // userRequest를 통해서 파싱해서 정보를 줌
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        Map<String, Object> userInfo = oAuth2User.getAttributes();

        // 중복을 피하기 위해 userInfo에서 sub를 가져와 사용
        String username = "google_"+(String)userInfo.get("sub"); // get(key값), 다운 캐스팅

        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String email = (String)userInfo.get("email");
        String name = (String)userInfo.get("name");

        User findUser = userRepository.findByEmail(email);

        // 이미 가입한 회원 구별하기
        if(findUser == null) {

            User user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .name(name)
                    .role("ROLE_GOOGLE_USER")
                    .build();

            return new PrincipalDetails(userRepository.save(user), userInfo);

        }else {
            return new PrincipalDetails(findUser, userInfo);
        }
    }
}
