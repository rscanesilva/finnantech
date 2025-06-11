# Regras Específicas - Backend Finnantech V2

## 1. ARQUITETURA OBRIGATÓRIA

### 1.1. Arquitetura Hexagonal (Ports & Adapters)
**OBRIGATÓRIO**: Toda nova funcionalidade DEVE seguir rigorosamente a arquitetura hexagonal.

#### Estrutura de Pastas
```
backend/
├── src/main/java/com/finnantech/
│   ├── domain/                    # 🔵 CORE - Domínio da aplicação
│   │   ├── entities/              # Entidades de negócio
│   │   ├── valueobjects/          # Value Objects
│   │   ├── services/              # Serviços de domínio
│   │   ├── repositories/          # Interfaces de repositório (PORTS)
│   │   └── exceptions/            # Exceções de domínio
│   ├── application/               # 🟡 CASOS DE USO
│   │   ├── usecases/              # Use Cases
│   │   ├── ports/                 # Interfaces (input/output ports)
│   │   │   ├── in/                # Input ports (comandos)
│   │   │   └── out/               # Output ports (interfaces)
│   │   └── services/              # Application Services
│   └── infrastructure/            # 🟢 ADAPTADORES
│       ├── adapters/              # Adaptadores externos
│       │   ├── web/               # Controllers REST
│       │   ├── persistence/       # Implementações JPA
│       │   ├── messaging/         # Message brokers
│       │   └── external/          # APIs externas
│       ├── config/                # Configurações Spring
│       └── security/              # Segurança
├── src/test/java/                 # 🧪 TESTES OBRIGATÓRIOS
│   ├── unit/                      # Testes unitários
│   ├── integration/               # Testes de integração
│   └── architecture/              # Testes de arquitetura
└── docs/                          # Documentação técnica
```

#### Princípios Fundamentais
1. **Dependency Inversion**: O core nunca depende da infraestrutura
2. **Single Responsibility**: Cada camada tem uma responsabilidade específica
3. **Interface Segregation**: Interfaces pequenas e específicas
4. **Open/Closed**: Aberto para extensão, fechado para modificação

### 1.2. Definição das Camadas

#### 🔵 DOMAIN (Core)
- **Entidades**: Objetos com identidade única
- **Value Objects**: Objetos imutáveis sem identidade
- **Domain Services**: Lógica que não pertence a uma entidade específica
- **Repository Interfaces**: Contratos de persistência (PORTS)

```java
// Exemplo: Entidade de Domínio
@Entity
public class Transaction {
    private TransactionId id;
    private Money amount;
    private TransactionDate date;
    private TransactionType type;
    private Category category;
    
    // Business logic aqui
    public void categorize(Category category) {
        if (this.category != null) {
            throw new TransactionAlreadyCategorizedException();
        }
        this.category = category;
    }
}
```

#### 🟡 APPLICATION (Use Cases)
- **Use Cases**: Orquestração de operações de negócio
- **Input Ports**: Interfaces que definem operações disponíveis
- **Output Ports**: Interfaces para operações externas

```java
// Exemplo: Use Case
@UseCase
public class CreateTransactionUseCase implements CreateTransactionInputPort {
    
    private final TransactionRepository transactionRepository;
    private final NotificationOutputPort notificationPort;
    
    @Override
    public TransactionResponse create(CreateTransactionCommand command) {
        // 1. Validações de negócio
        // 2. Criação da entidade
        // 3. Persistência
        // 4. Notificações
    }
}
```

#### 🟢 INFRASTRUCTURE (Adapters)
- **Controllers**: Adaptadores de entrada HTTP
- **Repositories**: Implementações de persistência
- **External Services**: Adaptadores para APIs externas

```java
// Exemplo: Adapter de Persistência
@Component
public class TransactionJpaAdapter implements TransactionRepository {
    
    private final TransactionJpaRepository jpaRepository;
    
    @Override
    public Transaction save(Transaction transaction) {
        return jpaRepository.save(TransactionEntity.from(transaction))
                           .toDomain();
    }
}
```

## 2. TESTES OBRIGATÓRIOS

### 2.1. Cobertura Mínima
**OBRIGATÓRIO**: Toda nova funcionalidade ou alteração DEVE ter:
- ✅ **Testes Unitários**: Cobertura mínima de 90%
- ✅ **Testes de Integração**: Para casos de uso críticos
- ✅ **Testes de Arquitetura**: Validação das regras arquiteturais

### 2.2. Estrutura de Testes

#### Testes Unitários (Obrigatórios)
```java
// Exemplo: Teste de Entidade de Domínio
class TransactionTest {
    
    @Test
    @DisplayName("Deve categorizar transação quando categoria válida")
    void shouldCategorizeTransactionWhenValidCategory() {
        // Given
        Transaction transaction = TransactionBuilder.create()
            .withAmount(Money.of(100.00))
            .build();
        Category category = CategoryBuilder.create()
            .withName("Alimentação")
            .build();
        
        // When
        transaction.categorize(category);
        
        // Then
        assertThat(transaction.getCategory()).isEqualTo(category);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando tentar categorizar transação já categorizada")
    void shouldThrowExceptionWhenCategorizingAlreadyCategorizedTransaction() {
        // Given
        Transaction transaction = TransactionBuilder.create()
            .withCategory(CategoryBuilder.create().build())
            .build();
        Category newCategory = CategoryBuilder.create().build();
        
        // When & Then
        assertThatThrownBy(() -> transaction.categorize(newCategory))
            .isInstanceOf(TransactionAlreadyCategorizedException.class);
    }
}
```

#### Testes de Use Case (Obrigatórios)
```java
@ExtendWith(MockitoExtension.class)
class CreateTransactionUseCaseTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private NotificationOutputPort notificationPort;
    
    @InjectMocks
    private CreateTransactionUseCase useCase;
    
    @Test
    @DisplayName("Deve criar transação com sucesso")
    void shouldCreateTransactionSuccessfully() {
        // Given
        CreateTransactionCommand command = CreateTransactionCommand.builder()
            .amount(BigDecimal.valueOf(100.00))
            .description("Compra no supermercado")
            .build();
        
        Transaction savedTransaction = TransactionBuilder.create()
            .withId(TransactionId.generate())
            .build();
        
        when(transactionRepository.save(any(Transaction.class)))
            .thenReturn(savedTransaction);
        
        // When
        TransactionResponse response = useCase.create(command);
        
        // Then
        assertThat(response.getId()).isEqualTo(savedTransaction.getId());
        verify(transactionRepository).save(any(Transaction.class));
        verify(notificationPort).sendTransactionCreatedNotification(any());
    }
}
```

#### Testes de Arquitetura (Obrigatórios)
```java
class ArchitectureTest {
    
    @ArchTest
    static final ArchRule domainShouldNotDependOnInfrastructure = 
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infrastructure..");
    
    @ArchTest
    static final ArchRule applicationShouldNotDependOnInfrastructure = 
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infrastructure..");
    
    @ArchTest
    static final ArchRule onlyUseCasesShouldBeAnnotatedWithUseCase =
        classes()
            .that().areAnnotatedWith(UseCase.class)
            .should().resideInAPackage("..application.usecases..");
}
```

### 2.3. Ferramentas de Teste Obrigatórias
```xml
<dependencies>
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- AssertJ -->
    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- ArchUnit -->
    <dependency>
        <groupId>com.tngtech.archunit</groupId>
        <artifactId>archunit-junit5</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Testcontainers -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 3. PADRÕES OBRIGATÓRIOS

### 3.1. Nomenclatura
- **Entidades**: `Transaction`, `User`, `Category`
- **Value Objects**: `Money`, `TransactionDate`, `UserId`
- **Use Cases**: `CreateTransactionUseCase`, `GetUserProfileUseCase`
- **Repositories**: `TransactionRepository` (interface), `TransactionJpaAdapter` (implementação)
- **Controllers**: `TransactionController`, `UserController`

### 3.2. Anotações Customizadas
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface UseCase {
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface DomainService {
}
```

### 3.3. Tratamento de Erros
```java
// Domain Exception
public class TransactionAlreadyCategorizedException extends DomainException {
    public TransactionAlreadyCategorizedException() {
        super("Transaction is already categorized and cannot be categorized again");
    }
}

// Global Exception Handler
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        return ResponseEntity.badRequest()
            .body(ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build());
    }
}
```

## 4. PIPELINE DE DESENVOLVIMENTO

### 4.1. Definition of Done
Uma funcionalidade só está PRONTA quando:
- ✅ Código implementado seguindo arquitetura hexagonal
- ✅ Testes unitários com cobertura ≥ 90%
- ✅ Testes de integração implementados
- ✅ Testes de arquitetura passando
- ✅ Documentação da API atualizada
- ✅ Code review aprovado
- ✅ Pipeline CI/CD passando

### 4.2. Comando de Verificação
```bash
# Antes de qualquer commit
mvn clean test                    # Roda todos os testes
mvn jacoco:report                # Gera relatório de cobertura
mvn archunit:verify              # Valida regras arquiteturais
mvn compile                      # Compila o projeto
```

### 4.3. Quality Gates
- **Cobertura de Testes**: ≥ 90%
- **Complexity**: ≤ 10 por método
- **Duplicação de Código**: ≤ 3%
- **Testes de Arquitetura**: 100% passando

## 5. FERRAMENTAS OBRIGATÓRIAS

### 5.1. Stack Tecnológico
- **Framework**: Spring Boot 3.2+
- **Java**: 17+ (LTS)
- **Build**: Maven
- **Banco**: MySQL 8.0+ ou PostgreSQL 15+
- **Cache**: Redis
- **Message Broker**: RabbitMQ
- **Tests**: JUnit 5 + Mockito + AssertJ + ArchUnit + Testcontainers

### 5.2. Dependências Essenciais
```xml
<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
    <!-- Documentation -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    </dependency>
</dependencies>
```

---

## ⚠️ REGRAS CRÍTICAS

1. **NUNCA** viole a arquitetura hexagonal
2. **SEMPRE** implemente testes antes ou junto com o código
3. **JAMAIS** faça merge sem 90% de cobertura de testes
4. **OBRIGATÓRIO** revisar código com foco em arquitetura
5. **SEMPRE** documente APIs com OpenAPI 3.0

> **Lembre-se**: Qualidade não é negociável. Código sem testes é código quebrado esperando para acontecer. 