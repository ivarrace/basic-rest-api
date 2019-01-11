package com.ivarrace.api.service;

import com.ivarrace.api.controller.UsuariosController;
import com.ivarrace.api.dao.UsuarioRepository;
import com.ivarrace.api.model.Usuario;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final Logger logger = LoggerFactory.getLogger(UsuariosController.class);

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> findAll() throws NoContentException {
        List<Usuario> usuarioList = repository.findAll();
        if (usuarioList == null || usuarioList.isEmpty()){
            logger.error("No users found");
            throw new NoContentException();
        }
        return usuarioList;
    }

    public Usuario findByAlias(String alias) throws NotFoundException{
        Optional<Usuario> persistedUser = repository.findByAlias(alias);
        if (!persistedUser.isPresent()){
            logger.error("Usuario with alias {} not found", alias);
            throw new NotFoundException();
        }
        return persistedUser.get();
    }

    public Usuario create(Usuario usuario) {
        //TODO validate exist
        usuario.set_id(ObjectId.get());
        return repository.save(usuario);
    }

    public Usuario update(String alias, Usuario usuarioData) throws NotFoundException {
        Usuario usuario = this.findByAlias(alias);
        //TODO update data
        usuario.setName(usuarioData.getName());
        return repository.save(usuario);
    }

    public void delete(String alias) throws NotFoundException {
        Usuario usuario = this.findByAlias(alias);
        repository.delete(usuario);
    }

}
