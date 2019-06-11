package com.kota101.innstant.service;

import com.kota101.innstant.data.model.Room;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomService {
    Flux<Room> getRooms();

    Mono<Room> getRoomById(ObjectId roomId);

    Mono<Room> createRoom(Room room);

    Mono<Room> updateRoom(ObjectId roomId, Room room);

    Mono<Room> deleteRoom(ObjectId roomId);
}
