# Monex - Sistema de Controle Financeiro

## 📋 Resumo Executivo

O **Monex** é um sistema de controle financeiro pessoal multi-usuário com integração ao Open Banking, desenvolvido para auxiliar pessoas físicas no gerenciamento inteligente de suas finanças. O projeto combina automação, inteligência artificial e uma interface moderna para oferecer uma experiência superior de controle financeiro.

### 🎯 Objetivos Principais
- Automatizar a coleta e categorização de transações financeiras
- Fornecer insights inteligentes sobre padrões de gastos  
- Simplificar o controle financeiro através de uma interface intuitiva
- Integrar-se nativamente com o sistema bancário brasileiro via Open Banking

### 🚀 Diferenciais Competitivos
- **Categorização Inteligente**: IA que aprende com o comportamento do usuário
- **Open Banking Nativo**: Sincronização automática com bancos brasileiros
- **Interface Moderna**: Design system consistente e responsivo
- **Multi-dispositivo**: Funciona perfeitamente em desktop e mobile

---

## 📚 Documentação Completa

### 1. [Documento de Requisitos do Produto (PRD)](./PRD.md)
**Visão geral do produto, personas e funcionalidades**
- Visão e missão do produto
- Definição de personas (Maria e João)
- Lista completa de funcionalidades
- Requisitos não-funcionais
- Critérios de aceitação

### 2. [Arquitetura do Sistema](./System_Architecture.md)
**Estrutura técnica e componentes do sistema**
- Arquitetura geral (Frontend, Backend, Database)
- Stack tecnológico detalhado
- Padrões de integração
- Estrutura de deployment
- Considerações de segurança

### 3. [UI Kit e Design System](./UI_Kit_Design_System.md)
**Guia completo de design e componentes**
- Design tokens (cores, tipografia, espaçamentos)
- Biblioteca de componentes
- Padrões de interface
- Guidelines de UX
- Especificações técnicas de implementação

### 4. [Fluxos do Usuário](./User_Flows.md)
**Jornadas e interações do usuário**
- Fluxo de onboarding
- Conexão com Open Banking
- Gestão de transações
- Configurações e preferências
- Casos de uso principais

### 5. [Especificação da API](./API_Specification.md)
**Documentação completa da API REST**
- Endpoints organizados por funcionalidade
- Schemas de request/response
- Códigos de erro padronizados
- Exemplos de uso
- Autenticação e segurança

### 6. [Esquema do Banco de Dados](./Database_Schema.md)
**Modelagem completa do banco de dados**
- Diagrama entidade-relacionamento
- Especificação detalhada de todas as tabelas
- Índices e otimizações de performance
- Relacionamentos e constraints
- Considerações de segurança

---

## 🛠️ Stack Tecnológico

### Frontend
- **Framework**: Next.js 14 (App Router)
- **Linguagem**: TypeScript
- **Styling**: Tailwind CSS
- **Componentes**: Shadcn/ui
- **Estado**: Zustand
- **Formulários**: React Hook Form + Zod

### Backend
- **API**: Next.js API Routes
- **ORM**: Prisma
- **Database**: PostgreSQL
- **Autenticação**: NextAuth.js
- **Validação**: Zod

### Infraestrutura
- **Deploy**: Vercel + Supabase
- **Monitoramento**: Vercel Analytics
- **Versionamento**: Git

---

## 🎨 Design System

### Paleta de Cores
- **Primary**: #10B981 (Verde)
- **Secondary**: #3B82F6 (Azul)
- **Success**: #059669
- **Warning**: #F59E0B
- **Error**: #EF4444

### Componentes Principais
- Cards de transação
- Gráficos interativos
- Formulários responsivos
- Navegação intuitiva
- Modais e overlays

---

## 🔐 Segurança e Compliance

### Autenticação
- JWT tokens com refresh
- OAuth2 (Google, Facebook)
- 2FA para operações sensíveis

### Dados Sensíveis
- Criptografia AES-256 para tokens
- Hash bcrypt para senhas
- Logs de auditoria completos

### Open Banking
- Compliance com regulamentações brasileiras
- Tokens criptografados no database
- Renovação automática de credenciais

---

## 📊 Funcionalidades Principais

### 🏦 Gestão de Contas
- Conexão via Open Banking
- Múltiplas contas bancárias
- Sincronização automática
- Suporte a diferentes tipos de conta

### 💰 Transações
- Importação automática
- Categorização inteligente
- Edição manual
- Transações recorrentes

### 📈 Relatórios e Insights
- Dashboard interativo
- Gráficos de gastos por categoria
- Análise de tendências
- Relatórios personalizados

### 🔔 Notificações
- Alertas de gastos
- Lembretes de pagamentos
- Relatórios periódicos
- Configurações personalizadas

---

## 🚀 Roadmap de Desenvolvimento

### Fase 1 - MVP (8 semanas)
- [ ] Autenticação e gestão de usuários
- [ ] Conexão básica com Open Banking
- [ ] CRUD de transações manuais
- [ ] Categorização básica
- [ ] Dashboard simples

### Fase 2 - Inteligência (6 semanas)
- [ ] IA para categorização automática
- [ ] Detecção de transações recorrentes
- [ ] Insights e relatórios avançados
- [ ] Otimizações de performance

### Fase 3 - Expansão (4 semanas)
- [ ] Múltiplos bancos
- [ ] Relatórios exportáveis
- [ ] Notificações push
- [ ] Melhorias de UX

---

## 📋 Regras de Desenvolvimento

Para garantir a qualidade e consistência do código, consulte o arquivo de regras do agente de IA:

### 📖 [Regras do Agente de IA](../rules/monex_ai_rules.md)
**Guia completo para desenvolvimento**
- Padrões de código obrigatórios
- Convenções de nomenclatura
- Estrutura de arquivos
- Regras de segurança
- Práticas de performance
- Fluxo de desenvolvimento

---

## 🏗️ Como Começar

### Pré-requisitos
- Node.js 18+
- PostgreSQL 14+
- Git

### Instalação
```bash
# Clone o repositório
git clone [url-do-repositorio]

# Instale as dependências
npm install

# Configure o banco de dados
npx prisma db push

# Inicie o servidor de desenvolvimento
npm run dev
```

### Configuração
1. Configure as variáveis de ambiente (`.env.local`)
2. Execute as migrações do banco de dados
3. Configure os provedores de autenticação
4. Teste a integração com Open Banking

---

## 📞 Contato e Suporte

Para dúvidas sobre o projeto ou documentação:

- **Documentação Técnica**: Consulte os arquivos específicos nesta pasta
- **Regras de Desenvolvimento**: Veja `../rules/monex_ai_rules.md`
- **Issues e Bugs**: Use o sistema de issues do repositório

---

## 📄 Licença

Este projeto está licenciado sob os termos definidos no arquivo LICENSE.

---

**Última atualização**: $(date)
**Versão da documentação**: v1.0.0

> 💡 **Dica**: Sempre consulte este README antes de iniciar qualquer desenvolvimento. Ele contém links para toda a documentação necessária e serve como ponto de partida para entender o projeto Monex. 