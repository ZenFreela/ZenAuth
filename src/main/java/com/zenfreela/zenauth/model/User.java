package com.zenfreela.zenauth.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter @Setter
public class User {

    @Id
    private String email;

    private String password;

}