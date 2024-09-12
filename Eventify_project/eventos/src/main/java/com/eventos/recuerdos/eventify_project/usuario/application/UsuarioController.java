package com.eventos.recuerdos.eventify_project.usuario.application;

import com.eventos.recuerdos.eventify_project.usuario.domain.Usuario;
import com.eventos.recuerdos.eventify_project.usuario.domain.UsuarioService;
import com.eventos.recuerdos.eventify_project.usuario.infrastructure.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/crear/users")
public class UsuarioController {

    @Autowired
    private UsuarioRepository userRepository;
    @Autowired
    private UsuarioService userService;

    @GetMapping
    public List<Usuario> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable Long id) {
        Optional<Usuario> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario createUser(@RequestBody Usuario user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable Long id, @RequestBody Usuario userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setNombre(userDetails.getNombre());
            user.setEmail(userDetails.getEmail());
            user.setContraseña(userDetails.getContraseña());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }
}
