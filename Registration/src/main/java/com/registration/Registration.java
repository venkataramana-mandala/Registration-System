package com.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Registration extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection con = null;
        PreparedStatement ps = null;
        
        if (!isValidPassword(password)) {
            out.println("<h3>Password must be 8-15 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special symbol.</h3>");
            return;
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "venkat", "venkat");
            ps = con.prepareStatement("INSERT INTO registration (name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, password);

            int result = ps.executeUpdate();
            
            if (result > 0) {
                response.sendRedirect("Login.html");
                }
            
        } 
        
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("RegistrationError.html");
            
        }
        
        finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        out.close();
    }
    
    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 15) {
            return false;
        }
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if (!Character.isLetterOrDigit(ch)) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}