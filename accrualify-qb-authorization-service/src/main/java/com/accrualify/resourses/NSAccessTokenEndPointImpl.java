package com.accrualify.resourses;

import com.accrualify.dao.CommonDao;
import com.accrualify.login.endpoint.AccessTokenEndPoint;
import com.accrualify.model.Response;
import com.accrualify.util.AES_Util;
import com.accrualify.util.Constants;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.config.Environment;
import com.intuit.oauth2.config.OAuth2Config;
import com.intuit.oauth2.data.BearerTokenResponse;
import com.intuit.oauth2.exception.OAuthException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chetan dahule
 * @since  04 April 2020
 */
public class NSAccessTokenEndPointImpl implements AccessTokenEndPoint {

    private static final transient Log log = LogFactory.getLog(NSAccessTokenEndPointImpl.class);

    @Autowired
    CommonDao commonDao;

    @ResponseBody
    public Response getAccessToken(Integer profileId) {
        Response response=new Response();
        try {
            response.setAccess_token(getContext(profileId));
            response.setStatus_code(HttpStatus.SC_OK);
        }catch (OAuthException e){
            response.setStatus_code(HttpStatus.SC_UNAUTHORIZED);
            response.setError(e.getMessage());
            log.error("Error while calling bearer token :: " + e.getMessage());
            return response;
        }
        return response;
    }

    private String getContext(Integer profileId) throws OAuthException {

        Map<String, Object> list = commonDao.getTWCredentials(profileId);
        Integer credential_id = (Integer) list.get("id");
        String realmId = (String)list.get("account");
        String consumer_key = AES_Util.decrypt((String)list.get("consumer_key"));
        String consumer_sec_key = AES_Util.decrypt((String) list.get("consumer_sec_key"));
        String access_token = AES_Util.decrypt((String) list.get("token_id"));
        String refresh_token = AES_Util.decrypt((String) list.get("token_sec_key"));
        String isTokenValid = (String) list.get("token_validity");
        String url = (String) list.get("url");
        String accessToken = getAccessToken(access_token, refresh_token, consumer_key, consumer_sec_key, isTokenValid, credential_id);
        return accessToken;
    }

    public String getAccessToken(String access_token,String refresh_token, String consumer_key, String consumer_sec_key,String isTokenValid,Integer credential_id) throws OAuthException{
        Map<String,String> checked_access_token = getRefreshAccessToken(access_token,refresh_token,consumer_key,consumer_sec_key,isTokenValid);
        if (!checked_access_token.get("access_token").equals(access_token)) {
            updateAccessToken(credential_id, AES_Util.encrypt(checked_access_token.get("access_token")),AES_Util.encrypt(checked_access_token.get("refresh_token")));
        }
        return checked_access_token.get("access_token");
    }

    public static Map<String,String> getRefreshAccessToken(String accessToken, String refreshToken,String consumerKey, String consumerSecKey, String tokenValidity) throws OAuthException{
        Map<String, String> keysMap = new HashMap<>();
        if (tokenValidity.equals("true")) {
            boolean tokenCreateFlag = false;
            synchronized ( NSAccessTokenEndPointImpl.class) {
                if (!tokenCreateFlag){
                    try {
                        OAuth2PlatformClient client  = getClient(consumerKey,consumerSecKey);
                        BearerTokenResponse bearerTokenResponse = client.refreshToken(refreshToken);
                        keysMap.put("access_token",bearerTokenResponse.getAccessToken());
                        keysMap.put("refresh_token",bearerTokenResponse.getRefreshToken());
                    } catch (OAuthException e) {
                        log.error("Error while calling bearer token :: " + e.getMessage());
                        throw e;
                    }
                }
            }
        }else {
            keysMap.put("access_token",accessToken);
        }
        return keysMap;
    }

    private static OAuth2PlatformClient getClient(String consumerKey,String consumerSecKey){
        OAuth2Config oauth2Config = new OAuth2Config.OAuth2ConfigBuilder(consumerKey, consumerSecKey) //set client id, secret
                .callDiscoveryAPI(System.getProperty("env").equals("QA/STAGE")? Environment.SANDBOX:Environment.PRODUCTION) // call discovery API to populate urls
                .buildConfig();
        return new OAuth2PlatformClient(oauth2Config);
    }

    private void updateAccessToken(Integer credentialId, String accessToken, String refreshToken) {
        commonDao.getJdbcTemplate().update("update credentials set token_id=?,token_sec_key=?,refreshed_at='" + Constants.getDateString(new Date()) + "' where id=?", accessToken,refreshToken, credentialId);
    }
}
