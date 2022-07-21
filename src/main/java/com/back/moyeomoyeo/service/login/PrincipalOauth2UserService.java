package com.back.moyeomoyeo.service.login;

import com.back.moyeomoyeo.entity.member.Member;
import com.back.moyeomoyeo.repository.member.MemberRepository;
import com.back.moyeomoyeo.security.AuthorizedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String nickname = oAuth2User.getAttribute("name");

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("임시" + uuid);

        String email = oAuth2User.getAttribute("email");

        Member byLoginId = memberRepository.findByLoginId(username);

        if (byLoginId == null) {
            byLoginId = new Member(username, password, nickname, nickname, "", "");
            memberRepository.save(byLoginId);
        }

        return new AuthorizedUser(byLoginId,oAuth2User.getAttributes());

    }
}
