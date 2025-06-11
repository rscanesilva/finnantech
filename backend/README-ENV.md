# 🔐 Configuração do Arquivo .env

Este diretório contém um arquivo `.env` com as variáveis de ambiente necessárias para o funcionamento do backend.

## 📋 Status Atual

✅ **Arquivo `.env` criado** com as seguintes variáveis:
- `JWT_SECRET` - Chave secreta para assinatura JWT
- `GOOGLE_CLIENT_ID` - ID do cliente Google OAuth
- `GOOGLE_CLIENT_SECRET` - Secret do cliente Google OAuth  
- `FACEBOOK_CLIENT_ID` - ID do cliente Facebook OAuth
- `FACEBOOK_CLIENT_SECRET` - Secret do cliente Facebook OAuth
- `SPRING_PROFILES_ACTIVE` - Perfil ativo da aplicação
- `SERVER_PORT` - Porta do servidor

## 🚀 Como Usar

### 1. O arquivo `.env` já está configurado
O arquivo já foi criado na pasta `backend/` com os valores atuais.

### 2. Iniciar a aplicação
```bash
# Na pasta backend/
mvn spring-boot:run
```

### 3. Verificar se está funcionando
- Aplicação disponível em: `http://localhost:8080`
- Health check: `http://localhost:8080/api/actuator/health`
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`

## ⚠️ Importante

- **O arquivo `.env` NÃO está no Git** (protegido pelo `.gitignore`)
- **Mantenha suas credenciais seguras**
- **Não compartilhe o arquivo `.env` publicamente**

## 🔧 Customização

Para usar suas próprias credenciais OAuth:

1. **Google OAuth:**
   - Acesse [Google Cloud Console](https://console.developers.google.com/)
   - Crie/configure um projeto OAuth
   - Substitua os valores no `.env`:
   ```bash
   GOOGLE_CLIENT_ID=seu-client-id.apps.googleusercontent.com
   GOOGLE_CLIENT_SECRET=seu-client-secret
   ```

2. **Facebook OAuth (opcional):**
   - Acesse [Facebook Developers](https://developers.facebook.com/)
   - Configure sua aplicação
   - Atualize no `.env`:
   ```bash
   FACEBOOK_CLIENT_ID=seu-app-id
   FACEBOOK_CLIENT_SECRET=seu-app-secret
   ```

3. **JWT Secret:**
   ```bash
   JWT_SECRET=sua-chave-super-secreta-minimo-256-bits
   ```

## 🐳 Docker (Futuro)

Quando usar Docker, as variáveis podem ser passadas via:
```bash
docker run -e JWT_SECRET=valor -e GOOGLE_CLIENT_ID=valor ...
```

## 🔍 Verificando Configuração

Para verificar se as variáveis estão sendo carregadas:
```bash
# Verificar logs na inicialização
mvn spring-boot:run | grep -i "oauth\|jwt"
```

## 🆘 Troubleshooting

**Erro: "Could not resolve placeholder"**
- Certifique-se que o arquivo `.env` existe na pasta `backend/`
- Verifique se todas as variáveis obrigatórias estão definidas

**OAuth não funciona:**
- Verifique as URLs de callback configuradas nos provedores
- Confirme que os Client IDs e Secrets estão corretos 