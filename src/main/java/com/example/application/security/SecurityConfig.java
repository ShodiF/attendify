package com.example.application.security;

import com.example.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.application.Application.*;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll());
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService nusers() {
        String sql = "select * from users";
        String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
        String username = "postgres";
        String password = "password";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                String usern = rs.getString(1);
                uNames.add(usern);

                String passW = rs.getString(2);
                passCodes.add(passW);

                String uR = rs.getString(3);
                uRoles.add(uR);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<UserDetails> userDetailsList = new ArrayList<>();
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        for(int i =0; i<uNames.size(); i++){
            userDetailsList.add(User.builder()
                    .username(uNames.get(i))
                    .password("{noop}" + passCodes.get(i))
                    .roles(uRoles.get(i))
                    .build());
        }
        return new InMemoryUserDetailsManager(userDetailsList);
    }
}
