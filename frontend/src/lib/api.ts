import axios from 'axios';
import Cookies from 'js-cookie';
import { LoginRequest, RegisterRequest, AuthResponse } from '@/types/auth';
import { 
  Transaction,
  RecentTransaction,
  Category, 
  PaymentMethod, 
  Investment, 
  Budget, 
  DashboardSummary,
  DashboardSummaryResponse,
  CategoryStatsResponse,
  MonthlyStatsResponse,
  MonthlyExpensesResponse,
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
  async getSummary(): Promise<DashboardSummaryResponse> {
    try {
      const response = await api.get('/v1/dashboard/summary');
      if (response.data.success) {
        // Mapear dados da API para o formato esperado pelo frontend
        const apiData = response.data.data;
        return {
          totalBalance: apiData.saldoAtual || 0,
          totalIncomes: apiData.totalReceitas || 0,
          totalExpenses: apiData.totalDespesas || 0,
          totalInvestments: 0, // Não disponível na API ainda
          currentMonthIncomes: apiData.totalReceitas || 0,
          currentMonthExpenses: apiData.totalDespesas || 0,
          currentMonthBalance: apiData.saldoAtual || 0,
          lastMonthIncomes: 0, // Calcular baseado em saldoAnterior se necessário
          lastMonthExpenses: 0,
          lastMonthBalance: apiData.saldoAnterior || 0,
          incomesVariation: apiData.variacaoReceitas || 0,
          expensesVariation: apiData.variacaoDespesas || 0,
          balanceVariation: apiData.variacaoSaldo || 0,
          transactionsCount: apiData.quantidadeTransacoes || 0,
          categoriesCount: 8, // Valor padrão
          averageTransactionAmount: apiData.gastoMedio || 0,
          lastUpdateDate: apiData.ultimaAtualizacao || new Date().toISOString()
        };
      }
      throw new Error(response.data.message || 'Erro ao obter resumo');
    } catch (error: any) {
      console.warn('Erro ao carregar resumo da API:', error);
      // Retornar dados mockados como fallback
      return {
        totalBalance: 15000.00,
        totalIncomes: 25000.00,
        totalExpenses: 10000.00,
        totalInvestments: 5000.00,
        currentMonthIncomes: 8500.00,
        currentMonthExpenses: 6200.00,
        currentMonthBalance: 2300.00,
        lastMonthIncomes: 8000.00,
        lastMonthExpenses: 5800.00,
        lastMonthBalance: 2200.00,
        incomesVariation: 6.25,
        expensesVariation: 6.90,
        balanceVariation: 4.55,
        transactionsCount: 127,
        categoriesCount: 8,
        averageTransactionAmount: 156.30,
        lastUpdateDate: new Date().toISOString()
      };
    }
  },

  // Obter todas as transações (temporariamente mockado)
  async getTransactions(filters?: TransactionFilters): Promise<Transaction[]> {
    try {
      // Como não temos o TransactionController, retornar array vazio
      return [];
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter transações');
    }
  },

  // Obter transações recentes
  async getRecentTransactions(limit: number = 10): Promise<RecentTransaction[]> {
    try {
      const response = await api.get(`/v1/dashboard/recent-transactions`);
      if (response.data.success) {
        return response.data.data.map((t: any) => ({
          id: t.id,
          type: t.type,
          amount: Math.abs(t.amount), // Converter para valor positivo
          description: t.description,
          transactionDate: t.date + 'T00:00:00Z', // Adicionar formato de data
          categoryName: t.categoryName,
          categoryIcon: t.categoryIcon,
          categoryColor: '#5392ff', // Cor padrão - será definida no frontend
          merchantCategory: t.categoryName,
          paymentMethodName: t.paymentMethodName,
          paymentMethodIcon: 'card', // Ícone padrão
          status: 'CONFIRMADA'
        }));
      }
      throw new Error(response.data.message || 'Erro ao obter transações recentes');
    } catch (error: any) {
      console.warn('Erro ao carregar transações recentes da API:', error);
      // Retornar dados mockados como fallback
      return [
        {
          id: "1",
          type: "RECEITA",
          amount: 5000.00,
          description: "Salário",
          transactionDate: "2024-02-01T00:00:00Z",
          categoryName: "Salário",
          categoryIcon: "Wallet",
          categoryColor: "#32d583",
          merchantCategory: "Receita",
          paymentMethodName: "PIX",
          paymentMethodIcon: "pix",
          status: "CONFIRMADA"
        },
        {
          id: "2",
          type: "DESPESA",
          amount: 120.00,
          description: "Walmart",
          transactionDate: "2024-02-09T09:22:00Z",
          categoryName: "Mercado",
          categoryIcon: "ShoppingCart",
          categoryColor: "#5392ff",
          merchantCategory: "Supermercado",
          paymentMethodName: "Cartão de Débito",
          paymentMethodIcon: "card",
          status: "CONFIRMADA"
        }
      ];
    }
  },

  // Obter categorias
  async getCategories(): Promise<Category[]> {
    try {
      const response = await api.get('/v1/categories');
      if (response.data.success) {
        return response.data.data;
      }
      throw new Error(response.data.message || 'Erro ao obter categorias');
    } catch (error: any) {
      console.warn('Erro ao carregar categorias da API:', error);
      return [];
    }
  },

  // Obter métodos de pagamento (temporariamente mockado)
  async getPaymentMethods(): Promise<PaymentMethod[]> {
    try {
      // Como não temos o PaymentMethodController, retornar array vazio
      return [];
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter métodos de pagamento');
    }
  },

  // Obter cartões de crédito (temporariamente mockado)
  async getCreditCards(): Promise<PaymentMethod[]> {
    try {
      // Como não temos o PaymentMethodController, retornar array vazio
      return [];
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter cartões de crédito');
    }
  },

  // Obter investimentos (temporariamente mockado)
  async getInvestments(): Promise<Investment[]> {
    try {
      // Retornar array vazio por enquanto
      return [];
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao obter investimentos');
    }
  },

  // Obter orçamentos
  async getBudgets(monthYear?: string): Promise<Budget[]> {
    try {
      const response = await api.get('/v1/dashboard/budgets');
      if (response.data.success) {
        return response.data.data;
      }
      throw new Error(response.data.message || 'Erro ao obter orçamentos');
    } catch (error: any) {
      console.warn('Erro ao carregar orçamentos da API:', error);
      return [];
    }
  },

  // Obter estatísticas de gastos por categoria
  async getCategoryExpenses(monthYear?: string): Promise<CategoryStatsResponse[]> {
    try {
      const response = await api.get('/v1/dashboard/categories/stats');
      if (response.data.success) {
        return response.data.data.map((cat: any) => ({
          categoryId: cat.categoryId,
          categoryName: cat.categoryName,
          categoryIcon: cat.categoryIcon || 'ShoppingCart',
          categoryColor: cat.categoryColor || '#5392ff',
          totalAmount: cat.totalAmount,
          transactionCount: cat.transactionCount,
          percentage: cat.percentage,
          averageAmount: cat.totalAmount / cat.transactionCount,
          monthYear: "2024-02"
        }));
      }
      throw new Error(response.data.message || 'Erro ao obter estatísticas por categoria');
    } catch (error: any) {
      console.warn('Erro ao carregar estatísticas por categoria da API:', error);
      // Retornar dados mockados como fallback
      return [
        {
          categoryId: "1",
          categoryName: "Mercado",
          categoryIcon: "ShoppingCart",
          categoryColor: "#5392ff",
          totalAmount: 850.50,
          transactionCount: 12,
          percentage: 26.15,
          averageAmount: 70.88,
          monthYear: "2024-02"
        },
        {
          categoryId: "2",
          categoryName: "Moradia",
          categoryIcon: "House",
          categoryColor: "#8b5cf6",
          totalAmount: 1200.00,
          transactionCount: 3,
          percentage: 36.92,
          averageAmount: 400.00,
          monthYear: "2024-02"
        }
      ];
    }
  },

  // Obter evolução mensal
  async getMonthlyEvolution(months: number = 6): Promise<MonthlyStatsResponse[]> {
    try {
      const response = await api.get('/v1/dashboard/evolution');
      if (response.data.success) {
        return response.data.data;
      }
      throw new Error(response.data.message || 'Erro ao obter evolução mensal');
    } catch (error: any) {
      console.warn('Erro ao carregar evolução mensal da API:', error);
      // Retornar dados mockados como fallback
      return [
        { monthYear: "2023-09", totalIncomes: 7200, totalExpenses: 5400, totalBalance: 1800, transactionCount: 25, averageTransaction: 156.30 },
        { monthYear: "2023-10", totalIncomes: 8500, totalExpenses: 6200, totalBalance: 2300, transactionCount: 28, averageTransaction: 165.20 },
        { monthYear: "2023-11", totalIncomes: 7800, totalExpenses: 5800, totalBalance: 2000, transactionCount: 22, averageTransaction: 145.80 },
        { monthYear: "2023-12", totalIncomes: 9200, totalExpenses: 6800, totalBalance: 2400, transactionCount: 35, averageTransaction: 172.50 },
        { monthYear: "2024-01", totalIncomes: 8800, totalExpenses: 6400, totalBalance: 2400, transactionCount: 30, averageTransaction: 160.00 },
        { monthYear: "2024-02", totalIncomes: 8500, totalExpenses: 6200, totalBalance: 2300, transactionCount: 27, averageTransaction: 155.75 }
      ];
    }
  },

  // Obter despesas mensais (últimos 6 meses)
  async getMonthlyExpenses(): Promise<MonthlyExpensesResponse[]> {
    try {
      const response = await api.get('/v1/dashboard/expenses/monthly');
      if (response.data.success) {
        return response.data.data;
      }
      throw new Error(response.data.message || 'Erro ao obter despesas mensais');
    } catch (error: any) {
      console.warn('Erro ao carregar despesas mensais da API:', error);
      // Retornar dados mockados como fallback
      return [
        { mesAno: "2025-01", mesNome: "Janeiro", despesas: 3250.00, quantidadeTransacoes: 3 },
        { mesAno: "2025-02", mesNome: "Fevereiro", despesas: 3700.00, quantidadeTransacoes: 3 },
        { mesAno: "2025-03", mesNome: "Março", despesas: 3440.00, quantidadeTransacoes: 3 },
        { mesAno: "2025-04", mesNome: "Abril", despesas: 3380.00, quantidadeTransacoes: 3 },
        { mesAno: "2025-05", mesNome: "Maio", despesas: 3540.00, quantidadeTransacoes: 3 },
        { mesAno: "2025-06", mesNome: "Junho", despesas: 6720.00, quantidadeTransacoes: 11 }
      ];
    }
  },

  // Obter despesas mensais por período específico
  async getMonthlyExpensesByPeriod(startDate: string, endDate: string): Promise<MonthlyExpensesResponse[]> {
    try {
      const response = await api.get(`/v1/dashboard/expenses/monthly/period?startDate=${startDate}&endDate=${endDate}`);
      if (response.data.success) {
        return response.data.data;
      }
      throw new Error(response.data.message || 'Erro ao obter despesas mensais por período');
    } catch (error: any) {
      console.warn('Erro ao carregar despesas mensais por período da API:', error);
      // Retornar dados mockados como fallback baseado no período
      return [
        { mesAno: "2025-01", mesNome: "Janeiro", despesas: 3250.00, quantidadeTransacoes: 3 },
        { mesAno: "2025-02", mesNome: "Fevereiro", despesas: 3700.00, quantidadeTransacoes: 3 },
        { mesAno: "2025-03", mesNome: "Março", despesas: 3440.00, quantidadeTransacoes: 3 }
      ];
    }
  },

  // Criar nova transação (temporariamente mockado)
  async createTransaction(transaction: Partial<Transaction>): Promise<Transaction> {
    try {
      // Como não temos o TransactionController, lançar erro
      throw new Error('API de transações não implementada');
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao criar transação');
    }
  },

  // Atualizar transação (temporariamente mockado)
  async updateTransaction(id: string, transaction: Partial<Transaction>): Promise<Transaction> {
    try {
      // Como não temos o TransactionController, lançar erro
      throw new Error('API de transações não implementada');
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao atualizar transação');
    }
  },

  // Deletar transação (temporariamente mockado)
  async deleteTransaction(id: string): Promise<void> {
    try {
      // Como não temos o TransactionController, lançar erro
      throw new Error('API de transações não implementada');
    } catch (error: any) {
      throw new Error(error.response?.data?.message || 'Erro ao deletar transação');
    }
  }
};

export default api;