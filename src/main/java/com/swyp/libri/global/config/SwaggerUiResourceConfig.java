package com.swyp.libri.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerUiResourceConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// build/swagger-ui-sample 폴더를 /docs/** URL로 매핑
		registry.addResourceHandler("/docs/**")
			.addResourceLocations("file:" + System.getProperty("user.dir") + "/build/swagger-ui-sample/");
	}
}
