//package com.digital.solution.generalservice.service.user;
//
//import com.digital.solution.generalservice.config.CustomTokenEnhancer;
//import com.digital.solution.generalservice.domain.dto.user.authorization.TokenExpiry;
//import com.digital.solution.generalservice.domain.dto.user.authorization.TokenResponse;
//import com.digital.solution.generalservice.exception.GenericException;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.infinispan.client.hotrod.RemoteCache;
//import org.infinispan.client.hotrod.RemoteCacheManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2RefreshToken;
//import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.ClientRegistrationException;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
//import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
//import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import javax.validation.constraints.NotNull;
//import java.security.Principal;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.concurrent.DelayQueue;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_ACCESS_TOKEN_EXPIRED;
//import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_CLIENT_NOT_VALID;
//import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INVALID_ACCESS_TOKEN;
//import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INVALID_TOKEN;
//import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.CLIENT_CREDENTIALS;
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.CLIENT_ID;
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.IS_LOGIN_SUSPECT;
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.IS_TRANSACTION_SESSION_SUCCESS;
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.SECRET;
//import static com.digital.solution.generalservice.domain.constant.user.AuthorizationConstant.TRANSACTION_SESSION;
//
//@Slf4j
//public class TokenService implements TokenStore, ResourceServerTokenServices {
//
//    @Autowired
//    @SuppressWarnings("all")
//    private CustomTokenEnhancer customTokenEnhancer;
//
//    @Autowired
//    @SuppressWarnings("all")
//    private RemoteCacheManager remoteCacheManager;
//
//    @SuppressWarnings("all")
//    @Value(value = "${auth.session.timeoutsec}")
//    private int sessionTimeout;
//
//    @SuppressWarnings("all")
//    @Value(value = "${auth.session.refreshsec}")
//    private int sessionRefresh;
//
//    @SuppressWarnings("all")
//    @Value(value = "${auth.session.flushinterval}")
//    private int flushInterval;
//
//    @SuppressWarnings("all")
//    private ClientDetailsService clientDetailsService;
//    private final DelayQueue<TokenExpiry> expiryQueue = new DelayQueue<>();
//    private static final String OAUTH_TOKEN_EXPIRY = TokenExpiry.class.getSimpleName();
//    private static final String OAUTH_ACCESS_TOKEN = OAuth2AccessToken.class.getSimpleName();
//    private static final String OAUTH_AUTHENTICATION = OAuth2Authentication.class.getSimpleName();
//    private final AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
//    private static final String SECOND_KEY = "resource_id";
//    private static final String TRANSACTIONAL_PROCESS = "|trx|";
//    private static final String OAUTH_REFRESH_TOKEN = "refreshTokenToAccessTokenStore";
//    private static final String OAUTH_ACCESS_REFRESH_TOKEN = "accessTokenToRefreshTokenStore";
//
//    private RemoteCache<Object, Object> getCache() {
//        return remoteCacheManager.getCache("session_clicks");
//    }
//
//    private boolean isTransactional(String token) {
//        return token.endsWith(TRANSACTIONAL_PROCESS);
//    }
//
//    private String getPlainToken(String token) {
//        return token.substring(0, token.length() - TRANSACTIONAL_PROCESS.length());
//    }
//
//    private String getSecondKey(OAuth2Authentication authentication) {
//        return authentication.getOAuth2Request().getRequestParameters().get(SECOND_KEY);
//    }
//
//    public void removeAccessToken(OAuth2AccessToken accessToken) {
//        this.removeAccessToken(accessToken.getValue());
//    }
//
//    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
//        getCache().put(OAUTH_REFRESH_TOKEN.concat(refreshToken.getValue()), refreshToken);
//        getCache().put(OAUTH_AUTHENTICATION.concat(refreshToken.getValue()), authentication);
//    }
//
//    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
//        return (OAuth2RefreshToken) getCache().get(OAUTH_REFRESH_TOKEN.concat(tokenValue));
//    }
//
//    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
//        return this.readAuthenticationForRefreshToken(token.getValue());
//    }
//
//    private OAuth2Authentication readAuthenticationForRefreshToken(String token) {
//        return (OAuth2Authentication) getCache().get(OAUTH_AUTHENTICATION.concat(token));
//    }
//
//    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
//        this.removeRefreshToken(refreshToken.getValue());
//    }
//
//    private void removeRefreshToken(String tokenValue) {
//        getCache().remove(OAUTH_REFRESH_TOKEN.concat(tokenValue));
//        getCache().remove(OAUTH_AUTHENTICATION.concat(tokenValue));
//        getCache().remove(OAUTH_REFRESH_TOKEN.concat(tokenValue));
//    }
//
//    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
//        this.removeAccessTokenUsingRefreshToken(refreshToken.getValue());
//    }
//
//    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
//        String key = this.authenticationKeyGenerator.extractKey(authentication);
//        String secondKey = getSecondKey(authentication);
//        OAuth2AccessToken accessToken = (OAuth2AccessToken) getCache().get(OAUTH_ACCESS_TOKEN.concat(key.concat(secondKey)));
//
//        if (accessToken != null && this.readAuthentication(accessToken.getValue()) != null
//                && !key.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken.getValue())))) {
//            this.storeAccessToken(accessToken, authentication);
//        }
//
//        return accessToken;
//    }
//
//    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
//        String accessToken = (String) getCache().remove(OAUTH_REFRESH_TOKEN.concat(refreshToken));
//        if (accessToken != null) {
//            this.removeAccessToken(accessToken);
//        }
//    }
//
//    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
//        Collection<OAuth2AccessToken> result = (Collection<OAuth2AccessToken>) getCache().
//                get(OAUTH_ACCESS_TOKEN.concat(this.getApprovalKey(clientId, userName)));
//        return (result != null ? Collections.unmodifiableCollection(result) : Collections.emptySet());
//    }
//
//    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
//        Collection<OAuth2AccessToken> result = (Collection<OAuth2AccessToken>) getCache().get(OAUTH_ACCESS_TOKEN.concat(clientId));
//        return (result != null ? Collections.unmodifiableCollection(result) : Collections.emptySet());
//    }
//
//    private void removeAccessToken(String tokenValue) {
//        OAuth2AccessToken removed = (OAuth2AccessToken) getCache().remove(OAUTH_ACCESS_TOKEN.concat(tokenValue));
//        getCache().remove(OAUTH_ACCESS_REFRESH_TOKEN.concat(tokenValue));
//        getCache().remove(OAUTH_TOKEN_EXPIRY.concat(tokenValue));
//        OAuth2Authentication getAuth = (OAuth2Authentication) getCache().get(OAUTH_AUTHENTICATION.concat(tokenValue));
//        String secondKey = StringUtils.EMPTY;
//        String clientId = StringUtils.EMPTY;
//        if (getAuth != null) {
//            secondKey = getSecondKey(getAuth);
//            clientId = getAuth.getOAuth2Request().getClientId();
//
//            getCache().remove(OAUTH_ACCESS_TOKEN.concat(this.authenticationKeyGenerator.extractKey(getAuth).concat(secondKey)));
//            getCache().remove(OAUTH_ACCESS_TOKEN.concat(this.getApprovalKey(clientId, secondKey)));
//        }
//
//        OAuth2Authentication authentication = (OAuth2Authentication) getCache().remove(OAUTH_AUTHENTICATION.concat(tokenValue));
//        if (authentication != null) {
//            getCache().remove(OAUTH_ACCESS_TOKEN.concat(this.authenticationKeyGenerator.extractKey(authentication).concat(secondKey)));
//
//            Collection<OAuth2AccessToken> tokens = (Collection<OAuth2AccessToken>) getCache()
//                    .get(OAUTH_ACCESS_TOKEN.concat(this.getApprovalKey(clientId, secondKey)));
//
//            if (tokens != null) {
//                tokens.remove(removed);
//            }
//
//            tokens = (Collection<OAuth2AccessToken>) getCache().get(OAUTH_ACCESS_TOKEN.concat(clientId));
//            if (tokens != null) {
//                tokens.remove(removed);
//            }
//
//            getCache().remove(OAUTH_ACCESS_TOKEN.concat(this.authenticationKeyGenerator.extractKey(authentication)));
//        }
//    }
//
//    private void flush() {
//        for (TokenExpiry expiry = this.expiryQueue.poll(); expiry != null; expiry = this.expiryQueue.poll()) {
//            this.removeAccessToken(expiry.getValue());
//        }
//    }
//
//    private void addToCollection(String key, OAuth2AccessToken token) {
//        Collection<OAuth2AccessToken> store = new HashSet<>();
//        store.add(token);
//        getCache().put(OAUTH_ACCESS_TOKEN.concat(key), store);
//        log.info("Adding to Collection Oauth2AccessToken, Store [{}], Key [{}]", store, key);
//    }
//
//    private String getApprovalKey(OAuth2Authentication authentication) {
//        String userName = authentication.getUserAuthentication() == null ? "" : authentication.getUserAuthentication().getName();
//        return this.getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
//    }
//
//    private String getApprovalKey(String clientId, String userName) {
//        String result = StringUtils.join(clientId, StringUtils.EMPTY);
//        if (StringUtils.isNotEmpty(userName)) {
//            result = StringUtils.join(clientId, ":", userName);
//        }
//
//        return result;
//    }
//
//    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
//        return this.readAuthentication(token.getValue());
//    }
//
//    public OAuth2Authentication readAuthentication(String token) {
//        return (OAuth2Authentication) getCache().get(OAUTH_AUTHENTICATION.concat(token));
//    }
//
//    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
//        log.info("Value token {}", flushInterval);
//        AtomicInteger flushCounter = new AtomicInteger(0);
//        if (flushCounter.incrementAndGet() >= this.flushInterval) {
//            this.flush();
//            flushCounter.set(0);
//        }
//        String secondKey = getSecondKey(authentication);
//        String clientId = authentication.getOAuth2Request().getClientId();
//
//        getCache().put(OAUTH_ACCESS_TOKEN.concat(token.getValue()), token);
//        getCache().put(OAUTH_AUTHENTICATION.concat(token.getValue()), authentication);
//        getCache().put(OAUTH_ACCESS_TOKEN.concat(this.authenticationKeyGenerator.extractKey(authentication).concat(secondKey)), token);
//
//        if (Boolean.FALSE.equals(authentication.isClientOnly())) {
//            this.addToCollection(this.getApprovalKey(authentication), token);
//        }
//
//        this.addToCollection(this.getApprovalKey(clientId, secondKey), token);
//
//        if (token.getExpiration() != null) {
//            TokenExpiry expiry = new TokenExpiry(token.getValue(), token.getExpiration());
//
//            getCache().remove(OAUTH_TOKEN_EXPIRY.concat(token.getValue()), expiry);
//            getCache().put(OAUTH_TOKEN_EXPIRY.concat(token.getValue()), expiry);
//        }
//
//        if (token.getRefreshToken() != null && token.getRefreshToken().getValue() != null) {
//            getCache().put(OAUTH_REFRESH_TOKEN.concat(token.getRefreshToken().getValue()), token.getValue());
//            getCache().put(OAUTH_ACCESS_REFRESH_TOKEN.concat(token.getValue()), token.getRefreshToken().getValue());
//        }
//    }
//
//    public OAuth2AccessToken readAccessToken(String tokenValue) {
//        log.info("key : {} + {}", OAUTH_ACCESS_TOKEN, tokenValue);
//        return (OAuth2AccessToken) getCache().get(OAUTH_ACCESS_TOKEN.concat(tokenValue));
//    }
//
//    public TokenResponse createAuthorization(@NotNull Long userId) {
//        log.info("start createAuthorization, request data : {}", userId);
//        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
//        map.add("client_id", CLIENT_ID);
//        map.add("client_secret", SECRET);
//        map.add("grant_type", CLIENT_CREDENTIALS);
//        map.add(SECOND_KEY, String.valueOf(userId));
//        return getAuthorizationRenew((Principal) map);
//    }
//
//    private TokenResponse getAuthorizationRenew(Principal principal) {
//        log.info("start getAuthorizationRenew, request data : {}", principal);
//        TokenResponse result;
//        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
//        OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();
//        String tokenValue = oauthDetails.getTokenValue();
//        boolean isTransactional = isTransactional(tokenValue);
//        if (Boolean.TRUE.equals(isTransactional)) {
//            tokenValue = getPlainToken(tokenValue);
//        }
//        OAuth2Authentication authentication = readAuthentication(tokenValue);
//        OAuth2AccessToken accessToken = readAccessToken(tokenValue);
//        Long userId = Long.parseLong(getSecondKey(authentication));
//
//        log.info("userId : {}", userId);
//        log.info("expiredTime : {}", accessToken.getExpiration());
//        log.info("transactionSessionRenew : {}", accessToken.getAdditionalInformation().get(IS_TRANSACTION_SESSION_SUCCESS));
//        log.info("transactionSessionRenew : {}", accessToken.getAdditionalInformation().get(TRANSACTION_SESSION));
//        if (Boolean.FALSE.equals(accessToken.isExpired())) {
//            //Session Renew
//            if (Boolean.TRUE.equals(isTransactional)) {
//                tokenValue = customTokenEnhancer.enhance(accessToken, authentication).getValue();
//                this.removeAccessToken(accessToken);
//            }
//            DefaultOAuth2AccessToken newToken = new DefaultOAuth2AccessToken(tokenValue);
//            newToken.setExpiration(new Date(System.currentTimeMillis() + (sessionTimeout * 1000L)));
//            newToken.setRefreshToken(null);
//            newToken.setScope(authentication.getOAuth2Request().getScope());
//            newToken.setAdditionalInformation(accessToken.getAdditionalInformation());
//            this.storeAccessToken(newToken, authentication);
//
//            result = TokenResponse.builder()
//                    .accessToken(tokenValue)
//                    .userId(userId)
//                    .isLoginSuspect((Boolean) accessToken.getAdditionalInformation().get(IS_LOGIN_SUSPECT))
//                    .transactionSessionMap(accessToken.getAdditionalInformation().get(TRANSACTION_SESSION))
//                    .isTransactionSessionSuccess((Boolean) accessToken.getAdditionalInformation().get(IS_TRANSACTION_SESSION_SUCCESS))
//                    .expiresIn(newToken.getExpiresIn())
//                    .build();
//        } else {
//            this.removeAccessToken(accessToken);
//            log.error("Access token expired : {}", accessToken);
//            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ACCESS_TOKEN_EXPIRED);
//        }
//
//        if (result == null || result.getIsLoginSuspect() != null) {
//            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_TOKEN);
//        }
//
//        return result;
//    }
//
//    @Override
//    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
//        if (isTransactional(accessTokenValue)) {
//            accessTokenValue = getPlainToken(accessTokenValue);
//        }
//        OAuth2AccessToken accessToken = this.readAccessToken(accessTokenValue);
//        if (accessToken == null) {
//            log.error("Invalid access token: {}", accessTokenValue);
//            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_ACCESS_TOKEN);
//        } else if (accessToken.isExpired()) {
//            this.removeAccessToken(accessToken);
//            log.error("Access token expired: {}", accessTokenValue);
//            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_ACCESS_TOKEN_EXPIRED);
//        }
//
//        OAuth2Authentication result = this.readAuthentication(accessToken);
//        if (result == null) {
//            // in case of race condition
//            log.error("Invalid access token: : {}", accessTokenValue);
//            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_ACCESS_TOKEN);
//        }
//        if (clientDetailsService != null) {
//            String clientId = result.getOAuth2Request().getClientId();
//            try {
//                clientDetailsService.loadClientByClientId(clientId);
//            } catch (ClientRegistrationException e) {
//                log.error("Client not valid: {}, message : {}", clientId, e.getMessage(), e);
//                throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_CLIENT_NOT_VALID);
//            }
//        }
//        return result;
//    }
//}
