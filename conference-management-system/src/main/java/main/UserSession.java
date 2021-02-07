package main;

import java.util.HashSet;
import java.util.Set;

public final class UserSession {

    private static UserSession instance;

    private String userName;
    private int userId;
    private String privileges;

    public UserSession(String userName, int userId, String privileges) {
        this.userName = userName;
        this.userId = userId;
        this.privileges = privileges;
    }

    public static UserSession getInstance(String userName, int userId, String privileges) {
        if (instance == null) {
            instance = new UserSession(userName, userId, privileges);
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void cleanUserSession() {
        userId = 0;
        userName = "";// or null
        privileges = "";// or null
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", privileges=" + privileges +
                ", userId=" + userId +
                '}';
    }
}
