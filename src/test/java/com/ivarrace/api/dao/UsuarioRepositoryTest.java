package com.ivarrace.api.dao;

import com.ivarrace.api.model.Usuario;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario mockUsuario = new Usuario();
    private final String mockAlias = "ALIAS";

    @Before
    public void init() {
        //Clear database
        usuarioRepository.deleteAll();
        //Save mock document
        mockUsuario.set_id(ObjectId.get());
        mockUsuario.setAlias(mockAlias);
        mockUsuario.setName("TestOwner");
        usuarioRepository.save(mockUsuario);
    }

    @Test
    public void findBy_alias_Found() {

        Optional<Usuario> result = usuarioRepository.findByAlias(mockAlias);

        Assert.assertTrue(result.isPresent());
        Assert.assertTrue(mockUsuario.equals(result.get()));
    }

    @Test
    public void findBy_alias_NotFound() {
        Optional<Usuario> result = usuarioRepository.findByAlias("NONE");
        Assert.assertFalse(result.isPresent());
    }

}