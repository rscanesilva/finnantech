# 🎯 Cursor Project Rules

Este diretório contém as [Project Rules do Cursor](https://docs.cursor.com/context/rules) no formato MDC (Markdown with Configuration) para o projeto Finnantech.

## 📁 Estrutura

```
.cursor/rules/
├── finnantech-project-standards.mdc  # Regras gerais (sempre aplicadas)
├── backend-standards.mdc             # Regras para Spring Boot (auto-attach)
├── frontend-standards.mdc            # Regras para Next.js (auto-attach)
└── code-templates.mdc                # Templates de código (sob demanda)
```

## 🔧 Tipos de Regras

### 🌟 Always Apply
- **`finnantech-project-standards.mdc`**
- Sempre incluída no contexto do AI
- Contém regras fundamentais do projeto

### ⚡ Auto Attached
- **`backend-standards.mdc`** - Ativa quando arquivos Java/Spring são referenciados
- **`frontend-standards.mdc`** - Ativa quando arquivos React/TS são referenciados

### 🎨 Agent Requested  
- **`code-templates.mdc`** - Templates disponíveis para o AI usar quando necessário

## 📋 Como as Regras Funcionam

1. **Always Apply**: Carregadas automaticamente em toda interação
2. **Auto Attached**: Carregadas quando arquivos matching os `globs` são referenciados
3. **Agent Requested**: O AI decide quando incluir baseado na descrição

## 🆕 Migração do Legacy

- ❌ **Removido**: `.cursorrules` (formato legacy)
- ✅ **Adicionado**: `.cursor/rules/*.mdc` (formato atual)

## 📖 Referência

- [Documentação Oficial](https://docs.cursor.com/context/rules)
- [Formato MDC](https://docs.cursor.com/context/rules#project-rules)
- [Exemplos da Comunidade](https://docs.cursor.com/context/rules#examples)

## ✨ Benefícios

- 🎯 **Contexto específico** por área do projeto
- 📦 **Versionamento** junto com o código
- 🔄 **Reutilização** de padrões e templates
- 🎨 **Flexibilidade** de aplicação das regras 