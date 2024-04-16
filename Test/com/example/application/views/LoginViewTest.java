package com.example.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.testbench.LoginFormElement;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.security.core.parameters.P;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

class LoginViewTest {

    private static final String URL = "jdbc:postgresql://localhost:5432/AttendiftDBS";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    private LoginForm loginForm = new LoginForm();
    WebDriver driver;

    @BeforeEach
    void setUp(){
        System.setProperty("webdriver.chrome.driver", "/Users/shodif/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();

    }

    @Test
    void accessesDBS() {
        assertDoesNotThrow(() -> {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.close();
        }, "Connection to the database should be successful");
    }

    @Test
    void errorMessageShows(){
        driver.get("http://attendance-management-system.com/login?error");
        if(driver.getCurrentUrl().contains("error")){
            loginForm.setError(true);
        }
        else{
            loginForm.setError(false);
        }
        assertEquals(true, loginForm.isError());
    }

}