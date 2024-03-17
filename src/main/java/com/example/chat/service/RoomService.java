package com.example.chat.service;

import com.example.chat.exception.RoomNotFoundException;
import com.example.chat.model.Room;
import com.example.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Flux<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Mono<Room> getRoomById(String roomId) {
        return roomRepository.findById(roomId)
                .switchIfEmpty(Mono.error(new RoomNotFoundException()));
    }
}
