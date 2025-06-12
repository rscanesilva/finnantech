'use client'

import React, { createContext, useContext, useEffect, useState } from 'react'

type Theme = 'dark' | 'light'

interface ThemeContextType {
  theme: Theme
  setTheme: (theme: Theme) => void
  toggleTheme: () => void
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined)

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [theme, setThemeState] = useState<Theme>('dark')
  const [mounted, setMounted] = useState(false)

  // CSS Variables para cada tema
  const applyThemeVariables = (newTheme: Theme) => {
    const root = document.documentElement
    
    const lightVars = {
      '--bg-primary': '#f8fafc',
      '--bg-secondary': '#ffffff',
      '--bg-tertiary': '#f1f5f9',
      '--text-primary': '#0f172a',
      '--text-secondary': '#64748b',
      '--text-muted': '#94a3b8',
      '--accent-green': '#059669',
      '--accent-blue': '#3b82f6',
      '--accent-pink': '#ec4899',
      '--border-primary': '#e2e8f0',
      '--border-secondary': '#cbd5e1',
    }
    
    const darkVars = {
      '--bg-primary': '#161618',
      '--bg-secondary': '#1e1e22',
      '--bg-tertiary': '#2a2a2e',
      '--text-primary': '#ffffff',
      '--text-secondary': '#a0a0a0',
      '--text-muted': '#6b7280',
      '--accent-green': '#32d583',
      '--accent-blue': '#5392ff',
      '--accent-pink': '#f178b6',
      '--border-primary': '#333336',
      '--border-secondary': '#27272a',
    }
    
    const vars = newTheme === 'light' ? lightVars : darkVars
    
    // Remove classes antigas e aplica nova
    root.classList.remove('light', 'dark')
    root.classList.add(newTheme)
    
    // Aplica todas as CSS variables
    Object.entries(vars).forEach(([property, value]) => {
      root.style.setProperty(property, value)
    })
    
    console.log(`Tema aplicado: ${newTheme}`, vars)
  }

  const setTheme = (newTheme: Theme) => {
    setThemeState(newTheme)
    localStorage.setItem('finnantech-theme', newTheme)
    
    if (mounted) {
      applyThemeVariables(newTheme)
    }
  }

  const toggleTheme = () => {
    const newTheme = theme === 'dark' ? 'light' : 'dark'
    setTheme(newTheme)
  }

  useEffect(() => {
    // Detectar tema inicial
    const savedTheme = localStorage.getItem('finnantech-theme') as Theme
    const systemTheme = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
    const initialTheme = savedTheme || systemTheme

    setThemeState(initialTheme)
    applyThemeVariables(initialTheme)
    setMounted(true)
  }, [])

  // Aplicar variáveis sempre que o tema mudar
  useEffect(() => {
    if (mounted) {
      applyThemeVariables(theme)
    }
  }, [theme, mounted])

  // Prevenção de flash durante hidratação
  if (!mounted) {
    return (
      <div style={{ visibility: 'hidden' }}>
        {children}
      </div>
    )
  }

  return (
    <ThemeContext.Provider value={{ theme, setTheme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  )
}

export function useTheme() {
  const context = useContext(ThemeContext)
  if (context === undefined) {
    throw new Error('useTheme deve ser usado dentro de um ThemeProvider')
  }
  return context
} 