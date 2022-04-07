package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.Usuariologin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Cadastrar usuário")
    public void deveCriarUmUsuario() {
        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "Paulo Antunes", "paulo_antunes@email.com.br", "13465278", "https://i.imgur.com/JR7kUFU.jpg"));

        ResponseEntity<Usuario> resposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
        assertEquals(requisicao.getBody().getUsuario(),resposta.getBody().getUsuario());

    }

    @Test
    @DisplayName("Não deve permitir duplicação do usuário")
    public void naoDeveDuplicarUsuario() {

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "Maria da Silva", "maria_silva@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

        ResponseEntity<Usuario> resposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());

    }

    @Test
    @DisplayName("Alterar um Usuário")
    public void deveAtualizarUmUsuario() {

        //Primeiro criando o usuário
        Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,
                "Juliana Andrews", "juliana_andrews@email.com.br",
                "juliana123", "https://i.imgur.com/yDRVeK7.jpg"));

        //Atualizando o usuário
        Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
                "Juliana Andrews Ramos", "juliana_ramos@email.com.br",
                "juliana123", "https://i.imgur.com/yDRVeK7.jpg");

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> resposta = testRestTemplate
                .withBasicAuth("root", "root")
                .exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
        assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
    }

    @Test
    @DisplayName("Listar todos os usuários")
    public void deveMostrarTodosUsuarios() {

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Sabrina Sanches", "sabrina_sanches@email.com.br",
                "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Ricardo Marques", "ricardo_marques@email.com.br",
                "ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));

        ResponseEntity<String> resposta = testRestTemplate//Do tipo String pois estou pegando uma lista
                .withBasicAuth("root", "root")
                .exchange("/usuarios/all", HttpMethod.GET, null, String.class);//String = lista

        assertEquals(HttpStatus.OK, resposta.getStatusCode());

    }

    @Test
    @DisplayName("Listar um usuario específico por id")
    public void deveMostrarUsuarioPorId() {

        Optional<Usuario> buscarUsuario = usuarioService.cadastrarUsuario(new Usuario(0L,
                "Sabrina Sanches", "sabrina_sanches@email.com.br",
                "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));

        ResponseEntity<String> resposta = testRestTemplate
                .withBasicAuth("root", "root")
                .exchange("/usuarios/" + buscarUsuario.get().getId(), HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());

    }

    @Test
    @DisplayName("Login do usuario funcionando")
    public void autenticarLoginUsuario() {

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Juliana Andrews", "juliana_andrews@email.com.br",
                "juliana123", "https://i.imgur.com/yDRVeK7.jpg"));

        HttpEntity<Usuariologin> requisicao = new HttpEntity<Usuariologin>(new Usuariologin(0L,
                "", "juliana_andrews@email.com.br",
                "juliana123", ""));

        ResponseEntity<Usuariologin> resposta = testRestTemplate
                .exchange("/usuarios/logar", HttpMethod.POST, requisicao, Usuariologin.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

}
