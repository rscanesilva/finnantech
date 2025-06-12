# UI Kit & Design System - Finnantech V2

Este documento formaliza os elementos visuais e interativos do sistema Finnantech V2, suportando tanto **Dark Theme** quanto **Light Theme**. Ele serve como a fonte da verdade visual para o agente de IA ao gerar código de interface, garantindo consistência e fidelidade ao modelo aprovado.

## 1. Tokens de Design

### 1.1. Sistema de Temas

O sistema suporta alternância entre dois temas principais:

#### Dark Theme (Padrão)
| Variável CSS         | Código Hexadecimal | Descrição                             |
| :------------------- | :----------------- | :------------------------------------ |
| `--bg-primary`       | `#161618`          | Cor de fundo principal da aplicação. |
| `--bg-secondary`     | `#1e1e22`          | Cor de fundo para os cartões e seções. |
| `--bg-tertiary`      | `#2a2a2e`          | Cor de fundo para elementos terciários. |
| `--text-primary`     | `#ffffff`          | Cor principal para textos e títulos. |
| `--text-secondary`   | `#a0a0a0`          | Cor secundária para textos e labels. |
| `--text-muted`       | `#6b7280`          | Cor para textos menos importantes. |
| `--accent-green`     | `#32d583`          | Cor de destaque para sucesso/positivo. |
| `--accent-blue`      | `#5392ff`          | Cor de destaque para informações/ações. |
| `--accent-pink`      | `#f178b6`          | Cor de destaque para gráficos/alertas. |
| `--border-primary`   | `#333336`          | Cor para bordas e divisores. |
| `--border-secondary` | `#27272a`          | Cor para bordas secundárias. |

#### Light Theme
| Variável CSS         | Código Hexadecimal | Descrição                             |
| :------------------- | :----------------- | :------------------------------------ |
| `--bg-primary`       | `#f8fafc`          | Cor de fundo principal da aplicação. |
| `--bg-secondary`     | `#ffffff`          | Cor de fundo para os cartões e seções. |
| `--bg-tertiary`      | `#f1f5f9`          | Cor de fundo para elementos terciários. |
| `--text-primary`     | `#0f172a`          | Cor principal para textos e títulos. |
| `--text-secondary`   | `#64748b`          | Cor secundária para textos e labels. |
| `--text-muted`       | `#94a3b8`          | Cor para textos menos importantes. |
| `--accent-green`     | `#059669`          | Cor de destaque para sucesso/positivo. |
| `--accent-blue`      | `#3b82f6`          | Cor de destaque para informações/ações. |
| `--accent-pink`      | `#ec4899`          | Cor de destaque para gráficos/alertas. |
| `--border-primary`   | `#e2e8f0`          | Cor para bordas e divisores. |
| `--border-secondary` | `#cbd5e1`          | Cor para bordas secundárias. |

### 1.2. Implementação de Temas

```css
/* CSS Variables para Themes */
:root {
  /* Dark Theme (padrão) */
  --bg-primary: #161618;
  --bg-secondary: #1e1e22;
  --bg-tertiary: #2a2a2e;
  /* ... demais variáveis dark */
}

/* Light Theme */
.light {
  --bg-primary: #f8fafc;
  --bg-secondary: #ffffff;
  --bg-tertiary: #f1f5f9;
  /* ... demais variáveis light */
}
```

### 1.3. Escala Tipográfica

*   **Fonte**: `Inter`, `sans-serif` (importada do Google Fonts)
*   **Pesos**:
    *   `400` (Regular)
    *   `500` (Medium)
    *   `600` (Semi-bold)
    *   `700` (Bold)
    *   `800` (Extra-bold)

**Hierarquia Tipográfica:**
*   `font-size: 2.5rem; font-weight: 700;` (para valores monetários principais)
*   `font-size: 1.5rem; font-weight: 700;` (para logo/marca)
*   `font-size: 1.25rem; font-weight: 600;` (para títulos de cards)
*   `font-size: 1.125rem; font-weight: 600;` (para subtítulos)
*   `font-size: 0.875rem; font-weight: 500;` (para navegação, labels)
*   `font-size: 0.75rem;` (para labels pequenos, informações secundárias)

### 1.4. Sistema de Espaçamento

O espaçamento é baseado em múltiplos de `0.5rem` (`8px`) e `1rem` (`16px`).

*   `gap: 1.5rem;` (espaçamento padrão entre seções/cards)
*   `padding: 1.5rem;` (padding padrão de cards)
*   `gap: 1rem;` (espaçamento entre ícones, itens de lista)
*   `gap: 0.75rem;` (espaçamento menor, ex: entre avatar e nome)
*   `gap: 0.5rem;` (espaçamento para itens de lista menores)
*   `padding: 0.75rem 1rem;` (padding de dropdowns e botões)
*   `padding: 0.5rem;` (padding de ícones e botões pequenos)

### 1.5. Raio de Borda (Border Radius)

*   `1rem` (para cartões principais)
*   `0.5rem` (para elementos menores como dropdowns, botões)
*   `0.3rem` (para badges/tags pequenos)
*   `50%` (para avatares e ícones circulares)

### 1.6. Sombras e Elevação

**Dark Theme:**
```css
.shadow-dark {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.3), 0 2px 4px -1px rgba(0, 0, 0, 0.2);
}
```

**Light Theme:**
```css
.shadow-light {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
}
```

## 2. Especificação de Componentes

### 2.1. Cards

**Estilo Base (adaptável ao tema):**
```css
.card {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-primary);
  border-radius: 1rem;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  color: var(--text-primary);
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}
```

**Variações:**
*   `header-card`: `flex-direction: row; align-items: center; justify-content: space-between; padding: 1rem 1.5rem;`

### 2.2. Botões e Elementos Interativos

**Navegação (`nav-link`):**
```css
.nav-link {
  color: var(--text-secondary);
  font-weight: 500;
  text-decoration: none;
  transition: color 0.3s ease;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
}

.nav-link:hover {
  color: var(--text-primary);
  background-color: var(--bg-tertiary);
}

.nav-link.active {
  color: var(--text-primary);
  background-color: var(--bg-tertiary);
  font-weight: 600;
}
```

**Botões Primários:**
```css
.btn-primary {
  background: linear-gradient(135deg, var(--accent-blue), var(--accent-green));
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(83, 146, 255, 0.3);
}
```

**Botões Secundários:**
```css
.btn-secondary {
  background-color: var(--bg-tertiary);
  color: var(--text-primary);
  border: 1px solid var(--border-primary);
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  background-color: var(--border-primary);
  transform: translateY(-1px);
}
```

### 2.3. Theme Toggle

**Componente de Alternância de Tema:**

O sistema inclui dois componentes de toggle:

1. **ThemeToggle**: Versão completa com animações
2. **ThemeToggleCompact**: Versão compacta para headers

```tsx
// Uso no header
<ThemeToggleCompact />

// Uso em configurações/preferências
<ThemeToggle />
```

**Características:**
- Transições suaves entre temas (0.3s ease)
- Ícones animados (sol/lua)
- Persistência no localStorage
- Detecção automática do tema do sistema

### 2.4. Ícones

*   **Fonte**: Phosphor Icons
*   **Cor Padrão**: `var(--text-secondary)` para ícones secundários, `var(--text-primary)` para ícones de ação
*   **Tamanhos**: 
    - `1.25rem` (ícones de ação no cabeçalho)
    - `1.5rem` (ícones maiores)
    - `2rem` (ícones de destaque)

**Estados Interativos:**
```css
.action-icon {
  color: var(--text-secondary);
  font-size: 1.25rem;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 0.5rem;
  border-radius: 0.5rem;
}

.action-icon:hover {
  color: var(--text-primary);
  background-color: var(--bg-tertiary);
  transform: scale(1.05);
}
```

### 2.5. Formulários

**Inputs e Campos:**
```css
input, textarea, select {
  background-color: var(--bg-tertiary);
  border: 1px solid var(--border-primary);
  color: var(--text-primary);
  border-radius: 0.5rem;
  padding: 0.75rem;
  transition: all 0.3s ease;
}

input::placeholder, textarea::placeholder {
  color: var(--text-muted);
}

input:focus, textarea:focus, select:focus {
  outline: none;
  border-color: var(--accent-blue);
  box-shadow: 0 0 0 3px rgba(83, 146, 255, 0.1);
}
```

### 2.6. Dropdowns

```css
.dropdown {
  background-color: var(--bg-tertiary);
  border: 1px solid var(--border-primary);
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  cursor: pointer;
  color: var(--text-primary);
  transition: all 0.3s ease;
}

.dropdown:hover {
  background-color: var(--border-primary);
  transform: translateY(-1px);
}
```

## 3. Animações e Transições

### 3.1. Transições de Tema

```css
.theme-transition {
  transition: background-color 0.3s ease, color 0.3s ease, border-color 0.3s ease;
}
```

### 3.2. Animações de Entrada

```css
@keyframes fade-in {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slide-up {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
```

### 3.3. Estados de Hover

- **Scale**: `transform: scale(1.05);` para ícones
- **Translate**: `transform: translateY(-1px);` para cards e botões
- **Brightness**: `filter: brightness(1.1);` para elementos gráficos

## 4. Responsividade

### 4.1. Breakpoints

*   **`@media (max-width: 1200px)`**: Ajustes de layout para tablets
*   **`@media (max-width: 992px)`**: Transição para mobile, navegação condensada
*   **`@media (max-width: 768px)`**: Layout móvel completo

### 4.2. Adaptações por Dispositivo

**Desktop (>1200px):**
- Grid completo com sidebar
- Navegação horizontal completa
- Cards em múltiplas colunas

**Tablet (768px - 1200px):**
- Grid compactado
- Navegação reduzida
- Cards responsivos

**Mobile (<768px):**
- Layout em coluna única
- Navegação drawer/menu
- Cards stack vertical

## 5. Acessibilidade

### 5.1. Contraste

**Dark Theme:**
- Texto principal sobre fundo: 7:1
- Texto secundário sobre fundo: 4.5:1

**Light Theme:**
- Texto principal sobre fundo: 8:1
- Texto secundário sobre fundo: 5:1

### 5.2. Estados de Foco

```css
*:focus {
  outline: 2px solid var(--accent-blue);
  outline-offset: 2px;
}
```

### 5.3. Tamanhos Mínimos

- Botões: mínimo 44px x 44px
- Áreas clicáveis: mínimo 44px x 44px
- Texto: mínimo 16px no mobile

## 6. Uso e Implementação

### 6.1. Classe Principal

Toda aplicação deve usar a classe `.theme-transition` no elemento `<html>` para transições suaves:

```html
<html class="theme-transition">
```

### 6.2. Detecção de Tema

O sistema detecta automaticamente:
1. Tema salvo no localStorage
2. Preferência do sistema (`prefers-color-scheme`)
3. Fallback para dark theme

### 6.3. Persistência

O tema selecionado é persistido em `localStorage` com a chave `finnantech-theme`.

---

Este UI Kit & Design System garante consistência visual entre os temas e fornece uma base sólida para a implementação de interfaces modernas e acessíveis no sistema Finnantech V2.