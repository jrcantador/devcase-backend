package com.vsm.devcase.infra.repository;

import com.vsm.devcase.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author junior.cantador
 */
@Repository
public interface ClienteRepository extends JpaRepository <Cliente, Long> {
    
    
}
