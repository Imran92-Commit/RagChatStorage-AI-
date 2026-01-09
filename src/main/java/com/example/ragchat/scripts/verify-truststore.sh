
#!/usr/bin/env bash
set -euo pipefail
STORE_PATH=${1:-src/main/resources/llm-truststore.jks}
PASS=${2:-mysecretpass}
if [ ! -f "$STORE_PATH" ]; then
  echo "Truststore not found: $STORE_PATH" >&2
  exit 1
fi
keytool -list -keystore "$STORE_PATH" -storepass "$PASS"
