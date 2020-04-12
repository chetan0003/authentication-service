package com.accrualify.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author chetan  dahule
 * @since Tuesday, 11 March 2020
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private String access_token;
    private Integer status_code;
    private String error;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getStatus_code() {
        return status_code;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
