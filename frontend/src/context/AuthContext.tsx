'use client';

import React, { createContext, useContext, useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import toast from 'react-hot-toast';
import { User, AuthContextType } from '@/types/auth';
import { authAPI, tokenUtils } from '@/lib/api';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  // Verificar se usuário está autenticado ao carregar a página
  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async () => {
    try {
      setLoading(true);
      const response = await authAPI.validateToken();
      
      if (response?.success && response.user) {
        setUser(response.user);
      } else {
        setUser(null);
        tokenUtils.removeToken();
      }
    } catch (error) {
      console.error('Erro ao validar token:', error);
      setUser(null);
      tokenUtils.removeToken();
    } finally {
      setLoading(false);
    }
  };

  const login = async (email: string, password: string) => {
    try {
      setLoading(true);
      const response = await authAPI.login({ email, password });

      if (response.token) {
        tokenUtils.setToken(response.token);
        
        // Revalidar para obter dados do usuário
        await checkAuth();
        
        toast.success('Login realizado com sucesso!');
        router.push('/dashboard');
      } else {
        throw new Error(response.message || 'Erro ao fazer login');
      }
    } catch (error: any) {
      console.error('Erro no login:', error);
      toast.error(error.message || 'Erro ao fazer login');
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const register = async (name: string, email: string, password: string) => {
    try {
      setLoading(true);
      const response = await authAPI.register({ name, email, password });

      if (response.message && response.message.includes('sucesso')) {
        toast.success('Cadastro realizado com sucesso! Faça login para continuar.');
        router.push('/login');
      } else {
        throw new Error(response.message || 'Erro ao cadastrar usuário');
      }
    } catch (error: any) {
      console.error('Erro no cadastro:', error);
      toast.error(error.message || 'Erro ao cadastrar usuário');
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    try {
      setLoading(true);
      
      // Chamar o endpoint de logout no backend
      await authAPI.logout();
      
      // Remover dados locais
      setUser(null);
      tokenUtils.removeToken();
      
      toast.success('Logout realizado com sucesso!');
      router.push('/login');
    } catch (error: any) {
      console.error('Erro no logout:', error);
      
      // Mesmo se der erro no backend, fazemos logout local
      setUser(null);
      tokenUtils.removeToken();
      
      toast.success('Logout realizado!');
      router.push('/login');
    } finally {
      setLoading(false);
    }
  };

  const loginWithGoogle = () => {
    authAPI.loginWithGoogle();
  };

  const loginWithFacebook = () => {
    authAPI.loginWithFacebook();
  };

  const value = {
    user,
    loading,
    login,
    register,
    logout,
    loginWithGoogle,
    loginWithFacebook,
    checkAuth,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  return context;
} 