# Documento de Arquitetura do Sistema - Monex

## 1. Visão Geral da Arquitetura

A arquitetura do Monex é baseada em microsserviços para o backend e uma aplicação web single-page (SPA) para o frontend, garantindo escalabilidade, resiliência e facilidade de manutenção. A comunicação entre os componentes será via APIs RESTful.

## 2. Diagrama de Arquitetura de Alto Nível

```mermaid
graph TD
    A[Usuário] -->|Acessa| B(Frontend - SPA)
    B -->|Consome API| C[Backend - Microsserviços]
    C -->|Persiste dados| D(Banco de Dados - SQL)
    C -->|Integração| E[Open Banking APIs]
    C -->|Notificações| F(Serviço de Notificações)
    B -->|Autenticação| G(Serviço de Autenticação / OAuth Providers)

    subgraph Backend Services
        C1(Serviço de Autenticação)
        C2(Serviço de Usuários e Assinatura)
        C3(Serviço de Lançamentos Financeiros)
        C4(Serviço de Categorização Inteligente)
        C5(Serviço de Integração Bancária / Open Banking)
        C6(Serviço de Relatórios e Dashboard)
    end

    C --> C1
    C --> C2
    C --> C3
    C --> C4
    C --> C5
    C --> C6

    D[Banco de Dados] --- H[Armazenamento de Dados - Não Relacional (Cache/Logs)]
```

## 3. Descrição dos Componentes e Responsabilidades

### 3.1. Frontend (SPA)
*   **Responsabilidade:** Interface do usuário, apresentação de dados, interação com o usuário, consumo das APIs do backend.
*   **Tecnologias:** React (ou similar como Angular/Vue.js), TypeScript, HTML5, CSS3/Sass/Styled Components.

### 3.2. Backend (Microsserviços)
*   **Responsabilidade:** Lógica de negócio, persistência de dados, integração com serviços externos (Open Banking), autenticação e autorização.
*   **Tecnologias:** Node.js com Express/NestJS (ou similar como Spring Boot/Django), TypeScript/Java/Python.

    *   **Serviço de Autenticação:** Gerencia o login de usuários (e-mail/senha, redes sociais), JWTs, recuperação de senha.
    *   **Serviço de Usuários e Assinatura:** Gerencia perfis de usuários, planos de assinatura, pagamentos e status da assinatura.
    *   **Serviço de Lançamentos Financeiros:** Gerencia o CRUD de despesas e receitas (únicas e recorrentes).
    *   **Serviço de Categorização Inteligente:** Lógica para categorização automática (ML) e manual, aprendizado de categorizações de usuários.
    *   **Serviço de Integração Bancária / Open Banking:** Interage com APIs de Open Banking e outros APIs bancárias para importar dados de transações.
    *   **Serviço de Relatórios e Dashboard:** Processa e agrega dados financeiros para o dashboard e relatórios, aplicando filtros e cálculos.

### 3.3. Banco de Dados (SQL)
*   **Responsabilidade:** Armazenamento persistente de dados estruturados (usuários, transações, categorias, assinaturas).
*   **Tecnologias:** PostgreSQL (ou MySQL, SQL Server).

### 3.4. Open Banking APIs
*   **Responsabilidade:** Fornecer acesso seguro e padronizado aos dados financeiros dos usuários em seus respectivos bancos.
*   **Tecnologias:** APIs RESTful padronizadas pelo Open Banking.

### 3.5. Serviço de Notificações
*   **Responsabilidade:** Envio de alertas de atraso de pagamento e outras notificações aos usuários (e-mail, push notifications).
*   **Tecnologias:** Serviço de mensageria (ex: RabbitMQ, Apache Kafka) e provedores de e-mail/SMS.

### 3.6. OAuth Providers (Google, Facebook)
*   **Responsabilidade:** Autenticação e autorização de usuários através de suas contas em redes sociais.
*   **Tecnologias:** OAuth 2.0.

### 3.7. Armazenamento de Dados - Não Relacional
*   **Responsabilidade:** Armazenamento de dados não estruturados, como logs, sessões de cache, dados de aprendizado de máquina para categorização.
*   **Tecnologias:** Redis (cache), Elasticsearch/MongoDB (logs/dados ML).

## 4. Fluxo de Dados Principal

1.  **Autenticação:** O usuário se autentica via frontend, que se comunica com o Serviço de Autenticação no backend. Este serviço pode delegar a autenticação a OAuth Providers ou verificar credenciais internas.
2.  **Conexão Open Banking:** O usuário inicia a conexão com um banco via frontend, que chama o Serviço de Integração Bancária. Este serviço interage com as Open Banking APIs para obter as credenciais e iniciar a importação de dados.
3.  **Importação de Transações:** O Serviço de Integração Bancária importa transações e as envia para o Serviço de Lançamentos Financeiros, que as persiste no Banco de Dados.
4.  **Categorização:** O Serviço de Categorização Inteligente processa as novas transações (automáticas ou manuais) e armazena as categorias no Banco de Dados. Ele também aprende com as categorizações manuais.
5.  **Visualização no Dashboard:** O frontend solicita dados ao Serviço de Relatórios e Dashboard, que consulta o Banco de Dados, agrega as informações e as envia para exibição no dashboard do usuário.
6.  **Alertas:** O Serviço de Notificações monitora os lançamentos recorrentes e envia alertas (e-mail, push) com base nas configurações do usuário. 