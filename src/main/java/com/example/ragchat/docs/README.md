# Truststore Kit for RAG Service

This kit helps you fix `PKIX path building failed` when calling your LLM endpoint from Java.

## What’s inside
- `scripts/extract-cert-openssl.ps1` – PowerShell helper to fetch server cert chain via OpenSSL (Windows)
- `scripts/create-truststore.ps1` – PowerShell to build `llm-truststore.jks` from a PEM/chain
- `scripts/extract-cert-openssl.sh` – Bash helper (Linux/macOS)
- `scripts/create-truststore.sh` – Bash truststore builder
- `config/application.yml` – Example Spring Boot SSL bundle config
- `src/main/resources/llm-truststore.jks` – (placeholder) copy your generated truststore here
- `config/LlmWebClientConfig.java` – WebClient bean wired to SSL bundle

## Quick Start (Windows)
1. **Extract the certificate chain**
   ```powershell
   ./scripts/extract-cert-openssl.ps1 -HostName api.openai.com -OutFile server-chain.pem
   ```
2. **Create truststore**
   ```powershell
   ./scripts/create-truststore.ps1 -CertFile server-chain.pem -Store llm-truststore.jks -Password changeit -Alias llm-root
   ```
3. **Place truststore**
   Copy `llm-truststore.jks` into `src/main/resources/`.

4. **Enable SSL bundle**
   Merge `config/application.yml` into your app’s `application.yml` and ensure the `location: classpath:llm-truststore.jks` path is correct.

5. **Wire WebClient**
   Add `config/LlmWebClientConfig.java` to your project so any bean that needs a TLS client can autowire `WebClient llmWebClient`.

6. **Run**
   ```powershell
   mvn clean package
   java -jar target/your-app.jar
   ```

## Quick Start (Linux/macOS)
```bash
./scripts/extract-cert-openssl.sh api.openai.com 443 server-chain.pem
./scripts/create-truststore.sh server-chain.pem llm-truststore.jks changeit llm-root
cp llm-truststore.jks src/main/resources/
```
Then configure `application.yml` as shown and restart.

## Alternate: VM Options Only
Instead of SSL bundle, you can pass VM args:
```bash
java -Djavax.net.ssl.trustStore=llm-truststore.jks      -Djavax.net.ssl.trustStorePassword=changeit      -jar target/your-app.jar
```
(Adjust path to point to your truststore file.)

## Notes
- If you’re behind a corporate proxy that *re-signs* TLS, import the **proxy’s root CA** into the truststore.
- If the chain includes multiple certs, the provided scripts import each cert with aliases `llm-root`, `llm-root-2`, ...
- Never disable SSL verification in production.
