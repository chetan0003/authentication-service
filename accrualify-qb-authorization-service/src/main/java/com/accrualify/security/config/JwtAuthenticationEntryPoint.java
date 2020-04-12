package com.accrualify.security.config;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
/**
 * @author chetan dahule
 * @since Sunday, 04 April 2020
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        StringBuilder builder=new StringBuilder();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        builder.append("{");
        builder.append("\"status\": \""+HttpServletResponse.SC_UNAUTHORIZED+ "\",");
        builder.append("\"error\": \"Unauthorize\",");
        builder.append("\"message\": \""+authException.getMessage()+ "\",");
        builder.append("\"path\": \""+request.getRequestURI()+ "\",");
        builder.append("\"timestamp\": \""+ timestamp.toString()+ "\"");
        builder.append("}");
        response.getOutputStream().println(builder.toString());
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }


}