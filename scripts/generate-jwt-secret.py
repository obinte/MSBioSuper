#!/usr/bin/env python3

"""
============================================
Script para Generar Clave Secreta JWT
MSBioSuper - Java 21 Migration
============================================

Este script genera una clave secreta criptográficamente segura
para usar con JWT (JSON Web Tokens)

Uso:
    python scripts/generate-jwt-secret.py [bits]

Ejemplos:
    python scripts/generate-jwt-secret.py        # Genera clave de 256 bits (por defecto)
    python scripts/generate-jwt-secret.py 512    # Genera clave de 512 bits

============================================
"""

import secrets
import base64
import sys
from datetime import datetime

# Colores ANSI
class Colors:
    RED = '\033[0;31m'
    GREEN = '\033[0;32m'
    YELLOW = '\033[1;33m'
    BLUE = '\033[0;34m'
    NC = '\033[0m'  # No Color

def print_banner():
    """Imprime el banner del script"""
    print(f"{Colors.BLUE}")
    print("╔════════════════════════════════════════════════════════════╗")
    print("║         Generador de Clave Secreta JWT                    ║")
    print("║         MSBioSuper - Java 21 Migration                    ║")
    print("╚════════════════════════════════════════════════════════════╝")
    print(f"{Colors.NC}")

def generate_jwt_secret(bits=256):
    """
    Genera una clave secreta JWT criptográficamente segura
    
    Args:
        bits: Número de bits de la clave (mínimo 256)
    
    Returns:
        str: Clave secreta en formato Base64
    """
    if bits < 256:
        raise ValueError("La clave debe tener al menos 256 bits")
    
    bytes_count = bits // 8
    secret_bytes = secrets.token_bytes(bytes_count)
    secret_base64 = base64.b64encode(secret_bytes).decode('utf-8')
    
    return secret_base64

def print_instructions(jwt_secret):
    """Imprime las instrucciones de uso de la clave"""
    print(f"{Colors.BLUE}╔════════════════════════════════════════════════════════════╗{Colors.NC}")
    print(f"{Colors.BLUE}║ INSTRUCCIONES DE USO                                      ║{Colors.NC}")
    print(f"{Colors.BLUE}╚════════════════════════════════════════════════════════════╝{Colors.NC}")
    print()
    print(f"{Colors.YELLOW}1. Configurar en archivo .env:{Colors.NC}")
    print(f"   {Colors.GREEN}JWT_SECRET={jwt_secret}{Colors.NC}")
    print()
    print(f"{Colors.YELLOW}2. Configurar como variable de entorno (Linux/macOS):{Colors.NC}")
    print(f"   {Colors.GREEN}export JWT_SECRET=\"{jwt_secret}\"{Colors.NC}")
    print()
    print(f"{Colors.YELLOW}3. Configurar como variable de entorno (Windows PowerShell):{Colors.NC}")
    print(f"   {Colors.GREEN}$env:JWT_SECRET=\"{jwt_secret}\"{Colors.NC}")
    print()
    print(f"{Colors.YELLOW}4. Configurar en Docker Compose:{Colors.NC}")
    print(f"   {Colors.GREEN}environment:{Colors.NC}")
    print(f"   {Colors.GREEN}  - JWT_SECRET={jwt_secret}{Colors.NC}")
    print()
    print(f"{Colors.YELLOW}5. Configurar en Kubernetes Secret:{Colors.NC}")
    print(f"   {Colors.GREEN}kubectl create secret generic jwt-secret \\{Colors.NC}")
    print(f"   {Colors.GREEN}  --from-literal=JWT_SECRET=\"{jwt_secret}\"{Colors.NC}")
    print()

def print_security_warnings():
    """Imprime advertencias de seguridad"""
    print(f"{Colors.BLUE}╔════════════════════════════════════════════════════════════╗{Colors.NC}")
    print(f"{Colors.BLUE}║ ADVERTENCIAS DE SEGURIDAD                                 ║{Colors.NC}")
    print(f"{Colors.BLUE}╚════════════════════════════════════════════════════════════╝{Colors.NC}")
    print()
    print(f"{Colors.RED}⚠️  IMPORTANTE:{Colors.NC}")
    print("   • NO commitear esta clave en Git")
    print("   • Usar claves diferentes para cada entorno (dev, test, prod)")
    print("   • Rotar claves cada 90 días")
    print("   • Limitar acceso a la clave (principio de mínimo privilegio)")
    print("   • Usar gestor de secretos en producción (Vault, AWS Secrets Manager)")
    print()

def save_to_file(jwt_secret):
    """
    Guarda la clave en un archivo
    
    Args:
        jwt_secret: Clave secreta a guardar
    """
    response = input(f"{Colors.YELLOW}¿Desea guardar la clave en un archivo? (s/N): {Colors.NC}")
    
    if response.lower() in ['s', 'si', 'sí', 'y', 'yes']:
        timestamp = datetime.now().strftime("%Y%m%d-%H%M%S")
        filename = f"jwt-secret-{timestamp}.txt"
        
        with open(filename, 'w') as f:
            f.write(jwt_secret)
        
        # Establecer permisos restrictivos (solo en Unix)
        try:
            import os
            os.chmod(filename, 0o600)
            print(f"{Colors.GREEN}✅ Clave guardada en: {filename}{Colors.NC}")
            print(f"{Colors.YELLOW}   Permisos establecidos: 600 (solo lectura para propietario){Colors.NC}")
        except Exception:
            print(f"{Colors.GREEN}✅ Clave guardada en: {filename}{Colors.NC}")
            print(f"{Colors.YELLOW}   Nota: Establecer permisos manualmente en Windows{Colors.NC}")
        
        print(f"{Colors.RED}   ⚠️  Eliminar este archivo después de configurar la clave{Colors.NC}")

def main():
    """Función principal"""
    print_banner()
    
    # Obtener bits desde argumentos de línea de comandos
    bits = 256
    if len(sys.argv) > 1:
        try:
            bits = int(sys.argv[1])
        except ValueError:
            print(f"{Colors.RED}❌ Error: El argumento debe ser un número entero{Colors.NC}")
            print(f"{Colors.YELLOW}   Uso: python {sys.argv[0]} [bits]{Colors.NC}")
            print(f"{Colors.YELLOW}   Ejemplo: python {sys.argv[0]} 512{Colors.NC}")
            sys.exit(1)
    
    # Validar bits
    if bits < 256:
        print(f"{Colors.RED}❌ Error: La clave debe tener al menos 256 bits{Colors.NC}")
        print(f"{Colors.YELLOW}   Uso: python {sys.argv[0]} [bits]{Colors.NC}")
        print(f"{Colors.YELLOW}   Ejemplo: python {sys.argv[0]} 512{Colors.NC}")
        sys.exit(1)
    
    bytes_count = bits // 8
    
    print(f"{Colors.BLUE}📊 Configuración:{Colors.NC}")
    print(f"   Bits: {Colors.GREEN}{bits}{Colors.NC}")
    print(f"   Bytes: {Colors.GREEN}{bytes_count}{Colors.NC}")
    print()
    
    # Generar clave
    print(f"{Colors.BLUE}🔐 Generando clave secreta...{Colors.NC}")
    jwt_secret = generate_jwt_secret(bits)
    
    # Mostrar resultado
    print()
    print(f"{Colors.GREEN}✅ Clave generada exitosamente!{Colors.NC}")
    print()
    print(f"{Colors.BLUE}╔════════════════════════════════════════════════════════════╗{Colors.NC}")
    print(f"{Colors.BLUE}║ CLAVE SECRETA JWT                                         ║{Colors.NC}")
    print(f"{Colors.BLUE}╚════════════════════════════════════════════════════════════╝{Colors.NC}")
    print()
    print(f"{Colors.GREEN}{jwt_secret}{Colors.NC}")
    print()
    
    # Instrucciones
    print_instructions(jwt_secret)
    
    # Advertencias de seguridad
    print_security_warnings()
    
    # Guardar en archivo (opcional)
    save_to_file(jwt_secret)
    
    print()
    print(f"{Colors.GREEN}✨ Proceso completado{Colors.NC}")
    print()

if __name__ == "__main__":
    main()
