package com.pogian.minhasfinancas.service.impl;

import com.pogian.minhasfinancas.exceptions.ErroAutenticacao;
import com.pogian.minhasfinancas.exceptions.RegraNegocioException;
import com.pogian.minhasfinancas.model.entity.Usuario;
import com.pogian.minhasfinancas.model.repository.UsuarioRepository;
import com.pogian.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImp implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImp(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if(!usuario.isPresent()){
            throw new ErroAutenticacao("Usuario não encontrado para o email informado.");
        }

        if(usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha invalida.");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {

        boolean existe = repository.existsByEmail(email);
        if(existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }

    }
}
