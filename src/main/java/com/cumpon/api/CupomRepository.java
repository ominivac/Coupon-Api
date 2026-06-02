package com.cumpon.api;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cumpon.api.entity.Cupom;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, String> {
    
    Optional<Cupom> findByCodeAndStatus(String code, String status);
    
    List<Cupom> findByStatus(String status);
    
    Optional<Cupom> findByIdAndStatus(String id, String status);
    
    
}