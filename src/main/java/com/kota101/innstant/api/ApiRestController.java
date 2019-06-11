package com.kota101.innstant.api;

import com.kota101.innstant.data.model.Room;
import com.kota101.innstant.data.model.User;
import com.kota101.innstant.service.RoomService;
import com.kota101.innstant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ApiRestController {
    private final UserService userService;
    private final RoomService roomService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> getUserById(@PathVariable("id") ObjectId id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> updateUser(@PathVariable ObjectId id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @PatchMapping("/users/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> modifyUserPassword(@PathVariable ObjectId id, @Valid @RequestBody String password) {
        return userService.modifyUserPassword(id, password);
    }

    @PatchMapping("/users/{id}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> modifyUserPin(@PathVariable ObjectId id, @Valid @RequestBody String pin) {
        return userService.modifyUserPin(id, pin);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> deleteUser(@PathVariable ObjectId id) {
        return userService.deleteUser(id);
    }

    @GetMapping(path = "/rooms")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping(path = "/rooms/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Room> getRoomById(@PathVariable("id") ObjectId roomId) {
        return roomService.getRoomById(roomId);
    }

    @PatchMapping("/users/{id}/rooms")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> createRoomThenModifyUserRooms(@PathVariable("id") ObjectId id, @Valid @RequestBody Room room) {
        Mono<Room> mono = roomService.createRoom(room);
        return userService.addUserRooms(id, Objects.requireNonNull(mono.block()).get_id());
    }

    @PutMapping(path = "/users/{userId}/rooms/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Room> updateRoom(@PathVariable("userId") ObjectId userId, @PathVariable("roomId") ObjectId roomId, @Valid @RequestBody Room room) {
        return roomService.updateRoom(roomId, room);
    }

    @PatchMapping(path = "/users/{userId}/rooms/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> deleteRoomThenModifyUserRooms(@PathVariable("userId") ObjectId userId, @PathVariable("roomId") ObjectId roomId) {
        Mono<Room> mono = roomService.deleteRoom(roomId);
        return userService.deleteUserRooms(userId, Objects.requireNonNull(mono.block()).get_id());
    }
}
