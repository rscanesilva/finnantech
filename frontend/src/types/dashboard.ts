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