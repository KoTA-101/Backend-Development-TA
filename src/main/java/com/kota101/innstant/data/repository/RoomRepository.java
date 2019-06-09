package com.kota101.innstant.data.repository;

import com.kota101.innstant.data.model.Room;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoomRepository extends ReactiveMongoRepository<Room, String> {
    Mono<Room> findBy_id(ObjectId _id);
}
