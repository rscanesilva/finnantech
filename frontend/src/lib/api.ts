import axios from 'axios';
import Cookies from 'js-cookie';
import { LoginRequest, RegisterRequest, AuthResponse } from '@/types/auth';
import { 
  Transaction, 
  Category, 
  PaymentMethod, 
  Investment, 
  Budget, 
  DashboardSummary, 
  TransactionFilters 
} from '@/types/dashboard';

// Configuração base da API
const API_BASE_URL = 'http://localhost:8080/api';

// Instância do axios
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para adicionar token JWT nas requisições
api.interceptors.request.use((config) => {
  const token = Cookies.get('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor para tratar respostas e erros
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Só redirecionar se não for uma tentativa de login ou registro
      const isAuthEndpoint = error.config?.url?.includes('/auth/login') || 
                            error.config?.url?.includes('/auth/register');
      
      if (!isAuthEndpoint) {
        // Token expirado ou inválido em outras operações
        Cookies.remove('token');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

// Funções de autenticação
export const authAPI = {
  // Login com email/senha
  async login(data: LoginRequest): Promise<AuthResponse> {
    try {
      const response = await api.post('/auth/login', data);
      return response.data;
    } catch (error: any) {
      // Tratamento específico para diferentes tipos de erro
      if (error.response) {
        const status = error.response.status;
        const message = error.response.data?.message;
        
        switch (status) {
          case 401:
            throw new Error(message || 'Email ou senha incorretos. Verifique suas credenciais.');
          case 403:
            throw new Error('Acesso negado. Conta pode estar desativada.');
          case 404:
            throw new Error('Usuário não encontrado.');
          case 422:
            throw new Error('Dados inválidos. Verifique email e senha.');
          case 500:
            throw new Error('Erro interno do servidor. Tente novamente mais tarde.');
          default:
            throw new Error(message || 'Erro ao fazer login');
        }
      } else if (error.request) {
        throw new Error('Não foi possível conectar ao servidor. Verifique sua conexão.');
      } else {
        throw new Error(error.message || 'Erro inesperado ao fazer login');
      }
    }
  },

  // Registro de novo usuário
  async register(data: RegisterRequest): Promise<AuthResponse> {
    try {
      const response = await api.post('/auth/register', data);
      return response.data;
    } catch (error: any) {
      // Tratamento específico para diferentes tipos de erro
      if (error.response) {
        const status = error.response.status;
        const message = error.response.data?.message;
        
        switch (status) {
          case 409:
            throw new Error(message || 'Este email já está cadastrado. Tente fazer login.');
          case 400:
            throw new Error(message || 'Dados inválidos. Verifique as informações fornecidas.');
          case 422:
            throw new Error('Dados inválidos. Verifique nome, email e senha.');
          case 500:
            throw new Error('Erro interno do servidor. Tente novamente mais tarde.');
          default:
            throw new Error(message || 'Erro ao cadastrar usuário');
        }
      } else if (error.request) {
        throw new Error('Não foi possível conectar ao servidor. Verifique sua conexão.');
      } else {
        throw new Error(error.message || 'Erro inesperado ao cadastrar usuário');
      }
    }
  },

  // Login com Google
  loginWithGoogle() {
    alert('Login com Google será implementado em breve!');
    // window.location.href = `${API_BASE_URL}/auth/oauth2/google`;
  },

  // Login com Facebook
  loginWithFacebook() {
    alert('Login com Facebook será implementado em breve!');
    // window.location.href = `${API_BASE_URL}/auth/oauth2/facebook`;
  },

  // Logout do usuário
  async logout(): Promise<AuthResponse> {
    try {
      const response = await api.post('/auth/logout');
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao fazer logout');
    }
  },

  // Validar token e obter dados do usuário
  async validateToken(): Promise<AuthResponse | null> {
    try {
      const token = Cookies.get('token');
      if (!token) return null;

      // Por enquanto, vamos decodificar o JWT no frontend
      // Em produção, seria melhor ter um endpoint /me no backend
      const payload = JSON.parse(atob(token.split('.')[1]));
      
      return {
        success: true,
        message: 'Token válido',
        user: {
          id: payload.userId || payload.sub,
          name: payload.name || 'Usuário',
          email: payload.sub || payload.email || '',
          provider: (payload.provider || 'LOCAL') as 'LOCAL' | 'GOOGLE' | 'FACEBOOK',
          emailVerified: payload.emailVerified || false,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        }
      };
    } catch (error) {
      console.error('Erro ao validar token:', error);
      Cookies.remove('token');
      return null;
    }
  }
};

// Funções utilitárias
export const tokenUtils = {
  setToken(token: string) {
    Cookies.set('token', token, { 
      expires: 1, // 1 dia
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'strict'
    });
  },

  removeToken() {
    Cookies.remove('token');
  },

  getToken() {
    return Cookies.get('token');
  }
};

// Funções para o dashboard
export const dashboardAPI = {
  // Obter resumo do dashboard
  async getSummary(): Promise<DashboardSummary> {
    try {
      const response = await api.get('/dashboard/summary');
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter resumo do dashboard');
    }
  },

  // Obter todas as transações
  async getTransactions(filters?: TransactionFilters): Promise<Transaction[]> {
    try {
      const params = new URLSearchParams();
      if (filters) {
        Object.entries(filters).forEach(([key, value]) => {
          if (value !== undefined && value !== null) {
            params.append(key, value.toString());
          }
        });
      }

      const response = await api.get(`/transactions?${params.toString()}`);
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter transações');
    }
  },

  // Obter transações recentes
  async getRecentTransactions(limit: number = 10): Promise<Transaction[]> {
    try {
      const response = await api.get(`/transactions/recent?limit=${limit}`);
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter transações recentes');
    }
  },

  // Obter categorias
  async getCategories(): Promise<Category[]> {
    try {
      const response = await api.get('/categories');
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter categorias');
    }
  },

  // Obter métodos de pagamento
  async getPaymentMethods(): Promise<PaymentMethod[]> {
    try {
      const response = await api.get('/payment-methods');
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter métodos de pagamento');
    }
  },

  // Obter cartões de crédito
  async getCreditCards(): Promise<PaymentMethod[]> {
    try {
      const response = await api.get('/payment-methods?type=CARTAO_CREDITO');
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter cartões de crédito');
    }
  },

  // Obter investimentos
  async getInvestments(): Promise<Investment[]> {
    try {
      const response = await api.get('/investments');
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter investimentos');
    }
  },

  // Obter orçamentos
  async getBudgets(monthYear?: string): Promise<Budget[]> {
    try {
      const params = monthYear ? `?monthYear=${monthYear}` : '';
      const response = await api.get(`/budgets${params}`);
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter orçamentos');
    }
  },

  // Obter estatísticas de gastos por categoria
  async getCategoryExpenses(monthYear?: string): Promise<any[]> {
    try {
      const params = monthYear ? `?monthYear=${monthYear}` : '';
      const response = await api.get(`/transactions/statistics/categories${params}`);
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter estatísticas por categoria');
    }
  },

  // Obter evolução mensal
  async getMonthlyEvolution(months: number = 6): Promise<any[]> {
    try {
      const response = await api.get(`/transactions/statistics/monthly?months=${months}`);
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter evolução mensal');
    }
  },

  // Criar nova transação
  async createTransaction(transaction: Partial<Transaction>): Promise<Transaction> {
    try {
      const response = await api.post('/transactions', transaction);
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao criar transação');
    }
  },

  // Atualizar transação
  async updateTransaction(id: string, transaction: Partial<Transaction>): Promise<Transaction> {
    try {
      const response = await api.put(`/transactions/${id}`, transaction);
      return response.data;
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao atualizar transação');
    }
  },

  // Deletar transação
  async deleteTransaction(id: string): Promise<void> {
    try {
      await api.delete(`/transactions/${id}`);
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao deletar transação');
    }
  }
};

export default api;