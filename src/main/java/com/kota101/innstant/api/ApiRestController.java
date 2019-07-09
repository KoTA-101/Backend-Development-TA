package com.kota101.innstant.api;

import com.kota101.innstant.data.model.Room;
import com.kota101.innstant.data.model.Transaction;
import com.kota101.innstant.data.model.User;
import com.kota101.innstant.properties.StorageProperties;
import com.kota101.innstant.service.FileStorageService;
import com.kota101.innstant.service.RoomService;
import com.kota101.innstant.service.TransactionService;
import com.kota101.innstant.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiRestController {
    private final UserService userService;
    private final RoomService roomService;
    private final TransactionService transactionService;
    private final FileStorageService fileStorageService;

    /*User Endpoints*/

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Flux<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<User> getUserById(@PathVariable("userId") ObjectId userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> updateUser(@PathVariable ObjectId userId, @Valid @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @PatchMapping("/users/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> modifyUserPassword(@PathVariable ObjectId userId, @Valid @RequestBody String password) {
        return userService.modifyUserPassword(userId, password);
    }

    @PatchMapping("/users/{userId}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> modifyUserPin(@PathVariable ObjectId userId, @Valid @RequestBody String pin) {
        return userService.modifyUserPin(userId, pin);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> deleteUser(@PathVariable ObjectId userId) {
        return userService.deleteUser(userId);
    }

    /*Room Endpoints*/

    @GetMapping("/rooms")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/rooms/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Room> getRoomById(@PathVariable("roomId") ObjectId roomId) {
        return roomService.getRoomById(roomId);
    }

    @PatchMapping("/users/{userId}/rooms")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> createRoomThenModifyUserRooms(@PathVariable("userId") ObjectId userId, @Valid @RequestBody Room room) {
        Mono<Room> mono = roomService.createRoom(userId.toString(), room);
        return userService.addUserRooms(userId, Objects.requireNonNull(mono.block()).get_id());
    }

    @PutMapping("/users/{userId}/rooms/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Room> updateRoom(@PathVariable("userId") ObjectId userId, @PathVariable("roomId") ObjectId roomId, @Valid @RequestBody Room room) {
        return roomService.updateRoom(userId.toString(), roomId, room);
    }

    @PatchMapping("/users/{userId}/rooms/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<User> deleteRoomThenModifyUserRooms(@PathVariable("userId") ObjectId userId, @PathVariable("roomId") ObjectId roomId) {
        Mono<Room> mono = roomService.deleteRoom(roomId);
        return userService.deleteUserRooms(userId, Objects.requireNonNull(mono.block()).get_id());
    }

    /*Transaction Endpoints*/

    @GetMapping("/transactions")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Transaction> getTransactionById(@PathVariable("transactionId") ObjectId transactionId) {
        return transactionService.getTransactionById(transactionId);
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Transaction> updateTransaction(@PathVariable("transactionId") ObjectId transactionId, @Valid @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(transactionId, transaction);
    }

    @PatchMapping("/transactions/{transactionId}/payment_status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Transaction> modifyTransactionPaymentStatus(@PathVariable("transactionId") ObjectId transactionId, @Valid @RequestBody String paymentStatus) {
        return transactionService.modifyTransactionPaymentStatus(transactionId, paymentStatus);
    }

    @PatchMapping("/transactions/{transactionId}/cancel_booking")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Transaction> cancelBooking(@PathVariable("transactionId") ObjectId transactionId) {
        return transactionService.cancelBooking(transactionId);
    }

    @DeleteMapping("transactions/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Transaction> deleteTransaction(@PathVariable("transactionId") ObjectId transactionId) {
        return transactionService.deleteTransaction(transactionId);
    }

    /*File Upload and Download Endpoints*/

    @PostMapping("/photos/upload_photo")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file, @RequestParam("save_directory") String saveDirectory) throws JSONException {
        StorageProperties properties = new StorageProperties();
        return ResponseEntity.created(properties.getBASE_IMG_DIRECTORY()).body(fileStorageService.store(file, saveDirectory).toString());
    }

    @PostMapping("/photos/upload_photos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<String>> uploadMultiplePhoto(@RequestParam("files") MultipartFile[] files, @RequestParam("save_directory") String saveDirectory) throws JSONException {
        StorageProperties properties = new StorageProperties();
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            fileNames.add(fileStorageService.store(file, saveDirectory).getString("fileName"));
        }
        return ResponseEntity.created(properties.getBASE_IMG_DIRECTORY()).body(fileNames);
    }

    @GetMapping("/photos/download_photo/{fileName:.+}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> downloadPhoto(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.warn("Couldn't determine file type ");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
