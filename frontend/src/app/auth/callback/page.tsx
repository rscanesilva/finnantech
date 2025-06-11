'use client';

import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { tokenUtils } from '@/lib/api';
import { useAuth } from '@/context/AuthContext';

export default function CallbackPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { checkAuth } = useAuth();
  const [message, setMessage] = useState('Processando login...');

  useEffect(() => {
    const processCallback = async () => {
      try {
        // Pegar token da URL
        const token = searchParams.get('token');
        const error = searchParams.get('error');

        if (error) {
          setMessage('Erro no login: ' + error);
          setTimeout(() => router.push('/login'), 3000);
          return;
        }

        if (token) {
          // Salvar token
          tokenUtils.setToken(token);
          
          // Revalidar autenticação no contexto
          await checkAuth();
          
          setMessage('Login realizado com sucesso! Redirecionando...');
          setTimeout(() => router.push('/dashboard'), 1000);
        } else {
          setMessage('Token não encontrado na URL');
          setTimeout(() => router.push('/login'), 3000);
        }
      } catch (error) {
        console.error('Erro ao processar callback:', error);
        setMessage('Erro ao processar login');
        setTimeout(() => router.push('/login'), 3000);
      }
    };

    processCallback();
  }, [searchParams, router, checkAuth]);

  return (
    <div className="min-h-screen bg-background flex items-center justify-center">
      <div className="text-center">
        <h1 className="text-2xl font-bold mb-4 text-text-primary">@Finnantech</h1>
        <div className="text-text-secondary">{message}</div>
        <div className="mt-4">
          <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-accent-blue"></div>
        </div>
      </div>
    </div>
  );
}