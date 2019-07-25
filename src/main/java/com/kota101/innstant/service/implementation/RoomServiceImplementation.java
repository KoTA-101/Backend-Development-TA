package com.kota101.innstant.service.implementation;

import com.kota101.innstant.data.model.HereResponse;
import com.kota101.innstant.data.model.Room;
import com.kota101.innstant.data.repository.RoomRepository;
import com.kota101.innstant.properties.HereApiProperties;
import com.kota101.innstant.service.FileWriterService;
import com.kota101.innstant.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class RoomServiceImplementation implements RoomService {
    private final RoomRepository roomRepository;
    private final FileWriterService fileWriterService;
    private final HereApiProperties hereApiProperties = new HereApiProperties();

    @Override
    public Flux<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Override
    public HereResponse getRoomsByLocation(double latitude, double longitude, float radius) {
        final String url = hereApiProperties.getBASE_URL() + "/search/proximity.json" +
                "?app_id=" + hereApiProperties.getAPP_ID() +
                "&app_code=" + hereApiProperties.getAPP_CODE() +
                "&proximity=" + latitude + "," + longitude + "," + radius +
                "&layer_ids=" + hereApiProperties.getLAYER_ID();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HereResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    @Override
    public Mono<Room> getRoomById(ObjectId roomId) {
        return roomRepository.findBy_id(roomId).switchIfEmpty(Mono.error(new Exception("No Room found with id: " + roomId)));
    }

    @Override
    public Mono<Room> createRoom(String ownerId, Room room) {
        long count = roomRepository.findAll().count().block();
        room.setGeometryId(count == 0 ? count : count + 1);
        room.setOwnerId(ownerId);
        return roomRepository.insert(room).doOnSuccess(createdRoom -> {
            createdRoom.setRoomId(createdRoom.get_id().toString());
            roomRepository.save(createdRoom).subscribe();
            writePayloadFile(createdRoom);
            final String url = hereApiProperties.getBASE_URL() + "/layers/modify.json" +
                    "?app_id=" + hereApiProperties.getAPP_ID() +
                    "&app_code=" + hereApiProperties.getAPP_CODE() +
                    "&layer_id=" + hereApiProperties.getLAYER_ID() +
                    "&action=append";
            try {
                sendPostRequestToHereApi(url, Files.readAllBytes(Paths.get("").resolve("innstant_map.wkt").toAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
            findRoom.setDescription(room.getDescription());
            findRoom.setDpPercentage(room.getDpPercentage());
            findRoom.setRating(room.getRating());
            findRoom.setTotalReview(room.getTotalReview());
            findRoom.setPhotos(room.getPhotos());
            roomRepository.save(findRoom).subscribe();
            writePayloadFile(findRoom);
            final String url = hereApiProperties.getBASE_URL() + "/layers/modify.json" +
                    "?app_id=" + hereApiProperties.getAPP_ID() +
                    "&app_code=" + hereApiProperties.getAPP_CODE() +
                    "&layer_id=" + hereApiProperties.getLAYER_ID() +
                    "&action=update";
            try {
                sendPostRequestToHereApi(url, Files.readAllBytes(Paths.get("").resolve("innstant_map.wkt").toAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Mono<Room> deleteRoom(ObjectId roomId) {
        return getRoomById(roomId).doOnSuccess(room -> {
            roomRepository.delete(room).subscribe();
            writePayloadFile(room);
            final String url = hereApiProperties.getBASE_URL() + "/layers/modify.json" +
                    "?app_id=" + hereApiProperties.getAPP_ID() +
                    "&app_code=" + hereApiProperties.getAPP_CODE() +
                    "&layer_id=" + hereApiProperties.getLAYER_ID() +
                    "&action=delete";
            try {
                sendPostRequestToHereApi(url, Files.readAllBytes(Paths.get("").resolve("innstant_map.wkt").toAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void writePayloadFile(Room room) {
        try {
            fileWriterService.writeToFile(room.getGeometryId() + "\t" +
                    room.getRoomId() + "\t" +
                    room.getName() + "\t" +
                    room.getOwnerId() + "\t" +
                    room.getType() + "\t" +
                    room.getLocation() + "\t" +
                    room.getDescription() + "\t" +
                    room.getRating() + "\t" +
                    room.getTotalReview() + "\t" +
                    "POINT(" + room.getLongitude() + " " + room.getLatitude() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPostRequestToHereApi(String url, byte[] file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("changes")
                .filename("innstant_map.wkt")
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(file, fileMap);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("changes", fileEntity);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            new RestTemplate().exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }
}
