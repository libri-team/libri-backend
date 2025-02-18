package com.swyp.libri.global.oauth2;

import com.swyp.libri.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    //    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo userInfo = new OAuth2UserInfo(attributes);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // GOOGLE, KAKAO

//        Optional<Member> existingMember = memberRepository.findByProviderAndProviderId(provider, userInfo.getId());
//        Member member = existingMember.orElseGet(() -> {
//            Member newMember = Member.of(userInfo.getName(), userInfo.getEmail(), provider, userInfo.getId());
//            return memberRepository.save(newMember);
//        });

//        return new CustomUserPrincipal(member, attributes, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return new CustomUserPrincipal(attributes, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
