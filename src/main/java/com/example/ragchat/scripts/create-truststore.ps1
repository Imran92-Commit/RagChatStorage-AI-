param(
  [Parameter(Mandatory=$true)][string]$CertFile,   # e.g. rootCA.cer or server-chain.pem
  [string]$Store = "llm-truststore.jks",
  [string]$Password = "changeit",
  [string]$Alias = "llm-root"
)
$javaHome = $env:JAVA_HOME
if (-not $javaHome) { Write-Warning "JAVA_HOME not set; will try system keytool" }
$keytool = if ($javaHome) { Join-Path $javaHome "bin/keytool.exe" } else { "keytool" }
if (-not (Get-Command $keytool -ErrorAction SilentlyContinue)) { throw "keytool not found. Install JDK and set JAVA_HOME." }

# If file contains multiple certs, import each
$content = Get-Content -Raw -Path $CertFile
$parts = ($content -split "-----BEGIN CERTIFICATE-----") | Where-Object { $_ -match "-----END CERTIFICATE-----" }
if ($parts.Count -gt 1) { Write-Host "Multiple certs detected ($($parts.Count)). Importing each with incremental alias." }
$idx=0
foreach ($p in $parts){
  $idx++
  $pem = "-----BEGIN CERTIFICATE-----" + ($p -split "-----END CERTIFICATE-----")[0] + "-----END CERTIFICATE-----\n"
  $tmp = [IO.Path]::GetTempFileName() + ".pem"
  [IO.File]::WriteAllText($tmp, $pem)
  $a = if ($parts.Count -gt 1) { "$Alias-$idx" } else { $Alias }
  & $keytool -importcert -noprompt -trustcacerts -alias $a -file $tmp -keystore $Store -storepass $Password | Out-Host
  Remove-Item $tmp -Force
}
Write-Host "Truststore created: $Store"
