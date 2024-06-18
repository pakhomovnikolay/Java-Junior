package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Lesson_3 {

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            String updateDepartmentId = "1";
            String searchDepartmentId = "1";
            String personAge = "55";
            long personId = 101;

            System.out.println(
                    " ====================================== Работаем с департаментами ====================================== ");
            DepartmentService dService = new DepartmentService();
            dService.createTable(connection);
            dService.insertData(connection);
            dService.updateData(connection, "ДРПО", updateDepartmentId);
            dService.selectData(connection);
            System.out.println("Department с идентификатором " + searchDepartmentId + ": "
                    + dService.selectNamesById(connection, searchDepartmentId));

            System.out.println(
                    " ======================================= Работаем с сотрудниками ======================================= ");
            PersonService pService = new PersonService();
            pService.createTable(connection);
            pService.insertData(connection);
            pService.updateData(connection);
            pService.selectData(connection);
            System.out.println(
                    "Person с возрастом " + personAge + ": " + pService.selectNamesByAge(connection, personAge));

            System.out.println(
                    " ============================ Получаем данные депаратмета по внешниму ключу ============================ ");
            System.out.println("Найдена строка: [department_name = "
                    + pService.getPersonDepartmentName(connection, personId) + "]");

            pService.getPersonDepartments(connection)
                    .forEach((arg0, arg1) -> System.out
                            .println("Имя сотрудника = " + arg0 + "; Имя департамента = " + arg1));

            pService.getDepartmentPersons(connection).forEach((d, pList) -> {
                StringBuilder result = new StringBuilder("Имя департамента = " + d + "\n[ ");
                pList.forEach(p -> result.append(p + "; "));
                result.append("]\n");

                System.err.println(result.toString());
            });


            System.out.println(
                    " =========== Получаем данные депаратмета по внешниму ключу, при использовании класса обертки =========== ");

            System.out.println(" =================================================================== getEntityDepartmentPersons");
            System.out.println(pService.getEntityDepartmentPersons(connection));

            System.out.println(" ================================================================ getEntityPersonDepartmentName");
            System.out.println(pService.getEntityPersonDepartmentName(connection, personId));

            System.out.println(" =================================================================== getEntityPersonDepartments");
            System.out.println(pService.getEntityPersonDepartments(connection));
            

        } catch (SQLException e) {
            System.err.println("Во время подключения произошла ошибка: " + e.getMessage());
        }
    }
}

/**
 * С помощью JDBC, выполнить следующие пункты:
 * 1. Создать таблицу Person (скопировать код с семниара)
 * 2. Создать таблицу Department (id bigint primary key, name varchar(128) not
 * null)
 * 3. Добавить в таблицу Person поле department_id типа bigint (внешний ключ)
 * 4. Написать метод, который загружает Имя department по Идентификатору person
 * 5. * Написать метод, который загружает Map<String, String>, в которой маппинг
 * person.name -> department.name
 * Пример:
 * [
 * {"person #1", "department #1"},
 * {"person #2", "department #3}
 * ]
 * 6. ** Написать метод, который загружает Map<String, List<String>>, в которой
 * маппинг department.name -> <person.name>
 * Пример:
 * [
 * {"department #1", ["person #1", "person #2"] },
 * {"department #2", ["person #3", "person #4"] }
 * ]
 * 7. *** Создать классы-обертки над таблицами, и в пунктах 4, 5, 6 возвращать
 * объекты.
 */