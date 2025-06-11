-- ===============================================
-- FINNANTECH V2 - DADOS DE TESTE SIMPLIFICADOS
-- ===============================================

-- ===========================================
-- 1. USUÁRIOS
-- ===========================================

INSERT INTO users (id, name, email, password_hash, provider, provider_id, email_verified, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'João Silva', 'joao@teste.com', '$2a$10$AwuNgP8vweHcWfevI0F5aOAV/oiseSUfuikGb2nMin8tyPcMmMcXq', 'LOCAL', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440001', 'Maria Santos', 'maria@teste.com', '$2a$10$AwuNgP8vweHcWfevI0F5aOAV/oiseSUfuikGb2nMin8tyPcMmMcXq', 'LOCAL', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ===========================================
-- 2. CATEGORIAS PADRÃO DO SISTEMA
-- ===========================================

INSERT INTO categories (id, user_id, name, description, color, icon, type, is_system_default, active, created_at, updated_at) VALUES
-- Categorias de DESPESA
('cat-system-001', NULL, 'Mercado', 'Compras de supermercado', '#5392ff', 'ShoppingCart', 'DESPESA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-002', NULL, 'Moradia', 'Aluguel e condomínio', '#8b5cf6', 'House', 'DESPESA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-003', NULL, 'Transporte', 'Combustível e transporte', '#32d583', 'Car', 'DESPESA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-004', NULL, 'Alimentação', 'Restaurantes e delivery', '#f178b6', 'ForkKnife', 'DESPESA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Categorias de RECEITA
('cat-system-011', NULL, 'Salário', 'Salário fixo', '#32d583', 'Money', 'RECEITA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-012', NULL, 'Freelance', 'Trabalhos autônomos', '#5ac8fa', 'Briefcase', 'RECEITA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ===========================================
-- 3. MÉTODOS DE PAGAMENTO
-- ===========================================

INSERT INTO payment_methods (id, user_id, type, name, description, card_last_digits, card_brand, card_expiry, card_limit, card_closing_day, card_due_day, is_default, active, created_at, updated_at) VALUES
-- Métodos do João Silva
('pm-joao-001', '550e8400-e29b-41d4-a716-446655440000', 'CARTAO_CREDITO', 'Nubank Roxinho', 'Cartão principal', '1234', 'Visa', '12/28', 5000.00, 20, 15, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('pm-joao-002', '550e8400-e29b-41d4-a716-446655440000', 'PIX', 'PIX Principal', 'Chave PIX pelo CPF', NULL, NULL, NULL, NULL, NULL, NULL, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Métodos da Maria Santos
('pm-maria-001', '550e8400-e29b-41d4-a716-446655440001', 'CARTAO_CREDITO', 'Inter Gold', 'Cartão principal', '9012', 'Mastercard', '05/29', 12000.00, 25, 20, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('pm-maria-002', '550e8400-e29b-41d4-a716-446655440001', 'PIX', 'PIX Maria', 'Chave PIX pelo email', NULL, NULL, NULL, NULL, NULL, NULL, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ===========================================
-- 4. TRANSAÇÕES
-- ===========================================

INSERT INTO transactions (id, user_id, category_id, payment_method_id, type, amount, currency, description, notes, transaction_date, merchant_name, merchant_category, status, tags, is_recurring, created_at, updated_at) VALUES
-- Transações do João
('tx-joao-001', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-011', NULL, 'RECEITA', 5000.00, 'BRL', 'Salário Março 2024', 'Salário líquido', '2024-03-01', 'Empresa XYZ Ltda', 'Salário', 'CONFIRMADA', '["salario", "trabalho"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-002', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-002', 'pm-joao-002', 'DESPESA', 1200.00, 'BRL', 'Aluguel Março', 'Aluguel + condomínio', '2024-03-05', 'Imobiliária ABC', 'Imóveis', 'CONFIRMADA', '["moradia", "aluguel"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-003', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-001', 'pm-joao-001', 'DESPESA', 387.50, 'BRL', 'Compras do mês', 'Supermercado semanal', '2024-03-12', 'Carrefour', 'Supermercado', 'CONFIRMADA', '["alimentacao", "mercado"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Transações da Maria
('tx-maria-001', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-011', NULL, 'RECEITA', 7500.00, 'BRL', 'Salário Março 2024', 'Salário líquido como Senior Dev', '2024-03-01', 'Tech Company SA', 'Salário', 'CONFIRMADA', '["salario", "tech"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-002', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-012', 'pm-maria-002', 'RECEITA', 3200.00, 'BRL', 'Consultoria React', 'Projeto de 2 semanas', '2024-03-15', 'StartupABC', 'Consultoria', 'CONFIRMADA', '["freelance", "react"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-003', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-002', 'pm-maria-002', 'DESPESA', 2200.00, 'BRL', 'Financiamento Casa', 'Parcela mensal', '2024-03-05', 'Banco do Brasil', 'Financiamento', 'CONFIRMADA', '["moradia", "financiamento"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ===========================================
-- COMENTÁRIOS SOBRE OS DADOS DE TESTE
-- ===========================================

/*
USUÁRIOS DE TESTE E SUAS SENHAS:
- joao@teste.com - Senha: "123456" (Hash BCrypt)
- maria@teste.com - Senha: "123456" (Hash BCrypt)

DADOS CRIADOS:
✅ 2 Usuários
✅ 6 Categorias padrão do sistema
✅ 4 Métodos de pagamento
✅ 6 Transações de teste

CENÁRIOS DE TESTE:
- Dashboard com dados reais de receitas/despesas
- Transações categorizadas e organizadas
- Múltiplos métodos de pagamento
*/ 