package main;

import dao.JdbcDao;
import entity.DTO.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ComboLoader {
    private ObservableList<String> obsStrings;

    public ComboLoader() {

        obsStrings = FXCollections.observableArrayList(createStrings());
    }

    private List<String> createStrings() {
        JdbcDao jdbcDao = new JdbcDao();
        return jdbcDao.getAllAuthor().stream()
                .map(UserDTO::getEmail)
                .collect(Collectors.toList());
    }

    //name of this method corresponds to itemLoader.items in xml.
    //if xml name was itemLoader.a this method should have been
    //getA(). A bit odd
    public ObservableList<String> getItems() {

        return obsStrings;
    }
}
