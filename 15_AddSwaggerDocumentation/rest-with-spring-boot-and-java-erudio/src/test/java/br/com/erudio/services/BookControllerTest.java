package br.com.erudio.controllers;

import br.com.erudio.data.dto.BookDTO;
import br.com.erudio.services.BookServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @InjectMocks
    private BookController controller;

    @Mock
    private BookServices service;

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Título Teste");
        bookDTO.setAuthor("Autor Teste");
        bookDTO.setPrice(50.0);
        bookDTO.setLaunchDate(new Date());
    }

    @Test
    void testFindAll() {
        when(service.findAll()).thenReturn(List.of(bookDTO));

        var response = controller.findAll();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(bookDTO.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    void testFindById() {
        when(service.findById(1L)).thenReturn(bookDTO);

        var response = controller.findById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bookDTO.getId(), response.getBody().getId());
    }

    @Test
    void testCreate() {
        when(service.create(any(BookDTO.class))).thenReturn(bookDTO);

        var response = controller.create(bookDTO);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(bookDTO.getId(), response.getBody().getId());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    void testUpdate() {
        when(service.update(any(BookDTO.class))).thenReturn(bookDTO);

        var response = controller.update(bookDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bookDTO.getTitle(), response.getBody().getTitle());
    }

    @Test
    void testDelete() {
        doNothing().when(service).delete(1L);

        var response = controller.delete(1L);
        assertEquals(204, response.getStatusCodeValue());
        verify(service, times(1)).delete(1L);
    }
}
