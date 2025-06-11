/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        background: '#161618',
        card: '#1e1e22',
        'text-primary': '#ffffff',
        'text-secondary': '#a0a0a0',
        'accent-green': '#32d583',
        'accent-blue': '#5392ff',
        'accent-pink': '#f178b6',
        border: '#333336',
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
    },
  },
  plugins: [],
} 