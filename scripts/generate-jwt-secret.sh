#!/bin/bash

# ============================================
# Script para Generar Clave Secreta JWT
# MSBioSuper - Java 21 Migration
# ============================================
#
# Este script genera una clave secreta criptográficamente segura
# para usar con JWT (JSON Web Tokens)
#
# Uso:
#   ./scripts/generate-jwt-secret.sh [bits]
#
# Ejemplos:
#   ./scripts/generate-jwt-secret.sh        # Genera clave de 256 bits (por defecto)
#   ./scripts/generate-jwt-secret.sh 512    # Genera clave de 512 bits
#
# ============================================

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Banner
echo -e "${BLUE}"
echo "╔════════════════════════════════════════════════════════════╗"
echo "║         Generador de Clave Secreta JWT                    ║"
echo "║         MSBioSuper - Java 21 Migration                    ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo -e "${NC}"

# Parámetros
BITS=${1:-256}
BYTES=$((BITS / 8))

# Validar bits
if [ $BITS -lt 256 ]; then
    echo -e "${RED}❌ Error: La clave debe tener al menos 256 bits${NC}"
    echo -e "${YELLOW}   Uso: $0 [bits]${NC}"
    echo -e "${YELLOW}   Ejemplo: $0 512${NC}"
    exit 1
fi

echo -e "${BLUE}📊 Configuración:${NC}"
echo -e "   Bits: ${GREEN}$BITS${NC}"
echo -e "   Bytes: ${GREEN}$BYTES${NC}"
echo ""

# Verificar si OpenSSL está instalado
if ! command -v openssl &> /dev/null; then
    echo -e "${RED}❌ Error: OpenSSL no está instalado${NC}"
    echo -e "${YELLOW}   Instalar OpenSSL:${NC}"
    echo -e "   - macOS: brew install openssl"
    echo -e "   - Ubuntu/Debian: sudo apt-get install openssl"
    echo -e "   - CentOS/RHEL: sudo yum install openssl"
    exit 1
fi

# Generar clave
echo -e "${BLUE}🔐 Generando clave secreta...${NC}"
JWT_SECRET=$(openssl rand -base64 $BYTES)

# Mostrar resultado
echo ""
echo -e "${GREEN}✅ Clave generada exitosamente!${NC}"
echo ""
echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║ CLAVE SECRETA JWT                                         ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${GREEN}$JWT_SECRET${NC}"
echo ""

# Instrucciones
echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║ INSTRUCCIONES DE USO                                      ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}1. Configurar en archivo .env:${NC}"
echo -e "   ${GREEN}JWT_SECRET=$JWT_SECRET${NC}"
echo ""
echo -e "${YELLOW}2. Configurar como variable de entorno (Linux/macOS):${NC}"
echo -e "   ${GREEN}export JWT_SECRET=\"$JWT_SECRET\"${NC}"
echo ""
echo -e "${YELLOW}3. Configurar como variable de entorno (Windows PowerShell):${NC}"
echo -e "   ${GREEN}\$env:JWT_SECRET=\"$JWT_SECRET\"${NC}"
echo ""
echo -e "${YELLOW}4. Configurar en Docker Compose:${NC}"
echo -e "   ${GREEN}environment:${NC}"
echo -e "   ${GREEN}  - JWT_SECRET=$JWT_SECRET${NC}"
echo ""
echo -e "${YELLOW}5. Configurar en Kubernetes Secret:${NC}"
echo -e "   ${GREEN}kubectl create secret generic jwt-secret \\${NC}"
echo -e "   ${GREEN}  --from-literal=JWT_SECRET=\"$JWT_SECRET\"${NC}"
echo ""

# Advertencias de seguridad
echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║ ADVERTENCIAS DE SEGURIDAD                                 ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${RED}⚠️  IMPORTANTE:${NC}"
echo -e "   • NO commitear esta clave en Git"
echo -e "   • Usar claves diferentes para cada entorno (dev, test, prod)"
echo -e "   • Rotar claves cada 90 días"
echo -e "   • Limitar acceso a la clave (principio de mínimo privilegio)"
echo -e "   • Usar gestor de secretos en producción (Vault, AWS Secrets Manager)"
echo ""

# Guardar en archivo (opcional)
echo -e "${YELLOW}¿Desea guardar la clave en un archivo? (s/N):${NC} "
read -r SAVE_TO_FILE

if [[ $SAVE_TO_FILE =~ ^[Ss]$ ]]; then
    FILENAME="jwt-secret-$(date +%Y%m%d-%H%M%S).txt"
    echo "$JWT_SECRET" > "$FILENAME"
    chmod 600 "$FILENAME"
    echo -e "${GREEN}✅ Clave guardada en: $FILENAME${NC}"
    echo -e "${YELLOW}   Permisos establecidos: 600 (solo lectura para propietario)${NC}"
    echo -e "${RED}   ⚠️  Eliminar este archivo después de configurar la clave${NC}"
fi

echo ""
echo -e "${GREEN}✨ Proceso completado${NC}"
echo ""
