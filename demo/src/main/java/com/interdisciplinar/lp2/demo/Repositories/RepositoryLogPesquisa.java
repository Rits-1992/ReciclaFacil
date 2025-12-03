package com.interdisciplinar.lp2.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interdisciplinar.lp2.demo.Entities.EntityLogPesquisa;

@Repository
public interface RepositoryLogPesquisa extends JpaRepository<EntityLogPesquisa, Long> {
}
