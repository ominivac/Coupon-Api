# Cupom API

[Java 17](https://www.oracle.com/java/)
[Spring Boot](https://spring.io/projects/spring-boot)
[Tests]


## Stacks

- Java 17
- Lombok
- H2 Database
- Spring Boot 
- Spring Data JPA
- JUnit / Mockito
- Docker

## Para rodar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Docker

### Instalação

```bash
# Clonar repositório
git clone https://github.com/ominivac/Coupon-Api
cd cupom-api

# Compilar
mvn clean install

# Executar
mvn spring-boot:run


## Endpoints da API

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/coupon` | Criar novo cupom | 201 Created |
| GET | `/coupon` | Listar todos  | 200 OK |
| GET | `/coupon/{id}` | Buscar cupom por id | 200 OK |
| DELETE | `/coupon/{id}` | Deletar cupom - não apaga do bd | 204 No Content |

## Exemplos de Uso

### Criar Cupom

```bash
curl -X POST http://localhost:8080/api/cupom \
  -H "Content-Type: application/json" \
  -d '{
    "code": "NATAL1",
    "description": "Cupom de Natal",
    "discountValue": 15.50,
    "expirationDate": "2025-12-31T23:59:59",
    "published": false
  }'
```

**Resposta (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "code": "NATAL1",
  "description": "Cupom de Natal",
  "discountValue": 15.50,
  "expirationDate": "2025-12-31T23:59:59",
  "status": "ACTIVE",
  "published": false,
  "redeemed": false
}
```

### Listar Cupons

```bash
curl http://localhost:8080/api/cupom
```

### Buscar Cupom por ID

```bash
curl http://localhost:8080/cupom/{id}
```

### Deletar Cupom

```bash
curl -X DELETE http://localhost:8080/api/cupom/{id}


### Console H2

Console H2 em: `http://localhost:8080/h2-console`

- Usuario: `sa`
- Senha: em branco

