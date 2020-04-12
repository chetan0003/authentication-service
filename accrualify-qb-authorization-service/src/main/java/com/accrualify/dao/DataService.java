package com.accrualify.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DataService {
    @Autowired
    CommonDao commonDao;

    public Map<Integer,String> getLoadSystemMap() {
        return commonDao.getSystemModuleDefaultsMap();
    }
    public Map<String,Integer> getLoadUsersMap() {
        return commonDao.getUsersModuleDefaultsMap();
    }
}
