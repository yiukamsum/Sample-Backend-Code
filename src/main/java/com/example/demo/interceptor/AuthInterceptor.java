package com.example.demo.interceptor;

import java.security.Key;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.service.UserService;
import com.example.demo.util.CurrentUser;
import com.example.demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${jwt.key}")
    private String jwtKey;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUser currentUser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 請求送到Controller前執行，回傳一個布林值，如果是true通過攔截器，反之則否
        Map<String, Object> jwtPayload = this.getJWT(request);
        if(jwtPayload != null){
            User user = userService.findById(Long.valueOf((String) jwtPayload.get("jti")));
            if(user != null){
                currentUser.setCurrentUser(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Controller處理完後執行。
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        // 整個請求及回應結束後執行。
    }

    private Map<String, Object> getJWT(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null){
            Key secretKey = new SecretKeySpec(this.jwtKey.getBytes(), "HmacSHA256");

            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();
            token = token.replace("Bearer ", "");
            Claims claims = parser
                    .parseClaimsJws(token)
                    .getBody();
    
            return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return null;
    }
}
