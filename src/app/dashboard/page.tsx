'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
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

// Dados mockados
const mockData = {
  saldoTotal: 15000.00,
  receitas: 25000.00,
  despesas: 10000.00,
  investimentos: 5000.00,
  ultimasTransacoes: [
    { id: 1, descricao: 'Salário', valor: 5000.00, tipo: 'receita', data: '2024-02-15' },
    { id: 2, descricao: 'Aluguel', valor: 1500.00, tipo: 'despesa', data: '2024-02-10' },
    { id: 3, descricao: 'Investimento', valor: 2000.00, tipo: 'investimento', data: '2024-02-05' },
  ],
};

// Dados mockados para as estatísticas
const mockStats = {
  totalGasto: 3250.75,
  categorias: [
    { nome: 'Mercado', valor: 850.50, icone: ShoppingCart, cor: '#5392ff' }, // Azul vibrante
    { nome: 'Moradia', valor: 1200.00, icone: House, cor: '#8b5cf6' }, // Roxo vibrante
    { nome: 'Transporte', valor: 450.25, icone: Car, cor: '#32d583' }, // Verde vibrante
    { nome: 'Alimentação', valor: 350.00, icone: ForkKnife, cor: '#f178b6' }, // Rosa vibrante
    { nome: 'Saúde', valor: 200.00, icone: FirstAid, cor: '#ff9500' }, // Laranja vibrante
    { nome: 'Educação', valor: 100.00, icone: GraduationCap, cor: '#ffcc00' }, // Amarelo vibrante
    { nome: 'Lazer', valor: 150.00, icone: Gift, cor: '#ff3b30' }, // Vermelho vibrante
    { nome: 'Outros', valor: 50.00, icone: ShoppingBag, cor: '#8e8e93' }, // Cinza
  ]
};

// Dados mockados para evolução financeira (últimos 6 meses)
const mockEvolution = {
  meses: ['Set', 'Out', 'Nov', 'Dez', 'Jan', 'Fev'],
  receitas: [7200, 8500, 7800, 9200, 8800, 8500],
  despesas: [5400, 6200, 5800, 6800, 6400, 6200],
};

// Dados mockados para cartões de crédito
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

// Dados mockados para transações recentes
const mockTransacoes = [
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
  },
  {
    empresa: "Adidas",
    data: "02 Mar, 10:32",
    status: "Concluída",
    categoria: "Shopping",
    valor: 890.00,
    tipo: "despesa",
    cor: "#32d583"
  },
  {
    empresa: "Uber",
    data: "01 Mar, 08:24",
    status: "Cancelada",
    categoria: "Transporte", 
    valor: 48.00,
    tipo: "despesa",
    cor: "#8e8e93"
  },
  {
    empresa: "Salário",
    data: "01 Mar, 00:00",
    status: "Concluída",
    categoria: "Receita",
    valor: 5000.00,
    tipo: "receita",
    cor: "#32d583"
  }
];

export default function DashboardPage() {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState('dashboard');
  const [data] = useState(mockData);
  const [evolutionPeriod, setEvolutionPeriod] = useState('Últimos 6 meses');
  const [categoryPeriod, setCategoryPeriod] = useState('Este mês');
  const [currentCard, setCurrentCard] = useState(0);

  const handleLogout = () => {
    // TODO: Implementar logout
    router.push('/login');
  };

  const nextCard = () => {
    setCurrentCard((prev) => (prev + 1) % mockCartoes.length);
  };

  const prevCard = () => {
    setCurrentCard((prev) => (prev - 1 + mockCartoes.length) % mockCartoes.length);
  };

  // Função para gerar pontos SVG das curvas
  const generateSVGPath = (data: number[], maxValue: number) => {
    const points = data.map((value, index) => {
      const x = (index / (data.length - 1)) * 500;
      const y = 150 - (value / maxValue) * 100;
      return `${x},${y}`;
    });
    
    return `M${points[0]} ${points.slice(1).map((point, index) => {
      const prevPoint = points[index];
      const [x, y] = point.split(',');
      const [prevX, prevY] = prevPoint.split(',');
      const cpX1 = Number(prevX) + 40;
      const cpX2 = Number(x) - 40;
      return `C${cpX1},${prevY} ${cpX2},${y} ${x},${y}`;
    }).join(' ')}`;
  };

  const maxEvolutionValue = Math.max(...mockEvolution.receitas, ...mockEvolution.despesas);

  return (
    <div className="min-h-screen bg-background p-6">
      {/* Header Card com Menu */}
      <header className="card mb-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-8">
            <h1 className="text-xl font-bold text-text-primary">Finnantech V2</h1>
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
          </div>
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
                src="https://ui-avatars.com/api/?name=João Silva&background=5392ff&color=fff"
                alt="Avatar"
                className="w-10 h-10 rounded-full object-cover"
              />
              <div>
                <div className="text-xs text-text-secondary">Conta Principal</div>
                <div className="text-text-primary font-semibold">João Silva</div>
              </div>
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
                <h3 className="text-2xl font-bold text-text-primary">R$ 25.000,00</h3>
              </div>
              <div className="p-3 rounded-lg bg-accent-blue/10">
                <Wallet className="h-6 w-6 text-accent-blue" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <span className="text-success flex items-center">
                <ArrowUpRight className="h-4 w-4 mr-1" />
                12%
              </span>
              <span className="text-text-secondary ml-2">vs. mês anterior</span>
            </div>
          </div>

          <div className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-text-secondary">Receitas</p>
                <h3 className="text-2xl font-bold text-text-primary">R$ 8.500,00</h3>
              </div>
              <div className="p-3 rounded-lg bg-success/10">
                <ArrowUpRight className="h-6 w-6 text-success" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <span className="text-success flex items-center">
                <ArrowUpRight className="h-4 w-4 mr-1" />
                8%
              </span>
              <span className="text-text-secondary ml-2">vs. mês anterior</span>
            </div>
          </div>

          <div className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-text-secondary">Despesas</p>
                <h3 className="text-2xl font-bold text-text-primary">R$ 3.200,00</h3>
              </div>
              <div className="p-3 rounded-lg bg-error/10">
                <ArrowDownRight className="h-6 w-6 text-error" />
              </div>
            </div>
            <div className="mt-4 flex items-center text-sm">
              <span className="text-error flex items-center">
                <ArrowDownRight className="h-4 w-4 mr-1" />
                5%
              </span>
              <span className="text-text-secondary ml-2">vs. mês anterior</span>
            </div>
          </div>

          <div className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-text-secondary">Investimentos</p>
                <h3 className="text-2xl font-bold text-text-primary">R$ 15.000,00</h3>
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
                  {currentCard + 1} de {mockCartoes.length}
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
                  {mockCartoes.map((cartao, index) => {
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
                {mockCartoes[currentCard] && (
                  <>
                    {/* Status da fatura */}
                    <div className="p-4 bg-background rounded-lg">
                      <div className="flex items-center justify-between mb-3">
                        <div className="flex items-center gap-3">
                          {mockCartoes[currentCard].faturaVencida ? (
                            <Warning className="h-5 w-5 text-error" />
                          ) : (
                            <Check className="h-5 w-5 text-success" />
                          )}
                          <span className="text-base text-text-primary font-medium">
                            {mockCartoes[currentCard].faturaVencida ? 'Fatura Vencida' : 'Fatura em Dia'}
                          </span>
                        </div>
                      </div>
                      <div className={`text-2xl font-bold ${
                        mockCartoes[currentCard].faturaVencida ? 'text-error' : 'text-success'
                      }`}>
                        R$ {mockCartoes[currentCard].faturaAberta.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                      </div>
                    </div>

                    {/* Detalhes */}
                    <div className="grid grid-cols-2 gap-4">
                      <div className="p-3 bg-background rounded-lg">
                        <span className="text-text-secondary text-sm">Vencimento da Fatura</span>
                        <div className="text-text-primary font-medium">
                          {new Date(mockCartoes[currentCard].vencimentoFatura).toLocaleDateString('pt-BR')}
                        </div>
                      </div>
                      <div className="p-3 bg-background rounded-lg">
                        <span className="text-text-secondary text-sm">Limite Disponível</span>
                        <div className="text-text-primary font-medium">
                          R$ {(mockCartoes[currentCard].limite - mockCartoes[currentCard].faturaAberta).toLocaleString('pt-BR')}
                        </div>
                      </div>
                    </div>

                    <div className="p-3 bg-background rounded-lg">
                      <span className="text-text-secondary text-sm">Melhor Data para Usar</span>
                      <div className="text-text-primary font-medium">
                        {mockCartoes[currentCard].melhorDiaUso}
                      </div>
                    </div>

                    {/* Barra de utilização do limite */}
                    <div className="p-4 bg-background rounded-lg">
                      <div className="flex justify-between text-sm text-text-secondary mb-2">
                        <span>Limite Utilizado</span>
                        <span>
                          {((mockCartoes[currentCard].faturaAberta / mockCartoes[currentCard].limite) * 100).toFixed(1)}%
                        </span>
                      </div>
                      <div className="h-3 bg-card rounded-full overflow-hidden">
                        <div 
                          className="h-full transition-all duration-1000 ease-out rounded-full"
                          style={{ 
                            width: `${(mockCartoes[currentCard].faturaAberta / mockCartoes[currentCard].limite) * 100}%`,
                            backgroundColor: (mockCartoes[currentCard].faturaAberta / mockCartoes[currentCard].limite) > 0.8 ? '#ff3b30' : 
                                           (mockCartoes[currentCard].faturaAberta / mockCartoes[currentCard].limite) > 0.6 ? '#ff9500' : '#32d583'
                          }}
                        />
                      </div>
                      <div className="flex justify-between text-xs text-text-secondary mt-1">
                        <span>R$ 0</span>
                        <span>R$ {mockCartoes[currentCard].limite.toLocaleString('pt-BR')}</span>
                      </div>
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>

          {/* Card: Evolução Financeira */}
          <div className="card">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold text-text-primary">Evolução Financeira</h3>
              <button className="bg-card-secondary border border-border px-3 py-1 rounded-lg text-sm text-text-secondary hover:text-text-primary flex items-center gap-2">
                {evolutionPeriod} <CaretDown className="h-4 w-4" />
              </button>
            </div>
            <div className="flex-1 flex items-center justify-center">
              <svg width="100%" height="150" viewBox="0 0 500 150" className="overflow-visible">
                {/* Grid lines */}
                <defs>
                  <pattern id="grid" width="50" height="25" patternUnits="userSpaceOnUse">
                    <path d="M 50 0 L 0 0 0 25" fill="none" stroke="rgb(51 51 54)" strokeWidth="0.5" opacity="0.3"/>
                  </pattern>
                </defs>
                <rect width="500" height="150" fill="url(#grid)" />
                
                {/* Linha de Receitas (Verde) */}
                <path
                  d={generateSVGPath(mockEvolution.receitas, maxEvolutionValue)}
                  stroke="#32d583"
                  fill="none"
                  strokeWidth="3"
                  className="animate-draw-line"
                />
                
                {/* Linha de Despesas (Rosa) */}
                <path
                  d={generateSVGPath(mockEvolution.despesas, maxEvolutionValue)}
                  stroke="#f178b6"
                  fill="none"
                  strokeWidth="3"
                  className="animate-draw-line"
                  style={{ animationDelay: '0.5s' }}
                />
                
                {/* Pontos das Receitas */}
                {mockEvolution.receitas.map((value, index) => {
                  const x = (index / (mockEvolution.receitas.length - 1)) * 500;
                  const y = 150 - (value / maxEvolutionValue) * 100;
                  return (
                    <circle
                      key={`receita-${index}`}
                      cx={x}
                      cy={y}
                      r="4"
                      fill="#32d583"
                      className="animate-fade-in"
                      style={{ animationDelay: `${1 + index * 0.1}s` }}
                    />
                  );
                })}
                
                {/* Pontos das Despesas */}
                {mockEvolution.despesas.map((value, index) => {
                  const x = (index / (mockEvolution.despesas.length - 1)) * 500;
                  const y = 150 - (value / maxEvolutionValue) * 100;
                  return (
                    <circle
                      key={`despesa-${index}`}
                      cx={x}
                      cy={y}
                      r="4"
                      fill="#f178b6"
                      className="animate-fade-in"
                      style={{ animationDelay: `${1.5 + index * 0.1}s` }}
                    />
                  );
                })}
              </svg>
            </div>
            
            {/* Labels dos meses */}
            <div className="flex justify-between text-xs text-text-secondary mt-2">
              {mockEvolution.meses.map((mes, index) => (
                <span key={index}>{mes}</span>
              ))}
            </div>
            
            {/* Legenda */}
            <div className="flex justify-center gap-6 mt-4">
              <div className="flex items-center gap-2">
                <div className="w-3 h-3 rounded-full bg-success"></div>
                <span className="text-sm text-text-secondary">Receitas</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-3 h-3 rounded-full bg-accent-pink"></div>
                <span className="text-sm text-text-secondary">Despesas</span>
              </div>
            </div>
          </div>
        </div>

        {/* Transações e Estatísticas */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Card: Resumo de Gastos */}
          <div className="card">
            <h3 className="text-lg font-semibold text-text-primary mb-4">Resumo de Gastos</h3>
            <div className="space-y-6">
              <div>
                <div className="flex justify-between items-center mb-2">
                  <span className="text-text-secondary">Total Gasto</span>
                  <span className="text-text-primary font-medium">
                    R$ {mockStats.totalGasto.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                  </span>
                </div>
                <div className="h-2 bg-background rounded-full overflow-hidden">
                  <div className="h-full bg-accent-blue transition-all duration-1000 ease-out" style={{ width: '100%' }} />
                </div>
              </div>

              <div className="space-y-4">
                <h4 className="text-sm font-medium text-text-secondary">Top Categorias</h4>
                {mockStats.categorias.slice(0, 3).map((categoria, index) => {
                  const porcentagem = (categoria.valor / mockStats.totalGasto) * 100;
                  return (
                    <div key={index} className="flex justify-between items-center">
                      <span className="text-text-primary">{categoria.nome}</span>
                      <span className="text-text-secondary">{porcentagem.toFixed(1)}%</span>
                    </div>
                  );
                })}
              </div>

              <div className="pt-4 border-t border-border">
                <h4 className="text-sm font-medium text-text-secondary mb-4">Distribuição</h4>
                <div className="space-y-2">
                  {mockStats.categorias.map((categoria, index) => {
                    const porcentagem = (categoria.valor / mockStats.totalGasto) * 100;
                    return (
                      <div key={index} className="flex items-center space-x-2">
                        <div className="w-3 h-3 rounded-full" style={{ backgroundColor: categoria.cor }} />
                        <span className="text-sm text-text-primary">{categoria.nome}</span>
                        <span className="text-sm text-text-secondary ml-auto">{porcentagem.toFixed(1)}%</span>
                      </div>
                    );
                  })}
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
              {mockTransacoes.map((transacao, index) => (
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
    </div>
  );
} 