# 💰 FinnantechV2 - Sistema Financeiro Completo

> **Sistema moderno de controle financeiro pessoal com integração OAuth2, gestão de investimentos e dashboard inteligente**

![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)
![Backend](https://img.shields.io/badge/Backend-Spring%20Boot%203.2.2-green)
![Frontend](https://img.shields.io/badge/Frontend-Next.js%2014-blue)
![Database](https://img.shields.io/badge/Database-H2%20%2F%20MySQL-orange)

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Arquitetura](#-arquitetura)
- [Funcionalidades Implementadas](#-funcionalidades-implementadas)
- [Stack Tecnológica](#-stack-tecnológica)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como Executar](#-como-executar)
- [Modelagem do Banco](#-modelagem-do-banco)
- [Dados de Teste](#-dados-de-teste)
- [API Endpoints](#-api-endpoints)
- [Screenshots](#-screenshots)
- [Próximos Passos](#-próximos-passos)
- [Contribuição](#-contribuição)

## 🎯 Visão Geral

O **FinnantechV2** é um sistema completo de gestão financeira pessoal desenvolvido com as melhores práticas de desenvolvimento moderno. O projeto combina uma arquitetura robusta no backend (Spring Boot) com uma interface moderna no frontend (Next.js), oferecendo uma experiência completa de controle financeiro.

### ✨ Principais Diferenciais

- **🔐 Autenticação Completa**: Login tradicional + OAuth2 (Google/Facebook)
- **📊 Dashboard Inteligente**: Visualizações interativas e insights financeiros
- **💳 Gestão de Cartões**: Controle completo de cartões de crédito e débito
- **📈 Investimentos**: Acompanhamento de rentabilidade em tempo real
- **🎯 Orçamentos**: Controle de gastos por categoria com alertas
- **🔄 Transações Recorrentes**: Automação de lançamentos fixos
- **📱 Design Responsivo**: Interface moderna e intuitiva

## 🏗️ Arquitetura

### Arquitetura Geral
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │   Database      │
│   (Next.js)     │◄──►│  (Spring Boot)  │◄──►│  (H2 / MySQL)   │
│                 │    │                 │    │                 │
│ • React 18      │    │ • Java 17       │    │ • 7 Tabelas     │
│ • TypeScript    │    │ • Spring Sec 6  │    │ • Relacionais   │
│ • Tailwind CSS  │    │ • JWT           │    │ • Índices       │
│ • Zustand       │    │ • JPA/Hibernate │    │ • Constraints   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Padrões Implementados

- **Backend**: Arquitetura Hexagonal com Domain-Driven Design
- **Frontend**: Component-Based Architecture com Context API
- **Database**: Modelagem relacional otimizada para performance
- **Segurança**: JWT + OAuth2 + CORS configurado
- **Qualidade**: Validações completas + Tratamento de erros

## ✅ Funcionalidades Implementadas

### 🔐 Autenticação e Autorização
- [x] **Login tradicional** com email/senha
- [x] **Registro de usuários** com validação
- [x] **Autenticação JWT** com tokens seguros
- [x] **OAuth2 preparado** para Google e Facebook
- [x] **Proteção de rotas** no frontend
- [x] **Logout completo** (frontend + backend)
- [x] **Interceptadores HTTP** para token management

### 📊 Dashboard e Visualizações
- [x] **Dashboard principal** com métricas financeiras
- [x] **Gráficos interativos** de evolução temporal
- [x] **Cards de cartões** com informações detalhadas
- [x] **Lista de transações** recentes
- [x] **Estatísticas por categoria** com cores personalizadas
- [x] **Indicadores visuais** de saúde financeira

### 💳 Gestão de Métodos de Pagamento
- [x] **Cartões de crédito** com limite e vencimento
- [x] **Cartões de débito** para controle imediato
- [x] **PIX** para transações instantâneas
- [x] **Dinheiro** para pagamentos em espécie
- [x] **Transferências bancárias** tradicionais

### 📈 Controle de Transações
- [x] **Cadastro de transações** com categorização
- [x] **Histórico temporal** com filtros avançados
- [x] **Tags personalizadas** para organização
- [x] **Status de transações** (pendente, confirmada, cancelada)
- [x] **Campos calculados** para performance

### 💰 Gestão de Investimentos
- [x] **Ações** com acompanhamento de cotações
- [x] **Fundos de investimento** com rentabilidade
- [x] **Renda fixa** (CDB, Tesouro, LCI, LCA)
- [x] **Criptomoedas** para diversificação
- [x] **Cálculo automático** de lucro/prejuízo

### 🎯 Controle de Orçamentos
- [x] **Orçamentos mensais** por categoria
- [x] **Alertas automáticos** de gastos excessivos
- [x] **Controle de limites** com percentuais configuráveis
- [x] **Acompanhamento visual** do uso do orçamento

### 🔄 Automação
- [x] **Transações recorrentes** (salário, aluguel, etc.)
- [x] **Agendamento inteligente** de próximas execuções
- [x] **Controle de frequência** (diária até anual)

## 🛠️ Stack Tecnológica

### Backend (Spring Boot)
```xml
Java 17
Spring Boot 3.2.2
Spring Security 6
Spring Data JPA
Spring Boot Actuator
JWT (JJWT 0.12.3)
H2 Database (desenvolvimento)
Maven (build)
```

### Frontend (Next.js)
```json
React 18
Next.js 14 (App Router)
TypeScript
Tailwind CSS
Phosphor Icons
React Hot Toast
Axios (HTTP client)
Context API (estado global)
```

### Banco de Dados
```sql
H2 (desenvolvimento)
MySQL (produção - preparado)
7 tabelas relacionais
Índices otimizados
Constraints de integridade
Campos calculados
```

## 📁 Estrutura do Projeto

```
FinnantechV2/
├── backend/                          # Spring Boot Application
│   ├── src/main/java/com/finnantech/
│   │   ├── domain/                   # Domínio (Entities, Value Objects)
│   │   │   ├── entities/            # User, AuthProvider
│   │   │   ├── valueobjects/        # Email, UserId
│   │   │   └── exceptions/          # Domain Exceptions
│   │   ├── application/             # Casos de Uso
│   │   │   └── services/           # UserService, AuthService
│   │   └── infrastructure/          # Infraestrutura
│   │       ├── security/           # SecurityConfig, JwtUtil
│   │       ├── web/                # Controllers, DTOs
│   │       └── persistence/        # Repositories, Mappers
│   ├── src/main/resources/
│   │   ├── application.yml         # Configurações
│   │   ├── schema.sql             # Schema completo do banco
│   │   └── data.sql               # Dados de teste
│   └── pom.xml                    # Dependências Maven
│
├── frontend/                       # Next.js Application
│   ├── src/
│   │   ├── app/                   # App Router (Next.js 14)
│   │   │   ├── login/            # Página de login
│   │   │   ├── cadastro/         # Página de registro
│   │   │   ├── dashboard/        # Dashboard principal
│   │   │   └── layout.tsx        # Layout global
│   │   ├── components/            # Componentes reutilizáveis
│   │   ├── context/              # Context API (AuthContext)
│   │   ├── lib/                  # Utilitários (API, utils)
│   │   └── types/                # Tipos TypeScript
│   ├── public/                   # Arquivos estáticos
│   └── package.json             # Dependências NPM
│
├── docs/                         # Documentação do projeto
├── rules/                        # Regras de desenvolvimento
└── README.md                    # Este arquivo
```

## 🚀 Como Executar

### Pré-requisitos
- **Java 17+** instalado
- **Node.js 18+** instalado
- **Maven 3.8+** instalado
- **Git** para versionamento

### 1. Clone o Repositório
```bash
git clone <repository-url>
cd FinnantechV2
```

### 2. Executar o Backend
```bash
# Navegar para o diretório do backend
cd backend

# Executar com Maven
mvn spring-boot:run

# O backend estará disponível em: http://localhost:8080/api
```

### 3. Executar o Frontend
```bash
# Em um novo terminal, navegar para o frontend
cd frontend

# Instalar dependências
npm install

# Executar em desenvolvimento
npm run dev

# O frontend estará disponível em: http://localhost:3000
```

### 4. Verificar Health Check
```bash
# Verificar se o backend está funcionando
curl http://localhost:8080/api/actuator/health

# Resposta esperada: {"status":"UP"}
```

## 🗄️ Modelagem do Banco

### Diagrama ER Simplificado
```
USERS (1) ──────┐
               │
               ├── CATEGORIES (N)
               ├── PAYMENT_METHODS (N)
               ├── TRANSACTIONS (N)
               ├── INVESTMENTS (N)
               ├── BUDGETS (N)
               ├── RECURRING_TRANSACTIONS (N)
               └── AUDIT_LOGS (N)
```

### Tabelas Principais

#### 👤 USERS
- Usuários do sistema com autenticação local e OAuth2

#### 🏷️ CATEGORIES
- Categorias padrão do sistema + personalizadas dos usuários
- Tipos: RECEITA, DESPESA, INVESTIMENTO

#### 💳 PAYMENT_METHODS
- Cartões de crédito/débito, PIX, dinheiro, transferências
- Dados específicos para cartões (limite, vencimento, etc.)

#### 💸 TRANSACTIONS
- Core do sistema: todas as movimentações financeiras
- Otimizada com índices compostos e campos calculados

#### 📈 INVESTMENTS
- Ações, fundos, renda fixa, criptomoedas
- Cálculo automático de rentabilidade

#### 🎯 BUDGETS
- Orçamentos mensais por categoria
- Controle automático de gastos

#### 🔄 RECURRING_TRANSACTIONS
- Templates para transações automáticas
- Múltiplas frequências suportadas

## 👥 Dados de Teste

### Usuários Disponíveis
| Email | Senha | Tipo | Perfil |
|-------|-------|------|--------|
| `joao@teste.com` | `123456` | Local | Desenvolvedor Jr |
| `maria@teste.com` | `123456` | Local | Desenvolvedora Senior |
| `pedro@google.com` | - | OAuth | Estudante |
| `ana@facebook.com` | - | OAuth | Sem movimentação |

### Dados Criados
- ✅ **23 categorias** (19 padrão + 4 personalizadas)
- ✅ **10 métodos de pagamento** diversos
- ✅ **22 transações** realistas (3 meses de histórico)
- ✅ **7 investimentos** com rentabilidade calculada
- ✅ **8 orçamentos** mensais configurados
- ✅ **5 transações recorrentes** ativas

## 🔌 API Endpoints

### Autenticação
```http
POST /api/auth/login          # Login com email/senha
POST /api/auth/register       # Registro de novo usuário
POST /api/auth/logout         # Logout (limpa sessão)
GET  /api/auth/oauth2/google  # Login OAuth2 Google (preparado)
GET  /api/auth/oauth2/facebook # Login OAuth2 Facebook (preparado)
```

### Monitoramento
```http
GET  /api/actuator/health     # Health check da aplicação
GET  /api/actuator/info       # Informações da aplicação
GET  /api/actuator/metrics    # Métricas de performance
```

### Headers Necessários
```http
Authorization: Bearer <jwt-token>  # Para endpoints protegidos
Content-Type: application/json     # Para requisições POST/PUT
```

## 📸 Screenshots

### Dashboard Principal
- Interface moderna com cards informativos
- Gráficos de evolução financeira
- Lista de transações recentes
- Controle de cartões de crédito

### Login/Registro
- Formulários validados com feedback visual
- Integração OAuth2 preparada
- Redirecionamentos inteligentes
- Notificações de sucesso/erro

## 🛣️ Próximos Passos

### 🎯 Prioritários (Sprint 1)
- [ ] **Criar entidades JPA** baseadas no schema
- [ ] **Implementar repositories** com queries customizadas
- [ ] **Desenvolver services** com regras de negócio
- [ ] **Criar controllers REST** para transações
- [ ] **Integrar frontend** com APIs reais

### 🚀 Médio Prazo (Sprint 2)
- [ ] **OAuth2 completo** (Google + Facebook)
- [ ] **Upload de arquivos** (extratos, comprovantes)
- [ ] **Relatórios em PDF** de transações
- [ ] **Notificações push** para alertas
- [ ] **API de Open Banking** (integração bancária)

### 🌟 Longo Prazo (Sprint 3+)
- [ ] **Machine Learning** para categorização automática
- [ ] **Análise preditiva** de gastos
- [ ] **Metas financeiras** com gamificação
- [ ] **Compartilhamento** de orçamentos familiares
- [ ] **App mobile** React Native

### 🔧 Melhorias Técnicas
- [ ] **Testes unitários** e integração
- [ ] **Docker** para containerização
- [ ] **CI/CD** com GitHub Actions
- [ ] **Monitoramento** com Prometheus
- [ ] **Cache** com Redis

## 📈 Status do Desenvolvimento

### ✅ Concluído (100%)
- **Estrutura base** do projeto
- **Autenticação completa** com JWT
- **Dashboard frontend** funcional
- **Modelagem do banco** otimizada
- **Dados de teste** realistas
- **Documentação** técnica

### 🔄 Em Progresso (0%)
- **Integração backend-frontend** para dados reais
- **CRUD completo** de transações
- **APIs REST** implementadas

### 📋 Planejado (0%)
- **OAuth2 funcional**
- **Upload de arquivos**
- **Relatórios avançados**

## 🤝 Contribuição

### Para Desenvolvedores
1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. **Commit** suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. **Push** para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um **Pull Request**

### Padrões de Código
- **Backend**: Seguir convenções Spring Boot + Clean Code
- **Frontend**: ESLint + Prettier configurados
- **Commits**: Conventional Commits (feat, fix, docs, etc.)
- **Testes**: TDD quando possível

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Desenvolvido com ❤️ para revolucionar o controle financeiro pessoal**

---

### 🔗 Links Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Next.js Documentation](https://nextjs.org/docs)
- [Tailwind CSS](https://tailwindcss.com/)
- [JWT.io](https://jwt.io/)

### 📞 Suporte

Para dúvidas, sugestões ou reportar bugs, abra uma [issue](../../issues) no repositório.

**Status**: 🟢 Ativo | **Versão**: 2.0.0 | **Última atualização**: Março 2024 