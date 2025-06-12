'use client'

import { useTheme } from '@/context/ThemeContext'

export function ThemeToggle() {
  const { theme, toggleTheme } = useTheme()

  return (
    <button
      onClick={toggleTheme}
      className="relative flex items-center justify-center w-14 h-8 border border-border rounded-full transition-all duration-300 group"
      style={{
        backgroundColor: 'var(--bg-secondary)',
        borderColor: 'var(--border-primary)'
      }}
      aria-label={`Alternar para tema ${theme === 'dark' ? 'claro' : 'escuro'}`}
    >
      {/* Background do toggle */}
      <div 
        className="absolute inset-1 rounded-full opacity-20"
        style={{
          background: `linear-gradient(to right, var(--accent-blue), var(--accent-green))`
        }}
      />
      
      {/* Slider */}
      <div
        className={`absolute w-6 h-6 rounded-full shadow-lg flex items-center justify-center transition-all duration-500 ease-out ${
          theme === 'dark' ? 'translate-x-[-12px]' : 'translate-x-[12px]'
        }`}
        style={{
          background: `linear-gradient(135deg, var(--accent-blue), var(--accent-green))`
        }}
      >
        {/* Ícone baseado no tema atual */}
        <div
          className={`text-white text-xs transition-all duration-300 ${
            theme === 'dark' ? 'rotate-0' : 'rotate-180'
          }`}
        >
          {theme === 'dark' ? (
            // Ícone da lua (tema escuro ativo)
            <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
              <path d="M21.752 15.002A9.718 9.718 0 0118 15.75c-5.385 0-9.75-4.365-9.75-9.75 0-1.33.266-2.597.748-3.752A9.753 9.753 0 003 11.25C3 16.635 7.365 21 12.75 21a9.753 9.753 0 009.002-5.998z" />
            </svg>
          ) : (
            // Ícone do sol (tema claro ativo)
            <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2.25a.75.75 0 01.75.75v2.25a.75.75 0 01-1.5 0V3a.75.75 0 01.75-.75zM7.5 12a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM18.894 6.166a.75.75 0 00-1.06-1.06l-1.591 1.59a.75.75 0 101.06 1.061l1.591-1.59zM21.75 12a.75.75 0 01-.75.75h-2.25a.75.75 0 010-1.5H21a.75.75 0 01.75.75zM17.834 18.894a.75.75 0 001.06-1.06l-1.59-1.591a.75.75 0 10-1.061 1.06l1.59 1.591zM12 18a.75.75 0 01.75.75V21a.75.75 0 01-1.5 0v-2.25A.75.75 0 0112 18zM7.758 17.303a.75.75 0 00-1.061-1.06l-1.591 1.59a.75.75 0 001.06 1.061l1.591-1.59zM6 12a.75.75 0 01-.75.75H3a.75.75 0 010-1.5h2.25A.75.75 0 016 12zM6.697 7.757a.75.75 0 001.06-1.06l-1.59-1.591a.75.75 0 00-1.061 1.06l1.59 1.591z" />
            </svg>
          )}
        </div>
      </div>

      {/* Efeito de glow no hover */}
      <div 
        className="absolute inset-0 rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-300"
        style={{
          background: `linear-gradient(to right, var(--accent-blue), var(--accent-green))`,
          opacity: 0.1
        }}
      />
    </button>
  )
}

// Versão compacta para usar no header
export function ThemeToggleCompact() {
  const { theme, toggleTheme } = useTheme()

  return (
    <button
      onClick={toggleTheme}
      className="p-2 rounded-lg transition-all duration-200 group"
      style={{
        backgroundColor: 'var(--bg-secondary)',
        color: 'var(--text-secondary)'
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.backgroundColor = 'var(--bg-tertiary)'
        e.currentTarget.style.color = 'var(--text-primary)'
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.backgroundColor = 'var(--bg-secondary)'
        e.currentTarget.style.color = 'var(--text-secondary)'
      }}
      aria-label={`Alternar para tema ${theme === 'dark' ? 'claro' : 'escuro'}`}
    >
      <div className={`transition-all duration-300 group-hover:scale-110 ${theme === 'dark' ? 'rotate-0' : 'rotate-180'}`}>
        {theme === 'dark' ? (
          // Ícone do sol (mostrar para ativar light mode)
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="12" cy="12" r="5"/>
            <path d="M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"/>
          </svg>
        ) : (
          // Ícone da lua (mostrar para ativar dark mode)
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>
          </svg>
        )}
      </div>
    </button>
  )
} 