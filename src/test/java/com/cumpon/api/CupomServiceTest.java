package com.cumpon.api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cumpon.api.dto.CupomDTO;
import com.cumpon.api.entity.Cupom;
import com.cumpon.api.exceptions.BusinessException;
import com.cumpon.api.service.CupomService;

@ExtendWith(MockitoExtension.class)
public class CupomServiceTest {

	
	@Mock
    private CupomRepository repository;

    @InjectMocks
    private CupomService service;

    private String cupomTestId;
    private CupomDTO validCupomDTO;
    private Cupom validCupom;

    @BeforeEach
    void setUp() {
        // Gerar UUID
        cupomTestId = UUID.randomUUID().toString();

        // DTO válido 
        validCupomDTO = CupomDTO.builder()
                .code("ABC123")
                .description("Desconto de Natal")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDateTime.now().plusDays(30))
                .published(false)
                .build();

        // Entidade equivalente a ser salva no bd
        validCupom = Cupom.builder()
                .id(cupomTestId)  // UUID
                .code("ABC123")
                .description("Desconto de Natal")
                .discountValue(new BigDecimal("13.50"))
                .expirationDate(LocalDateTime.now().plusDays(10))
                .status("ACTIVE")  
                .published(false)
                .redeemed(false)   
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    
    /**
     * TESTE : Criar cupom com dados válidos
     * <p>
     * Verifica que um cupom válido é criado com sucesso,
     * recebendo ID (UUID), status ACTIVE e redeemed false.
     */
    @Test
    void criarCupom() {
        
        when(repository.findByCodeAndStatus(anyString(), eq("ACTIVE")))
                .thenReturn(Optional.empty());
        when(repository.save(any(Cupom.class)))
                .thenReturn(validCupom);

        
        CupomDTO result = service.createNewCupom(validCupomDTO);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());  // UUID gerado
        assertEquals("ABC123", result.getCode());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(false, result.getRedeemed());
        verify(repository, times(1)).save(any(Cupom.class));
    }
    
    
    /**
     * TESTE : Sanitização de caracteres especiais
     * <p>
     * Verifica que caracteres especiais são removidos do código
     * antes de salvar, mantendo apenas alfanuméricos.
     * <p>
     * Exemplo: "AB@C#1$2%3" → "ABC123"
     */
    @Test
    void deveRemoverCaracteresEspeciaisDoCode() {
        // Arrange
        validCupomDTO.setCode("AB@C#1$2%3");  // Com caracteres especiais

        when(repository.findByCodeAndStatus(anyString(), eq("ACTIVE")))
                .thenReturn(Optional.empty());
        when(repository.save(any(Cupom.class)))
                .thenReturn(validCupom);

        // Act
        CupomDTO result = service.createNewCupom(validCupomDTO);

        // Assert
        assertNotNull(result);
        assertEquals("ABC123", result.getCode());  // Sanitizado
    }
    
    
    /**
     * TESTE : Valida tamanho do código
     * <p>
     * Verifica que cupom com código diferente de 6 caracteres
     * é rejeitado após sanitização.
     */
    @Test
    void deveLancarExcecaoQuandoCodeNaoTiver6Caracteres() {
        // Arrange
        validCupomDTO.setCode("ABC");  // Apenas 3 caracteres

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.createNewCupom(validCupomDTO)
        );

        assertEquals(
                "O código deve ter exatamente 6 caracteres alfanuméricos",
                exception.getMessage()
        );
        verify(repository, never()).save(any(Cupom.class));
    }
    
    
    /**
     * TESTE : Buscar cupom por ID
     * <p>
     * Verifica que é possível buscar um cupom pelo ID (UUID)
     * e que apenas cupons ACTIVE são retornados.
     */
    @Test
    void deveBuscarCupomPorId() {
        // Arrange
        when(repository.findByIdAndStatus(cupomTestId, "ACTIVE"))
                .thenReturn(Optional.of(validCupom));

        // Act
        CupomDTO result = service.getCupomById(cupomTestId);

        // Assert
        assertNotNull(result);
        assertEquals(cupomTestId, result.getId());
        assertEquals("ABC123", result.getCode());
        assertEquals("ACTIVE", result.getStatus());
    }
    
    
    /**
     * TESTE : Valida data de expiração
     * <p>
     * Verifica que se data no cupom é a partir de hoje
     */
    @Test
    void deveLancarExcecaoQuandoDataExpiracaoForPassado() {
        // Arrange
        validCupomDTO.setExpirationDate(LocalDateTime.now().minusDays(1));

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.createNewCupom(validCupomDTO)
        );

        assertEquals(
                "A data de expiração não pode ser no passado",
                exception.getMessage()
        );
        verify(repository, never()).save(any(Cupom.class));
    }
    
    /**
     * TESTE : Valida código duplicado
     * <p>
     * Verifica que não é possível criar cupom com código
     * que já existe e está ativo
     */
    @Test
    void deveLancarExcecaoQuandoCodeJaExiste() {
        // Arrange
        when(repository.findByCodeAndStatus("ABC123", "ACTIVE"))
                .thenReturn(Optional.of(validCupom));

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.createNewCupom(validCupomDTO)
        );

        assertEquals("Já existe um cupom com este código", exception.getMessage());
        verify(repository, never()).save(any(Cupom.class));
    }
    
    
    @Test
    void deveValidarDescontoMinimo() {
        // Arrange
        validCupomDTO.setDiscountValue(new BigDecimal("0.5"));

        when(repository.findByCodeAndStatus(anyString(), eq("ACTIVE")))
                .thenReturn(Optional.empty());
        when(repository.save(any(Cupom.class)))
                .thenReturn(validCupom);

        // Act & Assert
        assertDoesNotThrow(() -> service.createNewCupom(validCupomDTO));
    }
    
    
    /**
     * TESTE : Soft delete com sucesso
     * <p>
     * Verifica que ao deletar um cupom:
     * - Status muda de ACTIVE para DELETED
     * - Cupom continua no banco (soft delete)
     */
    @Test
    void deveDeletarCupomComSucesso() {
        
        when(repository.findById(cupomTestId))
                .thenReturn(Optional.of(validCupom));
        when(repository.save(any(Cupom.class)))
                .thenReturn(validCupom);

       
        assertDoesNotThrow(() -> service.deleteCupom(cupomTestId));

        
        verify(repository, times(1)).save(any(Cupom.class));
    }
    
    
    
	
}
