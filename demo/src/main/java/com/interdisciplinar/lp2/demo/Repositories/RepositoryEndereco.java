package com.interdisciplinar.lp2.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.interdisciplinar.lp2.demo.Entities.EntityEndereco;

@Repository
public interface RepositoryEndereco extends JpaRepository<EntityEndereco, Long> {

}