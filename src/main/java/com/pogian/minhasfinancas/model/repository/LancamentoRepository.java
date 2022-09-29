package com.pogian.minhasfinancas.model.repository;

import com.pogian.minhasfinancas.model.entity.Lancamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long> {
}
