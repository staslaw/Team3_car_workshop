package pl.coderslab.servlet;

import pl.coderslab.service.DbService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "CreateDatabase", urlPatterns = "/createDatabase")
public class CreateDatabase extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String query =
                "CREATE TABLE IF NOT EXISTS Client(" +
                        "id INT AUTO_INCREMENT NOT NULL, " +
                        "first_name VARCHAR(100), " +
                        "last_name VARCHAR(200), " +
                        "email VARCHAR(200) UNIQUE, " +
                        "phone VARCHAR(60), " +
                        "birthday VARCHAR(100), " +
                        "PRIMARY KEY(id))";


        try {
            DbService.executeUpdate(query,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
