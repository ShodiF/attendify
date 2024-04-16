package com.example.application.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

import java.awt.*;

@Route(value = "image", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class ImageView extends VerticalLayout {

    public ImageView() {
        // Create an image component
        Image image = new Image("https://static.vecteezy.com/system/resources/previews/006/460/116/non_2x/attendance-management-blue-gradient-concept-icon-tracking-discipline-at-work-control-productivity-employee-monitoring-abstract-idea-thin-line-illustration-isolated-outline-color-drawing-vector.jpg", "My Image");
        // Set the width and height of the image
        image.setWidth("1000px");
        image.setHeight("1000px");

        // Add the image component to the layout
        add(image);
    }
}