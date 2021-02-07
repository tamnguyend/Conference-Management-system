package dao;

import entity.DTO.PaperDTO;
import entity.DTO.UserDTO;
import entity.DTO.UserResponseDTO;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class JdbcDao {

    // Replace below database url, username and password with your actual database credentials
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/conference-management-system?useSSL=false";
    private static final String DATABASE_USERNAME = "root"; // username
    private static final String DATABASE_PASSWORD = "bibo1997"; //password to connect to mySQL of user root

    private static final String REGISTER_QUERY = "INSERT INTO user (email, password, firstname, lastname, university, role) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String CHECK_LOGIN_QUERY = "SELECT * FROM user where email = ? and password = ?";
    private static final String CHECK_USER_EXIST = "SELECT * from user where user.email = ?";
    private static final String INSERT_PAPER = "INSERT INTO paper (abstract,title,keywords,userId,subject) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_AUTHOR = "SELECT * FROM user where role = ?";


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

    public UserResponseDTO checkLogin(String emailId, String password) throws SQLException {

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LOGIN_QUERY)) {
            preparedStatement.setString(1, emailId);
            preparedStatement.setString(2, password);

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful");
                UserResponseDTO userResponseDTO = new UserResponseDTO(rs.getString("email"),
                        rs.getString("role"),
                        rs.getInt("id"));
                return userResponseDTO;
            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return null;
    }

    public boolean upload(PaperDTO paperDTO) {
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PAPER)) {
            preparedStatement.setString(1, paperDTO.getAbstract());
            preparedStatement.setString(2, paperDTO.getTile());
            preparedStatement.setString(3, paperDTO.getKeyword());
            preparedStatement.setInt(4, paperDTO.getUploadedAuthorId());
            preparedStatement.setString(5, paperDTO.getSubject());

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return true;
    }

    public List<UserDTO> getAllAuthor() {
        List<UserDTO> ll = new LinkedList();
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_AUTHOR)) {
            preparedStatement.setString(1, "author");
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(rs.getString("email"));
                userDTO.setUserId(rs.getInt("id"));
                ll.add(userDTO);
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return ll;
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