package com.swyp.libri.global.oauth2;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


@Getter
public class CustomUserPrincipal implements OAuth2User {
    private final String id;
    private final String email;
    private final String name;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

//    public CustomUserPrincipal(Member member, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
//        this.id = member.getMemberId();
//        this.email = member.getEmail();
//        this.name = member.getMemberName();
//        this.attributes = attributes;
//        this.authorities = authorities;
//    }

    public CustomUserPrincipal(Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        this.id = attributes.get("sub").toString();
        this.email = attributes.get("email").toString();
        this.name = attributes.get("name").toString();
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return this.email;
    }
}
