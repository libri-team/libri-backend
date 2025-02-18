package com.swyp.libri.domain.auth;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.swyp.libri.global.jwt.JwtTokenProvider;
import com.swyp.libri.global.util.ApiDocumentation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import jakarta.servlet.http.Cookie;

@WithMockUser
@AutoConfigureRestDocs
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private static final String TAG = "AUTH";

    @Test
    void refreshAccessToken() throws Exception {
        Cookie refreshTokenCookie = new Cookie("refreshToken", "mockRefreshToken");

        given(jwtTokenProvider.validateToken("mockAccessToken")).willReturn(true);
        given(jwtTokenProvider.validateToken("mockRefreshToken")).willReturn(true);
        given(jwtTokenProvider.getEmail("mockRefreshToken")).willReturn("test@example.com");
        given(jwtTokenProvider.createToken("test@example.com")).willReturn("newAccessToken");

        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(refreshTokenCookie)
                .header("Authorization", "Bearer mockAccessToken")
                .with(csrf().asHeader()));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andDo(ApiDocumentation.builder()
                        .tag(TAG)
                        .description("Refresh Access Token API")
                        .build())
                .andDo(print());
    }
}