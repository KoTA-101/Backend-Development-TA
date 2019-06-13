package com.kota101.innstant.service.implementation;

import com.kota101.innstant.data.model.Room;
import com.kota101.innstant.data.model.User;
import com.kota101.innstant.data.repository.UserRepository;
import com.kota101.innstant.security.CryptoGenerator;
import com.kota101.innstant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final CryptoGenerator cryptoGenerator;

    @Override
    public Flux<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> getUserById(ObjectId userId) {
        return userRepository.findBy_id(userId).switchIfEmpty(Mono.error(new Exception("No User found with ID: " + userId)));
    }

    @Override
    public Mono<User> createUser(User user) {
        user.setPassword(cryptoGenerator.generateHash(user.getPassword()));
        user.setPin(cryptoGenerator.generateHash(user.getPin()));
        return userRepository.insert(user);
    }

    @Override
    public Mono<User> updateUser(ObjectId userId, User user) {
        return getUserById(userId).doOnSuccess(findUser -> {
            findUser.setFirstName(user.getFirstName());
            findUser.setLastName(user.getLastName());
            findUser.setIdCardNumber(user.getIdCardNumber());
            findUser.setPhoneNumber(user.getPhoneNumber());
            findUser.setEmail(user.getEmail());
            findUser.setPassword(cryptoGenerator.generateHash(user.getPassword()));
            findUser.setPin(cryptoGenerator.generateHash(user.getPin()));
            findUser.setProfilePhoto(user.getProfilePhoto());
            findUser.setIdCardPhoto(user.getIdCardPhoto());
            findUser.setUserWithIdCardPhoto(user.getUserWithIdCardPhoto());
            userRepository.save(findUser).subscribe();
        });
    }

    @Override
    public Mono<User> modifyUserPassword(ObjectId userId, String password) {
        return getUserById(userId).doOnSuccess(findUser -> {
            findUser.setPassword(cryptoGenerator.generateHash(password));
            userRepository.save(findUser).subscribe();
        });
    }

    @Override
    public Mono<User> modifyUserPin(ObjectId userId, String pin) {
        return getUserById(userId).doOnSuccess(findUser -> {
            findUser.setPin(cryptoGenerator.generateHash(pin));
            userRepository.save(findUser).subscribe();
        });
    }

    @Override
    public Mono<User> addUserRooms(ObjectId userId, ObjectId roomId) {
        return getUserById(userId).doOnSuccess(findUser -> {
            List<Room> rooms = new ArrayList<>();
            if (findUser.getRooms() == null) {
                rooms.add(new Room(roomId));
            } else {
                rooms = findUser.getRooms();
                rooms.add(new Room(roomId));
            }
            findUser.setRooms(rooms);
            userRepository.save(findUser).subscribe();
        });
    }

    @Override
    public Mono<User> deleteUserRooms(ObjectId userId, ObjectId roomId) {
        return getUserById(userId).doOnSuccess(findUser -> {
            if (findUser.getRooms() != null) {
                for (Room room : findUser.getRooms()) {
                    if (room.get_id() == roomId) {
                        findUser.getRooms().remove(room);
                        break;
                    }
                }
            }
            userRepository.save(findUser).subscribe();
        });
    }

    @Override
    public Mono<User> deleteUser(ObjectId userId) {
        return getUserById(userId).doOnSuccess(user -> userRepository.delete(user).subscribe());
    }
}
