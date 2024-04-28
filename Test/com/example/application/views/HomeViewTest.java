package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.example.application.views.MainLayout.currentStudent;
import static org.junit.jupiter.api.Assertions.*;

class HomeViewTest {

    ArrayList<String> notification = new ArrayList<>();
    H3 message = new H3();
    HorizontalLayout h1 = new HorizontalLayout();
    Button reject = new Button("Decline Request!");
    Button mark = new Button("Mark Present");

    @BeforeEach
    void setUp() {
        notification.add("Student1");
        notification.add("Student2");
        notification.add("Student3");

        H4 warning = new H4("Please Enter the Correct Code");
        message.setText("User has requested that you mark their attendance for class");
        message.getElement().getStyle().setColor("Blue");
        DateTimePicker d1 = new DateTimePicker();
        d1.setLabel("Date and Time");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        d1.setValue(dateTime);
        TextField subject = new TextField();
        subject.setLabel("Subject");
        subject.setValue("English");
        subject.setReadOnly(true);
        TextField section = new TextField();
        section.setLabel("Section");
        TextField code = new TextField();
        code.setLabel("Code");
        h1.setVerticalComponentAlignment(FlexComponent.Alignment.END, mark);
        h1.setVerticalComponentAlignment(FlexComponent.Alignment.END, reject);

        section.setValue("English 1");
        section.setReadOnly(true);
        d1.setReadOnly(true);

        h1.add(d1, subject, section, code, mark, reject);
        warning.setVisible(false);

        reject.addClickListener(e ->{
            message.setText("Request has been denied.");

            h1.setVisible(false);
        });

        mark.addClickListener(e ->{
            message.setText("Attendance Marked");

            h1.setVisible(false);
        });
    }

    @Test
    void showNotification(){
        assertEquals("User has requested that you mark their attendance for class", message.getText());
        mark.click();
        assertEquals(message.getText(), "Attendance Marked");
        reject.click();
        assertEquals(message.getText(), "Request has been denied.");
    }

    @Test
    void changeView(){
        assertEquals(true, h1.isVisible());
        mark.click();
        assertEquals(false, h1.isVisible());
        h1.setVisible(true);
        reject.click();
        assertEquals(false, h1.isVisible());
    }

}