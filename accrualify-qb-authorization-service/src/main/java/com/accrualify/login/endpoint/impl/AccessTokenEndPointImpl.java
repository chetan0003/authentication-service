package com.accrualify.login.endpoint.impl;



import com.accrualify.login.endpoint.AccessTokenEndPoint;
import com.accrualify.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;


/**
 *@author chetan dahule
 * @since Sunday, 01 April 2020
 */
@RestController
public class AccessTokenEndPointImpl implements AccessTokenEndPoint
 {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    HttpServletRequest request;

    @Produces("application/json")
    @RequestMapping(value = { "/token" }, method = RequestMethod.GET)
    public Response getAccessToken(@RequestParam("profileId") Integer profileId) {
        return ((AccessTokenEndPoint)applicationContext.getBean(request.getAttribute("system")+"AccessTokenEndPointImpl")).getAccessToken(profileId);
    }
}
