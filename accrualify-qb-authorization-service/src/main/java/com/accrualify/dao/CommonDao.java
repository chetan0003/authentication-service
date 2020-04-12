package com.accrualify.dao;


import com.accrualify.model.Credentials;
import com.accrualify.util.AES_Util;
import com.accrualify.util.CustomPasswordEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Common Dao class providing attributes to Dao classes.
 *
 * @author chetan dahule
 * @since Sunday, 27 March 2016
 */
@Repository
public class CommonDao {
    private static final transient Log log = LogFactory.getLog(CommonDao.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @return the jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }


    /**
     * @param jdbcTemplate the jdbcTemplate to set
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public Map<String, Object> getTWCredentials(Integer profileId) {
        return getJdbcTemplate().queryForMap("select c.id,c.url,c.account,c.application_id,c.consumer_key,c.consumer_sec_key,c.token_id,c.token_sec_key,CASE when extract(epoch from (current_timestamp - c.refreshed_at))>expires_in then 'true' ELSE 'false' END as token_validity from profiles p join systems_credentials_mapping scm on scm.id=p.systems_credentials_id and p.id=? join credentials c on c.id=scm.credentials_id", profileId);
    }


    public Credentials findByUserName(String name) {
        String sql = "select name,password from users where name='" + name + "'";
        return getJdbcTemplate().query(sql, new ResultSetExtractor<Credentials>() {
            @Override
            public Credentials extractData(ResultSet rs) throws SQLException, DataAccessException {
                Credentials credentials = new Credentials();
                while (rs.next()) {
                    Credentials e = new Credentials();
                    e.setUsername(rs.getString("name"));
                    e.setPassword(customPasswordEncoder.encode(AES_Util.decrypt(rs.getString("password"))));
                    credentials = e;
                }
                return credentials;
            }
        });
    }

    public String findSystemByProfileId(Integer profileId) {
        String sql = "select systems.name from systems join systems_credentials_mapping scm on systems.id = scm.systems_id join profiles profiles on scm.id=profiles.systems_credentials_id where profiles.id='" + profileId + "'";
        return getJdbcTemplate().query(sql, new ResultSetExtractor<String>() {
            @Override
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                String name=null;
                while (rs.next()) {
                    name=rs.getString("name");
                }
                return name;
            }
        });
    }


    public Map<Integer,String> getSystemModuleDefaultsMap() {
        Map<Integer,String> systemModuleDefaultsMap = new TreeMap<Integer,String>();
        List<Map<String,Object>> data = getJdbcTemplate().queryForList("select p.id,s.name from systems s join systems_credentials_mapping scm on s.id = scm.systems_id join profiles p on p.systems_credentials_id=scm.id;");
        for(Map<String,Object> map:data){
            systemModuleDefaultsMap.put((Integer)map.get("id"),(String)map.get("name"));
        }
        return systemModuleDefaultsMap;
    }

    public Map<String,Integer> getUsersModuleDefaultsMap() {
        Map<String,Integer> systemModuleDefaultsMap = new TreeMap<String,Integer>();
        List<Map<String,Object>> data = getJdbcTemplate().queryForList("select u.name,p.id from profiles p join users u on p.user_id = u.id ;;");
        for(Map<String,Object> map:data){
            systemModuleDefaultsMap.put((String)map.get("name"),(Integer)map.get("id"));
        }
        return systemModuleDefaultsMap;
    }
}
