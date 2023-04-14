//package com.digital.solution.generalservice.config;
//
//import com.digital.solution.generalservice.service.user.TokenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.StandardPasswordEncoder;
//import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.CLIENT_CREDENTIALS;
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.CLIENT_ID;
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.SECRET;
//
//@Configuration
//@EnableAuthorizationServer
//public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    @SuppressWarnings("all")
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    @SuppressWarnings("all")
//    private WebResponseExceptionTranslator<OAuth2Exception> loggingExceptionTranslator;
//
//    @Autowired
//    @SuppressWarnings("all")
//    private CustomTokenEnhancer customTokenEnhancer;
//
//    @SuppressWarnings("all")
//    @Value(value = "${auth.session.timeoutsec}")
//    private int sessionTimedOut;
//
//    @SuppressWarnings("all")
//    @Value(value = "${auth.session.refreshsec}")
//    private int sessionRefresh;
//
//    @SuppressWarnings("all")
//    private final StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer configure) {
//        configure.authenticationManager(authenticationManager)
//                .tokenStore(tokenService())
//                .tokenEnhancer(customTokenEnhancer)
//                .exceptionTranslator(loggingExceptionTranslator);
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) {
//        security.passwordEncoder(passwordEncoder)
//                .allowFormAuthenticationForClients()
//                .checkTokenAccess("isAuthenticated()");
//    }
//
//    @Override
//    @SuppressWarnings("all")
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient(CLIENT_ID)
//                .authorizedGrantTypes(CLIENT_CREDENTIALS)
//                .scopes("read", "write", "trust")
//                .secret(passwordEncoder.encode(SECRET))
//                .accessTokenValiditySeconds(sessionTimedOut).//Access token is only valid for 2 minutes.
//                refreshTokenValiditySeconds(sessionRefresh);
//    }
//
//    @Bean
//    @Primary
//    public TokenService tokenService() {
//        return new TokenService();
//    }
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public DefaultTokenServices tokenServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenService());
//        return defaultTokenServices;
//    }
//}
