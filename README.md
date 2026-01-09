# rag-chat-storage-svc
Chat Storage Microservice -RAG (Retrieval-Augmented Generation) based chatbot system.
Simple Spring Boot Java application (Maven). Includes a small shell helper to verify a Java truststore.

## Overview
- Language: Java
- Framework: Spring Boot
- Build: Maven

## Prerequisites
1. Java 11+ (or the version configured for the project)
2. Maven 3.6+
3. For the provided script: Bash (Git Bash or WSL on Windows) or use `keytool` directly

## Build

1. Build with Maven:
# mvn clean package

2. Run with Spring Boot plugin:
   mvn spring-boot:run

3. Run the packaged jar:
 # java -jar target/*.jar

## Truststore verification
A small script is included to list entries in a Java KeyStore:
- Script path: `src/main/java/com/example/ragchat/scripts/verify-truststore.sh`
- Usage:
  ./src/main/java/com/example/ragchat/scripts/verify-truststore.sh [path-to-jks] [password]
-
    - Defaults: path = `src/main/resources/llm-truststore.jks`, password = `mysecretpass`
If you are on native Windows (CMD/PowerShell) and prefer not to run Bash, use `keytool` directly:

## keytool -list -keystore src/main/resources/llm-truststore.jks -storepass mysecretpass
## Tests
Run unit tests:

# mvn test
## Project structure (important files)

- `src/main/java` — application source  
- `src/main/resources` — resources (e.g. `llm-truststore.jks`)  
- `src/main/java/com/example/ragchat/scripts/verify-truststore.sh` — truststore helper script

## Contributing
- Open an issue or PR with a description of the change and tests where appropriate.

## License
Specify project license in the repository root (e.g. `LICENSE`).


