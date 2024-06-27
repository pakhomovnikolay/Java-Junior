package org.example;

import lombok.Data;

import java.util.List;

@Data
public class ListResponse {

    private List<User> users;
}
