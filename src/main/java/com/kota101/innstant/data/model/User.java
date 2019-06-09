package com.kota101.innstant.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    @NonNull
    private ObjectId _id;
    private String firstName;
    private String lastName;
    private String idCardNumber;
    private String phoneNumber;
    private String email;
    private String password;
    private BigInteger pin;
    private String profilePhoto;
    private String idCardPhoto;
    private String userWithIdCardPhoto;
    private ArrayList<ObjectId> rooms;
}
