package org.generatio.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generatio.blogpessoal.model.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
	
		usuarioRepository.save(new Usuario(0L,"Jonathan da Silva","jonathan@email.com","jonathan123","https://i.imgur.com/HadaebG.png"));
		
		usuarioRepository.save(new Usuario(0L,"Rodrigo da Silva","rodrigo@email.com","rodrigo123","https://i.imgur.com/HadaebG.png"));
		
		usuarioRepository.save(new Usuario(0L,"Ana Luiza Silva","analuiza@email.com","analuiza123","https://i.imgur.com/HadaebG.png"));
		
		usuarioRepository.save(new Usuario(0L,"Ana Clara","anaclara@email.com","anaclara123","https://i.imgur.com/HadaebG.png"));
	}
	
	@Test
	@DisplayName("Retorna 1 usuario")
	public void deveRetornarUmUsuario() {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("anaclara@email.com");
	    assertTrue(usuario.get().getUsuario().equals("anaclara@email.com"));

	}

	
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void dveRetornarTresUsuarios() {
		
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		
		assertEquals(3, listaDeUsuarios.size());
		
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Jonathan da Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Rodrigo da Silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Ana Luiza Silva"));
	
	}
	
	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	
	}
}
