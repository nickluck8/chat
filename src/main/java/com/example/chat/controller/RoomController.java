package com.example.chat.controller;

import com.example.chat.model.Room;
import com.example.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public Flux<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("{id}")
    public Mono<Room> getRoomById(@PathVariable(name = "id") String roomId) {
        return roomService.getRoomById(roomId);
    }
}
