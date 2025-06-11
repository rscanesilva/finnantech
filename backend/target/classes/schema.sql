-- ===============================================
-- FINNANTECH V2 - DATABASE SCHEMA (H2 Compatible)
-- Modelagem profissional para sistema financeiro
-- DBA: Especialista em modelagem de dados
-- ===============================================

-- ===========================================
-- 0. TABELA DE USUÁRIOS
-- ===========================================
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL',
    provider_id VARCHAR(100),
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_users_provider ON users (provider, provider_id);

-- ===========================================
-- 1. TABELA DE CATEGORIAS
-- ===========================================
CREATE TABLE IF NOT EXISTS categories (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36), -- NULL para categorias padrão do sistema
    name VARCHAR(100) NOT NULL,
    description TEXT,
    color VARCHAR(7) NOT NULL DEFAULT '#5392ff', -- Hex color
    icon VARCHAR(50) DEFAULT 'ShoppingCart', -- Nome do ícone
    type VARCHAR(20) NOT NULL CHECK (type IN ('RECEITA', 'DESPESA', 'INVESTIMENTO')),
    is_system_default BOOLEAN NOT NULL DEFAULT FALSE, -- Categoria padrão do sistema
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_categories_user_name UNIQUE (user_id, name) -- Evita duplicação por usuário
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_categories_user_active ON categories (user_id, active);
CREATE INDEX IF NOT EXISTS idx_categories_type ON categories (type);
CREATE INDEX IF NOT EXISTS idx_categories_system_default ON categories (is_system_default);

-- ===========================================
-- 2. TABELA DE MÉTODOS DE PAGAMENTO
-- ===========================================
-- Justificativa: Diferentes formas de pagamento precisam ser trackadas separadamente.
-- Estratégia: Flexibilidade para cartões, PIX, dinheiro, transferências, etc.

CREATE TABLE IF NOT EXISTS payment_methods (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX', 'DINHEIRO', 'TRANSFERENCIA', 'BOLETO')),
    name VARCHAR(100) NOT NULL, -- Ex: "Nubank", "Carteira", "PIX", etc.
    description TEXT,
    
    -- Dados específicos para cartões
    card_last_digits VARCHAR(4), -- Últimos 4 dígitos
    card_brand VARCHAR(50), -- Visa, Mastercard, etc.
    card_expiry VARCHAR(7), -- MM/YYYY
    card_limit DECIMAL(15,2), -- Limite do cartão
    card_closing_day INTEGER, -- Dia do fechamento da fatura
    card_due_day INTEGER, -- Dia do vencimento
    
    -- Metadados
    is_default BOOLEAN NOT NULL DEFAULT FALSE, -- Método padrão
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_payment_methods_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_payment_methods_user ON payment_methods (user_id);
CREATE INDEX IF NOT EXISTS idx_payment_methods_type ON payment_methods (type);
CREATE INDEX IF NOT EXISTS idx_payment_methods_default ON payment_methods (user_id, is_default);

-- ===========================================
-- 3. TABELA DE TRANSAÇÕES
-- ===========================================
-- Justificativa: Core do sistema financeiro. Precisa ser extremamente otimizada.
-- Estratégia: Campos desnormalizados para performance, particionamento futuro por data.
-- Performance: Múltiplos índices compostos para consultas comuns (por data, usuário, categoria).

CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    payment_method_id VARCHAR(36), -- Pode ser NULL para transações importadas
    
    -- Dados da transação
    type VARCHAR(20) NOT NULL CHECK (type IN ('RECEITA', 'DESPESA')), -- Investimentos ficam em tabela separada
    amount DECIMAL(15,2) NOT NULL, -- Valor sempre positivo
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    description VARCHAR(255) NOT NULL,
    notes TEXT, -- Observações do usuário
    
    -- Dados temporais
    transaction_date DATE NOT NULL, -- Data da transação real
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Dados do estabelecimento/origem
    merchant_name VARCHAR(150), -- Nome do estabelecimento
    merchant_category VARCHAR(100), -- Categoria do estabelecimento
    
    -- Metadados importantes
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA' CHECK (status IN ('PENDENTE', 'CONFIRMADA', 'CANCELADA')),
    is_recurring BOOLEAN NOT NULL DEFAULT FALSE, -- Se é transação recorrente
    recurring_group_id VARCHAR(36), -- Para agrupar transações recorrentes
    external_id VARCHAR(255), -- ID de sistemas externos (Open Banking)
    
    -- Tags para melhor organização (JSON simplificado como TEXT)
    tags_json TEXT,
    
    -- Campo calculado para performance (desnormalização estratégica)
    month_year VARCHAR(7), -- YYYY-MM (será preenchido por trigger)
    
    -- Constraints
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    CONSTRAINT fk_transactions_payment_method FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id) ON DELETE SET NULL,
    CONSTRAINT chk_transactions_amount CHECK (amount > 0),
    CONSTRAINT chk_transactions_currency CHECK (currency IN ('BRL', 'USD', 'EUR'))
);

-- Índices estratégicos para performance
CREATE INDEX IF NOT EXISTS idx_transactions_user_date ON transactions (user_id, transaction_date DESC);
CREATE INDEX IF NOT EXISTS idx_transactions_user_month ON transactions (user_id, month_year DESC);
CREATE INDEX IF NOT EXISTS idx_transactions_category ON transactions (category_id);
CREATE INDEX IF NOT EXISTS idx_transactions_payment_method ON transactions (payment_method_id);
CREATE INDEX IF NOT EXISTS idx_transactions_type_date ON transactions (type, transaction_date DESC);
CREATE INDEX IF NOT EXISTS idx_transactions_merchant ON transactions (merchant_name);
CREATE INDEX IF NOT EXISTS idx_transactions_recurring ON transactions (user_id, is_recurring);
CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions (status);
CREATE INDEX IF NOT EXISTS idx_transactions_user_type_date ON transactions (user_id, type, transaction_date DESC);

-- ===========================================
-- 4. TABELA DE INVESTIMENTOS
-- ===========================================
-- Justificativa: Investimentos têm características muito diferentes de transações comuns.
-- Estratégia: Tabela separada para flexibilidade e tipos específicos de investimento.

CREATE TABLE IF NOT EXISTS investments (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    
    -- Dados do investimento
    name VARCHAR(200) NOT NULL, -- Nome/descrição do investimento
    type VARCHAR(20) NOT NULL CHECK (type IN ('ACAO', 'FUNDO', 'TESOURO', 'CDB', 'LCI', 'LCA', 'CRYPTO', 'IMOVEL', 'OUTRO')),
    symbol VARCHAR(20), -- Símbolo/ticker (ex: PETR4, BTCBRL)
    
    -- Valores financeiros
    initial_amount DECIMAL(15,2) NOT NULL, -- Valor inicial investido
    current_amount DECIMAL(15,2), -- Valor atual (pode ser NULL se não atualizado)
    quantity DECIMAL(18,8), -- Quantidade de cotas/ações
    average_price DECIMAL(15,2), -- Preço médio de compra
    
    -- Datas importantes
    purchase_date DATE NOT NULL,
    maturity_date DATE, -- Para investimentos com vencimento
    last_update TIMESTAMP, -- Última atualização de preços
    
    -- Rentabilidade (calculada na aplicação)
    profit_loss DECIMAL(15,2),
    profit_loss_percent DECIMAL(8,4),
    
    -- Metadados
    broker VARCHAR(100), -- Corretora
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_investments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_investments_initial_amount CHECK (initial_amount > 0)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_investments_user ON investments (user_id);
CREATE INDEX IF NOT EXISTS idx_investments_type ON investments (type);
CREATE INDEX IF NOT EXISTS idx_investments_symbol ON investments (symbol);
CREATE INDEX IF NOT EXISTS idx_investments_purchase_date ON investments (purchase_date DESC);
CREATE INDEX IF NOT EXISTS idx_investments_user_active ON investments (user_id, active);
CREATE INDEX IF NOT EXISTS idx_investments_broker ON investments (broker);

-- ===========================================
-- 5. TABELA DE ORÇAMENTOS
-- ===========================================
-- Justificativa: Controle de gastos por categoria é fundamental em sistemas financeiros.
-- Estratégia: Orçamentos mensais por categoria com alertas automáticos.

CREATE TABLE IF NOT EXISTS budgets (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    
    -- Período do orçamento
    month_year VARCHAR(7) NOT NULL, -- YYYY-MM
    
    -- Valores
    planned_amount DECIMAL(15,2) NOT NULL, -- Valor planejado
    spent_amount DECIMAL(15,2) NOT NULL DEFAULT 0, -- Valor gasto (atualizado automaticamente)
    remaining_amount DECIMAL(15,2), -- Valor restante (calculado)
    
    -- Configurações
    alert_threshold DECIMAL(5,2) DEFAULT 80.00, -- % para alerta (80% = 0.80)
    alert_sent BOOLEAN NOT NULL DEFAULT FALSE, -- Se alerta já foi enviado
    
    -- Metadados
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_budgets_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_budgets_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT uk_budgets_user_category_month UNIQUE (user_id, category_id, month_year),
    CONSTRAINT chk_budgets_planned_amount CHECK (planned_amount > 0),
    CONSTRAINT chk_budgets_spent_amount CHECK (spent_amount >= 0),
    CONSTRAINT chk_budgets_alert_threshold CHECK (alert_threshold > 0 AND alert_threshold <= 100)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_budgets_user_month ON budgets (user_id, month_year DESC);
CREATE INDEX IF NOT EXISTS idx_budgets_category ON budgets (category_id);
CREATE INDEX IF NOT EXISTS idx_budgets_user_active ON budgets (user_id, active);

-- ===========================================
-- 6. TABELA DE TRANSAÇÕES RECORRENTES
-- ===========================================
-- Justificativa: Automação de lançamentos recorrentes (salário, aluguel, etc.).
-- Estratégia: Template para gerar transações automaticamente.

CREATE TABLE IF NOT EXISTS recurring_transactions (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    payment_method_id VARCHAR(36),
    
    -- Template da transação
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('RECEITA', 'DESPESA')),
    
    -- Configurações de recorrência
    frequency VARCHAR(20) NOT NULL CHECK (frequency IN ('DIARIA', 'SEMANAL', 'QUINZENAL', 'MENSAL', 'BIMESTRAL', 'TRIMESTRAL', 'SEMESTRAL', 'ANUAL')),
    start_date DATE NOT NULL,
    end_date DATE, -- NULL = sem fim
    
    -- Controle de execução
    next_execution DATE NOT NULL, -- Próxima data de execução
    last_execution DATE, -- Última execução
    execution_count INTEGER NOT NULL DEFAULT 0, -- Quantas vezes foi executada
    max_executions INTEGER, -- Máximo de execuções (NULL = ilimitado)
    
    -- Metadados
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_recurring_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_recurring_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_recurring_payment_method FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id) ON DELETE SET NULL,
    CONSTRAINT chk_recurring_amount CHECK (amount > 0),
    CONSTRAINT chk_recurring_dates CHECK (end_date IS NULL OR end_date >= start_date),
    CONSTRAINT chk_recurring_executions CHECK (max_executions IS NULL OR max_executions > 0)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_recurring_user ON recurring_transactions (user_id);
CREATE INDEX IF NOT EXISTS idx_recurring_next_execution ON recurring_transactions (next_execution ASC);
CREATE INDEX IF NOT EXISTS idx_recurring_user_active ON recurring_transactions (user_id, active);

-- ===========================================
-- 7. TABELA DE AUDITORIA
-- ===========================================
-- Justificativa: Rastreabilidade total das mudanças para compliance e debugging.
-- Estratégia: Log de todas as operações críticas no sistema.

CREATE TABLE IF NOT EXISTS audit_logs (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36), -- Pode ser NULL para operações do sistema
    
    -- Dados da operação
    table_name VARCHAR(50) NOT NULL, -- Nome da tabela afetada
    operation VARCHAR(20) NOT NULL CHECK (operation IN ('INSERT', 'UPDATE', 'DELETE')),
    record_id VARCHAR(36) NOT NULL, -- ID do registro afetado
    
    -- Dados da mudança
    old_values TEXT, -- JSON com valores antigos
    new_values TEXT, -- JSON com valores novos
    
    -- Metadados
    ip_address VARCHAR(45), -- IPv4 ou IPv6
    user_agent TEXT,
    session_id VARCHAR(100),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_audit_user ON audit_logs (user_id);
CREATE INDEX IF NOT EXISTS idx_audit_table_operation ON audit_logs (table_name, operation);
CREATE INDEX IF NOT EXISTS idx_audit_timestamp ON audit_logs (timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_audit_record ON audit_logs (table_name, record_id); 