# Regras Backend - Finnantech V2
## Arquitetura Hexagonal + Testes Obrigatórios

### 🎯 REGRAS FUNDAMENTAIS

**1. ARQUITETURA HEXAGONAL OBRIGATÓRIA**
- Toda funcionalidade DEVE seguir Ports & Adapters
- Core nunca depende de infraestrutura
- Dependency Inversion em todas as camadas

**2. TESTES UNITÁRIOS OBRIGATÓRIOS**
- Cobertura mínima: 90%
- Toda funcionalidade nova/alterada DEVE ter testes
- Usar JUnit 5 + Mockito + AssertJ

**3. TESTES DE ARQUITETURA OBRIGATÓRIOS**
- ArchUnit para validar regras arquiteturais
- Executar a cada build
- 100% de compliance

### 📁 ESTRUTURA OBRIGATÓRIA

```
backend/
├── domain/           # 🔵 CORE - Lógica de negócio
│   ├── entities/     # Entidades de domínio
│   ├── services/     # Serviços de domínio
│   └── ports/        # Interfaces (contracts)
├── application/      # 🟡 USE CASES
│   ├── usecases/     # Casos de uso
│   └── services/     # Application services
└── infrastructure/   # 🟢 ADAPTERS
    ├── web/          # Controllers REST
    ├── persistence/  # JPA implementations
    └── external/     # APIs externas
```

### ✅ DEFINITION OF DONE

Uma funcionalidade só está PRONTA quando:
- ✅ Implementa arquitetura hexagonal
- ✅ Testes unitários ≥ 90% cobertura
- ✅ Testes de arquitetura passando
- ✅ Code review aprovado
- ✅ Pipeline CI/CD verde

### 🧪 TIPOS DE TESTE OBRIGATÓRIOS

**1. Testes Unitários**
```java
@Test
@DisplayName("Deve criar transação válida")
void shouldCreateValidTransaction() {
    // Given, When, Then
}
```

**2. Testes de Use Case**
```java
@ExtendWith(MockitoExtension.class)
class CreateTransactionUseCaseTest {
    @Mock TransactionRepository repository;
    // testes aqui
}
```

**3. Testes de Arquitetura**
```java
@ArchTest
static final ArchRule domainIndependence = 
    noClasses().that().resideInAPackage("..domain..")
    .should().dependOnClassesThat()
    .resideInAPackage("..infrastructure..");
```

### 🛠 STACK TECNOLÓGICO

- **Framework**: Spring Boot 3.2+
- **Java**: 17+ (LTS)
- **Build**: Maven
- **Database**: MySQL 8.0+
- **Tests**: JUnit 5 + Mockito + ArchUnit
- **Documentation**: OpenAPI 3.0

### 🚨 REGRAS CRÍTICAS

1. **NUNCA** violar arquitetura hexagonal
2. **SEMPRE** implementar testes primeiro
3. **JAMAIS** fazer merge sem testes
4. **OBRIGATÓRIO** revisar arquitetura no code review

> Qualidade não é negociável! 