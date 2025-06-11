#!/bin/bash

# Script para carregar variáveis do arquivo .env e executar Spring Boot
# Uso: ./run-with-env.sh

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Finnantech Backend - Carregando variáveis do .env${NC}"

# Verificar se o arquivo .env existe
if [ ! -f ".env" ]; then
    echo -e "${RED}❌ Arquivo .env não encontrado na pasta backend/${NC}"
    echo -e "${YELLOW}💡 Crie o arquivo .env com as variáveis necessárias${NC}"
    exit 1
fi

# Carregar variáveis do arquivo .env
echo -e "${YELLOW}📋 Carregando variáveis do arquivo .env...${NC}"

# Carregar cada linha do .env como variável de ambiente
set -a  # Exportar automaticamente todas as variáveis
source .env
set +a  # Desabilitar export automático

# Mostrar variáveis carregadas (sem mostrar valores sensíveis)
echo -e "${GREEN}✅ Variáveis carregadas:${NC}"
echo "   - JWT_SECRET: [HIDDEN]"
echo "   - GOOGLE_CLIENT_ID: ${GOOGLE_CLIENT_ID:0:20}..."
echo "   - GOOGLE_CLIENT_SECRET: [HIDDEN]"
echo "   - FACEBOOK_CLIENT_ID: ${FACEBOOK_CLIENT_ID}"
echo "   - SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}"
echo "   - SERVER_PORT: ${SERVER_PORT}"

echo -e "${GREEN}🏃 Iniciando Spring Boot...${NC}"

# Executar Spring Boot com as variáveis carregadas
mvn spring-boot:run 