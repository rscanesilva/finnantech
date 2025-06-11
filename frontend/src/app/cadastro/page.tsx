'use client';

import { useState } from 'react';
import { useAuth } from '@/context/AuthContext';
import { User, EnvelopeSimple, Lock, GoogleLogo, FacebookLogo } from '@phosphor-icons/react';

export default function CadastroPage() {
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    confirmarSenha: '',
  });
  const { register, loginWithGoogle, loginWithFacebook, loading } = useAuth();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (formData.senha !== formData.confirmarSenha) {
      alert('As senhas não coincidem');
      return;
    }

    if (formData.senha.length < 6) {
      alert('A senha deve ter pelo menos 6 caracteres');
      return;
    }

    try {
      await register(formData.nome, formData.email, formData.senha);
    } catch (error) {
      // Erro já tratado no contexto
    }
  };

  const handleGoogleLogin = () => {
    loginWithGoogle();
  };

  const handleFacebookLogin = () => {
    loginWithFacebook();
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-6">
      <div className="w-full max-w-md">
        <div className="card">
          <div className="text-center">
            <h2 className="text-2xl font-bold mb-2 text-text-primary">Criar nova conta</h2>
            <p className="text-text-secondary">Preencha os dados abaixo para se cadastrar</p>
          </div>

          <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
            <div className="space-y-4">
              <div>
                <label htmlFor="nome" className="block text-sm font-medium text-text-secondary mb-1">
                  Nome completo
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <User className="h-5 w-5 text-text-secondary" />
                  </div>
                  <input
                    id="nome"
                    name="nome"
                    type="text"
                    required
                    className="block w-full pl-10 pr-3 py-2 border border-border rounded-lg bg-card text-text-primary placeholder-text-secondary focus:outline-none focus:ring-2 focus:ring-accent-blue focus:border-transparent"
                    placeholder="Seu nome completo"
                    value={formData.nome}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="email" className="block text-sm font-medium text-text-secondary mb-1">
                  Email
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <EnvelopeSimple className="h-5 w-5 text-text-secondary" />
                  </div>
                  <input
                    id="email"
                    name="email"
                    type="email"
                    required
                    className="block w-full pl-10 pr-3 py-2 border border-border rounded-lg bg-card text-text-primary placeholder-text-secondary focus:outline-none focus:ring-2 focus:ring-accent-blue focus:border-transparent"
                    placeholder="seu@email.com"
                    value={formData.email}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="senha" className="block text-sm font-medium text-text-secondary mb-1">
                  Senha
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Lock className="h-5 w-5 text-text-secondary" />
                  </div>
                  <input
                    id="senha"
                    name="senha"
                    type="password"
                    required
                    minLength={6}
                    className="block w-full pl-10 pr-3 py-2 border border-border rounded-lg bg-card text-text-primary placeholder-text-secondary focus:outline-none focus:ring-2 focus:ring-accent-blue focus:border-transparent"
                    placeholder="••••••••"
                    value={formData.senha}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="confirmarSenha" className="block text-sm font-medium text-text-secondary mb-1">
                  Confirmar Senha
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Lock className="h-5 w-5 text-text-secondary" />
                  </div>
                  <input
                    id="confirmarSenha"
                    name="confirmarSenha"
                    type="password"
                    required
                    minLength={6}
                    className="block w-full pl-10 pr-3 py-2 border border-border rounded-lg bg-card text-text-primary placeholder-text-secondary focus:outline-none focus:ring-2 focus:ring-accent-blue focus:border-transparent"
                    placeholder="••••••••"
                    value={formData.confirmarSenha}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </div>
              </div>
            </div>

            <div>
              <button
                type="submit"
                disabled={loading}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-lg text-sm font-medium text-background bg-accent-blue hover:bg-accent-blue/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-accent-blue disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? 'Cadastrando...' : 'Cadastrar'}
              </button>
            </div>

            {/* Divisor */}
            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-border" />
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-card text-text-secondary">Ou cadastre-se com</span>
              </div>
            </div>

            {/* Botões de cadastro social */}
            <div className="grid grid-cols-2 gap-3">
              <button
                type="button"
                onClick={handleGoogleLogin}
                disabled={loading}
                className="w-full inline-flex justify-center py-2 px-4 border border-border rounded-lg bg-card text-text-primary hover:bg-card/80 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-accent-blue disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <GoogleLogo className="h-5 w-5 mr-2" />
                Google
              </button>

              <button
                type="button"
                onClick={handleFacebookLogin}
                disabled={loading}
                className="w-full inline-flex justify-center py-2 px-4 border border-border rounded-lg bg-card text-text-primary hover:bg-card/80 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-accent-blue disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <FacebookLogo className="h-5 w-5 mr-2" />
                Facebook
              </button>
            </div>

            <div className="text-center">
              <p className="text-sm text-text-secondary">
                Já tem uma conta?{' '}
                <a href="/login" className="text-accent-blue hover:text-accent-blue/90">
                  Faça login
                </a>
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
} 