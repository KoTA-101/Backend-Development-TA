package com.kota101.innstant.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "room")
public class Room {
    @Id
    @NonNull
    private ObjectId _id;
    private String name;
    private String type;
    private String location;
    private double latitude;
    private double longitude;
    private ArrayList<String> amenities;
    private String description;
    private int price;
    private int dpPercentage;
}
