package com.denisov.onbalance.auth.user;

import com.denisov.onbalance.security.JWTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter{
    JWTService jwtService = new JWTService();

    @Value("${auth.endpoints.protected}")
    private String[] enabled;

    //TODO: add deactivation
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {
        String r = req.getRequestURI();
        for(String str: enabled){
            System.out.println("Restricted: " + str);
        }

        for(String str : enabled){
            if(str.compareTo(req.getRequestURI())==0){
                String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
                System.out.println(authHeader);


                if(authHeader == null || authHeader == null){
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else if(!checkAuthorization(authHeader)){
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else{
                    filterChain.doFilter(req, res);
                }
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    private boolean checkAuthorization(String auth){

        if(!auth.startsWith("Bearer ")) {
            System.out.println("b case");
            return false;
        }

        String token = auth.substring(7);
        return jwtService.checkToken(token);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }
}
