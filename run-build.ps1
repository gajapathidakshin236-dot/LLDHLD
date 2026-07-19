# =============================================================================
# run-build.ps1  -  self-bootstrapping build for booking-service
#
# Everything is logged to build-log.txt next to this script, and the window
# stays open at the end - so failures are always inspectable.
#
#   1. Finds a JDK 17+  (JAVA_HOME, then common install dirs, then repo jdks\)
#   2. Finds Maven      (PATH, else downloads portable Maven 3.9.9 once
#                        to %USERPROFILE%\.portable-maven - no admin needed)
#   3. Runs:  mvn clean test  on booking-service (fills ~/.m2 so IntelliJ
#      sync afterwards is instant and imports at the right language level)
# =============================================================================

$script:log = Join-Path $PSScriptRoot 'build-log.txt'
try { Start-Transcript -Path $script:log -Force | Out-Null } catch { }

$exit = 1
try {
    $ErrorActionPreference = 'Stop'
    $repo = $PSScriptRoot

    function Write-Section($t) {
        Write-Host ""; Write-Host "=== $t ===" -ForegroundColor Cyan
    }

    # -------------------------------------------------------------------------
    # 1. Locate a JDK (need 17+)
    # -------------------------------------------------------------------------
    Write-Section "1/3 Locating JDK"

    $jdkCandidates = @()
    if ($env:JAVA_HOME) { $jdkCandidates += $env:JAVA_HOME }

    $globs = @(
        "C:\Program Files\Microsoft\jdk-*",
        "C:\Program Files\Eclipse Adoptium\jdk-*",
        "C:\Program Files\Java\jdk-*",
        "C:\Program Files\Amazon Corretto\jdk*",
        "C:\Program Files\Zulu\zulu-*"
    )
    foreach ($g in $globs) {
        Get-Item $g -ErrorAction SilentlyContinue |
            Sort-Object Name -Descending |
            ForEach-Object { $jdkCandidates += $_.FullName }
    }
    $jdkCandidates += (Join-Path $repo "jdks\oracleJdk-26")

    $jdk = $null; $jdkMajor = 0
    foreach ($c in $jdkCandidates) {
        $javac = Join-Path $c 'bin\javac.exe'
        if (-not (Test-Path $javac)) { continue }
        try {
            $v = ((& $javac -version 2>&1) | Out-String).Trim()   # e.g. "javac 25.0.3"
            if ($v -match '(\d+)') {
                $major = [int]$Matches[1]
                if ($major -ge 17) { $jdk = $c; $jdkMajor = $major; break }
                Write-Host "  skipping $c (Java $major < 17)"
            }
        } catch { Write-Host "  probe failed for $c : $_" }
    }

    if (-not $jdk) {
        throw "No JDK 17+ found. Install Temurin 21 from https://adoptium.net (tick 'Set JAVA_HOME' + 'Add to PATH')."
    }
    Write-Host "[OK] Using JDK $jdkMajor at: $jdk" -ForegroundColor Green
    $env:JAVA_HOME = $jdk
    $env:Path = "$jdk\bin;$env:Path"

    # -------------------------------------------------------------------------
    # 2. Locate or download Maven
    # -------------------------------------------------------------------------
    Write-Section "2/3 Locating Maven"

    $mvn = $null
    $mvnCmd = Get-Command mvn.cmd -ErrorAction SilentlyContinue
    if (-not $mvnCmd) { $mvnCmd = Get-Command mvn -ErrorAction SilentlyContinue }

    if ($mvnCmd) {
        $mvn = $mvnCmd.Source
        Write-Host "[OK] Using Maven from PATH: $mvn" -ForegroundColor Green
    } else {
        $portableRoot = Join-Path $env:USERPROFILE ".portable-maven"
        $mavenHome    = Join-Path $portableRoot "apache-maven-3.9.9"
        $mvn          = Join-Path $mavenHome "bin\mvn.cmd"

        if (-not (Test-Path $mvn)) {
            Write-Host "Maven not on PATH - downloading portable Maven 3.9.9 (one time, ~9 MB)..."
            New-Item -ItemType Directory -Force -Path $portableRoot | Out-Null
            $zip = Join-Path $portableRoot "maven.zip"
            [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
            Invoke-WebRequest -UseBasicParsing `
                -Uri "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip" `
                -OutFile $zip
            Expand-Archive -Path $zip -DestinationPath $portableRoot -Force
            Remove-Item $zip
        }
        Write-Host "[OK] Using portable Maven: $mvn" -ForegroundColor Green
    }

    # -------------------------------------------------------------------------
    # 3. Build + test booking-service
    # -------------------------------------------------------------------------
    Write-Section "3/3 mvn clean test  (first run downloads Spring - a few minutes)"

    & $mvn -f (Join-Path $repo "booking-service\pom.xml") clean test
    $exit = $LASTEXITCODE

    if ($exit -eq 0) {
        Write-Host ""
        Write-Host "==========================================================" -ForegroundColor Green
        Write-Host " BUILD + ALL TESTS PASSED" -ForegroundColor Green
        Write-Host "==========================================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Now in IntelliJ: Maven panel -> Reload All Maven Projects."
        Write-Host "booking-service imports at language level 21, all errors clear."
    } else {
        Write-Host "[FAIL] Maven exited with code $exit - see errors above." -ForegroundColor Red
    }
}
catch {
    Write-Host ""
    Write-Host "[SCRIPT ERROR] $($_ | Out-String)" -ForegroundColor Red
}
finally {
    try { Stop-Transcript | Out-Null } catch { }
    Write-Host ""
    Read-Host "Done (log: build-log.txt). Press Enter to close this window"
}
exit $exit
