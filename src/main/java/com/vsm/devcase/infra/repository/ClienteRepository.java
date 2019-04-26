package com.vsm.devcase.infra.repository;

import com.vsm.devcase.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author junior.cantador
 */
public interface ClienteRepository extends JpaRepository <Cliente, Long> {
    
    
}
