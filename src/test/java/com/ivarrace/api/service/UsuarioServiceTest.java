package com.ivarrace.api.service;

import com.ivarrace.api.dao.UsuarioRepository;
import com.ivarrace.api.model.Usuario;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(SpringJUnit4ClassRunner.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService usuarioService;

    Usuario mockUsuario = new Usuario();
    String mockAlias = "ALIAS";

    @Before
    public void init() {
        mockUsuario.set_id(ObjectId.get());
        mockUsuario.setName("TestOwner");
        mockUsuario.setAlias(mockAlias);
    }

    @Test
    public void findAll_Content() throws NoContentException {
        List<Usuario> expectedList = Arrays.asList(mockUsuario);

        when(repository.findAll()).thenReturn(expectedList);

        List<Usuario> resultList = usuarioService.findAll();
        Assert.assertTrue(!resultList.isEmpty());
        Assert.assertArrayEquals(resultList.toArray(), expectedList.toArray());
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findAll_NoContent() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        try {
            List<Usuario> resultList = usuarioService.findAll();
            Assert.fail("Must throw NoContentException");
        } catch (NoContentException e) {
            verify(repository, times(1)).findAll();
            verifyNoMoreInteractions(repository);
        }

    }

    @Test
    public void findById_Found() throws NotFoundException {
        when(repository.findByAlias(mockAlias)).thenReturn(Optional.of(mockUsuario));

        Usuario result = usuarioService.findByAlias(mockAlias);
        Assert.assertEquals(result, mockUsuario);
        verify(repository, times(1)).findByAlias(mockAlias);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findById_NotFound() {
        when(repository.findByAlias(mockAlias)).thenReturn(Optional.empty());

        try {
            Usuario result = usuarioService.findByAlias(mockAlias);
            Assert.fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            verify(repository, times(1)).findByAlias(mockAlias);
            verifyNoMoreInteractions(repository);
        }
    }

    @Test
    public void create_Success() {
        when(repository.save(mockUsuario)).thenReturn(mockUsuario);

        Usuario result = usuarioService.create(mockUsuario);

        Assert.assertEquals(mockUsuario, result);
        verify(repository, times(1)).save(mockUsuario);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void update_success() throws NotFoundException {
        when(repository.findByAlias(mockAlias)).thenReturn(Optional.of(mockUsuario));
        when(repository.save(mockUsuario)).thenReturn(mockUsuario);

        Usuario result = usuarioService.update(mockAlias, mockUsuario);

        Assert.assertEquals(result.getName(), mockUsuario.getName());
        Assert.assertEquals(result.getAlias(), mockUsuario.getAlias());
        Assert.assertEquals(result.get_id(), mockUsuario.get_id());
        verify(repository, times(1)).save(mockUsuario);
        verify(repository, times(1)).findByAlias(mockAlias);
        verifyNoMoreInteractions(repository);
    }
    @Test
    public void update_not_found() {
        when(repository.findByAlias(mockAlias)).thenReturn(Optional.empty());

        try {
            usuarioService.update(mockAlias, mockUsuario);
            Assert.fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            verify(repository, times(1)).findByAlias(mockAlias);
            verify(repository, never()).save(any());
            verifyNoMoreInteractions(repository);
        }
    }

    @Test
    public void delete() throws NotFoundException {
        when(repository.findByAlias(mockAlias)).thenReturn(Optional.of(mockUsuario));
        doNothing().when(repository).delete(mockUsuario);

        usuarioService.delete(mockAlias);

        verify(repository, times(1)).delete(mockUsuario);
        verify(repository, times(1)).findByAlias(mockAlias);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void delete_notFound() {
        when(repository.findByAlias(mockAlias)).thenReturn(Optional.empty());

        try {
            usuarioService.delete(mockAlias);
            Assert.fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            verify(repository, times(1)).findByAlias(mockAlias);
            verify(repository, never()).delete(any());
            verifyNoMoreInteractions(repository);
        }
    }
}