# Configuração de Variáveis de Ambiente

Este projeto requer algumas variáveis de ambiente para funcionar corretamente. Por questões de segurança, essas credenciais não são incluídas no código fonte.

## Variáveis Obrigatórias

### JWT Configuration
```bash
JWT_SECRET=seu-jwt-secret-key-minimo-256-bits
```

### Google OAuth2 (Obrigatório para login social)
Para obter essas credenciais:
1. Acesse [Google Cloud Console](https://console.developers.google.com/)
2. Crie um novo projeto ou selecione um existente
3. Habilite a Google+ API
4. Crie credenciais OAuth 2.0
5. Configure a URL de redirect: `http://localhost:8080/oauth2/callback/google`

```bash
GOOGLE_CLIENT_ID=seu-google-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=seu-google-client-secret
```

## Variáveis Opcionais

### Facebook OAuth2 (Opcional)
```bash
FACEBOOK_CLIENT_ID=seu-facebook-app-id
FACEBOOK_CLIENT_SECRET=seu-facebook-app-secret
```

### Database (Opcional - padrão H2 em memória)
```bash
DB_URL=jdbc:postgresql://localhost:5432/finnantech
DB_USERNAME=seu-usuario-db
DB_PASSWORD=sua-senha-db
```

### Server Configuration (Opcional)
```bash
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=development
```

## Como Configurar

### Opção 1: Arquivo .env (Recomendado)
1. Crie um arquivo `.env` na raiz do projeto backend
2. Adicione as variáveis necessárias:
```bash
JWT_SECRET=minha-chave-secreta-super-segura-256-bits
GOOGLE_CLIENT_ID=123456789-abc.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=GOCSPX-abcdef123456
```

### Opção 2: Variáveis de Sistema
Configure as variáveis no seu sistema operacional:

**Linux/Mac:**
```bash
export JWT_SECRET="minha-chave-secreta"
export GOOGLE_CLIENT_ID="seu-client-id"
export GOOGLE_CLIENT_SECRET="seu-client-secret"
```

**Windows:**
```cmd
set JWT_SECRET=minha-chave-secreta
set GOOGLE_CLIENT_ID=seu-client-id
set GOOGLE_CLIENT_SECRET=seu-client-secret
```

### Opção 3: IDE Configuration
Configure as variáveis de ambiente na sua IDE (IntelliJ IDEA, VS Code, etc.)

## Segurança

⚠️ **IMPORTANTE**: Nunca commitei arquivos `.env` ou credenciais reais no Git!

- Arquivos `.env` estão no `.gitignore`
- Use valores diferentes para desenvolvimento e produção
- Mantenha suas credenciais seguras
- Rotacione chaves regularmente

## Testando a Configuração

Para verificar se as variáveis estão configuradas corretamente:

1. Inicie o backend: `mvn spring-boot:run`
2. Verifique os logs para confirmar que não há erros de configuração
3. Acesse `http://localhost:8080/api/actuator/health` para verificar o status

## Desenvolvimento Local Rápido

Para desenvolvimento local rápido, você pode usar valores fake para Google OAuth:
```bash
GOOGLE_CLIENT_ID=fake-client-id
GOOGLE_CLIENT_SECRET=fake-client-secret
```

Nota: Isso desabilitará o login social, mas manterá o login por email/senha funcionando. 