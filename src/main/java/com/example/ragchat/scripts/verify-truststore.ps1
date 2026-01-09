
param(
  [string]$StorePath = "src/main/resources/llm-truststore.jks",
  [string]$Password = "mysecretpass"
)
if (-not (Test-Path $StorePath)) { Write-Error "Truststore not found: $StorePath"; exit 1 }
Write-Host "Listing entries in $StorePath"
& keytool -list -keystore $StorePath -storepass $Password
