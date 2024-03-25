package com.example.application.views;

import com.example.application.data.Student;
import com.example.application.views.attendancesheet.AttendanceSheetView;
import com.example.application.views.attendancesheetteacher.AttendanceSheetTeacherView;
import com.example.application.views.profile.ProfileView;
import com.example.application.views.record.RecordView;
import com.example.application.views.schedule.ScheduleView;
import com.example.application.views.studentstats.StudentStatsView;
import com.example.application.views.teachersrecord.TeachersRecordView;
import com.example.application.views.usermanagement.UserManagementView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.vaadin.lineawesome.LineAwesomeIcon;
import com.example.application.security.SecurityService;

import javax.swing.text.html.ListView;
import java.sql.*;
import java.util.Collection;


@PermitAll
public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    private final HttpServletRequest request;


    private H2 viewTitle;
    public static Student currentStudent = new Student();

    public MainLayout(SecurityService securityService, HttpServletRequest request) {
        this.securityService = securityService;
        this.request = request;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");



        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);


        Button logout = new Button("Log Out " , e -> securityService.logout());

        var header = new HorizontalLayout(toggle, viewTitle, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(viewTitle);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);



        addToNavbar(header);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Attendify");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Welcome"));
        if(request.isUserInRole("USER")){
            nav.addItem(new SideNavItem("Attendance Sheet", AttendanceSheetView.class, LineAwesomeIcon.FILE_ALT.create()));
            nav.addItem(new SideNavItem("Record", RecordView.class, LineAwesomeIcon.CLIPBOARD_LIST_SOLID.create()));
            nav.addItem(new SideNavItem("Schedule", ScheduleView.class, LineAwesomeIcon.CALENDAR_ALT.create()));
            nav.addItem(new SideNavItem("Profile", ProfileView.class, LineAwesomeIcon.USER_GRADUATE_SOLID.create()));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String susername = userDetails.getUsername();
                currentStudent.setUsername(susername);

                String sql = "select * from students where username = '" + susername + "'";
                System.out.println(sql);
                String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
                String username = "postgres";
                String password = "password";
                try {
                    Connection con = DriverManager.getConnection(url, username, password);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    rs.next();
                    currentStudent.setFirstName(rs.getString(1));
                    currentStudent.setSurname(rs.getString(2));
                    currentStudent.setStudentID(rs.getString(4));
                    currentStudent.setEmail(rs.getString(5));
                    currentStudent.setBirthday(rs.getString(6));
                    currentStudent.setYearOfStudy(rs.getInt(7));

                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("THIS TEST FAILED");
            }

        }
        if(request.isUserInRole("TEACHER")){
            nav.addItem(new SideNavItem("Attendance Sheet Teacher", AttendanceSheetTeacherView.class,
                    LineAwesomeIcon.FILE_ALT.create()));
            nav.addItem(new SideNavItem("Teacher's Record", TeachersRecordView.class,
                    LineAwesomeIcon.CLIPBOARD_LIST_SOLID.create()));
            nav.addItem(new SideNavItem("Schedule", ScheduleView.class, LineAwesomeIcon.CALENDAR_ALT.create()));
            nav.addItem(new SideNavItem("Profile", ProfileView.class, LineAwesomeIcon.USER_GRADUATE_SOLID.create()));

        }
        if(request.isUserInRole("ADMIN")){
            nav.addItem(
                    new SideNavItem("Student Stats", StudentStatsView.class, LineAwesomeIcon.CHART_AREA_SOLID.create()));

            nav.addItem(new SideNavItem("User Management", UserManagementView.class, LineAwesomeIcon.USERS_SOLID.create()));
            nav.addItem(new SideNavItem("Profile", ProfileView.class, LineAwesomeIcon.USER_GRADUATE_SOLID.create()));
        }



        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }


}
