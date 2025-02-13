package com.swyp.libri.domain.test.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.swyp.libri.global.util.ApiDocumentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WithMockUser
@AutoConfigureRestDocs
@WebMvcTest(TestController.class)
class TestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private static final String TAG = "TEST";

	@Test
	void testGet() throws Exception {

		ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.get("/public/test")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.with(csrf().asHeader()));

		actions.andExpect(status().isOk())
			.andDo(ApiDocumentation.builder()
				.tag(TAG)
				.description("TEST API")
				.build())
			.andDo(print());
	}

}