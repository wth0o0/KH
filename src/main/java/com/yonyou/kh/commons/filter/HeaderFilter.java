package com.yonyou.kh.commons.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.util.StringUtils;

/**
 * request请求头filter
 * 
 * @author wangjian
 *
 */
public class HeaderFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;
		
		String origin = request.getHeader("Origin");
		if(!StringUtils.isEmpty(origin)){
			// 设置跨域请求Origin
			response.addHeader("Access-Control-Allow-Origin", origin);
		}
		
		//enable cookie
		response.setHeader("Access-Control-Allow-Credentials", "true");
		
		String headers = request.getHeader("Access-Control-Request-Headers");
		if(!StringUtils.isEmpty(headers)){
			// 设置请求头Headers
			response.addHeader("Access-Control-Allow-Headers", headers);
		}
		
		// 设置跨域请求方法
		response.setHeader("Access-Control-Allow-Methods", "*");  
		
		// 设置Option请求缓存时间
		response.setHeader("Access-Control-Max-Age", "3600");
		
		chain.doFilter(req, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
