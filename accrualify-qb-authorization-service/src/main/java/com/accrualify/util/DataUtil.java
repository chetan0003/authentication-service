package com.accrualify.util;

import com.accrualify.dao.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataUtil {
    private static final Map<Integer, String> systemsLoadMap = new HashMap();
    private static final Map<String,Integer> usersLoadMap = new HashMap<>();
    DataService dataService;

    public static Map<String,Integer> getUsersLoadMap() {
        return usersLoadMap;
    }
    public static Map<Integer, String> getSystemsLoadMap() {return systemsLoadMap;}

    @Autowired
    public DataUtil(DataService dataService) {
        this.dataService = dataService;
        usersLoadMap.putAll(dataService.getLoadUsersMap());
        systemsLoadMap.putAll(dataService.getLoadSystemMap());
    }
}

