package com.kota101.innstant.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    @Field("_id")
    private ObjectId _id;

    @Field("user_id")
    private String userId;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("id_card_number")
    private String idCardNumber;

    @Field("phone_number")
    private String phoneNumber;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    @Field("pin")
    private String pin;

    @Field("profile_photo")
    private String profilePhoto;

    @Field("id_card_photo")
    private String idCardPhoto;

    @Field("user_with_id_card_photo")
    private String userWithIdCardPhoto;

    @DBRef
    @Field("rooms")
    private List<Room> rooms;
}
