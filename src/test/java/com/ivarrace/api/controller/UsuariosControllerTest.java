package com.ivarrace.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivarrace.api.model.Usuario;
import com.ivarrace.api.service.NoContentException;
import com.ivarrace.api.service.NotFoundException;
import com.ivarrace.api.service.UsuarioService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UsuariosControllerTest {

    private final String CONTROLLER_ENDPOINT = "/api/public/usuarios";

    @Mock
    private UsuarioService service;

    @InjectMocks
    private UsuariosController usuariosController;

    private MockMvc mockMvc;
    Usuario mockUsuario = new Usuario();
    String mockAlias = "ALIAS";

    @Before
    public void init(){
        mockMvc = MockMvcBuilders
                .standaloneSetup(usuariosController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
        mockUsuario.set_id(ObjectId.get());
        mockUsuario.setAlias(mockAlias);
        mockUsuario.setName("TestOwner");
    }

    /* Test GET all */
    @Test
    public void get_all_success() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(mockUsuario));

        mockMvc.perform(get(CONTROLLER_ENDPOINT+"/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]._id", is(mockUsuario.get_id())))
                .andExpect(jsonPath("$[0].alias", is(mockUsuario.getAlias())))
                .andExpect(jsonPath("$[0].name", is(mockUsuario.getName())));

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);

    }
    @Test
    public void get_all_204_no_content() throws Exception {
        when(service.findAll()).thenThrow(new NoContentException());

        mockMvc.perform(get(CONTROLLER_ENDPOINT+"/"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);

    }

    /* Test GET byId */
    @Test
    public void get_by_alias_success() throws Exception {
        when(service.findByAlias(mockAlias)).thenReturn(mockUsuario);

        mockMvc.perform(get(CONTROLLER_ENDPOINT+"/{alias}", mockAlias))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._id", is(mockUsuario.get_id())))
                .andExpect(jsonPath("$.alias", is(mockUsuario.getAlias())))
                .andExpect(jsonPath("$.name", is(mockUsuario.getName())));

        verify(service, times(1)).findByAlias(mockAlias);
        verifyNoMoreInteractions(service);
    }
    @Test
    public void get_by_id_fail_404_not_found() throws Exception {
        when(service.findByAlias(mockAlias)).thenThrow(new NotFoundException());

        mockMvc.perform(get(CONTROLLER_ENDPOINT+"/{alias}", mockAlias))
                .andExpect(status().isNotFound());

        verify(service, times(1)).findByAlias(mockAlias);
        verifyNoMoreInteractions(service);
    }

    /* Test UPDATE */
    @Test
    public void update_success() throws Exception {
        when(service.update(any(), any())).thenReturn(mockUsuario);

        mockMvc.perform(
                put(CONTROLLER_ENDPOINT+"/{id}", mockAlias)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUsuario)))
                .andExpect(status().isOk());

        verify(service, times(1)).update(any(), any());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void update_fail_404_not_found() throws Exception {
        when(service.update(any(), any())).thenThrow(new NotFoundException());

        mockMvc.perform(
                put(CONTROLLER_ENDPOINT+"/{alias}", mockAlias)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUsuario)))
                .andExpect(status().isNotFound());

        verify(service, times(1)).update(any(), any());
        verifyNoMoreInteractions(service);
    }

    /* Test CREATE */
    @Test
    public void create_success() throws Exception {
        when(service.create(mockUsuario)).thenReturn(mockUsuario);

        mockMvc.perform(
                post(CONTROLLER_ENDPOINT+"/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUsuario)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/api/public/usuarios/")));

        verify(service, times(1)).create(mockUsuario);
        verifyNoMoreInteractions(service);
    }
    //TEst create already exist

    /* Test DELETE */
    @Test
    public void delete_success() throws Exception {
        doNothing().when(service).delete(mockAlias);

        mockMvc.perform(
                delete(CONTROLLER_ENDPOINT+"/{alias}", mockAlias))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(mockAlias);
        verifyNoMoreInteractions(service);
    }
    @Test
    public void delete_fail_404_not_found() throws Exception {
        doThrow(NotFoundException.class).when(service).delete(mockAlias);

        mockMvc.perform(
                delete(CONTROLLER_ENDPOINT+"/{alias}", mockAlias))
                .andExpect(status().isNotFound());

        verify(service, times(1)).delete(mockAlias);
        verifyNoMoreInteractions(service);
    }


    //UTILS
    public static String asJsonString(final Object obj) {
        try {
             return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}