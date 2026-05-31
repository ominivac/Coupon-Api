package com.cumpon.api.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)


public class CupomDTO {
    
    private String id;
    
    @NotBlank(message = "o campo código obrigatório")
    private String code;
    
    @NotBlank(message = "o campo descrição é obrigatório")
    private String description;
    
    @NotNull(message = "o campo desconto é obrigatório")
    @DecimalMin(value = "0.5", message = "O valor de desconto mínimo é 0.5")
    private BigDecimal discountValue;
    
    @NotNull(message = "o campo data de expiração é obrigatório")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;
    
    private String status;
    
    private Boolean published;
    
    private Boolean redeemed;
}