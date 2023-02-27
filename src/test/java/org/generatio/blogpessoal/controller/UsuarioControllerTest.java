package org.generatio.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generatio.blogpessoal.model.Usuario;
import org.generatio.blogpessoal.repository.UsuarioRepository;
import org.generatio.blogpessoal.service.UsuarioService;
import org.hibernate.annotations.NaturalId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "root", "root@root.com", "rootrooot", ""));
	}

	@Test
	@DisplayName("Cadastrar usuario")
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"Paulo Antunes","pulo_antune@email.com","12345678","https://i.imgur.com/HadaebG.png"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(),corpoResposta.getBody().getNome() );
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação de usuario")
	public void naoDevePermitirDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Laura Santos","laurinha@email.com","12345678","https://i.imgur.com/HadaebG.png"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Laura Santos","laurinha@email.com","12345678","https://i.imgur.com/HadaebG.png"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Deve atualizar um usuario")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(
				0L,"João de Sá","joao@email.com","12345678","https://i.imgur.com/HadaebG.png"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
				"João de Sá","joao@email.com","12345678","https://i.imgur.com/HadaebG.png");
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCodeValue());
		assertEquals(corpoRequisicao.getBody().getNome(),corpoResposta.getBody().getNome() );
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Listar todos os usuarios")
	public void deveMostrarTodosUsuarios() {

		usuarioService.cadastrarUsuario(new Usuario(0L,"Ana Paula","paulinha@email.com","paula1234","https://i.imgur.com/HadaebG.png"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L,"Marcos Paulo","marquinhos@email.com","marcos1234","https://i.imgur.com/HadaebG.png"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root@root.com","rootroot")
					.exchange("/usuarios/all",HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}
}
