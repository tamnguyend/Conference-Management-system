package dao;

import entity.DTO.UserDTO;

import java.sql.*;

public class JdbcDao {

    // Replace below database url, username and password with your actual database credentials
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/conference-management-system?useSSL=false";
    private static final String DATABASE_USERNAME = "root"; // username
    private static final String DATABASE_PASSWORD = "bibo1997"; //password to connect to mySQL of user root

    private static final String REGISTER_QUERY = "INSERT INTO user (email, password, firstname, lastname, university, role) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String CHECK_LOGIN_QUERY = "SELECT role FROM user where email = ? and password = ?";
    private static final String CHECK_USER_EXIST = "SELECT * from user where user.email = ?";


    public boolean registerUser(UserDTO userDTO) throws SQLException {
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        if (checkUserExist(userDTO.getEmail())) {
            try (Connection connection = DriverManager
                    .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                 // Step 2:Create a statement using connection object
                 PreparedStatement preparedStatement = connection.prepareStatement(REGISTER_QUERY)) {
                preparedStatement.setString(1, userDTO.getEmail());
                preparedStatement.setString(2, userDTO.getPassword());
                preparedStatement.setString(3, userDTO.getFirstName());
                preparedStatement.setString(4, userDTO.getLastName());
                preparedStatement.setString(5, userDTO.getUniversity());
                preparedStatement.setString(6, userDTO.getRoles());

                System.out.println(preparedStatement);
                // Step 3: Execute the query or update query
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                // print SQL exception information
                printSQLException(e);
            }
            return true;
        }
        return false;
    }


    public boolean checkUserExist(String username) throws SQLException {
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_EXIST)) {
            preparedStatement.setString(1, username);

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return true;
    }

    public String checkLogin(String emailId, String password) throws SQLException {

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LOGIN_QUERY)) {
            preparedStatement.setString(1, emailId);
            preparedStatement.setString(2, password);

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful");
                return rs.getString("role");
            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return null;
    }


    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}