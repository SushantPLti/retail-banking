package com.rbanking.account.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rbanking.account.util.Constant;

import feign.RequestTemplate;

class FeignClientInterceptorTest {

	private FeignClientInterceptor feignClientInterceptor;

	@BeforeEach
	void setUp() {
		feignClientInterceptor = new FeignClientInterceptor();
	}

	@Test
	void testApply_withAuthorizationHeader() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.addHeader(Constant.AUTHORIZATION, "Bearer dummy-token");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

		RequestTemplate template = new RequestTemplate();
		feignClientInterceptor.apply(template);

		assertTrue(template.headers().containsKey(Constant.AUTHORIZATION));
		assertEquals("Bearer dummy-token", template.headers().get(Constant.AUTHORIZATION).iterator().next());
	}

	@Test
	void testApply_withoutAuthorizationHeader() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

		RequestTemplate template = new RequestTemplate();
		feignClientInterceptor.apply(template);

		assertFalse(template.headers().containsKey(Constant.AUTHORIZATION));
	}

	@Test
	void testApply_noAttributes() {
		RequestContextHolder.resetRequestAttributes();

		RequestTemplate template = new RequestTemplate();
		feignClientInterceptor.apply(template);

		assertFalse(template.headers().containsKey(Constant.AUTHORIZATION));
	}

}
