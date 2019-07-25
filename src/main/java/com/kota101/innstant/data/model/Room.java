package com.kota101.innstant.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "room")
public class Room {
    @Id
    @Field("_id")
    private ObjectId _id;

    @Field("room_id")
    private String roomId;

    @Field("geometry_id")
    private long geometryId;

    @Field("name")
    private String name;

    @Field("owner_id")
    private String ownerId;

    @Field("type")
    private String type;

    @Field("location")
    private String location;

    @Field("latitude")
    private double latitude;

    @Field("longitude")
    private double longitude;

    @Field("amenities")
    private List<String> amenities;

    @Field("description")
    private String description;

    @Field("price")
    private int price;

    @Field("dp_percentage")
    private int dpPercentage;

    @Field("rating")
    private float rating;

    @Field("total_review")
    private int totalReview;

    @Field("photos")
    private List<String> photos;

    public Room(ObjectId id) {
        this._id = id;
    }
}
