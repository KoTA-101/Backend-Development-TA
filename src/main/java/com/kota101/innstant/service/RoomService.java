package com.kota101.innstant.service;

import com.kota101.innstant.data.model.Room;
import com.kota101.innstant.data.repository.RoomRepository;
import com.kota101.innstant.service.contract.RoomServiceContract;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class RoomService implements RoomServiceContract {
    private final RoomRepository repository;

    @Override
    public Flux<Room> getRooms() {
        return repository.findAll();
    }

    @Override
    public Mono<Room> getRoomById(ObjectId id) {
        return repository.findBy_id(id)
                .switchIfEmpty(Mono.error(new Exception("No Room found with id: " + id)));
    }

    @Override
    public Mono<Room> createRoom(Room room) {
        return repository.insert(room);
    }

    @Override
    public Mono<Room> updateRoom(ObjectId id, Room room) {
        return getRoomById(id).doOnSuccess(findRoom -> {
            findRoom.setName(room.getName());
            findRoom.setType(room.getType());
            findRoom.setLocation(room.getLocation());
            findRoom.setLatitude(room.getLatitude());
            findRoom.setLongitude(room.getLongitude());
            findRoom.setAmenities(room.getAmenities());
            findRoom.setDescription(room.getDescription());
            findRoom.setPrice(room.getPrice());
            findRoom.setDpPercentage(room.getDpPercentage());
        });
    }

    @Override
    public Mono<Room> deleteRoom(ObjectId id) {
        return getRoomById(id).doOnSuccess(room -> repository.delete(room).subscribe());
    }
}
