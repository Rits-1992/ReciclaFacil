package com.interdisciplinar.lp2.demo.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interdisciplinar.lp2.demo.Entities.MensagensCompletoView;

@Repository
public interface MensagensCompletoRepository extends JpaRepository<MensagensCompletoView, Long> {
    List<MensagensCompletoView> findAll();
}
