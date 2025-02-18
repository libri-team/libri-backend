package com.swyp.libri.domain.auth;

import com.swyp.libri.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/token")
    public Map<String, String> getToken(Principal principal) {
//        return jwtTokenProvider.createToken(principal.getName());
        String token = jwtTokenProvider.createToken("test@example.com");
//        memberRepository.findByEmail("test@example.com").orElseGet(() ->
//                memberRepository.save(Member.of("test", "test@example.com", "", ""))
//        );

        Map<String, String> response = new HashMap<>();
        response.put("token", "Bearer " + token);
        return response;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

        // HTTP 헤더에서 기존 액세스 토큰 가져오기
        String authorizationHeader = request.getHeader("Authorization");
        String existingAccessToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            existingAccessToken = authorizationHeader.substring(7);
        }

        // 기존 액세스 토큰이 유효하면 그대로 반환
        if (existingAccessToken != null && jwtTokenProvider.validateToken(existingAccessToken)) {
            return ResponseEntity.ok(Map.of("accessToken", existingAccessToken));
        }

        // HTTP-Only 쿠키에서 리프레시 토큰 가져오기
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // ️ 리프레시 토큰이 없거나 유효하지 않으면 403 반환
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Invalid or expired refresh token"));
        }

        // 리프레시 토큰이 유효하면 새 액세스 토큰 발급
        String email = jwtTokenProvider.getEmail(refreshToken);
        String newAccessToken = jwtTokenProvider.createToken(email);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
