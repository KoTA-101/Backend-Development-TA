package com.kota101.innstant.service.contract;

import com.kota101.innstant.data.model.Room;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomServiceContract {
    Flux<Room> getRooms();

    Mono<Room> getRoomById(ObjectId id);

    Mono<Room> createRoom(Room room);

    Mono<Room> updateRoom(ObjectId id, Room room);

    Mono<Room> deleteRoom(ObjectId id);
}
