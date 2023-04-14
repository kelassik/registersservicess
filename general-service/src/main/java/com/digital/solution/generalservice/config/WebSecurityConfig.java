//package com.digital.solution.generalservice.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
//import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
//import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
//
//@Slf4j
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    @SuppressWarnings("all")
//    public WebResponseExceptionTranslator loggingExceptionTranslator() {
//        return new DefaultWebResponseExceptionTranslator() {
//            @Override
//            public ResponseEntity<OAuth2Exception> translate(Exception ex) throws Exception {
//                log.info("ERROR RESPONSE, Message [{}]", ex.getMessage());
//                ResponseEntity<OAuth2Exception> responseEntity = super.translate(ex);
//                HttpHeaders headers = new HttpHeaders();
//                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
//                OAuth2Exception excBody = responseEntity.getBody();
//                return new ResponseEntity<>(excBody, headers, responseEntity.getStatusCode());
//            }
//        };
//    }
//}
