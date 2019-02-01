/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.contactlistspringmvc.dao;

import com.sg.contactlistspringmvc.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Roger Brock
 */
public class UserDaoDbImpl implements UserDao {

    private static final String SQL_INSERT_USER
            = "insert into users (username, password, enabled) values (?, ?, 1)";
    private static final String SQL_INSERT_AUTHORITY
            = "insert into authorities (username, authority) values (?, ?)";
    private static final String SQL_DELETE_USER
            = "delete from users where username = ?";
    private static final String SQL_DELETE_AUTHORITIES
            = "delete from authorities where username = ?";
    private static final String SQL_GET_ALL_USERS
            = "select * from users";

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User addUser(User newUser) {
        jdbcTemplate.update(SQL_INSERT_USER,
                newUser.getUsername(),
                newUser.getPassword());
        newUser.setId(jdbcTemplate.queryForObject("select LAST_INSERT_ID()",
                Integer.class));

        // now insert user's roles
        ArrayList<String> authorities = newUser.getAuthorities();
        for (String authority : authorities) {
            jdbcTemplate.update(SQL_INSERT_AUTHORITY,
                    newUser.getUsername(),
                    authority);
        }

        return newUser;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteUser(String username) {
        // first delete all authorities for this user
        jdbcTemplate.update(SQL_DELETE_AUTHORITIES, username);
        // second delete the user
        jdbcTemplate.update(SQL_DELETE_USER, username);
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(SQL_GET_ALL_USERS, new UserMapper());
    }

    private static final class UserMapper implements RowMapper<User> {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }

}
