'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/context/AuthContext';
import { useDashboard } from '@/hooks/useDashboard';
import {
  ChartLine,
  Wallet,
  CreditCard,
  PiggyBank,
  ArrowUpRight,
  ArrowDownRight,
  Bell,
  User,
  Gear,
  SignOut,
  ShoppingCart,
  House,
  Car,
  ForkKnife,
  FirstAid,
  GraduationCap,
  Gift,
  ShoppingBag,
  Question,
  CaretDown,
  CaretLeft,
  CaretRight,
  Warning,
  Check,
  MagnifyingGlass,
  DotsThree,
} from '@phosphor-icons/react';
import { 
  DashboardSummaryResponse,
  RecentTransaction,
  CategoryStatsResponse,
  MonthlyStatsResponse 
} from '@/types/dashboard';

// Dados mockados - usados como fallback para componentes que ainda não têm API
const mockCartoes = [
  {
    id: 1,
    nome: "João Silva",
    ultimosDigitos: "1234",
    bandeira: "Visa",
    vencimento: "12/28",
    limite: 5000.00,
    faturaAberta: 1250.75,
    vencimentoFatura: "2024-03-15",
    faturaVencida: false,
    melhorDiaUso: "Após dia 20",
    cor: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    bandeiraIcone: "💳"
  },
  {
    id: 2,
    nome: "João Silva",
    ultimosDigitos: "5678",
    bandeira: "Mastercard",
    vencimento: "08/27",
    limite: 8000.00,
    faturaAberta: 2890.30,
    vencimentoFatura: "2024-03-10",
    faturaVencida: true,
    melhorDiaUso: "Após dia 15",
    cor: "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)",
    bandeiraIcone: "💳"
  },
  {
    id: 3,
    nome: "João Silva",
    ultimosDigitos: "9012",
    bandeira: "American Express",
    vencimento: "05/29",
    limite: 12000.00,
    faturaAberta: 156.90,
    vencimentoFatura: "2024-03-25",
    faturaVencida: false,
    melhorDiaUso: "Qualquer dia",
    cor: "linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)",
    bandeiraIcone: "💳"
  }
];

// Função para mapear ícones de categoria
const getCategoryIcon = (iconName: string) => {
  const iconMap: Record<string, any> = {
    'ShoppingCart': ShoppingCart,
    'House': House,
    'Car': Car,
    'ForkKnife': ForkKnife,
    'FirstAid': FirstAid,
    'GraduationCap': GraduationCap,
    'Gift': Gift,
    'ShoppingBag': ShoppingBag,
    'Wallet': Wallet,
    'Money': Wallet,
    'ChartLine': ChartLine,
    'TrendingUp': ArrowUpRight,
    'ChartPie': PiggyBank,
    'Bank': House,
    'CurrencyBtc': Question,
    'Briefcase': ShoppingBag,
    'Monitor': Question,
    'Gear': Gear,
    'Laptop': Question,
    'Star': Gift,
    'Heart': Gift,
  };
  
  return iconMap[iconName] || Question;
};

// Função para converter dados do backend para formato do frontend
const processApiData = (
  summary: DashboardSummaryResponse | null,
  recentTransactions: RecentTransaction[],
  categoryExpenses: CategoryStatsResponse[],
  monthlyEvolution: MonthlyStatsResponse[]
) => {
  // Usar dados do resumo do backend ou fallback
  const processedSummary = summary ? {
    saldoTotal: summary.totalBalance,
    receitas: summary.totalIncomes,
    despesas: summary.totalExpenses,
    investimentos: summary.totalInvestments,
    receitasVariacao: summary.incomesVariation,
    despesasVariacao: summary.expensesVariation,
    saldoVariacao: summary.balanceVariation
  } : {
    saldoTotal: 15000.00,
    receitas: 25000.00,
    despesas: 10000.00,
    investimentos: 5000.00,
    receitasVariacao: 8.0,
    despesasVariacao: 5.0,
    saldoVariacao: 12.0
  };

  // Converter transações recentes para o formato esperado
  const ultimasTransacoes = recentTransactions.slice(0, 3).map((t) => ({
    id: t.id,
    descricao: t.description,
    valor: t.amount,
    tipo: t.type === 'RECEITA' ? 'receita' : 'despesa',
    data: new Date(t.transactionDate).toLocaleDateString('pt-BR'),
  }));

  // Converter categorias para o formato esperado
  const categorias = categoryExpenses.map((cat) => ({
    nome: cat.categoryName,
    valor: cat.totalAmount,
    icone: getCategoryIcon(cat.categoryIcon),
    cor: cat.categoryColor,
  }));

  const totalGasto = categorias.reduce((sum, cat) => sum + cat.valor, 0);

  // Processar evolução mensal para o gráfico
  const evolucao = monthlyEvolution && monthlyEvolution.length > 0 ? {
    meses: monthlyEvolution.map(m => {
      if (!m.monthYear) return 'N/A';
      const [year, month] = m.monthYear.split('-');
      const monthNames = ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 
                         'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'];
      return monthNames[parseInt(month) - 1] || 'N/A';
    }),
    receitas: monthlyEvolution.map(m => m.totalIncomes || 0),
    despesas: monthlyEvolution.map(m => m.totalExpenses || 0),
  } : {
    meses: [],
    receitas: [],
    despesas: [],
  };

  return {
    ...processedSummary,
    ultimasTransacoes,
    stats: {
      totalGasto,
      categorias,
    },
    evolucao
  };
};

export default function DashboardPage() {
  const router = useRouter();
  const { user, logout, loading: authLoading } = useAuth();
  const dashboardData = useDashboard();
  
  const [activeTab, setActiveTab] = useState('dashboard');

  const [categoryPeriod, setCategoryPeriod] = useState('Este mês');
  const [currentCard, setCurrentCard] = useState(0);

  // Verificar se temos dados reais das APIs
  const hasRealData = dashboardData.summary !== null || 
                      dashboardData.recentTransactions.length > 0 ||
                      dashboardData.categoryExpenses.length > 0;

  // Processar dados - usar dados reais se disponíveis, senão usar fallback
  const data = processApiData(
    dashboardData.summary,
    dashboardData.recentTransactions,
    dashboardData.categoryExpenses,
    dashboardData.monthlyEvolution
  );

  // Proteger rota - redirecionar se não estiver autenticado
  useEffect(() => {
    if (!authLoading && !user) {
      router.push('/login');
    }
  }, [user, authLoading, router]);

  const handleLogout = async () => {
    await logout();
  };

  // Mostrar loading enquanto verifica autenticação
  if (authLoading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-text-primary">Carregando...</div>
      </div>
    );
  }

  // Se não estiver autenticado, não mostrar nada (será redirecionado)
  if (!user) {
    return null;
  }

  // Função para mostrar indicador de dados mockados vs reais
  const DataSourceIndicator = () => (
    <div className="fixed bottom-4 right-4 z-50">
      <div className={`px-3 py-2 rounded-lg text-sm font-medium ${
        hasRealData 
          ? 'bg-green-100 text-green-800 border border-green-200' 
          : 'bg-yellow-100 text-yellow-800 border border-yellow-200'
      }`}>
        {hasRealData ? '✅ Dados da API' : '⚠️ Dados Mockados'}
      </div>
    </div>
  );

  // Processar cartões - usar dados reais se disponíveis (mantendo mock por enquanto)
  const cartoes = mockCartoes;

  // Processar transações recentes - usar dados reais
  const transacoes = dashboardData.recentTransactions.length > 0
    ? dashboardData.recentTransactions.slice(0, 5).map((t) => ({
        empresa: t.description,
        data: new Date(t.transactionDate).toLocaleDateString('pt-BR'),
        status: t.status === 'CONFIRMADA' ? 'Concluída' : 
                t.status === 'PENDENTE' ? 'Pendente' : 'Cancelada',
        categoria: t.categoryName || t.merchantCategory || 'Geral',
        valor: t.amount,
        tipo: t.type === 'RECEITA' ? 'receita' : 'despesa',
        cor: t.type === 'RECEITA' ? '#32d583' : '#ff3b30'
      }))
    : [
        {
          empresa: "Adobe CC",
          data: "12 Mar, 11:28",
          status: "Concluída",
          categoria: "Assinatura",
          valor: 35.00,
          tipo: "despesa",
          cor: "#ff3b30"
        },
        {
          empresa: "Walmart",
          data: "09 Mar, 09:22",
          status: "Concluída", 
          categoria: "Mercado",
          valor: 120.00,
          tipo: "despesa",
          cor: "#5392ff"
        }
      ];



  const nextCard = () => {
    setCurrentCard((prev) => (prev + 1) % cartoes.length);
  };

  const prevCard = () => {
    setCurrentCard((prev) => (prev - 1 + cartoes.length) % cartoes.length);
  };



  return (
    <div className="min-h-screen bg-background p-6">
      {/* Header Card com Menu */}
      <header className="card mb-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center">
            <h1 className="text-xl font-bold text-text-primary">Finnantech V2</h1>
          </div>
          
          <nav className="flex gap-8">
            <button
              onClick={() => setActiveTab('dashboard')}
              className={`nav-link ${
                activeTab === 'dashboard' ? 'text-text-primary' : 'text-text-secondary hover:text-text-primary'
              }`}
            >
              Dashboard
            </button>
            <button
              onClick={() => setActiveTab('transacoes')}
              className={`nav-link ${
                activeTab === 'transacoes' ? 'text-text-primary' : 'text-text-secondary hover:text-text-primary'
              }`}
            >
              Transações
            </button>
            <button
              onClick={() => setActiveTab('investimentos')}
              className={`nav-link ${
                activeTab === 'investimentos' ? 'text-text-primary' : 'text-text-secondary hover:text-text-primary'
              }`}
            >
              Investimentos
            </button>
            <button
              onClick={() => setActiveTab('relatorios')}
              className={`nav-link ${
                activeTab === 'relatorios' ? 'text-text-primary' : 'text-text-secondary hover:text-text-primary'
              }`}
            >
              Relatórios
            </button>
          </nav>
          <div className="flex items-center gap-6">
            <div className="flex gap-4 pr-6 border-r border-border">
              <button className="text-text-secondary hover:text-text-primary">
                <Bell className="h-5 w-5" />
              </button>
              <button className="text-text-secondary hover:text-text-primary">
                <Question className="h-5 w-5" />
              </button>
            </div>
            <div className="flex items-center gap-3">
              <img
                src={`https://ui-avatars.com/api/?name=${encodeURIComponent(user?.name || 'Usuário')}&background=5392ff&color=fff`}
                alt="Avatar"
                className="w-10 h-10 rounded-full object-cover"
              />
              <div>
                <div className="text-xs text-text-secondary">Conta Principal</div>
                <div className="text-text-primary font-semibold">{user?.name || 'Usuário'}</div>
              </div>
              <button
                onClick={handleLogout}
                disabled={authLoading}
                className="ml-3 p-2 rounded-lg text-text-secondary hover:text-error hover:bg-error/10 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                title="Fazer logout"
              >
                <SignOut className="h-5 w-5" />
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="w-full">
        {/* Overview Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-text-secondary">Saldo Total</p>
                <h3 className="text-2xl font-bold text-text-primary">R$ {data.saldoTotal.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</h3>
              </div>
              <div className="p-3 rounded-lg bg-accent-blue/10">
                <Wallet className="h-6 w-6 text-accent-blue" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <span className="text-success flex items-center">
                <ArrowUpRight className="h-4 w-4 mr-1" />
                {data.saldoVariacao?.toFixed(1) || '12'}%
              </span>
              <span className="text-text-secondary ml-2">vs. mês anterior</span>
            </div>
          </div>

          <div className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-text-secondary">Receitas</p>
                <h3 className="text-2xl font-bold text-text-primary">R$ {data.receitas.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</h3>
              </div>
              <div className="p-3 rounded-lg bg-success/10">
                <ArrowUpRight className="h-6 w-6 text-success" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <span className="text-success flex items-center">
                <ArrowUpRight className="h-4 w-4 mr-1" />
                {data.receitasVariacao?.toFixed(1) || '8'}%
              </span>
              <span className="text-text-secondary ml-2">vs. mês anterior</span>
            </div>
          </div>

          <div className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-text-secondary">Despesas</p>
                <h3 className="text-2xl font-bold text-text-primary">R$ {data.despesas.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</h3>
              </div>
              <div className="p-3 rounded-lg bg-error/10">
                <ArrowDownRight className="h-6 w-6 text-error" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <span className="text-error flex items-center">
                <ArrowDownRight className="h-4 w-4 mr-1" />
                {data.despesasVariacao?.toFixed(1) || '5'}%
              </span>
              <span className="text-text-secondary ml-2">vs. mês anterior</span>
            </div>
          </div>

          <div className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-text-secondary">Investimentos</p>
                <h3 className="text-2xl font-bold text-text-primary">R$ {data.investimentos.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</h3>
              </div>
              <div className="p-3 rounded-lg bg-accent-purple/10">
                <PiggyBank className="h-6 w-6 text-accent-purple" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <span className="text-success flex items-center">
                <ArrowUpRight className="h-4 w-4 mr-1" />
                15%
              </span>
              <span className="text-text-secondary ml-2">vs. mês anterior</span>
            </div>
          </div>
        </div>

        {/* Cards de Cartões de Crédito e Evolução Financeira */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          {/* Card de Cartões de Crédito */}
          <div className="card">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-lg font-semibold text-text-primary">Meus Cartões de Crédito</h3>
              <div className="flex items-center gap-2">
                <button 
                  onClick={prevCard}
                  className="p-2 rounded-full hover:bg-background text-text-secondary hover:text-text-primary transition-all duration-200 hover:scale-110 active:scale-95"
                >
                  <CaretLeft className="h-5 w-5" />
                </button>
                <span className="text-sm text-text-secondary px-3 font-medium">
                  {currentCard + 1} de {cartoes.length}
                </span>
                <button 
                  onClick={nextCard}
                  className="p-2 rounded-full hover:bg-background text-text-secondary hover:text-text-primary transition-all duration-200 hover:scale-110 active:scale-95"
                >
                  <CaretRight className="h-5 w-5" />
                </button>
              </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              {/* Cartão Visual */}
              <div className="relative max-w-[980px]">
                <div className="relative w-full overflow-hidden rounded-xl" style={{ aspectRatio: '1/0.55', minHeight: '200px' }}>
                  {cartoes.map((cartao, index) => {
                    const isActive = index === currentCard;
                    const offset = (index - currentCard) * 115; // Mais espaçamento
                    const scale = isActive ? 1 : 0.92;
                    const opacity = isActive ? 1 : Math.max(0.2, 1 - Math.abs(index - currentCard) * 0.5);
                    const blur = isActive ? 0 : Math.min(3, Math.abs(index - currentCard) * 1.5);
                    
                    return (
                      <div
                        key={cartao.id}
                        className="absolute inset-0 transition-all duration-700 ease-in-out"
                        style={{ 
                          transform: `translateX(${offset}%) scale(${scale})`,
                          opacity: opacity,
                          filter: `blur(${blur}px)`,
                          zIndex: isActive ? 10 : 5 - Math.abs(index - currentCard)
                        }}
                      >
                        {/* Design do Cartão */}
                        <div 
                          className="w-full h-full rounded-xl p-4 sm:p-6 text-white relative overflow-hidden shadow-2xl"
                          style={{ 
                            background: cartao.cor,
                            minHeight: '200px'
                          }}
                        >
                          {/* Padrões decorativos */}
                          <div className="absolute top-0 right-0 w-16 h-16 sm:w-20 sm:h-20 rounded-full bg-white opacity-10 -mr-8 sm:-mr-10 -mt-8 sm:-mt-10"></div>
                          <div className="absolute bottom-0 left-0 w-12 h-12 sm:w-16 sm:h-16 rounded-full bg-white opacity-5 -ml-6 sm:-ml-8 -mb-6 sm:-mb-8"></div>
                          
                          {/* Header do cartão */}
                          <div className="flex justify-between items-start mb-4 sm:mb-6">
                            <div className="text-sm sm:text-base opacity-90 font-medium">
                              {cartao.bandeira}
                            </div>
                            <div className="text-lg sm:text-xl">
                              {cartao.bandeiraIcone}
                            </div>
                          </div>

                          {/* Número do cartão */}
                          <div className="mb-3 sm:mb-4">
                            <div className="text-base sm:text-lg lg:text-xl font-mono tracking-widest">
                              •••• •••• •••• {cartao.ultimosDigitos}
                            </div>
                          </div>

                          {/* Footer do cartão */}
                          <div className="flex justify-between items-end">
                            <div>
                              <div className="text-xs opacity-70 uppercase tracking-wide mb-1">
                                Portador
                              </div>
                              <div className="text-sm sm:text-base font-medium">
                                {cartao.nome}
                              </div>
                            </div>
                            <div>
                              <div className="text-xs opacity-70 uppercase tracking-wide mb-1">
                                Válido até
                              </div>
                              <div className="text-sm sm:text-base font-medium font-mono">
                                {cartao.vencimento}
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>

              {/* Informações do cartão */}
              <div className="space-y-4">
                {cartoes[currentCard] && (
                  <>
                    {/* Status da fatura */}
                    <div className="p-4 bg-background rounded-lg">
                      <div className="flex items-center justify-between mb-3">
                        <div className="flex items-center gap-3">
                          {cartoes[currentCard].faturaVencida ? (
                            <Warning className="h-5 w-5 text-error" />
                          ) : (
                            <Check className="h-5 w-5 text-success" />
                          )}
                          <span className="text-base text-text-primary font-medium">
                            {cartoes[currentCard].faturaVencida ? 'Fatura Vencida' : 'Fatura em Dia'}
                          </span>
                        </div>
                      </div>
                      <div className={`text-2xl font-bold ${
                        cartoes[currentCard].faturaVencida ? 'text-error' : 'text-success'
                      }`}>
                        R$ {cartoes[currentCard].faturaAberta.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                      </div>
                    </div>

                    {/* Detalhes */}
                    <div className="grid grid-cols-2 gap-4">
                      <div className="p-3 bg-background rounded-lg">
                        <span className="text-text-secondary text-sm">Vencimento da Fatura</span>
                        <div className="text-text-primary font-medium">
                          {new Date(cartoes[currentCard].vencimentoFatura).toLocaleDateString('pt-BR')}
                        </div>
                      </div>
                      <div className="p-3 bg-background rounded-lg">
                        <span className="text-text-secondary text-sm">Limite Disponível</span>
                        <div className="text-text-primary font-medium">
                          R$ {(cartoes[currentCard].limite - cartoes[currentCard].faturaAberta).toLocaleString('pt-BR')}
                        </div>
                      </div>
                    </div>

                    <div className="p-3 bg-background rounded-lg">
                      <span className="text-text-secondary text-sm">Melhor Data para Usar</span>
                      <div className="text-text-primary font-medium">
                        {cartoes[currentCard].melhorDiaUso}
                      </div>
                    </div>

                    {/* Barra de utilização do limite */}
                    <div className="p-4 bg-background rounded-lg">
                      <div className="flex justify-between text-sm text-text-secondary mb-2">
                        <span>Limite Utilizado</span>
                        <span>
                          {((cartoes[currentCard].faturaAberta / cartoes[currentCard].limite) * 100).toFixed(1)}%
                        </span>
                      </div>
                      <div className="h-3 bg-card rounded-full overflow-hidden">
                        <div 
                          className="h-full transition-all duration-1000 ease-out rounded-full"
                          style={{ 
                            width: `${(cartoes[currentCard].faturaAberta / cartoes[currentCard].limite) * 100}%`,
                            backgroundColor: (cartoes[currentCard].faturaAberta / cartoes[currentCard].limite) > 0.8 ? '#ff3b30' : 
                                           (cartoes[currentCard].faturaAberta / cartoes[currentCard].limite) > 0.6 ? '#ff9500' : '#32d583'
                          }}
                        />
                      </div>
                      <div className="flex justify-between text-xs text-text-secondary mt-1">
                        <span>R$ 0</span>
                        <span>R$ {cartoes[currentCard].limite.toLocaleString('pt-BR')}</span>
                      </div>
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>

          {/* Card: Despesas Mensais */}
          <div className="card">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold text-text-primary">Despesas Mensais</h3>
              <button className="bg-card-secondary border border-border px-3 py-1 rounded-lg text-sm text-text-secondary hover:text-text-primary flex items-center gap-2">
                Últimos 6 meses <CaretDown className="h-4 w-4" />
              </button>
            </div>
            <div className="flex-1 flex items-end justify-center gap-6 h-64 px-4">
              {/* Gráfico de barras usando dados de despesas mensais */}
              {dashboardData.monthlyExpenses.length > 0 
                ? dashboardData.monthlyExpenses.map((monthData, index) => {
                    const maxExpense = Math.max(...dashboardData.monthlyExpenses.map(m => m.despesas));
                    const barHeight = (monthData.despesas / maxExpense) * 180; // altura máxima de 180px
                    
                    // Cores dinâmicas baseadas no valor
                    const getBarColor = (value: number, maxValue: number) => {
                      const percentage = (value / maxValue) * 100;
                      if (percentage > 80) return 'from-red-500 to-red-600';
                      if (percentage > 60) return 'from-orange-500 to-orange-600';
                      if (percentage > 40) return 'from-yellow-500 to-yellow-600';
                      return 'from-blue-500 to-blue-600';
                    };
                    
                    return (
                      <div key={monthData.mesAno} className="flex flex-col items-center gap-3">
                        {/* Valor da despesa */}
                        <div className="text-sm font-bold text-text-primary mb-1">
                          R$ {(monthData.despesas / 1000).toFixed(1)}k
                        </div>
                        
                        {/* Barra */}
                        <div 
                          className={`w-16 bg-gradient-to-t ${getBarColor(monthData.despesas, maxExpense)} rounded-t-lg transition-all duration-1000 ease-out hover:scale-105 hover:shadow-lg cursor-pointer animate-grow-up`}
                          style={{ 
                            height: `${Math.max(barHeight, 20)}px`, // altura mínima de 20px
                            animationDelay: `${index * 0.15}s`
                          }}
                          title={`${monthData.mesNome}: R$ ${monthData.despesas.toLocaleString('pt-BR', { minimumFractionDigits: 2 })} (${monthData.quantidadeTransacoes} transações)`}
                        />
                        
                        {/* Label do mês */}
                        <div className="text-sm font-medium text-text-secondary text-center">
                          {monthData.mesNome.substring(0, 3)}
                        </div>
                      </div>
                    );
                  })
                : // Fallback com dados mockados se não houver dados da API
                  [
                    { mesAno: "2025-01", mesNome: "Janeiro", despesas: 3250.00, quantidadeTransacoes: 3 },
                    { mesAno: "2025-02", mesNome: "Fevereiro", despesas: 3700.00, quantidadeTransacoes: 3 },
                    { mesAno: "2025-03", mesNome: "Março", despesas: 3440.00, quantidadeTransacoes: 3 },
                    { mesAno: "2025-04", mesNome: "Abril", despesas: 3380.00, quantidadeTransacoes: 3 },
                    { mesAno: "2025-05", mesNome: "Maio", despesas: 3540.00, quantidadeTransacoes: 3 },
                    { mesAno: "2025-06", mesNome: "Junho", despesas: 6720.00, quantidadeTransacoes: 11 }
                  ].map((monthData, index) => {
                    const maxExpense = 6720.00; // valor máximo do mock
                    const barHeight = (monthData.despesas / maxExpense) * 180;
                    
                    // Cores dinâmicas baseadas no valor
                    const getBarColor = (value: number, maxValue: number) => {
                      const percentage = (value / maxValue) * 100;
                      if (percentage > 80) return 'from-red-500 to-red-600';
                      if (percentage > 60) return 'from-orange-500 to-orange-600';
                      if (percentage > 40) return 'from-yellow-500 to-yellow-600';
                      return 'from-blue-500 to-blue-600';
                    };
                    
                    return (
                      <div key={monthData.mesAno} className="flex flex-col items-center gap-3">
                        <div className="text-sm font-bold text-text-primary mb-1">
                          R$ {(monthData.despesas / 1000).toFixed(1)}k
                        </div>
                        <div 
                          className={`w-16 bg-gradient-to-t ${getBarColor(monthData.despesas, maxExpense)} rounded-t-lg transition-all duration-1000 ease-out hover:scale-105 hover:shadow-lg cursor-pointer animate-grow-up`}
                          style={{ 
                            height: `${Math.max(barHeight, 20)}px`,
                            animationDelay: `${index * 0.15}s`
                          }}
                          title={`${monthData.mesNome}: R$ ${monthData.despesas.toLocaleString('pt-BR', { minimumFractionDigits: 2 })} (${monthData.quantidadeTransacoes} transações)`}
                        />
                        <div className="text-sm font-medium text-text-secondary text-center">
                          {monthData.mesNome.substring(0, 3)}
                        </div>
                      </div>
                    );
                  })
              }
            </div>
            
            {/* Informações adicionais */}
            <div className="mt-4 pt-4 border-t border-border">
              <div className="flex justify-between items-center text-sm mb-3">
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 rounded bg-gradient-to-r from-blue-500 to-red-600"></div>
                  <span className="text-text-secondary">Despesas Mensais</span>
                </div>
                <div className="text-text-secondary">
                  Total de {dashboardData.monthlyExpenses.length > 0 
                    ? dashboardData.monthlyExpenses.reduce((sum, m) => sum + m.quantidadeTransacoes, 0)
                    : 27} transações
                </div>
              </div>
              
              {/* Legenda das cores */}
              <div className="grid grid-cols-2 gap-2 text-xs mb-3">
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 rounded bg-gradient-to-t from-blue-500 to-blue-600"></div>
                  <span className="text-text-secondary">Baixo (0-40%)</span>
                </div>
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 rounded bg-gradient-to-t from-yellow-500 to-yellow-600"></div>
                  <span className="text-text-secondary">Médio (40-60%)</span>
                </div>
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 rounded bg-gradient-to-t from-orange-500 to-orange-600"></div>
                  <span className="text-text-secondary">Alto (60-80%)</span>
                </div>
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 rounded bg-gradient-to-t from-red-500 to-red-600"></div>
                  <span className="text-text-secondary">Muito Alto (+80%)</span>
                </div>
              </div>
              
              <div className="text-xs text-text-secondary">
                Clique nas barras para ver detalhes • Maior gasto: {dashboardData.monthlyExpenses.length > 0 
                  ? dashboardData.monthlyExpenses.find(m => m.despesas === Math.max(...dashboardData.monthlyExpenses.map(d => d.despesas)))?.mesNome || 'Junho'
                  : 'Junho'}
              </div>
            </div>
          </div>
        </div>

        {/* Transações e Estatísticas */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Card: Resumo de Gastos */}
          <div className="card">
            <h3 className="text-lg font-semibold text-text-primary mb-4 animate-fade-in">Resumo de Gastos</h3>
            <div className="space-y-6">
              {/* Total Gasto com animação */}
              <div className="animate-fade-in" style={{ animationDelay: '0.2s' }}>
                <div className="flex justify-between items-center mb-2">
                  <span className="text-text-secondary">Total Gasto</span>
                  <span className="text-text-primary font-medium animate-counter" data-target={data.stats.totalGasto}>
                    R$ {data.stats.totalGasto.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                  </span>
                </div>
                <div className="h-3 bg-background rounded-full overflow-hidden shadow-inner">
                  <div className="h-full bg-gradient-to-r from-accent-blue to-blue-600 animate-fill-right shadow-sm" style={{ animationDelay: '0.5s' }} />
                </div>
              </div>

              {/* Top Categorias com animação sequencial */}
              <div className="space-y-4">
                <h4 className="text-sm font-medium text-text-secondary animate-fade-in" style={{ animationDelay: '0.7s' }}>Top Categorias</h4>
                {data.stats.categorias.slice(0, 3).map((categoria: any, index: number) => {
                  const porcentagem = (categoria.valor / data.stats.totalGasto) * 100;
                  return (
                    <div 
                      key={index} 
                      className="flex justify-between items-center animate-slide-in-left"
                      style={{ animationDelay: `${0.9 + index * 0.2}s` }}
                    >
                      <div className="flex items-center space-x-3">
                        <div className="animate-bounce-soft" style={{ animationDelay: `${1.2 + index * 0.2}s` }}>
                          <categoria.icone className="h-5 w-5 transition-all duration-300 hover:scale-110 category-icon-glow" style={{ color: categoria.cor }} />
                        </div>
                        <span className="text-text-primary text-sm font-medium">{categoria.nome}</span>
                      </div>
                      <div className="text-right">
                        <div className="text-text-primary font-bold text-sm">
                          R$ {categoria.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                        </div>
                        <div className="text-text-secondary text-xs bg-background px-2 py-1 rounded-full">
                          {porcentagem.toFixed(1)}%
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>

              {/* Distribuição com barras animadas */}
              <div className="pt-4 border-t border-border">
                <h4 className="text-sm font-medium text-text-secondary mb-4 animate-fade-in" style={{ animationDelay: '1.5s' }}>Distribuição</h4>
                <div className="space-y-3">
                  {data.stats.categorias.map((categoria: any, index: number) => {
                    const porcentagem = (categoria.valor / data.stats.totalGasto) * 100;
                    return (
                      <div 
                        key={index} 
                        className="flex items-center space-x-2 animate-fade-in"
                        style={{ animationDelay: `${1.7 + index * 0.1}s` }}
                      >
                        <div className="animate-bounce-soft" style={{ animationDelay: `${1.8 + index * 0.1}s` }}>
                          <categoria.icone className="h-4 w-4 transition-all duration-300 hover:scale-110 category-icon-glow" style={{ color: categoria.cor }} />
                        </div>
                        <span className="text-xs text-text-secondary w-20 font-medium">{categoria.nome}</span>
                        <div className="flex-1 bg-background rounded-full h-3 overflow-hidden shadow-inner">
                          <div
                            className="h-full rounded-full transition-all duration-300 hover:brightness-110 cursor-pointer animate-fill-bar"
                            style={{ 
                              animationDelay: `${2.0 + index * 0.1}s`,
                              backgroundColor: categoria.cor,
                              '--target-width': `${porcentagem}%`
                            } as any}
                            title={`${categoria.nome}: ${porcentagem.toFixed(1)}%`}
                          />
                        </div>
                        <span className="text-xs text-text-secondary w-10 text-right font-mono">
                          {porcentagem.toFixed(0)}%
                        </span>
                      </div>
                    );
                  })}
                </div>
              </div>
              
              {/* Estatística adicional */}
              <div className="animate-fade-in bg-gradient-to-r from-background to-card p-3 rounded-lg border border-border/50" style={{ animationDelay: `${2.2 + data.stats.categorias.length * 0.1}s` }}>
                <div className="flex justify-between items-center">
                  <span className="text-text-secondary text-sm">Média por categoria</span>
                  <span className="text-text-primary font-bold">
                    R$ {(data.stats.totalGasto / data.stats.categorias.length).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Card: Transações Recentes */}
          <div className="card lg:col-span-2">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-lg font-semibold text-text-primary">Transações</h3>
              <div className="flex items-center gap-2 text-text-secondary">
                <MagnifyingGlass className="h-4 w-4" />
                <span className="text-sm">Ver Todas</span>
              </div>
            </div>
            
            <div className="space-y-4">
              {transacoes.map((transacao, index) => (
                <div key={index} className="grid grid-cols-7 items-center gap-4 py-3 border-b border-border last:border-b-0">
                  {/* Ícone */}
                  <div className="w-10 h-10 rounded-full bg-card flex items-center justify-center">
                    <div className="w-6 h-6 rounded-full" style={{ backgroundColor: transacao.cor }}>
                      <span className="text-white text-xs flex items-center justify-center h-full font-bold">
                        {transacao.empresa.charAt(0)}
                      </span>
                    </div>
                  </div>
                  
                  {/* Nome */}
                  <div className="font-medium text-text-primary">
                    {transacao.empresa}
                  </div>
                  
                  {/* Data */}
                  <div className="text-sm text-text-secondary">
                    {transacao.data}
                  </div>
                  
                  {/* Status */}
                  <div className="flex items-center gap-2">
                    <div className={`w-2 h-2 rounded-full ${
                      transacao.status === 'Concluída' ? 'bg-success' : 'bg-text-secondary'
                    }`}></div>
                    <span className="text-sm text-text-secondary">{transacao.status}</span>
                  </div>
                  
                  {/* Categoria */}
                  <div className="text-sm text-text-secondary">
                    {transacao.categoria}
                  </div>
                  
                  {/* Valor */}
                  <div className={`font-medium text-right ${
                    transacao.tipo === 'receita' ? 'text-success' : 'text-text-primary'
                  }`}>
                    {transacao.tipo === 'receita' ? '+' : '-'} R$ {transacao.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                  </div>
                  
                  {/* Ações */}
                  <div className="text-right">
                    <DotsThree className="h-5 w-5 text-text-secondary cursor-pointer hover:text-text-primary" />
                  </div>
                </div>
              ))}
            </div>
          </div>


        </div>
      </main>

      <DataSourceIndicator />
    </div>
  );
} 