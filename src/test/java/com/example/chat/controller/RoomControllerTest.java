//package com.example.chat.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import com.example.chat.model.Room;
//import com.example.chat.service.RoomService;
//import java.util.Arrays;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class RoomControllerTest {
//
//    @Mock
//    private RoomService roomService;
//
//    @InjectMocks
//    private RoomController roomController;
//
//    @Test
//    public void testGetAllRooms() {
//        Room room1 = new Room("room1", "Room 1");
//        Room room2 = new Room("room2", "Room 2");
//        List<Room> expectedRooms = Arrays.asList(room1, room2);
//        when(roomService.getAllRooms()).thenReturn(expectedRooms);
//
//        List<Room> actualRooms = roomController.getAllRooms();
//        assertEquals(expectedRooms.size(), actualRooms.size());
//        for (int i = 0; i < expectedRooms.size(); i++) {
//            assertEquals(expectedRooms.get(i), actualRooms.get(i));
//        }
//    }
//}
