# Regras Unificadas - Sistema Monex (Versão Final)

## 1. CONTEXTO GERAL DO PROJETO

### 1.1. Visão Geral
O **Monex** é um sistema de controle financeiro multi-usuário integrado com Open Banking, desenvolvido para auxiliar pessoas físicas no gerenciamento inteligente de suas finanças. Combina automação, inteligência artificial e interface moderna para oferecer experiência superior de controle financeiro.

### 1.2. Objetivos Principais
- Automatizar coleta e categorização de transações financeiras
- Fornecer insights inteligentes sobre padrões de gastos
- Simplificar controle financeiro através de interface intuitiva
- Integrar-se nativamente com Open Banking brasileiro

### 1.3. Documentação Base Obrigatória
SEMPRE consulte estes documentos antes de implementar qualquer funcionalidade:
- `docs/README.md` - Resumo executivo e índice
- `docs/PRD.md` - Requisitos do produto
- `docs/System_Architecture.md` - Arquitetura técnica
- `docs/UI_Kit_Design_System.md` - Design system e componentes
- `docs/User_Flows.md` - Fluxos do usuário
- `docs/API_Specification.md` - Especificação da API
- `docs/Database_Schema.md` - Schema do banco de dados

---

## 2. STACK TECNOLÓGICO OBRIGATÓRIO

### 2.1. Backend (Java Spring Boot)
- **Framework**: Spring Boot 3.2+
- **Linguagem**: Java 17+ (LTS)
- **Build Tool**: Maven
- **Banco de Dados**: MySQL 8.0+
- **ORM**: Spring Data JPA + Hibernate
- **Autenticação**: Spring Security + JWT + OAuth2
- **Validação**: Bean Validation (JSR-303)
- **Documentação API**: OpenAPI 3.0 (SpringDoc)
- **Cache**: Redis
- **Mensageria**: RabbitMQ ou Apache Kafka
- **Monitoramento**: Spring Boot Actuator + Micrometer

### 2.2. Frontend (React Separado)
- **Framework**: React 18+ com TypeScript
- **Build Tool**: Vite (mais rápido que Create React App)
- **Styling**: Tailwind CSS
- **Componentes UI**: Shadcn/ui como base
- **Estado Global**: Zustand (mais simples que Redux)
- **Roteamento**: React Router v6
- **Formulários**: React Hook Form + Zod
- **HTTP Client**: Axios com interceptors
- **Gráficos**: Recharts
- **Ícones**: Phosphor Icons
- **Notificações**: React Hot Toast
- **Testes**: Vitest + Testing Library

### 2.3. Infraestrutura & DevOps
- **Containerização**: Docker + Docker Compose
- **Deploy Backend**: AWS ECS ou DigitalOcean
- **Deploy Frontend**: Vercel ou Netlify
- **Database**: AWS RDS MySQL ou PlanetScale
- **CDN**: CloudFlare
- **Monitoramento**: DataDog ou New Relic
- **CI/CD**: GitHub Actions

### 2.4. Integrações Obrigatórias
- **Open Banking**: APIs BACEN (Banco Central)
- **Pagamentos**: Stripe + PIX (PagSeguro/Mercado Pago)
- **E-mail**: SendGrid ou AWS SES
- **SMS**: Twilio
- **Storage**: AWS S3 ou Cloudinary

---

## 3. ARQUITETURA DO SISTEMA

### 3.1. Estrutura Backend (Spring Boot)
```
src/main/java/com/monex/
├── MonexApplication.java          # Main class
├── config/                        # Configurações
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   ├── RedisConfig.java
│   └── OpenBankingConfig.java
├── controller/                    # REST Controllers
│   ├── AuthController.java
│   ├── UserController.java
│   ├── TransactionController.java
│   ├── CategoryController.java
│   └── DashboardController.java
├── service/                       # Business Logic
│   ├── UserService.java
│   ├── TransactionService.java
│   ├── OpenBankingService.java
│   ├── CategorizationService.java
│   └── NotificationService.java
├── repository/                    # Data Access
│   ├── UserRepository.java
│   ├── TransactionRepository.java
│   └── CategoryRepository.java
├── model/                         # Entities/DTOs
│   ├── entity/
│   │   ├── User.java
│   │   ├── Transaction.java
│   │   └── Category.java
│   └── dto/
│       ├── UserDTO.java
│       ├── TransactionDTO.java
│       └── DashboardDTO.java
├── security/                      # Security
│   ├── JwtTokenProvider.java
│   ├── CustomUserDetailsService.java
│   └── SecurityUtils.java
├── exception/                     # Exception Handling
│   ├── GlobalExceptionHandler.java
│   └── BusinessException.java
└── util/                         # Utilities
    ├── CryptoUtils.java
    └── DateUtils.java

src/main/resources/
├── application.yml               # Configurações
├── application-dev.yml          # Dev environment
├── application-prod.yml         # Production environment
└── db/migration/               # Flyway migrations
    ├── V1__Create_users_table.sql
    ├── V2__Create_transactions_table.sql
    └── V3__Create_categories_table.sql
```

### 3.2. Estrutura Frontend (React)
```
monex-frontend/
├── public/
├── src/
│   ├── components/              # Componentes React
│   │   ├── ui/                 # Componentes base (shadcn/ui)
│   │   ├── forms/              # Formulários
│   │   ├── charts/             # Gráficos
│   │   ├── layouts/            # Layouts
│   │   └── features/           # Componentes por feature
│   │       ├── auth/
│   │       ├── dashboard/
│   │       ├── transactions/
│   │       └── settings/
│   ├── pages/                  # Páginas principais
│   ├── hooks/                  # Custom hooks
│   ├── stores/                 # Zustand stores
│   ├── services/               # API calls
│   ├── utils/                  # Utilitários
│   ├── types/                  # TypeScript types
│   ├── styles/                 # CSS global
│   └── constants/              # Constantes
├── package.json
├── vite.config.ts
├── tailwind.config.js
└── tsconfig.json
```

---

## 4. DESIGN SYSTEM E UI/UX OBRIGATÓRIO

### 4.1. Paleta de Cores (Usar SEMPRE)
```css
:root {
    /* Background */
    --bg-primary: #0a0a0b;
    --bg-secondary: #161618;
    --card-bg: #1e1e22;
    
    /* Text */
    --text-primary: #f0f0f0;
    --text-secondary: #a0a0a0;
    --text-muted: #6b7280;
    
    /* Brand Colors */
    --accent-green: #10b981;      /* Primary */
    --accent-blue: #3b82f6;       /* Secondary */
    --accent-pink: #f178b6;       /* Accent */
    
    /* Status Colors */
    --success: #059669;
    --warning: #f59e0b;
    --error: #ef4444;
    --info: #0ea5e9;
    
    /* Borders */
    --border-primary: #333336;
    --border-secondary: #27272a;
}
```

### 4.2. Tipografia Obrigatória
- **Fonte**: Inter (Google Fonts)
- **Pesos**: 400, 500, 600, 700, 800
- **Escala**: 0.875rem, 1rem, 1.125rem, 1.25rem, 1.5rem, 2rem, 3rem

### 4.3. Componentes UI Mandatórios
Implementar EXATAMENTE conforme especificado no `UI_Kit_Design_System.md`:
- Cards com `border-radius: 1rem`
- Botões com estados hover/active/disabled
- Inputs com validação visual
- Modais responsivos
- Loading skeletons (não spinners)
- Toast notifications
- Gráficos interativos

### 4.4. Responsividade Obrigatória
```css
/* Breakpoints mandatórios */
sm: 640px    /* Mobile landscape */
md: 768px    /* Tablet */
lg: 1024px   /* Desktop */
xl: 1280px   /* Large desktop */
2xl: 1536px  /* Extra large */
```

---

## 5. REGRAS DE DESENVOLVIMENTO BACKEND

### 5.1. Estrutura de Controllers
```java
@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin(origins = "${app.frontend.url}")
@Validated
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // Implementação
    }
}
```

### 5.2. Padrão de Response Obrigatório
```java
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    
    // Construtores, getters, setters
}
```

### 5.3. Validação Obrigatória
```java
@Entity
@Table(name = "transactions")
public class Transaction {
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String description;
}
```

### 5.4. Segurança Obrigatória
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/health").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .build();
    }
}
```

---

## 6. REGRAS DE DESENVOLVIMENTO FRONTEND

### 6.1. Estrutura de Componentes React
```typescript
interface TransactionCardProps {
  transaction: Transaction;
  onEdit?: (id: string) => void;
  onDelete?: (id: string) => void;
}

export function TransactionCard({ 
  transaction, 
  onEdit, 
  onDelete 
}: TransactionCardProps) {
  // Hooks primeiro
  const [isLoading, setIsLoading] = useState(false);
  
  // Handlers
  const handleEdit = useCallback(() => {
    onEdit?.(transaction.id);
  }, [transaction.id, onEdit]);
  
  // Early returns
  if (!transaction) return null;
  
  return (
    <div className="card-base p-6 hover:bg-card-hover transition-colors">
      {/* JSX organizado */}
    </div>
  );
}
```

### 6.2. Gerenciamento de Estado (Zustand)
```typescript
interface TransactionStore {
  transactions: Transaction[];
  isLoading: boolean;
  error: string | null;
  
  fetchTransactions: () => Promise<void>;
  addTransaction: (transaction: TransactionInput) => Promise<void>;
  updateTransaction: (id: string, data: Partial<Transaction>) => Promise<void>;
  deleteTransaction: (id: string) => Promise<void>;
}

export const useTransactionStore = create<TransactionStore>((set, get) => ({
  transactions: [],
  isLoading: false,
  error: null,
  
  fetchTransactions: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await transactionService.getAll();
      set({ transactions: response.data, isLoading: false });
    } catch (error) {
      set({ error: error.message, isLoading: false });
    }
  },
  
  // outros métodos...
}));
```

### 6.3. Validação de Formulários (Zod + React Hook Form)
```typescript
const transactionSchema = z.object({
  amount: z.number().min(0.01, "Valor deve ser maior que zero"),
  description: z.string().min(1, "Descrição é obrigatória"),
  categoryId: z.string().uuid("Categoria inválida"),
  date: z.date(),
});

type TransactionForm = z.infer<typeof transactionSchema>;

export function TransactionForm() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm<TransactionForm>({
    resolver: zodResolver(transactionSchema)
  });
  
  const onSubmit = async (data: TransactionForm) => {
    try {
      await transactionService.create(data);
      toast.success("Transação criada com sucesso!");
    } catch (error) {
      toast.error("Erro ao criar transação");
    }
  };
  
  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      {/* Campos do formulário */}
    </form>
  );
}
```

---

## 7. REGRAS DE SEGURANÇA E COMPLIANCE

### 7.1. Autenticação e Autorização
- **JWT** com expiração de 15 minutos
- **Refresh Token** com expiração de 7 dias
- **2FA** obrigatório para operações sensíveis
- **OAuth2** com Google e Facebook
- **Rate Limiting**: 100 requests/min por IP

### 7.2. Dados Sensíveis
- **Criptografia AES-256** para tokens Open Banking
- **bcrypt** com salt 12+ para senhas
- **Mascaramento** de dados sensíveis em logs
- **HTTPS** obrigatório em produção
- **CORS** configurado apenas para domínios autorizados

### 7.3. Open Banking Compliance
- Tokens **SEMPRE** criptografados no banco
- **Renovação automática** de tokens
- **Logs de auditoria** para todas as operações
- **Timeout** de 30 segundos para APIs bancárias
- **Retry logic** com backoff exponencial

---

## 8. REGRAS DE PERFORMANCE

### 8.1. Backend Performance
- **Connection Pool**: 20 conexões máximo
- **Cache Redis**: TTL de 5 minutos para dados frequentes
- **Paginação**: Máximo 50 itens por página
- **Índices** obrigatórios conforme Database Schema
- **Query optimization**: N+1 queries proibidas

### 8.2. Frontend Performance
- **Bundle size**: < 500KB inicial
- **Lazy loading**: Componentes e rotas
- **Memoização**: React.memo, useMemo, useCallback
- **Virtualization**: Para listas > 100 itens
- **Image optimization**: WebP + lazy loading

---

## 9. REGRAS DE TESTES ORIENTADOS A REGRAS DE NEGÓCIO

### 9.1. Princípios Fundamentais ⚠️ OBRIGATÓRIO

#### 9.1.1. Testes Devem Validar Comportamentos, Não Implementação
```java
// ❌ ERRADO - Testa implementação
@Test
void shouldCallRepositorySaveMethod() {
    transactionService.createTransaction(input);
    verify(transactionRepository).save(any(Transaction.class));
}

// ✅ CORRETO - Testa regra de negócio
@Test
void shouldCreateTransactionWithCorrectCalculatedFields() {
    // Given
    var input = CreateTransactionRequest.builder()
        .amount(new BigDecimal("100.50"))
        .description("Compra supermercado")
        .categoryId("groceries-id")
        .build();
    
    // When
    var result = transactionService.createTransaction(input);
    
    // Then
    assertThat(result.getAmount()).isEqualTo(new BigDecimal("100.50"));
    assertThat(result.getType()).isEqualTo(TransactionType.EXPENSE);
    assertThat(result.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    assertThat(result.getCreatedAt()).isNotNull();
}
```

#### 9.1.2. Testes Devem Ser Resilientes a Mudanças de Implementação
```java
// ✅ CORRETO - Testa regra de negócio específica
@Test
void shouldRejectTransactionWhenAmountIsZero() {
    // Given
    var input = CreateTransactionRequest.builder()
        .amount(BigDecimal.ZERO)
        .description("Transação inválida")
        .build();
    
    // When & Then
    assertThatThrownBy(() -> transactionService.createTransaction(input))
        .isInstanceOf(BusinessException.class)
        .hasMessage("Valor da transação deve ser maior que zero");
}

@Test
void shouldRejectTransactionWhenDescriptionIsEmpty() {
    // Given
    var input = CreateTransactionRequest.builder()
        .amount(new BigDecimal("50.00"))
        .description("")
        .build();
    
    // When & Then
    assertThatThrownBy(() -> transactionService.createTransaction(input))
        .isInstanceOf(BusinessException.class)
        .hasMessage("Descrição da transação é obrigatória");
}
```

### 9.2. Padrões de Teste por Domínio

#### 9.2.1. Testes de Categorização Automática
```java
@Test
void shouldCategorizeSupermarketTransactionAsGroceries() {
    // Given
    var transaction = Transaction.builder()
        .description("SUPERMERCADO EXTRA")
        .merchantName("Extra Supermercados")
        .amount(new BigDecimal("85.50"))
        .build();
    
    // When
    var category = categorizationService.categorizeTransaction(transaction);
    
    // Then
    assertThat(category.getName()).isEqualTo("Alimentação");
    assertThat(category.getType()).isEqualTo(CategoryType.EXPENSE);
}

@Test
void shouldLearnFromUserCategorization() {
    // Given
    var transaction = createTransaction("PADARIA NOVA", new BigDecimal("15.00"));
    var userCategory = createCategory("Alimentação", CategoryType.EXPENSE);
    
    // When
    categorizationService.learnFromUserChoice(transaction, userCategory);
    
    // Then - Próxima transação similar deve ser categorizada automaticamente
    var newTransaction = createTransaction("PADARIA CENTRAL", new BigDecimal("12.00"));
    var suggestedCategory = categorizationService.categorizeTransaction(newTransaction);
    
    assertThat(suggestedCategory.getName()).isEqualTo("Alimentação");
}
```

#### 9.2.2. Testes de Validação de Open Banking
```java
@Test
void shouldRefreshTokenWhenExpired() {
    // Given
    var expiredToken = "expired_token";
    var user = createUserWithBankAccount(expiredToken, LocalDateTime.now().minusHours(1));
    
    // When
    var transactions = openBankingService.syncTransactions(user.getId());
    
    // Then
    assertThat(transactions).isNotEmpty();
    var updatedUser = userRepository.findById(user.getId()).orElseThrow();
    assertThat(updatedUser.getBankAccounts().get(0).getTokenExpiresAt())
        .isAfter(LocalDateTime.now());
}

@Test
void shouldHandleOpenBankingServiceUnavailable() {
    // Given
    var user = createUserWithBankAccount();
    mockOpenBankingServiceDown();
    
    // When
    var result = openBankingService.syncTransactions(user.getId());
    
    // Then
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getErrorCode()).isEqualTo("OPEN_BANKING_UNAVAILABLE");
    // Verificar que transações locais ainda funcionam
    var localTransactions = transactionService.getUserTransactions(user.getId());
    assertThat(localTransactions).isNotNull();
}
```

#### 9.2.3. Testes de Regras de Negócio Financeiras
```java
@Test
void shouldCalculateMonthlySpendingCorrectly() {
    // Given
    var user = createUser();
    createTransactionForUser(user, "2024-01-15", new BigDecimal("100.00"), EXPENSE);
    createTransactionForUser(user, "2024-01-20", new BigDecimal("50.00"), EXPENSE);
    createTransactionForUser(user, "2024-01-25", new BigDecimal("200.00"), REVENUE);
    
    // When
    var monthlyReport = reportService.getMonthlyReport(user.getId(), 2024, 1);
    
    // Then
    assertThat(monthlyReport.getTotalExpenses()).isEqualTo(new BigDecimal("150.00"));
    assertThat(monthlyReport.getTotalRevenue()).isEqualTo(new BigDecimal("200.00"));
    assertThat(monthlyReport.getNetAmount()).isEqualTo(new BigDecimal("50.00"));
}

@Test
void shouldTriggerBudgetAlertWhenLimitExceeded() {
    // Given
    var user = createUser();
    var category = createCategory("Alimentação", EXPENSE);
    createBudgetLimit(user, category, new BigDecimal("500.00"));
    
    // When - Adicionar transação que excede o orçamento
    var transaction = createTransactionForUser(user, category, new BigDecimal("600.00"));
    
    // Then
    var alerts = alertService.getActiveAlerts(user.getId());
    assertThat(alerts).hasSize(1);
    assertThat(alerts.get(0).getType()).isEqualTo(AlertType.BUDGET_EXCEEDED);
    assertThat(alerts.get(0).getMessage()).contains("Orçamento de Alimentação excedido");
}
```

### 9.3. Testes Frontend - Regras de Negócio

#### 9.3.1. Validação de Formulários
```typescript
// ✅ CORRETO - Testa regras de validação
describe('TransactionForm - Business Rules', () => {
  it('should prevent negative amounts', async () => {
    render(<TransactionForm />);
    
    const amountInput = screen.getByLabelText(/valor/i);
    const submitButton = screen.getByRole('button', { name: /salvar/i });
    
    fireEvent.change(amountInput, { target: { value: '-50.00' } });
    fireEvent.click(submitButton);
    
    await waitFor(() => {
      expect(screen.getByText(/valor deve ser positivo/i)).toBeInTheDocument();
    });
  });
  
  it('should format currency input correctly', async () => {
    render(<TransactionForm />);
    
    const amountInput = screen.getByLabelText(/valor/i);
    
    fireEvent.change(amountInput, { target: { value: '12345' } });
    fireEvent.blur(amountInput);
    
    await waitFor(() => {
      expect(amountInput).toHaveValue('R$ 123,45');
    });
  });
});
```

#### 9.3.2. Comportamento de Estado
```typescript
describe('TransactionStore - Business Logic', () => {
  it('should update balance when transaction is created', async () => {
    const { result } = renderHook(() => useTransactionStore());
    
    // Given - Estado inicial
    expect(result.current.balance).toBe(0);
    
    // When - Criar transação
    await act(async () => {
      await result.current.addTransaction({
        amount: 100,
        type: 'revenue',
        description: 'Salary'
      });
    });
    
    // Then - Saldo deve ser atualizado
    expect(result.current.balance).toBe(100);
    expect(result.current.transactions).toHaveLength(1);
  });
  
  it('should prevent duplicate transactions', async () => {
    const { result } = renderHook(() => useTransactionStore());
    
    const transaction = {
      externalId: 'bank-tx-123',
      amount: 50,
      type: 'expense',
      description: 'Duplicate test'
    };
    
    // When - Tentar adicionar a mesma transação duas vezes
    await act(async () => {
      await result.current.addTransaction(transaction);
      await result.current.addTransaction(transaction);
    });
    
    // Then - Deve ter apenas uma transação
    expect(result.current.transactions).toHaveLength(1);
    expect(result.current.error).toBe('Transação já existe');
  });
});
```

### 9.4. Estrutura de Testes Obrigatória

#### 9.4.1. Organização de Testes
```
src/test/java/com/monex/
├── unit/                          # Testes unitários
│   ├── service/                   # Testes de regras de negócio
│   │   ├── TransactionServiceTest.java
│   │   ├── CategorizationServiceTest.java
│   │   └── OpenBankingServiceTest.java
│   ├── controller/                # Testes de API contracts
│   └── util/                      # Testes de utilitários
├── integration/                   # Testes de integração
│   ├── repository/                # Testes de acesso a dados
│   ├── external/                  # Testes de APIs externas
│   └── end-to-end/               # Testes e2e
└── fixtures/                      # Dados de teste
    ├── TestDataBuilder.java
    └── TransactionFixtures.java
```

#### 9.4.2. Builders de Teste (Test Data Builders)
```java
public class TransactionTestDataBuilder {
    private String description = "Default transaction";
    private BigDecimal amount = new BigDecimal("100.00");
    private TransactionType type = TransactionType.EXPENSE;
    private String userId;
    private String categoryId;
    
    public static TransactionTestDataBuilder aTransaction() {
        return new TransactionTestDataBuilder();
    }
    
    public TransactionTestDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public TransactionTestDataBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
    
    public TransactionTestDataBuilder forUser(String userId) {
        this.userId = userId;
        return this;
    }
    
    public Transaction build() {
        return Transaction.builder()
            .description(description)
            .amount(amount)
            .type(type)
            .userId(userId)
            .categoryId(categoryId)
            .build();
    }
}

// Uso nos testes
@Test
void shouldCreateExpenseTransaction() {
    // Given
    var transaction = aTransaction()
        .withDescription("Compra supermercado")
        .withAmount(new BigDecimal("85.50"))
        .forUser("user-123")
        .build();
    
    // When & Then...
}
```

### 9.5. Métricas de Qualidade Obrigatórias

#### 9.5.1. Cobertura e Qualidade
- **Cobertura mínima**: 80% de linhas de código
- **Cobertura de branches**: 70% mínimo
- **Testes de regras críticas**: 100% (transações, autenticação, Open Banking)
- **Mutation testing**: 70% de mutantes mortos (usar PIT)

#### 9.5.2. Tipos de Teste Obrigatórios
```java
// 1. Testes de Cenários Felizes (Happy Path)
@Test
void shouldCreateValidTransaction() { /* ... */ }

// 2. Testes de Cenários de Erro (Edge Cases)
@Test
void shouldRejectInvalidTransaction() { /* ... */ }

// 3. Testes de Boundary Conditions
@Test
void shouldHandleMinimumTransactionAmount() { /* ... */ }

// 4. Testes de Regras de Negócio Específicas
@Test
void shouldApplyCorrectCategoryBasedOnMerchant() { /* ... */ }

// 5. Testes de Integração com Dependências
@Test
void shouldHandleExternalServiceFailure() { /* ... */ }
```

### 9.6. Automação de Testes

#### 9.6.1. Pipeline de Testes
```yaml
# .github/workflows/tests.yml
name: Quality Gate

on: [push, pull_request]

jobs:
  unit-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Run Unit Tests
        run: ./mvnw test
      - name: Run Mutation Tests
        run: ./mvnw org.pitest:pitest-maven:mutationCoverage
      - name: Quality Gate
        run: |
          if [ $(./mvnw jacoco:report | grep -o '[0-9]*%' | head -1 | sed 's/%//') -lt 80 ]; then
            echo "Coverage below 80%"
            exit 1
          fi
```

#### 9.6.2. Testes de Regressão Automáticos
```java
@Test
@Tag("regression")
void shouldMaintainBackwardsCompatibilityInAPI() {
    // Testes que garantem que mudanças não quebram funcionalidades existentes
    // Executados em toda mudança de API
}

@Test
@Tag("critical")
void shouldProcessOpenBankingDataCorrectly() {
    // Testes críticos que devem sempre passar
    // Executados em deploy para produção
}
```

---

## 10. REGRAS DE TESTES

### 10.1. Backend (JUnit + TestContainers)
```java
@SpringBootTest
@Testcontainers
class TransactionServiceTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("monex_test")
            .withUsername("test")
            .withPassword("test");
    
    @Test
    void shouldCreateTransaction() {
        // Given
        var input = TransactionInput.builder()
                .amount(BigDecimal.valueOf(100.00))
                .description("Test transaction")
                .build();
        
        // When
        var result = transactionService.create(input);
        
        // Then
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(100.00));
    }
}
```

### 10.2. Frontend (Vitest + Testing Library)
```typescript
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { TransactionForm } from './TransactionForm';

describe('TransactionForm', () => {
  it('should create transaction on valid submit', async () => {
    render(<TransactionForm />);
    
    fireEvent.change(screen.getByLabelText(/valor/i), {
      target: { value: '100.00' }
    });
    
    fireEvent.change(screen.getByLabelText(/descrição/i), {
      target: { value: 'Test transaction' }
    });
    
    fireEvent.click(screen.getByRole('button', { name: /salvar/i }));
    
    await waitFor(() => {
      expect(screen.getByText(/sucesso/i)).toBeInTheDocument();
    });
  });
});
```

---

## 11. COMANDOS E SCRIPTS OBRIGATÓRIOS

### 11.1. Backend (Maven)
```bash
# Desenvolvimento
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Testes
./mvnw test
./mvnw test -Dtest=TransactionServiceTest

# Build
./mvnw clean package -DskipTests

# Docker
docker-compose up -d mysql redis
```

### 11.2. Frontend (npm/yarn)
```bash
# Desenvolvimento
npm run dev

# Build
npm run build
npm run preview

# Testes
npm run test
npm run test:coverage

# Linting
npm run lint
npm run type-check
```

---

## 12. PIPELINE CI/CD OBRIGATÓRIO

### 12.1. GitHub Actions Backend
```yaml
name: Backend CI/CD
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
      - run: ./mvnw test
      - run: ./mvnw verify
```

### 12.2. GitHub Actions Frontend
```yaml
name: Frontend CI/CD
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: '18'
      - run: npm ci
      - run: npm run type-check
      - run: npm run lint
      - run: npm run test
      - run: npm run build
```

---

## 13. REGRAS DE QUALIDADE E MONITORAMENTO

### 13.1. Métricas Obrigatórias
- **Response time**: < 500ms (95th percentile)
- **Uptime**: > 99.9%
- **Error rate**: < 0.1%
- **Code coverage**: > 80%
- **Bundle size**: < 500KB

### 13.2. Alertas Obrigatórios
- Response time > 1s
- Error rate > 1%
- Memory usage > 80%
- Disk usage > 85%
- Failed logins > 10/min

---

## 14. REGRAS DE CONDUTA FINAL

### ⚠️ OBRIGATÓRIAS - NUNCA QUEBRAR:

1. **SEMPRE** seguir o schema de banco definido em `Database_Schema.md`
2. **SEMPRE** implementar validação completa (backend + frontend)
3. **SEMPRE** usar tipos TypeScript no frontend
4. **SEMPRE** implementar tratamento de erro
5. **SEMPRE** seguir design system exatamente
6. **SEMPRE** implementar loading states
7. **SEMPRE** fazer logs de auditoria para operações sensíveis
8. **SEMPRE** criptografar dados sensíveis
9. **SEMPRE** implementar testes que validem regras de negócio
10. **SEMPRE** documentar APIs com OpenAPI
11. **SEMPRE** escrever testes que validem comportamentos, não implementação
12. **SEMPRE** criar testes resilientes a mudanças de código

### 🚫 PROIBIDO - NUNCA FAZER:

1. **NUNCA** expor dados sensíveis em logs
2. **NUNCA** commitar secrets no código
3. **NUNCA** fazer queries N+1
4. **NUNCA** ignorar validação de entrada
5. **NUNCA** quebrar responsividade mobile
6. **NUNCA** usar componentes sem TypeScript
7. **NUNCA** armazenar senhas em plain text
8. **NUNCA** fazer deploy sem testes passando
9. **NUNCA** ignorar erros silenciosamente
10. **NUNCA** implementar sem considerar performance
11. **NUNCA** escrever testes apenas para cobertura
12. **NUNCA** criar testes que dependem de implementação específica

---

**🎯 IMPORTANTE**: Este documento é a **única fonte da verdade** para o desenvolvimento do Monex. Qualquer desvio deve ser justificado tecnicamente e documentado. O sucesso do projeto depende da consistência e qualidade implementada seguindo estas diretrizes.

**📚 Documentos de Referência**:
- `docs/README.md` - Índice e resumo
- `docs/PRD.md` - Requisitos do produto  
- `docs/System_Architecture.md` - Arquitetura técnica
- `docs/UI_Kit_Design_System.md` - Design system
- `docs/Database_Schema.md` - Schema do banco 