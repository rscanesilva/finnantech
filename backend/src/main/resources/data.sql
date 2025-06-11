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
('cat-system-005', NULL, 'Saúde', 'Medicamentos e consultas', '#ff6b6b', 'Heart', 'DESPESA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-006', NULL, 'Entretenimento', 'Cinema, shows e lazer', '#4ecdc4', 'Music', 'DESPESA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-007', NULL, 'Educação', 'Cursos e livros', '#45b7d1', 'Book', 'DESPESA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-008', NULL, 'Roupas', 'Vestuário e acessórios', '#96ceb4', 'Shirt', 'DESPESA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Categorias de RECEITA
('cat-system-011', NULL, 'Salário', 'Salário fixo', '#32d583', 'Money', 'RECEITA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-012', NULL, 'Freelance', 'Trabalhos autônomos', '#5ac8fa', 'Briefcase', 'RECEITA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-013', NULL, 'Vendas', 'Vendas de produtos', '#feca57', 'Store', 'RECEITA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-system-014', NULL, 'Investimentos', 'Rendimentos e dividendos', '#ff9ff3', 'TrendingUp', 'RECEITA', TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ===========================================
-- 3. MÉTODOS DE PAGAMENTO
-- ===========================================

INSERT INTO payment_methods (id, user_id, type, name, description, card_last_digits, card_brand, card_expiry, card_limit, card_closing_day, card_due_day, is_default, active, created_at, updated_at) VALUES
-- Métodos do João Silva
('pm-joao-001', '550e8400-e29b-41d4-a716-446655440000', 'CARTAO_CREDITO', 'Nubank Roxinho', 'Cartão principal', '1234', 'Visa', '12/28', 5000.00, 20, 15, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('pm-joao-002', '550e8400-e29b-41d4-a716-446655440000', 'PIX', 'PIX Principal', 'Chave PIX pelo CPF', NULL, NULL, NULL, NULL, NULL, NULL, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('pm-joao-003', '550e8400-e29b-41d4-a716-446655440000', 'DINHEIRO', 'Dinheiro', 'Pagamentos em espécie', NULL, NULL, NULL, NULL, NULL, NULL, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Métodos da Maria Santos
('pm-maria-001', '550e8400-e29b-41d4-a716-446655440001', 'CARTAO_CREDITO', 'Inter Gold', 'Cartão principal', '9012', 'Mastercard', '05/29', 12000.00, 25, 20, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('pm-maria-002', '550e8400-e29b-41d4-a716-446655440001', 'PIX', 'PIX Maria', 'Chave PIX pelo email', NULL, NULL, NULL, NULL, NULL, NULL, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('pm-maria-003', '550e8400-e29b-41d4-a716-446655440001', 'CARTAO_DEBITO', 'Cartão Débito', 'Conta corrente', '5678', 'Visa', NULL, NULL, NULL, NULL, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ===========================================
-- 4. TRANSAÇÕES JUNHO 2025
-- ===========================================

INSERT INTO transactions (id, user_id, category_id, payment_method_id, type, amount, currency, description, notes, transaction_date, merchant_name, merchant_category, status, tags_json, is_recurring, created_at, updated_at) VALUES

-- ============= TRANSAÇÕES DO JOÃO SILVA (15 transações) =============
-- Receitas
('tx-joao-jun-001', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-011', NULL, 'RECEITA', 5500.00, 'BRL', 'Salário Junho 2025', 'Salário líquido + bonificação', '2025-06-01', 'Empresa XYZ Ltda', 'Salário', 'CONFIRMADA', '["salario", "trabalho"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-002', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-012', 'pm-joao-002', 'RECEITA', 1200.00, 'BRL', 'Consultoria Fim de Semana', 'Projeto web freelance', '2025-06-03', 'Cliente Particular', 'Consultoria', 'CONFIRMADA', '["freelance", "web"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-003', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-013', 'pm-joao-002', 'RECEITA', 850.00, 'BRL', 'Venda Notebook Antigo', 'MacBook 2019', '2025-06-05', 'OLX', 'Marketplace', 'CONFIRMADA', '["venda", "eletrônicos"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Despesas
('tx-joao-jun-004', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-002', 'pm-joao-002', 'DESPESA', 1350.00, 'BRL', 'Aluguel Junho', 'Aluguel + condomínio + IPTU', '2025-06-01', 'Imobiliária ABC', 'Imóveis', 'CONFIRMADA', '["moradia", "aluguel"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-005', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-001', 'pm-joao-001', 'DESPESA', 420.00, 'BRL', 'Compras Supermercado', 'Compras semanais', '2025-06-02', 'Carrefour', 'Supermercado', 'CONFIRMADA', '["alimentação", "mercado"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-006', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-003', 'pm-joao-001', 'DESPESA', 180.00, 'BRL', 'Combustível', 'Gasolina para o carro', '2025-06-03', 'Posto Shell', 'Combustível', 'CONFIRMADA', '["transporte", "gasolina"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-007', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-004', 'pm-joao-001', 'DESPESA', 125.00, 'BRL', 'Almoço Restaurante', 'Reunião de trabalho', '2025-06-04', 'Restaurante Italiano', 'Alimentação', 'CONFIRMADA', '["alimentação", "restaurante"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-008', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-005', 'pm-joao-002', 'DESPESA', 280.00, 'BRL', 'Consulta Médica', 'Check-up anual', '2025-06-06', 'Dr. Silva', 'Saúde', 'CONFIRMADA', '["saúde", "consulta"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-009', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-006', 'pm-joao-001', 'DESPESA', 75.00, 'BRL', 'Cinema Weekend', 'Filme com a família', '2025-06-07', 'Cinemark', 'Entretenimento', 'CONFIRMADA', '["lazer", "cinema"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-010', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-007', 'pm-joao-001', 'DESPESA', 199.00, 'BRL', 'Curso Online', 'Curso de React Avançado', '2025-06-08', 'Udemy', 'Educação', 'CONFIRMADA', '["educação", "programação"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-011', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-008', 'pm-joao-001', 'DESPESA', 320.00, 'BRL', 'Roupas para Trabalho', 'Camisas sociais', '2025-06-09', 'Renner', 'Vestuário', 'CONFIRMADA', '["roupas", "trabalho"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-012', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-001', 'pm-joao-003', 'DESPESA', 85.00, 'BRL', 'Feira Orgânica', 'Frutas e verduras', '2025-06-10', 'Feira da Vila', 'Alimentação', 'CONFIRMADA', '["alimentação", "orgânicos"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-013', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-004', 'pm-joao-002', 'DESPESA', 45.00, 'BRL', 'Delivery Pizza', 'Jantar em casa', '2025-06-10', 'Dominos', 'Delivery', 'CONFIRMADA', '["alimentação", "delivery"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-014', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-003', 'pm-joao-001', 'DESPESA', 50.00, 'BRL', 'Uber', 'Transporte para o aeroporto', '2025-06-11', 'Uber', 'Transporte', 'CONFIRMADA', '["transporte", "uber"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jun-015', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-005', 'pm-joao-002', 'DESPESA', 95.00, 'BRL', 'Farmácia', 'Medicamentos diversos', '2025-06-11', 'Drogasil', 'Farmácia', 'CONFIRMADA', '["saúde", "medicamentos"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ============= TRANSAÇÕES DA MARIA SANTOS (15 transações) =============
-- Receitas
('tx-maria-jun-001', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-011', NULL, 'RECEITA', 8200.00, 'BRL', 'Salário Junho 2025', 'Salário como Senior Developer', '2025-06-01', 'Tech Company SA', 'Salário', 'CONFIRMADA', '["salario", "tech"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-002', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-012', 'pm-maria-002', 'RECEITA', 2800.00, 'BRL', 'Consultoria React', 'Projeto para startup', '2025-06-03', 'StartupABC', 'Consultoria', 'CONFIRMADA', '["freelance", "react"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-003', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-014', 'pm-maria-002', 'RECEITA', 450.00, 'BRL', 'Dividendos', 'Ações da ITSA4', '2025-06-05', 'Clear Corretora', 'Investimentos', 'CONFIRMADA', '["investimentos", "dividendos"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-004', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-013', 'pm-maria-002', 'RECEITA', 1200.00, 'BRL', 'Venda de Curso', 'Curso online de programação', '2025-06-07', 'Hotmart', 'Educação', 'CONFIRMADA', '["vendas", "curso"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Despesas
('tx-maria-jun-005', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-002', 'pm-maria-002', 'DESPESA', 2400.00, 'BRL', 'Financiamento Casa', 'Parcela mensal + seguro', '2025-06-01', 'Banco do Brasil', 'Financiamento', 'CONFIRMADA', '["moradia", "financiamento"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-006', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-001', 'pm-maria-001', 'DESPESA', 680.00, 'BRL', 'Supermercado Premium', 'Compras mensais', '2025-06-02', 'Pão de Açúcar', 'Supermercado', 'CONFIRMADA', '["alimentação", "mercado"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-007', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-007', 'pm-maria-001', 'DESPESA', 890.00, 'BRL', 'Curso de Inglês', 'Mensalidade English Live', '2025-06-03', 'English Live', 'Educação', 'CONFIRMADA', '["educação", "idiomas"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-008', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-005', 'pm-maria-001', 'DESPESA', 520.00, 'BRL', 'Plano de Saúde', 'Mensalidade familiar', '2025-06-04', 'Unimed', 'Saúde', 'CONFIRMADA', '["saúde", "plano"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-009', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-008', 'pm-maria-001', 'DESPESA', 750.00, 'BRL', 'Shopping Roupas', 'Roupas de verão', '2025-06-06', 'Shopping Iguatemi', 'Vestuário', 'CONFIRMADA', '["roupas", "moda"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-010', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-006', 'pm-maria-001', 'DESPESA', 320.00, 'BRL', 'Show Musical', 'Concert Coldplay', '2025-06-07', 'Allianz Parque', 'Entretenimento', 'CONFIRMADA', '["lazer", "show"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-011', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-003', 'pm-maria-003', 'DESPESA', 280.00, 'BRL', 'Combustível Premium', 'Gasolina aditivada', '2025-06-08', 'Posto Ipiranga', 'Combustível', 'CONFIRMADA', '["transporte", "gasolina"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-012', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-004', 'pm-maria-001', 'DESPESA', 420.00, 'BRL', 'Restaurante Japonês', 'Jantar especial', '2025-06-09', 'Sushi Yassu', 'Alimentação', 'CONFIRMADA', '["alimentação", "japonês"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-013', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-001', 'pm-maria-003', 'DESPESA', 185.00, 'BRL', 'Produtos Orgânicos', 'Mercado especializado', '2025-06-10', 'Mundo Verde', 'Alimentação', 'CONFIRMADA', '["alimentação", "orgânicos"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-014', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-005', 'pm-maria-002', 'DESPESA', 180.00, 'BRL', 'Exames Médicos', 'Exames de rotina', '2025-06-10', 'Laboratório Fleury', 'Saúde', 'CONFIRMADA', '["saúde", "exames"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jun-015', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-006', 'pm-maria-002', 'DESPESA', 95.00, 'BRL', 'Netflix Premium', 'Assinatura streaming', '2025-06-11', 'Netflix', 'Entretenimento', 'CONFIRMADA', '["lazer", "streaming"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ===========================================
-- 5. DADOS HISTÓRICOS PARA EVOLUÇÃO (JAN-MAI 2025)
-- ===========================================

-- ============= JANEIRO 2025 =============
-- João Silva - Janeiro
('tx-joao-jan-001', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-011', NULL, 'RECEITA', 5000.00, 'BRL', 'Salário Janeiro 2025', 'Salário do primeiro mês', '2025-01-01', 'Empresa XYZ Ltda', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jan-002', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-002', 'pm-joao-002', 'DESPESA', 1200.00, 'BRL', 'Aluguel Janeiro', 'Aluguel mensal', '2025-01-05', 'Imobiliária ABC', 'Imóveis', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jan-003', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-001', 'pm-joao-001', 'DESPESA', 380.00, 'BRL', 'Supermercado Janeiro', 'Compras mensais', '2025-01-15', 'Extra', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-jan-004', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-003', 'pm-joao-001', 'DESPESA', 150.00, 'BRL', 'Combustível Janeiro', 'Gasolina do mês', '2025-01-20', 'Posto BR', 'Combustível', 'CONFIRMADA', '["transporte"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Maria Santos - Janeiro
('tx-maria-jan-001', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-011', NULL, 'RECEITA', 7500.00, 'BRL', 'Salário Janeiro 2025', 'Salário como Dev Senior', '2025-01-01', 'Tech Company SA', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jan-002', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-012', 'pm-maria-002', 'RECEITA', 1500.00, 'BRL', 'Freelance Janeiro', 'Projeto mobile', '2025-01-10', 'Cliente Corp', 'Consultoria', 'CONFIRMADA', '["freelance"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jan-003', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-002', 'pm-maria-002', 'DESPESA', 2200.00, 'BRL', 'Financiamento Janeiro', 'Parcela da casa', '2025-01-05', 'Banco do Brasil', 'Financiamento', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jan-004', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-001', 'pm-maria-001', 'DESPESA', 600.00, 'BRL', 'Supermercado Premium Janeiro', 'Compras do mês', '2025-01-12', 'Pão de Açúcar', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-jan-005', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-005', 'pm-maria-001', 'DESPESA', 450.00, 'BRL', 'Plano de Saúde Janeiro', 'Mensalidade familiar', '2025-01-15', 'Unimed', 'Saúde', 'CONFIRMADA', '["saude"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ============= FEVEREIRO 2025 =============
-- João Silva - Fevereiro
('tx-joao-fev-001', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-011', NULL, 'RECEITA', 5200.00, 'BRL', 'Salário Fevereiro 2025', 'Salário + ajuste', '2025-02-01', 'Empresa XYZ Ltda', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-fev-002', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-012', 'pm-joao-002', 'RECEITA', 800.00, 'BRL', 'Freelance Fevereiro', 'Manutenção de site', '2025-02-15', 'Cliente Local', 'Consultoria', 'CONFIRMADA', '["freelance"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-fev-003', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-002', 'pm-joao-002', 'DESPESA', 1200.00, 'BRL', 'Aluguel Fevereiro', 'Aluguel mensal', '2025-02-05', 'Imobiliária ABC', 'Imóveis', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-fev-004', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-001', 'pm-joao-001', 'DESPESA', 420.00, 'BRL', 'Supermercado Fevereiro', 'Compras mensais', '2025-02-10', 'Carrefour', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-fev-005', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-006', 'pm-joao-001', 'DESPESA', 180.00, 'BRL', 'Cinema Fevereiro', 'Filme de carnaval', '2025-02-20', 'Cinemark', 'Entretenimento', 'CONFIRMADA', '["lazer"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Maria Santos - Fevereiro
('tx-maria-fev-001', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-011', NULL, 'RECEITA', 7800.00, 'BRL', 'Salário Fevereiro 2025', 'Salário + bônus', '2025-02-01', 'Tech Company SA', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-fev-002', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-014', 'pm-maria-002', 'RECEITA', 320.00, 'BRL', 'Dividendos Fevereiro', 'Ações diversas', '2025-02-08', 'Clear Corretora', 'Investimentos', 'CONFIRMADA', '["investimentos"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-fev-003', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-002', 'pm-maria-002', 'DESPESA', 2200.00, 'BRL', 'Financiamento Fevereiro', 'Parcela da casa', '2025-02-05', 'Banco do Brasil', 'Financiamento', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-fev-004', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-007', 'pm-maria-001', 'DESPESA', 850.00, 'BRL', 'Curso Inglês Fevereiro', 'Mensalidade curso', '2025-02-10', 'English Live', 'Educação', 'CONFIRMADA', '["educacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-fev-005', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-008', 'pm-maria-001', 'DESPESA', 650.00, 'BRL', 'Roupas Fevereiro', 'Compras de verão', '2025-02-14', 'Shopping', 'Vestuário', 'CONFIRMADA', '["roupas"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ============= MARÇO 2025 =============
-- João Silva - Março (mês com mais gastos)
('tx-joao-mar-001', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-011', NULL, 'RECEITA', 5300.00, 'BRL', 'Salário Março 2025', 'Salário mensal', '2025-03-01', 'Empresa XYZ Ltda', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-mar-002', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-002', 'pm-joao-002', 'DESPESA', 1250.00, 'BRL', 'Aluguel Março', 'Aluguel + condomínio', '2025-03-05', 'Imobiliária ABC', 'Imóveis', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-mar-003', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-001', 'pm-joao-001', 'DESPESA', 480.00, 'BRL', 'Supermercado Março', 'Compras + festa', '2025-03-10', 'Extra', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-mar-004', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-005', 'pm-joao-002', 'DESPESA', 350.00, 'BRL', 'Dentista Março', 'Tratamento dental', '2025-03-15', 'Clínica Dent', 'Saúde', 'CONFIRMADA', '["saude"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-mar-005', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-007', 'pm-joao-001', 'DESPESA', 299.00, 'BRL', 'Curso Node.js', 'Curso online', '2025-03-20', 'Udemy', 'Educação', 'CONFIRMADA', '["educacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Maria Santos - Março
('tx-maria-mar-001', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-011', NULL, 'RECEITA', 8000.00, 'BRL', 'Salário Março 2025', 'Salário + aumento', '2025-03-01', 'Tech Company SA', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mar-002', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-012', 'pm-maria-002', 'RECEITA', 2200.00, 'BRL', 'Consultoria Março', 'Projeto grande', '2025-03-10', 'Startup Tech', 'Consultoria', 'CONFIRMADA', '["freelance"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mar-003', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-002', 'pm-maria-002', 'DESPESA', 2300.00, 'BRL', 'Financiamento Março', 'Parcela + seguro', '2025-03-05', 'Banco do Brasil', 'Financiamento', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mar-004', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-001', 'pm-maria-001', 'DESPESA', 720.00, 'BRL', 'Supermercado Março', 'Compras especiais', '2025-03-12', 'Pão de Açúcar', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mar-005', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-006', 'pm-maria-001', 'DESPESA', 420.00, 'BRL', 'Teatro Março', 'Peça teatral', '2025-03-18', 'Teatro Municipal', 'Entretenimento', 'CONFIRMADA', '["lazer"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ============= ABRIL 2025 =============
-- João Silva - Abril (recuperação)
('tx-joao-abr-001', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-011', NULL, 'RECEITA', 5400.00, 'BRL', 'Salário Abril 2025', 'Salário + horas extras', '2025-04-01', 'Empresa XYZ Ltda', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-abr-002', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-013', 'pm-joao-002', 'RECEITA', 600.00, 'BRL', 'Venda Abril', 'Venda de móveis', '2025-04-12', 'OLX', 'Vendas', 'CONFIRMADA', '["vendas"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-abr-003', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-002', 'pm-joao-002', 'DESPESA', 1200.00, 'BRL', 'Aluguel Abril', 'Aluguel mensal', '2025-04-05', 'Imobiliária ABC', 'Imóveis', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-abr-004', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-001', 'pm-joao-001', 'DESPESA', 360.00, 'BRL', 'Supermercado Abril', 'Compras básicas', '2025-04-15', 'Carrefour', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-abr-005', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-003', 'pm-joao-001', 'DESPESA', 170.00, 'BRL', 'Combustível Abril', 'Gasolina mensal', '2025-04-20', 'Posto Shell', 'Combustível', 'CONFIRMADA', '["transporte"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Maria Santos - Abril
('tx-maria-abr-001', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-011', NULL, 'RECEITA', 8100.00, 'BRL', 'Salário Abril 2025', 'Salário estável', '2025-04-01', 'Tech Company SA', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-abr-002', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-014', 'pm-maria-002', 'RECEITA', 380.00, 'BRL', 'Dividendos Abril', 'Investimentos diversos', '2025-04-10', 'Clear Corretora', 'Investimentos', 'CONFIRMADA', '["investimentos"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-abr-003', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-002', 'pm-maria-002', 'DESPESA', 2250.00, 'BRL', 'Financiamento Abril', 'Parcela mensal', '2025-04-05', 'Banco do Brasil', 'Financiamento', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-abr-004', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-001', 'pm-maria-001', 'DESPESA', 650.00, 'BRL', 'Supermercado Abril', 'Compras mensais', '2025-04-12', 'Pão de Açúcar', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-abr-005', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-005', 'pm-maria-001', 'DESPESA', 480.00, 'BRL', 'Plano Saúde Abril', 'Mensalidade família', '2025-04-15', 'Unimed', 'Saúde', 'CONFIRMADA', '["saude"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ============= MAIO 2025 =============
-- João Silva - Maio (estabilização)
('tx-joao-mai-001', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-011', NULL, 'RECEITA', 5450.00, 'BRL', 'Salário Maio 2025', 'Salário + gratificação', '2025-05-01', 'Empresa XYZ Ltda', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-mai-002', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-012', 'pm-joao-002', 'RECEITA', 950.00, 'BRL', 'Freelance Maio', 'Projeto fim de semana', '2025-05-15', 'Cliente Remoto', 'Consultoria', 'CONFIRMADA', '["freelance"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-mai-003', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-002', 'pm-joao-002', 'DESPESA', 1220.00, 'BRL', 'Aluguel Maio', 'Aluguel mensal', '2025-05-05', 'Imobiliária ABC', 'Imóveis', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-mai-004', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-001', 'pm-joao-001', 'DESPESA', 400.00, 'BRL', 'Supermercado Maio', 'Compras do mês', '2025-05-12', 'Extra', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-joao-mai-005', '550e8400-e29b-41d4-a716-446655440000', 'cat-system-004', 'pm-joao-001', 'DESPESA', 220.00, 'BRL', 'Restaurante Maio', 'Jantar especial', '2025-05-20', 'Restaurante Italiano', 'Alimentação', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Maria Santos - Maio
('tx-maria-mai-001', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-011', NULL, 'RECEITA', 8150.00, 'BRL', 'Salário Maio 2025', 'Salário + participação', '2025-05-01', 'Tech Company SA', 'Salário', 'CONFIRMADA', '["salario"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mai-002', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-012', 'pm-maria-002', 'RECEITA', 1800.00, 'BRL', 'Consultoria Maio', 'Projeto React Native', '2025-05-10', 'App Startup', 'Consultoria', 'CONFIRMADA', '["freelance"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mai-003', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-013', 'pm-maria-002', 'RECEITA', 900.00, 'BRL', 'Venda Curso Maio', 'Curso online vendido', '2025-05-18', 'Hotmart', 'Vendas', 'CONFIRMADA', '["vendas"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mai-004', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-002', 'pm-maria-002', 'DESPESA', 2280.00, 'BRL', 'Financiamento Maio', 'Parcela + taxa', '2025-05-05', 'Banco do Brasil', 'Financiamento', 'CONFIRMADA', '["moradia"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mai-005', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-001', 'pm-maria-001', 'DESPESA', 680.00, 'BRL', 'Supermercado Maio', 'Compras premium', '2025-05-15', 'Pão de Açúcar', 'Supermercado', 'CONFIRMADA', '["alimentacao"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tx-maria-mai-006', '550e8400-e29b-41d4-a716-446655440001', 'cat-system-008', 'pm-maria-001', 'DESPESA', 580.00, 'BRL', 'Roupas Maio', 'Compras de inverno', '2025-05-22', 'Shopping Center', 'Vestuário', 'CONFIRMADA', '["roupas"]', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ===========================================
-- COMENTÁRIOS SOBRE OS DADOS DE TESTE
-- ===========================================

/*
USUÁRIOS DE TESTE E SUAS SENHAS:
- joao@teste.com - Senha: "123456" (Hash BCrypt)
- maria@teste.com - Senha: "123456" (Hash BCrypt)

DADOS CRIADOS:
✅ 2 Usuários
✅ 12 Categorias padrão do sistema (8 despesas + 4 receitas)
✅ 6 Métodos de pagamento
✅ 30 Transações de junho 2025 (15 para cada usuário)

RESUMO FINANCEIRO JUNHO 2025:
João Silva:
- Receitas: R$ 7.550,00 (Salário + Freelance + Venda)
- Despesas: R$ 3.229,00
- Saldo: R$ 4.321,00

Maria Santos:
- Receitas: R$ 12.650,00 (Salário + Freelance + Dividendos + Vendas)
- Despesas: R$ 7.230,00
- Saldo: R$ 5.420,00

CENÁRIOS DE TESTE:
- Dashboard com dados reais e atuais
- Variedade de categorias e tipos de transação
- Diferentes métodos de pagamento
- Transações distribuídas ao longo do período
*/ 