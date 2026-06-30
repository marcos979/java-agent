#!/usr/bin/env bash
# init.sh — Verificación e inicialización del entorno Java/Maven
#
# Este script lo ejecuta el agente al COMENZAR una sesión y antes de
# declarar cualquier tarea como `done`. Si falla, la sesión no debe avanzar.
#
# Salida esperada: códigos de salida claros y bloques marcados con [OK]/[FAIL].

set -u
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m'

ok()    { printf "${GREEN}[OK]${NC}    %s\n" "$1"; }
warn()  { printf "${YELLOW}[WARN]${NC}  %s\n" "$1"; }
fail()  { printf "${RED}[FAIL]${NC}  %s\n" "$1"; }

EXIT_CODE=0

echo "── 1. Verificando entorno ─────────────────────────────"

# ------------------------------------------------------------------
# Java disponible
# ------------------------------------------------------------------
if ! command -v java >/dev/null 2>&1; then
  fail "java no está instalado"
  exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -1 | grep -oP '\d+\.\d+\.\d+' | head -1)
if [ -z "$JAVA_VERSION" ]; then
  # Fallback: algunas distribuciones imprimen la versión en otro formato
  JAVA_VERSION=$(java -version 2>&1 | head -1 | grep -oP '\d+' | head -1)
fi
ok "java -> $JAVA_VERSION"

# Versión mínima Java 25
JAVA_MAJOR=$(echo "$JAVA_VERSION" | cut -d. -f1)
if [ -z "$JAVA_MAJOR" ] || [ "$JAVA_MAJOR" -lt 25 ]; then
  fail "Se requiere Java >= 25 (detectado: $JAVA_VERSION)"
  exit 1
fi
ok "Versión de Java compatible ($JAVA_MAJOR >= 25)"

# ------------------------------------------------------------------
# Maven disponible
# ------------------------------------------------------------------
if ! command -v mvn >/dev/null 2>&1; then
  fail "mvn (Maven) no está instalado"
  exit 1
fi
MVN_VERSION=$(mvn --version 2>&1 | head -1 | awk '{print $3}')
ok "mvn -> $MVN_VERSION"

echo ""
echo "── 2. Verificando archivos base del proyecto ──────────"

for f in README.md pom.xml .gitignore docs/architecture.md docs/conventions.md CHECKPOINTS.md; do
  if [ ! -f "$f" ]; then
    fail "Falta archivo base: $f"
    EXIT_CODE=1
  else
    ok "Existe $f"
  fi
done

echo ""
echo "── 3. Validando pom.xml ───────────────────────────────"

# Validar que pom.xml es XML bien formado
if command -v xmllint >/dev/null 2>&1; then
  if xmllint --noout pom.xml 2>/dev/null; then
    ok "pom.xml es XML válido"
  else
    fail "pom.xml no es XML bien formado"
    EXIT_CODE=1
  fi
else
  warn "xmllint no disponible — se omite validación XML"
fi

# Verificar propiedades mínimas: groupId, artifactId, version
for prop in groupId artifactId version; do
  if grep -q "<$prop>" pom.xml 2>/dev/null; then
    ok "Propiedad <$prop> encontrada"
  else
    fail "Falta propiedad <$prop> en pom.xml"
    EXIT_CODE=1
  fi
done

# Validar feature_list.json si existe
FEATURE_FILE="feature_list.json"
if [ -f "$FEATURE_FILE" ]; then
  python3 - <<'PY'
import json, sys
try:
    data = json.load(open("feature_list.json"))
    features = data.get("features", [])
    if not isinstance(features, list):
        print("[FAIL]  feature_list.json: la clave 'features' no es un array")
        sys.exit(1)
    valid = {"pending", "in_progress", "done", "blocked"}
    in_progress = [f for f in features if f.get("status") == "in_progress"]
    if len(in_progress) > 1:
        print(f"[FAIL]  Hay {len(in_progress)} features en in_progress (máximo 1)")
        sys.exit(1)
    for f in features:
        if f.get("status") not in valid:
            fid = f.get("id", "?")
            print(f"[FAIL]  Estado inválido en feature {fid}: {f.get('status')}")
            sys.exit(1)
    print(f"[OK]    feature_list.json válido ({len(features)} features)")
except json.JSONDecodeError as e:
    print(f"[FAIL]  feature_list.json no es JSON válido: {e}")
    sys.exit(1)
except Exception as e:
    print(f"[FAIL]  feature_list.json inválido: {e}")
    sys.exit(1)
PY

  if [ $? -ne 0 ]; then EXIT_CODE=1; fi
else
  warn "feature_list.json no encontrado — se omite validación de features"
fi

echo ""
echo "── 4. Ejecutando tests ────────────────────────────────"

if [ -d "src/test/java" ]; then
  if mvn test --batch-mode 2>&1; then
    ok "Todos los tests pasan"
  else
    fail "Hay tests rotos"
    EXIT_CODE=1
  fi
else
  warn "Directorio src/test/java/ no existe todavía"
fi

echo ""
echo "── 5. Resumen ─────────────────────────────────────────"

if [ $EXIT_CODE -eq 0 ]; then
  ok "Entorno listo. Puedes empezar a trabajar."
else
  fail "Entorno NO está listo. Resuelve los errores antes de avanzar."
fi

exit $EXIT_CODE
