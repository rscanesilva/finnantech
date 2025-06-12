'use client'

import { useTheme } from '@/context/ThemeContext'
import { useEffect } from 'react'

export function ThemeFixGuide() {
  const { theme } = useTheme()
  
  useEffect(() => {
    const applyThemeStyles = () => {
      const root = document.documentElement
      
      // Força a aplicação da classe correta
      if (theme === 'light') {
        root.classList.remove('dark')
        root.classList.add('light')
      } else {
        root.classList.remove('light')
        root.classList.add('dark')
      }
      
      // Força a aplicação das CSS variables via JavaScript
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
      
      const vars = theme === 'light' ? lightVars : darkVars
      
      Object.entries(vars).forEach(([property, value]) => {
        root.style.setProperty(property, value)
      })
      
      console.log(`Tema aplicado: ${theme}`, vars)
    }
    
    applyThemeStyles()
  }, [theme])
  
  return null // Componente invisível
} 