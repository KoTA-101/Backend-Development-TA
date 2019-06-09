package com.kota101.innstant.api;

import com.kota101.innstant.data.model.Room;
import com.kota101.innstant.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomRestController {
    private final RoomService service;

    @GetMapping(value = "/rooms")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Room> getRooms() {
        log.debug("EndPoint: /rooms, Method: GET\nGet All Room");
        return service.getRooms();
    }

    @GetMapping(value = "/rooms/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Room> getRoomById(@PathVariable("id") ObjectId id) {
        log.debug("EndPoint: /users/{}, Method: GET\nGet User with id: {}", id, id);
        return service.getRoomById(id);
    }

    @PostMapping(value = "/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Room> createRoom(@Valid @RequestBody Room room) {
        log.debug("EndPoint: /rooms, Method: POST\nCreate Room with room: {}", room);
        return service.createRoom(room);
    }

    @PutMapping(value = "/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Room> updateRoom(@PathVariable("id") ObjectId id, @Valid @RequestBody Room room) {
        log.debug("EndPoint: /rooms/{}, Method: PUT\nUpdate User with id: {}, and room: {}", id, id, room);
        return service.updateRoom(id, room);
    }

    @DeleteMapping(value = "/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Room> deleteRoom(@PathVariable("id") ObjectId id) {
        log.debug("EndPoint: /rooms/{} with id: {}",id, id);
        return service.deleteRoom(id);
    }
}
