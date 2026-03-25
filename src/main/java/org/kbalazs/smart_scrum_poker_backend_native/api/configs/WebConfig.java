package org.kbalazs.smart_scrum_poker_backend_native.api.configs;//package com.kbalazsworks.smartscrumpokerbackend.api.socket;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//class WebConfig implements Filter
//{
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//        throws IOException, ServletException
//    {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request  = (HttpServletRequest) req;
//        response.setHeader("Access-Control-Allow-Origin", "https://localhost:4200");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
//        response.setHeader(
//            "Access-Control-Allow-Headers",
//            "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, " +
//                "Access-Control-Request-Headers, Authorization, " +
//                "Authorization-Ids-Id-Token, Authorization-Ids-Access-Token"
//        );
//
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod()))
//        {
//            response.setStatus(HttpServletResponse.SC_OK);
//        }
//        else
//        {
//            chain.doFilter(req, res);
//        }
//    }
//
//
//    public void init(FilterConfig filterConfig)
//    {
//    }
//
//    public void destroy()
//    {
//    }
//}
