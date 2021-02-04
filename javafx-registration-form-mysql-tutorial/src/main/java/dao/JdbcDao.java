package dao;

import entity.DTO.UserDTO;

import java.sql.*;

public class JdbcDao {

    // Replace below database url, username and password with your actual database credentials
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/conference-management-system?useSSL=false";
    private static final String DATABASE_USERNAME = "root"; // username
    private static final String DATABASE_PASSWORD = "bibo1997"; //password to connect to mySQL of user root

    private static final String REGISTER_QUERY = "INSERT INTO user (username, password) VALUES (?, ?)";
    private static final String CHECK_LOGIN_QUERY = "SELECT username, password ,fk_user,fk_role FROM user " +
            "INNER JOIN user_role ON user.id = user_role.fk_user " +
            "where username = ? and password = ? and fk_role = ?";
    private static final String INSERT_USER_ROLE = "INSERT INTO user_role (fk_user, fk_role) VALUES (?, ?)";
    private static final String CHECK_USER_EXIST = "SELECT * from user INNER JOIN user_role where user.username = ? and " +
            "user_role.fk_role = ?";
    private static final String GET_USER_ID = "SELECT id from user where username = ?";


    public boolean registerUser(String emailId, String password, String role) throws SQLException {
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        int existId = checkUserExist(emailId, role);
        if (existId == 0) {
            try (Connection connection = DriverManager
                    .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
                 // Step 2:Create a statement using connection object
                 PreparedStatement preparedStatement = connection.prepareStatement(REGISTER_QUERY,
                         Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, emailId);
                preparedStatement.setString(2, password);

                System.out.println(preparedStatement);
                // Step 3: Execute the query or update query
                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                int generatedKey;
                if (rs.next()) {
                    generatedKey = rs.getInt(1);
                    if (role.equals("Author")) {
                        createRoleUser(generatedKey, 2);
                    } else {
                        createRoleUser(generatedKey, 3);
                    }
                }
            } catch (SQLException e) {
                // print SQL exception information
                printSQLException(e);
            }
            return true;
        } else if (existId != 0 && existId != -1) {
            if (role.equals("Author")) {
                createRoleUser(existId, 2);
            } else {
                createRoleUser(existId, 3);
            }
        }
        return false;
    }

    public void createRoleUser(int userId, int roleId) throws SQLException {
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_ROLE)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, roleId);

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public int checkUserExist(String username, String role) throws SQLException {
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_EXIST)) {
            preparedStatement.setString(1, username);
            int roleId = 0;
            if (role.equals("Author")) {
                roleId = 2;
            } else {
                roleId = 3;
            }
            preparedStatement.setInt(2, roleId);

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            int generatedKey;
            if (rs.next()) {
                generatedKey = rs.getInt("id");
                int roleIdDb = rs.getInt("fk_role");
                if (roleIdDb == roleId) {
                    return -1;
                }
            } else { //GET_USER_ID
                PreparedStatement preparedStatementUserId = connection.prepareStatement(GET_USER_ID);
                preparedStatementUserId.setString(1, username);
                System.out.println(preparedStatementUserId);
                rs = preparedStatementUserId.executeQuery();
                if (rs.next())
                    return rs.getInt("id");
            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return 0;
    }

    public boolean checkLogin(String emailId, String password, String role) throws SQLException {

        int roleId = role.equals("Author") ? 2 : 3;
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LOGIN_QUERY)) {
            preparedStatement.setString(1, emailId);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, roleId);

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful");
                return true;
            }
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
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