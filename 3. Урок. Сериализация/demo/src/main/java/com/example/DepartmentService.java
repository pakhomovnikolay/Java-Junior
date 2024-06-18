package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table department (
                      id bigint,
                      department_name varchar(256),
                      primary key (id)
                    )
                    """);
        } catch (SQLException e) {
            System.err.println("Во время создания таблицы произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    public void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            StringBuilder insertQuery = new StringBuilder("insert into department(id, department_name) values\n");
            for (int i = 1; i <= 5; i++) {
                insertQuery.append(String.format("(%s, '%s')", i, "Department #" + i));

                if (i != 5) {
                    insertQuery.append(",\n");
                }
            }

            int insertCount = statement.executeUpdate(insertQuery.toString());
            System.out.println("Вставлено строк: " + insertCount);
        }
    }

    public void updateData(Connection connection, String newName, String id) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sqlCommand = "update department set department_name = '" + newName + "' where id = " + id;

            int updateCount = statement.executeUpdate(sqlCommand);
            System.out.println("Обновлено строк: " + updateCount);
        }
    }

    public List<String> selectNamesById(Connection connection, String id) throws SQLException {
        try (PreparedStatement statement = connection
                .prepareStatement("select department_name from department where id = ?")) {
            statement.setLong(1, Long.parseLong(id));
            ResultSet resultSet = statement.executeQuery();

            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("department_name"));
            }
            return names;
        }
    }

    public void selectData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                    select id, department_name
                    from department
                    """);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("department_name");
                System.out.println("Найдена строка: [id = " + id + ", department_name = " + name + "]");
            }
        }
    }
}
