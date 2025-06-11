import { useState, useEffect } from 'react';
import { dashboardAPI } from '@/lib/api';
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
  CategoryExpense,
  MonthlyEvolution
} from '@/types/dashboard';

interface DashboardData {
  // Dados principais - usando as novas interfaces do backend
  summary: DashboardSummaryResponse | null;
  transactions: Transaction[];
  recentTransactions: RecentTransaction[];
  categories: Category[];
  paymentMethods: PaymentMethod[];
  creditCards: PaymentMethod[];
  investments: Investment[];
  budgets: Budget[];
  
  // Estatísticas - usando as novas interfaces do backend
  categoryExpenses: CategoryStatsResponse[];
  monthlyEvolution: MonthlyStatsResponse[];
  monthlyExpenses: MonthlyExpensesResponse[];
  
  // Estados de loading
  loading: {
    summary: boolean;
    transactions: boolean;
    categories: boolean;
    paymentMethods: boolean;
    investments: boolean;
    budgets: boolean;
    statistics: boolean;
  };
  
  // Estados de erro
  errors: {
    summary: string | null;
    transactions: string | null;
    categories: string | null;
    paymentMethods: string | null;
    investments: string | null;
    budgets: string | null;
    statistics: string | null;
  };
}

interface DashboardActions {
  // Funções de carregamento
  loadSummary: () => Promise<void>;
  loadTransactions: () => Promise<void>;
  loadRecentTransactions: (limit?: number) => Promise<void>;
  loadCategories: () => Promise<void>;
  loadPaymentMethods: () => Promise<void>;
  loadCreditCards: () => Promise<void>;
  loadInvestments: () => Promise<void>;
  loadBudgets: (monthYear?: string) => Promise<void>;
  loadCategoryExpenses: (monthYear?: string) => Promise<void>;
  loadMonthlyEvolution: (months?: number) => Promise<void>;
  loadMonthlyExpenses: () => Promise<void>;
  loadMonthlyExpensesByPeriod: (startDate: string, endDate: string) => Promise<void>;
  
  // Função para carregar todos os dados
  loadAllData: () => Promise<void>;
  
  // Função para atualizar dados após mudanças
  refresh: () => Promise<void>;
}

export function useDashboard(): DashboardData & DashboardActions {
  const [data, setData] = useState<DashboardData>({
    summary: null,
    transactions: [],
    recentTransactions: [],
    categories: [],
    paymentMethods: [],
    creditCards: [],
    investments: [],
    budgets: [],
    categoryExpenses: [],
    monthlyEvolution: [],
    monthlyExpenses: [],
    loading: {
      summary: false,
      transactions: false,
      categories: false,
      paymentMethods: false,
      investments: false,
      budgets: false,
      statistics: false,
    },
    errors: {
      summary: null,
      transactions: null,
      categories: null,
      paymentMethods: null,
      investments: null,
      budgets: null,
      statistics: null,
    },
  });

  // Função helper para atualizar loading
  const setLoading = (key: keyof DashboardData['loading'], value: boolean) => {
    setData(prev => ({
      ...prev,
      loading: { ...prev.loading, [key]: value }
    }));
  };

  // Função helper para atualizar errors
  const setError = (key: keyof DashboardData['errors'], value: string | null) => {
    setData(prev => ({
      ...prev,
      errors: { ...prev.errors, [key]: value }
    }));
  };

  // Carregar resumo do dashboard
  const loadSummary = async () => {
    try {
      setLoading('summary', true);
      setError('summary', null);
      
      const summary = await dashboardAPI.getSummary();
      setData(prev => ({ ...prev, summary }));
    } catch (error: any) {
      console.error('Erro ao carregar resumo:', error);
      setError('summary', error.message);
    } finally {
      setLoading('summary', false);
    }
  };

  // Carregar todas as transações
  const loadTransactions = async () => {
    try {
      setLoading('transactions', true);
      setError('transactions', null);
      
      const transactions = await dashboardAPI.getTransactions();
      setData(prev => ({ ...prev, transactions }));
    } catch (error: any) {
      console.error('Erro ao carregar transações:', error);
      setError('transactions', error.message);
    } finally {
      setLoading('transactions', false);
    }
  };

  // Carregar transações recentes
  const loadRecentTransactions = async (limit: number = 10) => {
    try {
      setLoading('transactions', true);
      setError('transactions', null);
      
      const recentTransactions = await dashboardAPI.getRecentTransactions(limit);
      setData(prev => ({ ...prev, recentTransactions }));
    } catch (error: any) {
      console.error('Erro ao carregar transações recentes:', error);
      setError('transactions', error.message);
    } finally {
      setLoading('transactions', false);
    }
  };

  // Carregar categorias
  const loadCategories = async () => {
    try {
      setLoading('categories', true);
      setError('categories', null);
      
      const categories = await dashboardAPI.getCategories();
      setData(prev => ({ ...prev, categories }));
    } catch (error: any) {
      console.error('Erro ao carregar categorias:', error);
      setError('categories', error.message);
    } finally {
      setLoading('categories', false);
    }
  };

  // Carregar métodos de pagamento
  const loadPaymentMethods = async () => {
    try {
      setLoading('paymentMethods', true);
      setError('paymentMethods', null);
      
      const paymentMethods = await dashboardAPI.getPaymentMethods();
      setData(prev => ({ ...prev, paymentMethods }));
    } catch (error: any) {
      console.error('Erro ao carregar métodos de pagamento:', error);
      setError('paymentMethods', error.message);
    } finally {
      setLoading('paymentMethods', false);
    }
  };

  // Carregar cartões de crédito
  const loadCreditCards = async () => {
    try {
      setLoading('paymentMethods', true);
      setError('paymentMethods', null);
      
      const creditCards = await dashboardAPI.getCreditCards();
      setData(prev => ({ ...prev, creditCards }));
    } catch (error: any) {
      console.error('Erro ao carregar cartões de crédito:', error);
      setError('paymentMethods', error.message);
    } finally {
      setLoading('paymentMethods', false);
    }
  };

  // Carregar investimentos
  const loadInvestments = async () => {
    try {
      setLoading('investments', true);
      setError('investments', null);
      
      const investments = await dashboardAPI.getInvestments();
      setData(prev => ({ ...prev, investments }));
    } catch (error: any) {
      console.error('Erro ao carregar investimentos:', error);
      setError('investments', error.message);
    } finally {
      setLoading('investments', false);
    }
  };

  // Carregar orçamentos
  const loadBudgets = async (monthYear?: string) => {
    try {
      setLoading('budgets', true);
      setError('budgets', null);
      
      const budgets = await dashboardAPI.getBudgets(monthYear);
      setData(prev => ({ ...prev, budgets }));
    } catch (error: any) {
      console.error('Erro ao carregar orçamentos:', error);
      setError('budgets', error.message);
    } finally {
      setLoading('budgets', false);
    }
  };

  // Carregar estatísticas por categoria
  const loadCategoryExpenses = async (monthYear?: string) => {
    try {
      setLoading('statistics', true);
      setError('statistics', null);
      
      const categoryExpenses = await dashboardAPI.getCategoryExpenses(monthYear);
      setData(prev => ({ ...prev, categoryExpenses }));
    } catch (error: any) {
      console.error('Erro ao carregar estatísticas por categoria:', error);
      setError('statistics', error.message);
    } finally {
      setLoading('statistics', false);
    }
  };

  // Carregar evolução mensal
  const loadMonthlyEvolution = async (months: number = 6) => {
    try {
      setLoading('statistics', true);
      setError('statistics', null);
      
      const monthlyEvolution = await dashboardAPI.getMonthlyEvolution(months);
      setData(prev => ({ ...prev, monthlyEvolution }));
    } catch (error: any) {
      console.error('Erro ao carregar evolução mensal:', error);
      setError('statistics', error.message);
    } finally {
      setLoading('statistics', false);
    }
  };

  // Carregar despesas mensais
  const loadMonthlyExpenses = async () => {
    try {
      setLoading('statistics', true);
      setError('statistics', null);
      
      const monthlyExpenses = await dashboardAPI.getMonthlyExpenses();
      setData(prev => ({ ...prev, monthlyExpenses }));
    } catch (error: any) {
      console.error('Erro ao carregar despesas mensais:', error);
      setError('statistics', error.message);
    } finally {
      setLoading('statistics', false);
    }
  };

  // Carregar despesas mensais por período
  const loadMonthlyExpensesByPeriod = async (startDate: string, endDate: string) => {
    try {
      setLoading('statistics', true);
      setError('statistics', null);
      
      const monthlyExpenses = await dashboardAPI.getMonthlyExpensesByPeriod(startDate, endDate);
      setData(prev => ({ ...prev, monthlyExpenses }));
    } catch (error: any) {
      console.error('Erro ao carregar despesas mensais por período:', error);
      setError('statistics', error.message);
    } finally {
      setLoading('statistics', false);
    }
  };

  // Carregar todos os dados do dashboard
  const loadAllData = async () => {
    console.log('🔄 Carregando dados do dashboard via API...');
    
    try {
      await Promise.allSettled([
        loadSummary(),
        loadRecentTransactions(10),
        loadCategories(),
        loadCreditCards(),
        loadInvestments(),
        loadCategoryExpenses(),
        loadMonthlyExpenses(),
        loadBudgets()
      ]);
      
      console.log('✅ Dados do dashboard carregados com sucesso');
    } catch (error) {
      console.error('❌ Erro ao carregar dados do dashboard:', error);
    }
  };

  // Função para atualizar todos os dados
  const refresh = async () => {
    console.log('🔄 Atualizando dados do dashboard via API...');
    await loadAllData();
  };

  // Carregar dados iniciais
  useEffect(() => {
    console.log('🎯 Iniciando hook useDashboard com APIs reais');
    loadAllData();
  }, []);

  return {
    ...data,
    loadSummary,
    loadTransactions,
    loadRecentTransactions,
    loadCategories,
    loadPaymentMethods,
    loadCreditCards,
    loadInvestments,
    loadBudgets,
    loadCategoryExpenses,
    loadMonthlyEvolution,
    loadMonthlyExpenses,
    loadMonthlyExpensesByPeriod,
    loadAllData,
    refresh,
  };
} 