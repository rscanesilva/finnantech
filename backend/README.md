# Backend Finnantech V2

Sistema backend para controle financeiro desenvolvido com **Spring Boot** seguindo **Arquitetura Hexagonal**.

## 🎯 Arquitetura

Este projeto segue rigorosamente a **Arquitetura Hexagonal (Ports & Adapters)** com as seguintes camadas:

### 🔵 Domain (Core)
- **Entidades**: Objetos de negócio com lógica comportamental
- **Value Objects**: Objetos imutáveis sem identidade
- **Domain Services**: Lógica que não pertence a uma entidade específica
- **Ports**: Interfaces que definem contratos

### 🟡 Application (Use Cases)
- **Use Cases**: Orquestração das operações de negócio
- **Application Services**: Coordenação entre use cases
- **DTOs**: Objetos de transferência de dados

### 🟢 Infrastructure (Adapters)
- **Web**: Controllers REST (entrada)
- **Persistence**: Implementações JPA (saída)
- **External**: Integrações com APIs externas (saída)

## 🧪 Testes Obrigatórios

### Tipos de Teste
- **Unitários**: ≥ 90% cobertura (JUnit 5 + Mockito)
- **Integração**: Casos críticos (Testcontainers)
- **Arquitetura**: Validação de regras (ArchUnit)

### Comandos
```bash
mvn test                    # Executa todos os testes
mvn jacoco:report          # Relatório de cobertura
mvn archunit:verify        # Valida arquitetura
```

## 🛠 Stack Tecnológico

- **Framework**: Spring Boot 3.2+
- **Java**: 17+ (LTS)
- **Build**: Maven
- **Database**: MySQL 8.0+
- **Cache**: Redis
- **Message**: RabbitMQ
- **Security**: Spring Security + JWT
- **Documentation**: OpenAPI 3.0

## 🚀 Configuração Inicial

### 1. Estrutura de Pastas
```
backend/
├── src/main/java/com/finnantech/
│   ├── domain/
│   │   ├── entities/
│   │   ├── valueobjects/
│   │   ├── services/
│   │   └── ports/
│   ├── application/
│   │   ├── usecases/
│   │   └── services/
│   └── infrastructure/
│       ├── web/
│       ├── persistence/
│       └── external/
└── src/test/java/
    ├── unit/
    ├── integration/
    └── architecture/
```

### 2. Dependências Principais
```xml
<dependencies>
    <!-- Spring Boot Starters -->
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
    
    <!-- Database -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
    <!-- Tests -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <dependency>
        <groupId>com.tngtech.archunit</groupId>
        <artifactId>archunit-junit5</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 📋 Próximos Passos

1. **Setup Inicial**
   - [ ] Configurar Maven (`pom.xml`)
   - [ ] Estrutura de pastas da arquitetura hexagonal
   - [ ] Configurações Spring Boot (`application.yml`)

2. **Configuração de Testes**
   - [ ] Configurar JUnit 5 + Mockito
   - [ ] Implementar testes de arquitetura com ArchUnit
   - [ ] Configurar relatórios de cobertura (JaCoCo)

3. **Segurança**
   - [ ] Configurar Spring Security
   - [ ] Implementar JWT
   - [ ] Sistema de autenticação/autorização

4. **Banco de Dados**
   - [ ] Configurar MySQL
   - [ ] Migrations com Flyway
   - [ ] Entidades JPA

## 🚨 Regras Críticas

> ⚠️ **LEIA AS REGRAS**: Consulte sempre `rules/backend_architecture_rules.md`

1. **NUNCA** viole a arquitetura hexagonal
2. **SEMPRE** implemente testes (90% cobertura mínima)
3. **JAMAIS** faça merge sem testes passando
4. **OBRIGATÓRIO** revisar arquitetura no code review

---

> **Lembre-se**: Qualidade não é negociável. Código sem testes é código quebrado esperando para acontecer. 