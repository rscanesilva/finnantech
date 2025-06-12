/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        'background': 'var(--bg-primary)',
        'card': 'var(--bg-secondary)',
        'card-secondary': 'var(--bg-tertiary)',
        'text': {
          'primary': 'var(--text-primary)',
          'secondary': 'var(--text-secondary)',
          'muted': 'var(--text-muted)',
        },
        'accent-green': 'var(--accent-green)',
        'accent-blue': 'var(--accent-blue)',
        'accent-pink': 'var(--accent-pink)',
        'border': 'var(--border-primary)',
        'border-secondary': 'var(--border-secondary)',
        'success': '#10b981',
        'warning': '#f59e0b',
        'error': '#ef4444',
        'info': '#0ea5e9',
        light: {
          background: '#f8fafc',
          card: '#ffffff',
          'card-secondary': '#f1f5f9',
          'text-primary': '#0f172a',
          'text-secondary': '#64748b',
          'text-muted': '#94a3b8',
          'accent-green': '#059669',
          'accent-blue': '#3b82f6',
          'accent-pink': '#ec4899',
          border: '#e2e8f0',
        },
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
      },
      borderRadius: {
        'card': '1rem',
      },
      spacing: {
        'card': '1.5rem',
      },
      animation: {
        'theme-switch': 'theme-switch 0.3s ease-in-out',
        'bounce-soft': 'bounce-soft 0.6s ease-out',
        'fade-in': 'fade-in 0.5s ease-out',
        'slide-up': 'slide-up 0.4s ease-out',
      },
      keyframes: {
        'theme-switch': {
          '0%': { transform: 'scale(1) rotate(0deg)' },
          '50%': { transform: 'scale(1.1) rotate(180deg)' },
          '100%': { transform: 'scale(1) rotate(360deg)' },
        },
        'bounce-soft': {
          '0%': { transform: 'scale(1)' },
          '50%': { transform: 'scale(1.05)' },
          '100%': { transform: 'scale(1)' },
        },
        'fade-in': {
          '0%': { opacity: '0', transform: 'translateY(10px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        'slide-up': {
          '0%': { transform: 'translateY(20px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
      },
    },
  },
  plugins: [
    function({ addUtilities }) {
      const newUtilities = {
        '.text-text-primary': {
          color: 'var(--text-primary) !important',
        },
        '.text-text-secondary': {
          color: 'var(--text-secondary) !important',
        },
        '.text-text-muted': {
          color: 'var(--text-muted) !important',
        },
        '.bg-background': {
          backgroundColor: 'var(--bg-primary) !important',
        },
        '.bg-card': {
          backgroundColor: 'var(--bg-secondary) !important',
        },
        '.bg-card-secondary': {
          backgroundColor: 'var(--bg-tertiary) !important',
        },
        '.border-border': {
          borderColor: 'var(--border-primary) !important',
        },
        '.border-border-secondary': {
          borderColor: 'var(--border-secondary) !important',
        },
        '.hover\\:text-text-primary:hover': {
          color: 'var(--text-primary) !important',
        },
        '.hover\\:text-text-secondary:hover': {
          color: 'var(--text-secondary) !important',
        },
        '.hover\\:bg-background:hover': {
          backgroundColor: 'var(--bg-primary) !important',
        },
        '.hover\\:bg-card:hover': {
          backgroundColor: 'var(--bg-secondary) !important',
        },
        '.hover\\:bg-card-secondary:hover': {
          backgroundColor: 'var(--bg-tertiary) !important',
        },
        '.bg-theme-primary': {
          backgroundColor: 'var(--bg-primary)',
        },
        '.bg-theme-secondary': {
          backgroundColor: 'var(--bg-secondary)',
        },
        '.bg-theme-tertiary': {
          backgroundColor: 'var(--bg-tertiary)',
        },
        '.text-theme-primary': {
          color: 'var(--text-primary)',
        },
        '.text-theme-secondary': {
          color: 'var(--text-secondary)',
        },
        '.text-theme-muted': {
          color: 'var(--text-muted)',
        },
        '.border-theme': {
          borderColor: 'var(--border-primary)',
        },
        '.border-theme-secondary': {
          borderColor: 'var(--border-secondary)',
        },
        '.hover\\:bg-theme-tertiary:hover': {
          backgroundColor: 'var(--bg-tertiary)',
        },
        '.hover\\:text-theme-primary:hover': {
          color: 'var(--text-primary)',
        },
      }
      
      addUtilities(newUtilities)
    }
  ],
} 