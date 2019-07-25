package com.kota101.innstant.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HereResponse implements Serializable {
    @JsonProperty("geometries")
    private List<HereGeometry> hereGeometries;
    @JsonProperty("response_code")
    private String responseCode;

    @JsonCreator
    public HereResponse(@JsonProperty("geometries") List<HereGeometry> hereGeometries,
                        @JsonProperty("response_code") String responseCode) {
        this.hereGeometries = hereGeometries;
        this.responseCode = responseCode;
    }

    @Data
    private static class HereGeometry implements Serializable {
        @JsonProperty("attributes")
        private HereAttributes attributes;
        @JsonProperty("distance")
        private float distance;
        @JsonProperty("nearestLat")
        private double nearestLat;
        @JsonProperty("nearestLon")
        private double nearestLon;
        @JsonProperty("layerId")
        private String layerId;
        @JsonProperty("geometry")
        private String geometry;

        @JsonCreator
        public HereGeometry(@JsonProperty("attributes") HereAttributes attributes,
                            @JsonProperty("distance") float distance,
                            @JsonProperty("nearestLat") double nearestLat,
                            @JsonProperty("nearestLon") double nearestLon,
                            @JsonProperty("layerId") String layerId,
                            @JsonProperty("geometry") String geometry) {
            this.attributes = attributes;
            this.distance = distance;
            this.nearestLat = nearestLat;
            this.nearestLon = nearestLon;
            this.layerId = layerId;
            this.geometry = geometry;
        }

        @Data
        private static class HereAttributes implements Serializable {
            @JsonProperty("GEOMETRY_ID")
            private String geometryId;
            @JsonProperty("ROOM_ID")
            private String roomId;
            @JsonProperty("ROOM_NAME")
            private String roomName;
            @JsonProperty("OWNER_ID")
            private String ownerId;
            @JsonProperty("ROOM_TYPE")
            private String roomType;
            @JsonProperty("LOCATION")
            private String location;
            @JsonProperty("DESCRIPTION")
            private String description;
            @JsonProperty("RATING")
            private float rating;
            @JsonProperty("TOTAL_REVIEW")
            private int totalReview;

            @JsonCreator
            public HereAttributes(@JsonProperty("GEOMETRY_ID") String geometryId,
                                  @JsonProperty("ROOM_ID") String roomId,
                                  @JsonProperty("ROOM_NAME") String roomName,
                                  @JsonProperty("OWNER_ID") String ownerId,
                                  @JsonProperty("ROOM_TYPE") String roomType,
                                  @JsonProperty("LOCATION") String location,
                                  @JsonProperty("DESCRIPTION") String description,
                                  @JsonProperty("RATING") float rating,
                                  @JsonProperty("TOTAL_REVIEW") int totalReview) {
                this.geometryId = geometryId;
                this.roomId = roomId;
                this.roomName = roomName;
                this.ownerId = ownerId;
                this.roomType = roomType;
                this.location = location;
                this.description = description;
                this.rating = rating;
                this.totalReview = totalReview;
            }
        }
    }
}
