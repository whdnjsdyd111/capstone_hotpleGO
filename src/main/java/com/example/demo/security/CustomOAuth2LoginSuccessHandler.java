package com.example.demo.security;

import com.example.demo.domain.UserVO;
import com.example.demo.service.implement.UserImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;

@Log4j2
@Component
@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserImpl user;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String auth = oAuth2User.getAuthorities().toArray()[0].toString();

        log.info(auth);

        if (auth.equals("N")) {
            UserVO vo = UserVO.builder().Ucode(oAuth2User.getAttributes().get("email").toString()).build();
            authentication.setAuthenticated(false);
            request.getSession().setAttribute("OAuthUser", vo);
            response.sendRedirect("/oauth2_subscription");
        } else if (auth.equals("U")) {
            response.sendRedirect("/");
        }
    }
}
