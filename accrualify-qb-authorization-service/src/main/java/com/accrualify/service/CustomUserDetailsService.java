package com.accrualify.service;


import com.accrualify.dao.CommonDao;
import com.accrualify.model.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
/**
 * @author chetan dahule
 * @since  05 April 2020
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    CommonDao commonDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials user = commonDao.findByUserName(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }


}
