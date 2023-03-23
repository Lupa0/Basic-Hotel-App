package com.endava.hotelApp.controllers;

import com.endava.hotelApp.dto.RoomDTO;
import com.endava.hotelApp.enums.RoomType;
import com.endava.hotelApp.exception.RoomExistException;
import com.endava.hotelApp.exception.RoomNotExistException;
import com.endava.hotelApp.services.IRoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {
    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public IRoomService roomService;

    private static String route = "/api/rooms";

    private RoomDTO roomDTO = new RoomDTO(35,3000, RoomType.STANDARD);
    private String requestJSON = "{\"roomNumber\":\"35\",\"price\":\"3000\",\"roomType\":\"STANDARD\"}";

    @Test
    void createRoomWithSuccess() throws Exception {
        doNothing().when(roomService).addRoom(roomDTO);

        mockMvc.perform(post(route).contentType(MediaType.APPLICATION_JSON).content(requestJSON))
                .andExpect(status().isCreated());
    }

    @Test
    void createRoomShouldThrowAnErrorRoomExists() throws Exception {
        doThrow(RoomExistException.class).when(roomService).addRoom(any());

        mockMvc.perform(post(route).contentType(MediaType.APPLICATION_JSON).content(requestJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteRoomWithSuccess() throws Exception {
        doNothing().when(roomService).deleteRoom(35);

        mockMvc.perform(delete(route + "/35").contentType(MediaType.APPLICATION_JSON).content(requestJSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRoomShouldThrowAnErrorRoomNotExist() throws Exception {
        doThrow(RoomNotExistException.class).when(roomService).deleteRoom(35);

        mockMvc.perform(delete(route + "/35").contentType(MediaType.APPLICATION_JSON).content(requestJSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void getRoomsIfItsNull() throws Exception {
        when(roomService.getRooms()).thenReturn(null);

        mockMvc.perform(get(route)).andExpect(status().isNoContent());
    }

    @Test
    void getBookingsByRoomIfItsNull() throws Exception {
        when(roomService.reservationHistoryByRoom(any())).thenReturn(null);

        mockMvc.perform(get(route + "/getBookings/10")).andExpect(status().isNoContent());
    }
}
