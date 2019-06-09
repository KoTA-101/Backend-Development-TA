package com.kota101.innstant.api;

import com.kota101.innstant.data.model.User;
import com.kota101.innstant.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigInteger;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserRestController {
    private final UserService service;

    @GetMapping(value = "/users")
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> getUsers() {
        log.debug("EndPoint: /users, Method: GET\nGet All User");
        return service.getUsers();
    }

    @GetMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> getUserById(@PathVariable("id") ObjectId id) {
        log.debug("EndPoint: /users/{}, Method: GET\nGet User with id: {}", id, id);
        return service.getUserById(id);
    }

    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@Valid @RequestBody User user) {
        log.debug("EndPoint: /users, Method: POST\nCreate User with user: {}", user);
        return service.createUser(user);
    }

    @PutMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> updateUser(@PathVariable ObjectId id, @Valid @RequestBody User user) {
        log.debug("EndPoint: /users/{}, Method: PUT\nUpdate User with id: {}, and user: {}", id, id, user);
        return service.updateUser(id, user);
    }

    @PatchMapping(value = "/users/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> modifyUserPassword(@PathVariable ObjectId id, @Valid @RequestBody String password) {
        log.debug("EndPoint: /users/{}/password, Method: PATCH\nModify User's Password with id: {}", id, id);
        return service.modifyUserPassword(id, password);
    }

    @PatchMapping(value = "/users/{id}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> modifyUserPin(@PathVariable ObjectId id, @Valid @RequestBody BigInteger pin) {
        log.debug("EndPoint: /users/{}/pin, Method: PATCH\nModify User's Pin with id: {}", id, id);
        return service.modifyUserPin(id, pin);
    }

    @DeleteMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> deleteUser(@PathVariable ObjectId id) {
        log.debug("EndPoint: /users/{}, Method: DELETE\nDelete User with id: {}", id, id);
        return service.deleteUser(id);
    }
}

