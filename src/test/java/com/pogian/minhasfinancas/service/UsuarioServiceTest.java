package com.pogian.minhasfinancas.service;


import com.pogian.minhasfinancas.exceptions.ErroAutenticacao;
import com.pogian.minhasfinancas.exceptions.RegraNegocioException;
import com.pogian.minhasfinancas.model.entity.Usuario;
import com.pogian.minhasfinancas.model.repository.UsuarioRepository;
import com.pogian.minhasfinancas.service.impl.UsuarioServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImp service;

    @MockBean
    UsuarioRepository repository;

/* --Forma padrão de se criar um Spy--
    @BeforeEach
    public void setUp(){
        service = Mockito.spy(UsuarioServiceImp.class);
        service = new UsuarioServiceImp(repository);
    }*/

    @Test
    public void deveSalvarUmUsuario() {
        //cenario
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("nome")
                .email("email@email.com")
                .senha("senha").build();

        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        //ação
        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        //verificação
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }

    @Test
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
        //cenario
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        //acao
        org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario));

        //verificacao
        Mockito.verify(repository, Mockito.never() ).save(usuario);
    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        //cenário
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when( repository.findByEmail(email) ).thenReturn(Optional.of(usuario));

        //acao
        Usuario result = service.autenticar(email, senha);

        //verificacao
        org.assertj.core.api.Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {

        //cenario
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //acao
        Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email", "senha") );

        //verificação
        Assertions.assertThat(exception)
                .isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");

    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {
        //cenário
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        //ação
        Throwable exception = Assertions.catchThrowable( () ->   service.autenticar("email@email.com", "123"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha invalida.");
    }

    @Test
    public void deveValidarEmail() {
            //cenário
            Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

            //ação
            service.validarEmail("email@email.com");
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
            //cenário
            Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

            //ação
            org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> service.validarEmail("email@email.com"));
    }
}
