package com.kota101.innstant.service.implementation;

import com.kota101.innstant.data.model.Room;
import com.kota101.innstant.data.repository.RoomRepository;
import com.kota101.innstant.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoomServiceImplementation implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public Flux<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Mono<Room> getRoomById(ObjectId roomId) {
        return roomRepository.findBy_id(roomId).switchIfEmpty(Mono.error(new Exception("No Room found with id: " + roomId)));
    }

    @Override
    public Mono<Room> createRoom(String ownerId, Room room) {
        room.setOwnerId(ownerId);
        return roomRepository.insert(room);
    }

    @Override
    public Mono<Room> updateRoom(String ownerId, ObjectId roomId, Room room) {
        return getRoomById(roomId).doOnSuccess(findRoom -> {
            findRoom.setName(room.getName());
            findRoom.setOwnerId(ownerId);
            findRoom.setType(room.getType());
            findRoom.setLocation(room.getLocation());
            findRoom.setLatitude(room.getLatitude());
            findRoom.setLongitude(room.getLongitude());
            findRoom.setAmenities(room.getAmenities());
            findRoom.setDescription(room.getDescription());
            findRoom.setPrice(room.getPrice());
            findRoom.setDpPercentage(room.getDpPercentage());
            findRoom.setRating(room.getRating());
            findRoom.setTotalReview(room.getTotalReview());
            findRoom.setPhotos(room.getPhotos());
            roomRepository.save(findRoom).subscribe();
        });
    }

    @Override
    public Mono<Room> deleteRoom(ObjectId roomId) {
        return getRoomById(roomId).doOnSuccess(room -> roomRepository.delete(room).subscribe());
    }
}
