package com.transaction.feingClient;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.transaction.config.FeignClientInterceptor;
import com.transaction.util.Constant;

import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

class FeignClientInterceptorTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletRequestAttributes servletRequestAttributes;

    @Mock
    private RequestTemplate requestTemplate;

    @InjectMocks
    private FeignClientInterceptor feignClientInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApply_withAuthorizationHeader() {
        when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getHeader(Constant.AUTHORIZATION)).thenReturn("Bearer token");
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);

        feignClientInterceptor.apply(requestTemplate);

        verify(requestTemplate).header(Constant.AUTHORIZATION, "Bearer token");
    }

    @Test
    void testApply_withoutAuthorizationHeader() {
        when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getHeader(Constant.AUTHORIZATION)).thenReturn(null);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);

        feignClientInterceptor.apply(requestTemplate);

        verify(requestTemplate, never()).header(eq(Constant.AUTHORIZATION), anyString());
    }

    @Test
    void testApply_noRequestAttributes() {
        RequestContextHolder.setRequestAttributes(null);

        feignClientInterceptor.apply(requestTemplate);

        verify(requestTemplate, never()).header(eq(Constant.AUTHORIZATION), anyString());
    }
}