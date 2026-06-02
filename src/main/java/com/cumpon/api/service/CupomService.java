package com.cumpon.api.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cumpon.api.CupomRepository; 
import com.cumpon.api.dto.CupomDTO;
import com.cumpon.api.entity.Cupom;
import com.cumpon.api.exceptions.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CupomService {
    
    private final CupomRepository repository;
    
    @Transactional
    public CupomDTO createNeeCupom(CupomDTO dto) {
        validateExpirationDate(dto.getExpirationDate());
        
        String sanitizedCode = sanitizeCode(dto.getCode());
        verifyLenghCode(sanitizedCode);
        verifyIsCodigoDuplicated(sanitizedCode);
        
        Cupom cupom = Cupom.builder()
                .code(sanitizedCode)
                .description(dto.getDescription())
                .discountValue(dto.getDiscountValue())
                .expirationDate(dto.getExpirationDate())
                .published(dto.getPublished() != null ? dto.getPublished() : false)
                .status(dto.getStatus() )
                .redeemed(false)
                .build();
        
        Cupom saved = repository.save(cupom);
        return toDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public List<CupomDTO> getAllCupons() {
        return repository.findByStatus("ACTIVE")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CupomDTO getCupomById(String id) {
        Cupom cupom = repository.findByIdAndStatus(id, "ACTIVE")
                .orElseThrow(() -> new BusinessException("Cupom não encontrado"));
        return toDTO(cupom);
    }
    
    @Transactional
    public void deleteCupom(String id) {
        Cupom cupom = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cupom não encontrado"));
        
        if ("DELETED".equals(cupom.getStatus())) {
            throw new BusinessException("Cupom já foi deletado");
        }
        
        cupom.setStatus("DELETED");
        repository.save(cupom);
    }
    
    private void verifyIsCodigoDuplicated(String code) {
        repository.findByCodeAndStatus(code, "ACTIVE")
                .ifPresent(c -> {
                    throw new BusinessException("Existe um cupom com este código");
                });
    }
    
    
    private void verifyLenghCode(String code) {
        if (code.length() != 6) {
            throw new BusinessException("O campo código deve ter exatamente 6 caracteres alfanuméricos");
        }
    }
    
    private void validateExpirationDate(LocalDateTime expirationDate) {
        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw new BusinessException("o campo data de expiração deve ser a partir de hoje");
        }
    }
    
    private String sanitizeCode(String code) {
        if (code == null) {
            throw new BusinessException("o campo código não pode ser nulo");
        }
        return code.replaceAll("[^a-zA-Z0-9]", "");
    }
    
    
    
    private CupomDTO toDTO(Cupom cupom) {
        return CupomDTO.builder()
                .id(cupom.getId())
                .code(cupom.getCode())
                .description(cupom.getDescription())
                .discountValue(cupom.getDiscountValue())
                .expirationDate(cupom.getExpirationDate())
                .status(cupom.getStatus())
                .published(cupom.getPublished())
                .redeemed(cupom.getRedeemed())
                .build();
    }
}