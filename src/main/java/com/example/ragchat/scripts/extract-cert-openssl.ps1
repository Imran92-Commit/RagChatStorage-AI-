# requires OpenSSL in PATH
param(
  [Parameter(Mandatory=$true)][string]$HostName,
  [int]$Port = 443,
  [string]$OutFile = "server-chain.pem"
)
$cmd = "echo | openssl s_client -servername $HostName -connect ${HostName}:$Port -showcerts"
Write-Host "Running: $cmd"
$certs = cmd /c $cmd
if (-not $certs) { throw "OpenSSL output empty. Ensure OpenSSL is installed and in PATH." }
$blocks = ($certs -split "-----BEGIN CERTIFICATE-----") | Where-Object { $_ -match "-----END CERTIFICATE-----" }
$i=0
$accum=""
foreach ($b in $blocks){
  $i++
  $pem = "-----BEGIN CERTIFICATE-----" + ($b -split "-----END CERTIFICATE-----")[0] + "-----END CERTIFICATE-----\n"
  $file = "cert_$i.pem"
  [IO.File]::WriteAllText($file, $pem)
  $accum += $pem+"\n"
  Write-Host "Saved $file"
}
[IO.File]::WriteAllText($OutFile, $accum)
Write-Host "Combined chain saved to $OutFile"
