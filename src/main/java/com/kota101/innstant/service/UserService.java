package com.kota101.innstant.service;

import com.kota101.innstant.data.model.User;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<User> getUsers();

    Mono<User> getUserById(ObjectId userId);

    Mono<User> createUser(User user);

    Mono<User> updateUser(ObjectId userId, User user);

    Mono<User> modifyUserPassword(ObjectId userId, String password);

    Mono<User> modifyUserPin(ObjectId userId, String pin);

    Mono<User> addUserRooms(ObjectId userId, ObjectId roomId);

    Mono<User> deleteUserRooms(ObjectId userId, ObjectId roomId);

    Mono<User> deleteUser(ObjectId userId);
}
