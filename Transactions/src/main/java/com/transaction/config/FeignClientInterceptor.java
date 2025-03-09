package com.transaction.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.transaction.util.Constant;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interceptor for Feign clients to add Authorization header from the current request.
 * @author Saurav Mishra
 */
@Component
public class FeignClientInterceptor implements RequestInterceptor{

    /**
     * Intercepts the outgoing request and adds the Authorization header 
     * from the current HTTP request, if available.
     *
     * @param template the request template
     */
	@Override
	public void apply(RequestTemplate template) {
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) 
				RequestContextHolder.getRequestAttributes();
		if(attributes != null) {
			HttpServletRequest req = attributes.getRequest();
			String authHeader = req.getHeader(Constant.AUTHORIZATION);
			if(authHeader != null) {
				template.header(Constant.AUTHORIZATION, authHeader);
			}
		}
		
	}

}
