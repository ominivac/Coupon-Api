package com.cumpon.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cumpon.api.dto.CupomDTO;
import com.cumpon.api.service.CupomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cupom")
@RequiredArgsConstructor
public class CupomController {
    
    private final CupomService service;
    
    @PostMapping
    public ResponseEntity<CupomDTO> createCupom(@Valid @RequestBody CupomDTO dto) {
        CupomDTO cumpomCreated = service.createNewCupom(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cumpomCreated);
    }
    
    @GetMapping
    public ResponseEntity<List<CupomDTO>> getAll() {
        List<CupomDTO> cupons = service.getAllCupons();
        return ResponseEntity.ok(cupons);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CupomDTO> getCupomById(@PathVariable String id) {
        CupomDTO cupomFound = service.getCupomById(id);
        return ResponseEntity.ok(cupomFound);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCupom(@PathVariable String id) {
        service.deleteCupom(id);
        return ResponseEntity.noContent().build();
    }
}