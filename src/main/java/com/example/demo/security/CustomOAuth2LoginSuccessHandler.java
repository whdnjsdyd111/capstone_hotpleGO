package com.example.demo.security;

import com.example.demo.domain.UserVO;
import com.example.demo.service.implement.UserImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
            String email = null;
            String profile_img = null;
            Map<String, Object> attr = oAuth2User.getAttributes();
            try {
                email = attr.get("email").toString();
                profile_img = attr.get("picture").toString();
                log.info(profile_img);
            } catch (NullPointerException e) {

                Object obj = attr.get("kakao_account");
                if (obj instanceof Map<?, ?>) email = ((Map<String, Object>) obj).get("email").toString();
                obj = attr.get("properties");
                if (obj instanceof Map<?, ?>) profile_img = ((Map<String, Object>) obj).get("thumbnail_image").toString();

                log.info(profile_img);
            }

            UserVO vo = UserVO.builder().uCode(email).profileImg(profile_img).build();
            authentication.setAuthenticated(false);
            request.getSession().setAttribute("OAuthUser", vo);
            response.sendRedirect("/oauth2_subscription");
        } else if (auth.equals("U")) {
            response.sendRedirect("/");
        } else if (auth.equals("E")) {
            authentication.setAuthenticated(false);
            response.sendRedirect("/login?err=exist");
        }
    }
}
