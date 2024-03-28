package com.example.application.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


class SecurityConfigTest {

    private static final String URL = "jdbc:postgresql://localhost:5432/AttendiftDBS";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    private static final String USERNAME1 = "user1";
    private static final String USERNAME2 = "user2";
    private static final String PASSWORD2 = "password";
    private static final List<String> ROLES1 = Arrays.asList("ROLE_1");
    private static final List<String> ROLES2 = Arrays.asList("ROLE_2", "ROLE_3");

    @Test
    void databaseAccessWorks() {
        assertDoesNotThrow(() -> {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.close();
        }, "Connection to the database should be successful");
    }

    @Test
    public void testUsersAddedToInMemoryUserDetailsManager() {
        UserDetails user1 = User.builder()
                .username(USERNAME1)
                .password("{noop}" + PASSWORD2)
                .roles(String.valueOf(ROLES1))
                .build();

        UserDetails user2 = User.builder()
                .username(USERNAME2)
                .password("{noop}" + PASSWORD2)
                .roles(String.valueOf(ROLES2))
                .build();

        UserDetailsService userDetailsService = new InMemoryUserDetailsManager(Arrays.asList(user1, user2));

        assertEquals(USERNAME1, userDetailsService.loadUserByUsername(USERNAME1).getUsername(), "The users should match");
        assertEquals(USERNAME2, userDetailsService.loadUserByUsername(USERNAME2).getUsername(), "The roles should match");
    }
}