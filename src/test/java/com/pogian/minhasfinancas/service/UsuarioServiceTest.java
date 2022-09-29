package com.pogian.minhasfinancas.service;

import com.pogian.minhasfinancas.exceptions.RegraNegocioException;
import com.pogian.minhasfinancas.model.entity.Usuario;
import com.pogian.minhasfinancas.model.repository.UsuarioRepository;
import com.pogian.minhasfinancas.service.impl.UsuarioServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class UsuarioServiceTest {

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @BeforeEach
    public void setUp(){
        service = new UsuarioServiceImp(repository);
    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
            //cenario
            String email = "email@email.com";
            String senha = "senha";

            Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
            Mockito.when( repository.findByEmail(email) ).thenReturn(Optional.of(usuario));

            //acao
            Usuario result = service.autenticar(email, senha);

            //verificação



    }

    @Test
    public void deveValidarEmail() {
        Assertions.assertDoesNotThrow(() -> {
            //cenário
            Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

            //ação
            service.validarEmail("email@email.com");


        });

    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        Assertions.assertThrows(RegraNegocioException.class, () -> {
            //cenário
            Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

            //ação
            service.validarEmail("email@email.com");

        });
    }
}
