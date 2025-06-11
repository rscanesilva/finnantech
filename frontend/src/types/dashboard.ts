export interface Transaction {
  id: string;
  userId: string;
  categoryId: string;
  paymentMethodId?: string;
  type: 'RECEITA' | 'DESPESA';
  amount: number;
  currency: string;
  description: string;
  notes?: string;
  transactionDate: string;
  createdAt: string;
  updatedAt: string;
  merchantName?: string;
  merchantCategory?: string;
  status: 'PENDENTE' | 'CONFIRMADA' | 'CANCELADA';
  isRecurring: boolean;
  recurringGroupId?: string;
  externalId?: string;
  tagsJson?: string;
  monthYear?: string;
}

// Interface para transações recentes do dashboard (correspondente ao RecentTransactionResponse)
export interface RecentTransaction {
  id: string;
  type: 'RECEITA' | 'DESPESA';
  amount: number;
  description: string;
  transactionDate: string;
  categoryName?: string;
  categoryIcon?: string;
  categoryColor?: string;
  merchantCategory?: string;
  paymentMethodName?: string;
  paymentMethodIcon?: string;
  status: 'PENDENTE' | 'CONFIRMADA' | 'CANCELADA';
}

// Interface para resumo do dashboard (correspondente ao DashboardSummaryResponse)
export interface DashboardSummaryResponse {
  totalBalance: number;
  totalIncomes: number;
  totalExpenses: number;
  totalInvestments: number;
  currentMonthIncomes: number;
  currentMonthExpenses: number;
  currentMonthBalance: number;
  lastMonthIncomes: number;
  lastMonthExpenses: number;
  lastMonthBalance: number;
  incomesVariation: number;
  expensesVariation: number;
  balanceVariation: number;
  transactionsCount: number;
  categoriesCount: number;
  averageTransactionAmount: number;
  lastUpdateDate: string;
}

// Interface para estatísticas por categoria (correspondente ao CategoryStatsResponse)
export interface CategoryStatsResponse {
  categoryId: string;
  categoryName: string;
  categoryIcon: string;
  categoryColor: string;
  totalAmount: number;
  transactionCount: number;
  percentage: number;
  averageAmount: number;
  monthYear: string;
}

// Interface para evolução mensal (correspondente ao MonthlyStatsResponse)
export interface MonthlyStatsResponse {
  monthYear: string; // Formato: "2024-02"
  totalIncomes: number;
  totalExpenses: number;
  totalBalance: number;
  transactionCount: number;
  averageTransaction: number;
}

// Interface para despesas mensais (correspondente ao MonthlyExpensesResponse)
export interface MonthlyExpensesResponse {
  mesAno: string; // Formato: "2025-06"
  mesNome: string; // Nome do mês em português: "Janeiro", "Fevereiro", etc.
  despesas: number; // Total de despesas do mês
  quantidadeTransacoes: number; // Número de transações no mês
}

export interface Category {
  id: string;
  userId?: string;
  name: string;
  description?: string;
  color: string;
  icon: string;
  type: 'RECEITA' | 'DESPESA' | 'INVESTIMENTO';
  isSystemDefault: boolean;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface PaymentMethod {
  id: string;
  userId: string;
  type: 'CARTAO_CREDITO' | 'CARTAO_DEBITO' | 'PIX' | 'DINHEIRO' | 'TRANSFERENCIA' | 'BOLETO';
  name: string;
  description?: string;
  cardLastDigits?: string;
  cardBrand?: string;
  cardExpiry?: string;
  cardLimit?: number;
  cardClosingDay?: number;
  cardDueDay?: number;
  isDefault: boolean;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Investment {
  id: string;
  userId: string;
  name: string;
  type: 'ACAO' | 'FUNDO' | 'TESOURO' | 'CDB' | 'LCI' | 'LCA' | 'CRYPTO' | 'IMOVEL' | 'OUTRO';
  symbol?: string;
  initialAmount: number;
  currentAmount?: number;
  quantity?: number;
  averagePrice?: number;
  purchaseDate: string;
  maturityDate?: string;
  lastUpdate?: string;
  profitLoss?: number;
  profitLossPercent?: number;
  broker?: string;
  notes?: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Budget {
  id: string;
  userId: string;
  categoryId: string;
  monthYear: string;
  plannedAmount: number;
  spentAmount: number;
  remainingAmount?: number;
  alertThreshold: number;
  alertSent: boolean;
  notes?: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

// Interface para compatibilidade (mantida para não quebrar código existente)
export interface DashboardSummary {
  totalBalance: number;
  totalIncomes: number;
  totalExpenses: number;
  totalInvestments: number;
  monthlyEvolution: MonthlyEvolution[];
  categoryExpenses: CategoryExpense[];
  recentTransactions: Transaction[];
  creditCards: PaymentMethod[];
}

export interface MonthlyEvolution {
  month: string;
  incomes: number;
  expenses: number;
}

export interface CategoryExpense {
  categoryId: string;
  categoryName: string;
  categoryIcon: string;
  categoryColor: string;
  totalAmount: number;
  percentage: number;
}

export interface TransactionFilters {
  type?: 'RECEITA' | 'DESPESA';
  categoryId?: string;
  paymentMethodId?: string;
  startDate?: string;
  endDate?: string;
  status?: 'PENDENTE' | 'CONFIRMADA' | 'CANCELADA';
  limit?: number;
  offset?: number;
} 