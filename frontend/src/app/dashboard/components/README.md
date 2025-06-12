# Sistema de Temas - Finnantech V2

## 📋 Visão Geral

O sistema de temas do Finnantech V2 suporta alternância dinâmica entre **Dark Theme** e **Light Theme**, com persistência automática das preferências do usuário.

## 🎨 Componentes de Tema

### ThemeToggleCompact
Versão compacta para uso em headers e barras de ferramentas.

```tsx
import { ThemeToggleCompact } from './components/ThemeToggle'

// No header
<ThemeToggleCompact />
```

### ThemeToggle  
Versão completa com animações para páginas de configurações.

```tsx
import { ThemeToggle } from './components/ThemeToggle'

// Em configurações
<ThemeToggle />
```

## 🔧 Implementação

### 1. Context Provider
O `ThemeProvider` deve envolver toda a aplicação:

```tsx
// layout.tsx
import { ThemeProvider } from '@/context/ThemeContext'

export default function RootLayout({ children }) {
  return (
    <html lang="pt-BR">
      <body>
        <ThemeProvider>
          {children}
        </ThemeProvider>
      </body>
    </html>
  )
}
```

### 2. Hook personalizado
Use o hook `useTheme` para acessar o estado do tema:

```tsx
import { useTheme } from '@/context/ThemeContext'

function MyComponent() {
  const { theme, toggleTheme, setTheme } = useTheme()
  
  return (
    <div>
      <p>Tema atual: {theme}</p>
      <button onClick={toggleTheme}>Alternar Tema</button>
    </div>
  )
}
```

## 🎨 CSS Variables

O sistema usa CSS Variables que se adaptam automaticamente ao tema:

```css
/* Cores principais */
background-color: var(--bg-primary);     /* Fundo principal */
background-color: var(--bg-secondary);   /* Fundo de cards */
background-color: var(--bg-tertiary);    /* Fundo de elementos */

/* Texto */
color: var(--text-primary);             /* Texto principal */
color: var(--text-secondary);           /* Texto secundário */
color: var(--text-muted);              /* Texto menos importante */

/* Bordas */
border-color: var(--border-primary);    /* Bordas principais */
border-color: var(--border-secondary);  /* Bordas secundárias */

/* Cores de destaque */
color: var(--accent-green);             /* Verde (sucesso) */
color: var(--accent-blue);              /* Azul (informação) */
color: var(--accent-pink);              /* Rosa (destaque) */
```

## 🔄 Valores por Tema

### Dark Theme (Padrão)
```css
--bg-primary: #161618;
--bg-secondary: #1e1e22;
--bg-tertiary: #2a2a2e;
--text-primary: #ffffff;
--text-secondary: #a0a0a0;
--text-muted: #6b7280;
--accent-green: #32d583;
--accent-blue: #5392ff;
--accent-pink: #f178b6;
--border-primary: #333336;
--border-secondary: #27272a;
```

### Light Theme
```css
--bg-primary: #f8fafc;
--bg-secondary: #ffffff;
--bg-tertiary: #f1f5f9;
--text-primary: #0f172a;
--text-secondary: #64748b;
--text-muted: #94a3b8;
--accent-green: #059669;
--accent-blue: #3b82f6;
--accent-pink: #ec4899;
--border-primary: #e2e8f0;
--border-secondary: #cbd5e1;
```

## 💾 Persistência

O tema é automaticamente salvo no `localStorage` e restaurado na próxima visita:

```typescript
// Chave no localStorage
localStorage.getItem('finnantech-theme') // 'dark' | 'light'
```

## 🎯 Detecção Automática

O sistema detecta automaticamente a preferência do usuário:

1. **Tema salvo** no localStorage (prioridade alta)
2. **Preferência do sistema** (`prefers-color-scheme`)
3. **Fallback** para dark theme

## ✨ Animações

Todas as transições de tema são suaves (0.3s ease):

```css
.theme-transition {
  transition: background-color 0.3s ease, color 0.3s ease, border-color 0.3s ease;
}
```

## 📱 Exemplo Completo

```tsx
'use client'

import { useTheme } from '@/context/ThemeContext'
import { ThemeToggleCompact } from './components/ThemeToggle'

export function Header() {
  const { theme } = useTheme()
  
  return (
    <header className="card header-card">
      <h1>Finnantech V2</h1>
      
      <div className="flex items-center gap-4">
        {/* Outros botões */}
        <button className="action-icon">
          <Bell className="h-5 w-5" />
        </button>
        
        {/* Toggle de tema */}
        <ThemeToggleCompact />
        
        {/* Perfil do usuário */}
        <div className="user-profile">
          <img src="avatar.jpg" className="user-avatar" />
          <span className="user-name">Usuário</span>
        </div>
      </div>
    </header>
  )
}
```

## 🔍 Debugging

Para debugar o tema, use as devtools:

```javascript
// Console do navegador
console.log('Tema atual:', localStorage.getItem('finnantech-theme'))
console.log('Classe do HTML:', document.documentElement.className)
console.log('CSS Variables:', getComputedStyle(document.documentElement))
```

---

🎨 **Sistema implementado com sucesso!** O toggle de tema está disponível no header do dashboard e funciona com persistência automática. 