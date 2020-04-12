package com.accrualify.login.endpoint;

import com.accrualify.model.Response;
import org.springframework.http.ResponseEntity;

/**
 * @author chetan dahule
 * @since  28 March 2020
 */

public interface AccessTokenEndPoint {
    Response getAccessToken(Integer profileId);
}
