package com.generation.blogpessoal.repository;

import com.generation.blogpessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){

        usuarioRepository.deleteAll();

            usuarioRepository.save(new Usuario(0L, "Afonsinho da Silva", "afonso@email.com", "12345678",
                    "https://i.imgur.com/NtyGneo.jpg"));

            usuarioRepository.save(new Usuario(0L, "Manuela da Silva", "manuela@email.com.br", "13465278",
                "https://i.imgur.com/NtyGneo.jpg"));

            usuarioRepository.save(new Usuario(0L, "Adriana da Silva", "adriana@email.com.br", "13465278",
                "https://i.imgur.com/mB3VM2N.jpg"));

            usuarioRepository.save(new Usuario(0L, "Paulo Silva", "paulo@email.com.br", "13465278",
                "https://i.imgur.com/JR7kUFU.jpg"));

    }

    @Test
    @DisplayName("Retorna 1 usuario 🔥")
    public void deveRetornarUmUsuario() {

        Optional<Usuario> usuario = usuarioRepository.findByUsuario("afonso@email.com");
        assertTrue(usuario.get().getUsuario().equals("afonso@email.com"));

    }

    @Test
    @DisplayName("Retorna 3 usuarios 🔥🔥🔥")
    public void deveRetornarTresUsuarios() {
        List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
        assertEquals(4, listaDeUsuarios.size());
        assertTrue(listaDeUsuarios.get(0).getNome().equals("Afonsinho da Silva"));
        assertTrue(listaDeUsuarios.get(1).getNome().equals("Manuela da Silva"));
        assertTrue(listaDeUsuarios.get(2).getNome().equals("Adriana da Silva"));

    }

}
