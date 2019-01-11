package com.ivarrace.api.controller;

import com.ivarrace.api.model.Usuario;
import com.ivarrace.api.service.NoContentException;
import com.ivarrace.api.service.NotFoundException;
import com.ivarrace.api.service.UsuarioService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/public/usuarios")
public class UsuariosController {

    private final Logger logger = LoggerFactory.getLogger(UsuariosController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/")
    @ApiOperation(value = "View a list of available users")
    public ResponseEntity<List<Usuario>> getAll() throws NoContentException {
        logger.info("Getting all users");
        List<Usuario> usuarioList = usuarioService.findAll();;
        return new ResponseEntity<>(usuarioList, HttpStatus.OK);
    }

    @GetMapping(value = "/{alias}")
    @ApiOperation(value = "Search user with alias")
    public ResponseEntity<Usuario> get(@PathVariable("alias") String alias) throws NotFoundException {
        logger.info("Getting usuario with alias: {}", alias);
        Usuario usuario = usuarioService.findByAlias(alias);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    @ApiOperation(value = "Add usuario")
    public ResponseEntity<Void> create(@RequestBody Usuario usuario, UriComponentsBuilder ucBuilder) {
        logger.info("Creating new usuario: {}", usuario);
        Usuario created = usuarioService.create(usuario);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/public/usuarios/{id}").buildAndExpand(created.get_id()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);

    }

    @PutMapping(value = "/{alias}")
    @ApiOperation(value = "Update user")
    public ResponseEntity<Usuario> update(@PathVariable String alias, @RequestBody Usuario usuarioData) throws NotFoundException {
        logger.info("Updating user {}", alias);
        Usuario updated = usuarioService.update(alias, usuarioData);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{alias}")
    @ApiOperation(value = "Delete user")
    public ResponseEntity<Void> delete(@PathVariable("alias") String alias) throws NotFoundException {
        logger.info("Deleting user with alias: {}", alias);
        usuarioService.delete(alias);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
