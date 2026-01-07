#!/usr/bin/env bash
set -euo pipefail
CERT_FILE=${1:-server-chain.pem}
STORE=${2:-llm-truststore.jks}
PASSWORD=${3:-changeit}
ALIAS=${4:-llm-root}

if ! command -v keytool >/dev/null 2>&1; then
  echo "keytool not found; install JDK" >&2; exit 1
fi
awk 'BEGIN{n=0} /-----BEGIN CERTIFICATE-----/{n++; file=sprintf("cert_%d.pem",n)} {print > file} /-----END CERTIFICATE-----/{close(file)}' "$CERT_FILE"
idx=0
for f in cert_*.pem; do
  idx=$((idx+1))
  a=$ALIAS
  if [ $idx -gt 1 ]; then a="${ALIAS}-${idx}"; fi
  keytool -importcert -noprompt -trustcacerts -alias "$a" -file "$f" -keystore "$STORE" -storepass "$PASSWORD"
  rm -f "$f"
done
echo "Truststore created: $STORE"
