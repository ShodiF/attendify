package com.example.application.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsersRepo {

    private JdbcTemplate template;

    public JdbcTemplate getTemplate() {
        return template;
    }

    @Autowired
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public void save(Users user){
        String sql = "insert into users (username, password, role) values (?,?,?)";
        template.update(sql, user.getUsername(), user.getPassword(), user.getRole());
    }

    public List<Users> findAll(){

        String sql  = "select * from users";

        RowMapper<Users> mapper = new RowMapper<Users>() {
            @Override
            public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
                Users u = new Users();
                u.setUsername(rs.getString(1));
                u.setPassword(rs.getString(2));
                u.setRole(rs.getString(3));
                return u;
            }
        };

        List<Users> userList = template.query(sql, mapper);


        return userList;
    }
}
