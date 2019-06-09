package com.kota101.innstant.service;

import com.kota101.innstant.security.CryptoGenerator;
import com.kota101.innstant.data.model.User;
import com.kota101.innstant.data.repository.UserRepository;
import com.kota101.innstant.service.contract.UserServiceContract;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceContract {
    private final UserRepository repository;
    private final CryptoGenerator cryptoGenerator;

    @Override
    public Flux<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public Mono<User> getUserById(ObjectId id) {
        return repository.findBy_id(id)
                .switchIfEmpty(Mono.error(new Exception("No User found with ID: " + id)));
    }

    @Override
    public Mono<User> createUser(User user) {
        user.setPassword(cryptoGenerator.generateHashedPassword(user.getPassword()));
        user.setPin(cryptoGenerator.generateHashedPin(user.getPin()));
        return repository.insert(user);
    }

    @Override
    public Mono<User> updateUser(ObjectId id, User user) {
        return getUserById(id).doOnSuccess(findUser -> {
            findUser.setFirstName(user.getFirstName());
            findUser.setLastName(user.getLastName());
            findUser.setIdCardNumber(user.getIdCardNumber());
            findUser.setPhoneNumber(user.getPhoneNumber());
            findUser.setEmail(user.getEmail());
            findUser.setPassword(cryptoGenerator.generateHashedPassword(user.getPassword()));
            findUser.setPin(cryptoGenerator.generateHashedPin(user.getPin()));
            findUser.setProfilePhoto(user.getProfilePhoto());
            findUser.setIdCardPhoto(user.getIdCardPhoto());
            findUser.setUserWithIdCardPhoto(user.getUserWithIdCardPhoto());
            findUser.setRooms(user.getRooms());
            repository.save(findUser).subscribe();
        });
    }

    @Override
    public Mono<User> modifyUserPassword(ObjectId id, String password) {
        return getUserById(id).doOnSuccess(findUser -> {
            findUser.setPassword(cryptoGenerator.generateHashedPassword(password));
            repository.save(findUser).subscribe();
        });
    }

    @Override
    public Mono<User> modifyUserPin(ObjectId id, BigInteger pin) {
        return getUserById(id).doOnSuccess(findUser -> {
            findUser.setPin(cryptoGenerator.generateHashedPin(pin));
            repository.save(findUser).subscribe();
        });
    }

    @Override
    public Mono<User> deleteUser(ObjectId id) {
        return getUserById(id).doOnSuccess(user -> repository.delete(user).subscribe());
    }
}
