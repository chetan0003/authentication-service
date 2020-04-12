package com.accrualify.resourses;

import com.accrualify.dao.CommonDao;
import com.accrualify.model.Credentials;
import com.accrualify.model.Response;
import com.accrualify.util.Constants;
import com.accrualify.util.JwtUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


/**
 * @author chetan dahule
 * @since Sunday, 27 March 2020
 */
@RestController
public class AuthenticateEndPointImpl {



    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CommonDao commonDao;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    Response register(@RequestBody Credentials userVO) throws AuthenticationException {
        Credentials credentials = commonDao.findByUserName(userVO.getUsername());
        Response response= new Response();
        if (bCryptPasswordEncoder.matches(userVO.getPassword(), credentials.getPassword())) {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userVO.getUsername(),userVO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtUtil.generateToken(authentication);
            response.setAccess_token(token);
            response.setStatus_code(HttpStatus.SC_OK);
            return response;
        } else {
            response.setError(Constants.UNAUTHORIZED_MSG);
            response.setStatus_code(HttpStatus.SC_UNAUTHORIZED);
            return response;
        }
    }
}
