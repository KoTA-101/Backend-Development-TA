package com.kota101.innstant.service.contract;

import com.kota101.innstant.data.model.User;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UserServiceContract {
    Flux<User> getUsers();

    Mono<User> getUserById(ObjectId id);

    Mono<User> createUser(User user);

    Mono<User> updateUser(ObjectId id, User user);

    Mono<User> modifyUserPassword(ObjectId id, String password);

    Mono<User> modifyUserPin(ObjectId id, BigInteger pin);

    Mono<User> deleteUser(ObjectId id);
}
