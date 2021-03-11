package com.example.demo.security;

import com.example.demo.domain.UserVO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Log4j2
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserService user;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String socialType = registrationId.substring(0, 2).toUpperCase();
        boolean exist = false;
        boolean match = false;
        oAuth2User.getAttributes().forEach((k, v) -> log.info(k + " : " + v));

        UserVO vo = user.getByEmail(attributes.getEmail());

        if(vo == null) {
            session.setAttribute("registrationId", socialType);
        } else  {
            exist = true;

            if (vo.getUCode().equals(attributes.getEmail() + "/U/" + socialType)) {
                match = true;
            }
        }

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(exist ? (
                                match ? "U" : "E"
                        ) : "N")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
}
