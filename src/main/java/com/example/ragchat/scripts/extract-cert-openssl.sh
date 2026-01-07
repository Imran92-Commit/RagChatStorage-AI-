#!/usr/bin/env bash
set -euo pipefail
HOST=${1:?Usage: $0 host [port]}
PORT=${2:-443}
OUT=${3:-server-chain.pem}
if ! command -v openssl >/dev/null 2>&1; then
  echo "openssl not found" >&2; exit 1
fi
( echo | openssl s_client -servername "$HOST" -connect "$HOST:$PORT" -showcerts ) 2>/dev/null | awk 'BEGIN{c=0} /BEGIN CERTIFICATE/{c=1} c; /END CERTIFICATE/{c=0}' > "$OUT"
echo "Saved certificate chain to $OUT"
