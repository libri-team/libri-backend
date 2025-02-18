package com.swyp.libri.global.oauth2;

import java.util.Map;

public class OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        // 구글은 "sub", 카카오는 "id" 등을 사용할 수 있음
        if (attributes.containsKey("sub")) {
            return (String) attributes.get("sub"); // 구글
        } else if (attributes.containsKey("id")) {
            return (String) attributes.get("id"); // 카카오
        }
        throw new IllegalArgumentException("Unsupported provider or invalid attributes");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getName() {
        return (String) attributes.get("name");
    }
}
