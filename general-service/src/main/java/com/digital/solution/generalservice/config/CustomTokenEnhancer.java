//package com.digital.solution.generalservice.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@SuppressWarnings("all")
//public class CustomTokenEnhancer implements TokenEnhancer {
//
//    @Override
//    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
//        result.setValue(DigestUtils.sha256Hex(result.getValue()));
//        return result;
//    }
//}
