# Regras Consolidadas do Projeto Monex

**Sistema de Controle Financeiro com Open Banking**

Este documento consolida e padroniza todas as regras de desenvolvimento para o projeto Monex, definindo a arquitetura tecnológica recomendada e as diretrizes de implementação.

## 1. Visão Geral da Arquitetura

### 1.1. Decisão Arquitetural
**Arquitetura Recomendada: Backend Java + Frontend React (Separados)**

**Justificativa:**
- **Performance Superior**: SPA (Single Page Application) oferece melhor experiência do usuário
- **Escalabilidade**: Frontend e backend podem escalar independentemente
- **Design System Consistente**: Framework de componentes React permite UI/UX superior
- **Manutenibilidade**: Código mais organizado e facilidade para desenvolvimento com IA
- **Flexibilidade**: Permite futuras expansões (app mobile, múltiplos frontends)

### 1.2. Stack Tecnológica Definida

#### Backend (Java Spring Boot)
```
- Java 17+
- Spring Boot 3.2+
- Spring Security 6+ (JWT)
- Spring Data JPA
- MySQL 8.0+
- Spring Cloud OpenFeign (APIs externas)
- Spring Boot Actuator (monitoramento)
- Maven (build)
```

#### Frontend (React - Separado)
```
- React 18+
- TypeScript
- Next.js 14+ (SSR/SSG)
- Tailwind CSS (estilização)
- Shadcn/ui (componentes)
- React Hook Form (formulários)
- Zustand ou Redux Toolkit (estado global)
- React Query/TanStack Query (cache API)
- Axios (HTTP client)
```

## 2. Estrutura de Projeto

### 2.1. Backend (monex-api)
```
monex-api/
├── src/main/java/com/monex/
│   ├── config/           # Configurações (Security, CORS, etc)
│   ├── controller/       # Controllers REST
│   ├── service/          # Lógica de negócio
│   ├── repository/       # Acesso a dados (JPA)
│   ├── entity/           # Entidades JPA
│   ├── dto/              # Data Transfer Objects
│   ├── security/         # JWT, autenticação
│   ├── openbanking/      # Integração Open Banking
│   ├── exception/        # Tratamento de exceções
│   └── util/             # Utilitários
├── src/main/resources/
│   ├── application.yml   # Configurações
│   └── db/migration/     # Scripts Flyway
└── pom.xml
```

### 2.2. Frontend (monex-web)
```
monex-web/
├── src/
│   ├── app/              # Next.js App Router
│   ├── components/       # Componentes reutilizáveis
│   │   ├── ui/           # Componentes básicos (shadcn)
│   │   ├── forms/        # Formulários específicos
│   │   ├── layout/       # Layout components
│   │   └── charts/       # Gráficos e dashboards
│   ├── lib/              # Utilitários e configurações
│   ├── hooks/            # Custom hooks
│   ├── store/            # Estado global (Zustand)
│   ├── types/            # Tipos TypeScript
│   └── styles/           # Estilos globais
├── public/               # Arquivos estáticos
├── package.json
└── tailwind.config.js
```

## 3. Padrões de Desenvolvimento

### 3.1. Padrões Backend (Java Spring Boot)

#### Estrutura de Controller
```java
@RestController
@RequestMapping("/api/v1/transactions")
@PreAuthorize("hasRole('USER')")
@Validated
public class TransactionController {
    
    @GetMapping
    public ResponseEntity<PagedResponse<TransactionDTO>> getTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // implementação
    }
}
```

#### Estrutura de Service
```java
@Service
@Transactional
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;
    
    public TransactionService(TransactionRepository transactionRepository, 
                            CategoryService categoryService) {
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
    }
    
    public TransactionDTO createTransaction(CreateTransactionRequest request) {
        // implementação com validações
    }
}
```

#### Configuration Classes
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
            .build();
    }
}
```

### 3.2. Padrões Frontend (React + TypeScript)

#### Estrutura de Componentes
```typescript
interface TransactionCardProps {
  transaction: Transaction;
  onEdit: (id: string) => void;
  onDelete: (id: string) => void;
}

export const TransactionCard: React.FC<TransactionCardProps> = ({
  transaction,
  onEdit,
  onDelete
}) => {
  return (
    <Card className="w-full p-4">
      <CardHeader>
        <CardTitle>{transaction.description}</CardTitle>
      </CardHeader>
      <CardContent>
        {/* conteúdo */}
      </CardContent>
    </Card>
  );
};
```

#### Estado Global (Zustand)
```typescript
interface AuthStore {
  user: User | null;
  token: string | null;
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
}

export const useAuthStore = create<AuthStore>((set, get) => ({
  user: null,
  token: localStorage.getItem('token'),
  isAuthenticated: false,
  
  login: async (credentials) => {
    const response = await authService.login(credentials);
    set({ 
      user: response.user, 
      token: response.token, 
      isAuthenticated: true 
    });
  },
  
  logout: () => {
    localStorage.removeItem('token');
    set({ user: null, token: null, isAuthenticated: false });
  }
}));
```

#### Gerenciamento de Formulários
```typescript
const createTransactionSchema = z.object({
  description: z.string().min(1, "Descrição é obrigatória"),
  amount: z.number().positive("Valor deve ser positivo"),
  categoryId: z.string().uuid("Categoria inválida"),
  transactionDate: z.date()
});

type CreateTransactionForm = z.infer<typeof createTransactionSchema>;

export const CreateTransactionForm: React.FC = () => {
  const form = useForm<CreateTransactionForm>({
    resolver: zodResolver(createTransactionSchema)
  });
  
  const { mutate: createTransaction } = useMutation({
    mutationFn: transactionService.create,
    onSuccess: () => {
      toast.success("Transação criada com sucesso!");
      form.reset();
    }
  });
  
  // render form
};
```

## 4. Design System e UI/UX

### 4.1. Tokens de Design (Tailwind Config)
```javascript
module.exports = {
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#f0f9ff',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8'
        },
        success: {
          500: '#10b981',
          600: '#059669'
        },
        danger: {
          500: '#ef4444',
          600: '#dc2626'
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif']
      }
    }
  }
};
```

### 4.2. Componentes Base (Shadcn/ui)
- Button, Input, Card, Dialog, Sheet
- Form, Select, DatePicker, Checkbox
- Table, Pagination, Skeleton
- Toast, Alert, Badge, Avatar

### 4.3. Padrões de Responsividade
```css
/* Mobile First */
.container {
  @apply px-4 mx-auto;
}

/* Breakpoints */
sm: 640px   /* Mobile landscape */
md: 768px   /* Tablet */
lg: 1024px  /* Desktop */
xl: 1280px  /* Large desktop */
```

## 5. Integração e Comunicação

### 5.1. API REST (Backend → Frontend)
```typescript
// API Client Configuration
const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  timeout: 10000
});

apiClient.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Service Layer
export const transactionService = {
  getAll: (params: TransactionFilters): Promise<PagedResponse<Transaction>> =>
    apiClient.get('/api/v1/transactions', { params }).then(res => res.data),
    
  create: (data: CreateTransactionRequest): Promise<Transaction> =>
    apiClient.post('/api/v1/transactions', data).then(res => res.data),
    
  update: (id: string, data: UpdateTransactionRequest): Promise<Transaction> =>
    apiClient.put(`/api/v1/transactions/${id}`, data).then(res => res.data)
};
```

### 5.2. Tratamento de Erros
```typescript
// Frontend Error Handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      useAuthStore.getState().logout();
      router.push('/login');
    }
    
    const message = error.response?.data?.message || 'Erro inesperado';
    toast.error(message);
    
    return Promise.reject(error);
  }
);
```

```java
// Backend Exception Handling
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
```

## 6. Segurança

### 6.1. Autenticação JWT (Backend)
```java
@Component
public class JwtUtil {
    
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration}")
    private int jwtExpirationMs;
    
    public String generateToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
            .setSubject(userPrincipal.getId())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }
}
```

### 6.2. Proteção de Rotas (Frontend)
```typescript
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated } = useAuthStore();
  const router = useRouter();
  
  useEffect(() => {
    if (!isAuthenticated) {
      router.push('/login');
    }
  }, [isAuthenticated, router]);
  
  if (!isAuthenticated) {
    return <LoadingSpinner />;
  }
  
  return <>{children}</>;
};
```

## 7. Open Banking Integration

### 7.1. Configuração Spring (Backend)
```java
@Service
public class OpenBankingService {
    
    @Value("${openbanking.client-id}")
    private String clientId;
    
    @Value("${openbanking.base-url}")
    private String baseUrl;
    
    @Autowired
    private OpenBankingClient openBankingClient;
    
    public List<BankAccount> getUserAccounts(String userId) {
        // implementação da integração
    }
}

@FeignClient(name = "open-banking", url = "${openbanking.base-url}")
public interface OpenBankingClient {
    
    @GetMapping("/accounts")
    List<BankAccountResponse> getAccounts(
        @RequestHeader("Authorization") String authorization);
        
    @GetMapping("/accounts/{accountId}/transactions")
    List<TransactionResponse> getTransactions(
        @PathVariable String accountId,
        @RequestHeader("Authorization") String authorization);
}
```

## 8. Performance e Otimização

### 8.1. Caching (Backend)
```java
@EnableCaching
@Configuration
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager.Builder builder = RedisCacheManager
            .RedisCacheManagerBuilder
            .fromConnectionFactory(jedisConnectionFactory())
            .cacheDefaults(cacheConfiguration());
        return builder.build();
    }
}

@Service
public class CategoryService {
    
    @Cacheable(value = "categories", key = "#userId")
    public List<CategoryDTO> getUserCategories(String userId) {
        // implementação
    }
}
```

### 8.2. Query Optimization (Frontend)
```typescript
// React Query com cache inteligente
export const useTransactions = (filters: TransactionFilters) => {
  return useQuery({
    queryKey: ['transactions', filters],
    queryFn: () => transactionService.getAll(filters),
    staleTime: 5 * 60 * 1000, // 5 minutos
    cacheTime: 10 * 60 * 1000, // 10 minutos
    refetchOnWindowFocus: false
  });
};

// Virtualization para listas grandes
import { FixedSizeList as List } from 'react-window';

const TransactionList: React.FC = () => {
  const { data: transactions } = useTransactions();
  
  return (
    <List
      height={600}
      itemCount={transactions.length}
      itemSize={80}
      itemData={transactions}
    >
      {TransactionItem}
    </List>
  );
};
```

## 9. Deploy e DevOps

### 9.1. Docker Configuration

#### Backend Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/monex-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

#### Frontend Dockerfile
```dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production

COPY . .
RUN npm run build

FROM node:18-alpine AS runner
WORKDIR /app
COPY --from=builder /app/next.config.js ./
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json

EXPOSE 3000
CMD ["npm", "start"]
```

### 9.2. Docker Compose
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: monex
    ports:
      - "3306:3306"
    
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
      
  api:
    build: ./monex-api
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/monex
      SPRING_REDIS_HOST: redis
      
  web:
    build: ./monex-web
    ports:
      - "3000:3000"
    environment:
      NEXT_PUBLIC_API_URL: http://localhost:8080
    depends_on:
      - api
```

## 10. Comandos de Desenvolvimento

### 10.1. Backend (Maven)
```bash
# Desenvolvimento
mvn spring-boot:run

# Build
mvn clean package -DskipTests

# Testes
mvn test

# Docker build
docker build -t monex-api .
```

### 10.2. Frontend (npm/yarn)
```bash
# Desenvolvimento
npm run dev

# Build
npm run build

# Testes
npm run test

# Lint
npm run lint

# Type check
npm run type-check
```

## 11. Padrões de Qualidade

### 11.1. Code Style
- **Backend**: Google Java Style Guide
- **Frontend**: Prettier + ESLint (Airbnb config)
- **Commits**: Conventional Commits
- **Branching**: Git Flow

### 11.2. Testing Strategy
```java
// Backend - Unit Tests
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private TransactionService transactionService;
    
    @Test
    void shouldCreateTransaction() {
        // test implementation
    }
}
```

```typescript
// Frontend - Component Tests
import { render, screen } from '@testing-library/react';
import { TransactionCard } from './TransactionCard';

describe('TransactionCard', () => {
  it('should render transaction details', () => {
    const transaction = createMockTransaction();
    
    render(
      <TransactionCard 
        transaction={transaction}
        onEdit={jest.fn()}
        onDelete={jest.fn()}
      />
    );
    
    expect(screen.getByText(transaction.description)).toBeInTheDocument();
  });
});
```

## 12. Roadmap de Implementação

### Fase 1 - Fundação (4 semanas)
1. Setup inicial dos projetos
2. Configuração do banco de dados
3. Autenticação e autorização
4. CRUD básico de usuários

### Fase 2 - Core Features (6 semanas)
1. Gestão de transações
2. Sistema de categorias
3. Dashboard básico
4. Integração Open Banking

### Fase 3 - Features Avançadas (4 semanas)
1. Relatórios e gráficos
2. Transações recorrentes
3. Notificações
4. Otimizações de performance

### Fase 4 - Polimento (2 semanas)
1. Testes e2e
2. Ajustes de UI/UX
3. Documentação
4. Deploy em produção

---

**Este documento serve como referência única para todo o desenvolvimento do projeto Monex, garantindo consistência e qualidade em todas as etapas de implementação.** 