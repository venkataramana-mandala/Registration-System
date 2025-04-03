package com.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewRegistrations extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        out.println("<html><head><title>Registered Users</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background: linear-gradient(135deg, #74ebd5, #acb6e5); text-align: center; }");
        out.println("table { margin: auto; border-collapse: collapse; width: 80%; background: white; }");
        out.println("th, td { border: 1px solid black; padding: 10px; }");
        out.println("th { background: #007bff; color: white; }");
        out.println("td { background: #f9f9f9; }");
        out.println("</style></head><body>");
        out.println("<h2>Registered Users</h2>");
        out.println("<table><tr><th>Name</th><th>Email</th><th>Phone</th><th>Address</th></tr>");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "venkat", "venkat");
            ps = con.prepareStatement("SELECT name, email, phone, address FROM registration");
            rs = ps.executeQuery();

            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("phone") + "</td>");
                out.println("<td>" + rs.getString("address") + "</td></tr>");
            }

            out.println("</table>");
            out.println("<br><br><button onclick=\"window.location.href='Home.html'\" style=\"padding: 10px 20px; font-size: 16px; background: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;\">Go to Home</button>");
            out.println("</body></html>");
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("<h2>Error Fetching Data</h2>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        out.close();
    }
}
