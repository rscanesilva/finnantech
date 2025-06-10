'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { User, EnvelopeSimple, Lock, IdentificationCard, Phone } from '@phosphor-icons/react';

export default function CadastroPage() {
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    confirmarSenha: '',
    cpf: '',
    telefone: '',
  });
  const router = useRouter();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    // TODO: Implementar validação e cadastro
    console.log('Cadastro:', formData);
    router.push('/login');
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
                  />
                </div>
              </div>

              <div>
                <label htmlFor="cpf" className="block text-sm font-medium text-text-secondary mb-1">
                  CPF
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <IdentificationCard className="h-5 w-5 text-text-secondary" />
                  </div>
                  <input
                    id="cpf"
                    name="cpf"
                    type="text"
                    required
                    className="block w-full pl-10 pr-3 py-2 border border-border rounded-lg bg-card text-text-primary placeholder-text-secondary focus:outline-none focus:ring-2 focus:ring-accent-blue focus:border-transparent"
                    placeholder="000.000.000-00"
                    value={formData.cpf}
                    onChange={handleChange}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="telefone" className="block text-sm font-medium text-text-secondary mb-1">
                  Telefone
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <Phone className="h-5 w-5 text-text-secondary" />
                  </div>
                  <input
                    id="telefone"
                    name="telefone"
                    type="tel"
                    required
                    className="block w-full pl-10 pr-3 py-2 border border-border rounded-lg bg-card text-text-primary placeholder-text-secondary focus:outline-none focus:ring-2 focus:ring-accent-blue focus:border-transparent"
                    placeholder="(00) 00000-0000"
                    value={formData.telefone}
                    onChange={handleChange}
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
                    className="block w-full pl-10 pr-3 py-2 border border-border rounded-lg bg-card text-text-primary placeholder-text-secondary focus:outline-none focus:ring-2 focus:ring-accent-blue focus:border-transparent"
                    placeholder="••••••••"
                    value={formData.senha}
                    onChange={handleChange}
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
                    className="block w-full pl-10 pr-3 py-2 border border-border rounded-lg bg-card text-text-primary placeholder-text-secondary focus:outline-none focus:ring-2 focus:ring-accent-blue focus:border-transparent"
                    placeholder="••••••••"
                    value={formData.confirmarSenha}
                    onChange={handleChange}
                  />
                </div>
              </div>
            </div>

            <div>
              <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-lg text-sm font-medium text-background bg-accent-blue hover:bg-accent-blue/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-accent-blue"
              >
                Cadastrar
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