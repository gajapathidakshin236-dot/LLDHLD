# ============================================================================
# api-test.ps1 - full CRUD verification against the RUNNING booking-service
# Results land in api-test-results.txt next to this script.
# ============================================================================
$base = 'http://localhost:8080'
$out  = Join-Path $PSScriptRoot 'api-test-results.txt'
"BOOKING-SERVICE API TEST  $(Get-Date)" | Set-Content $out
$H = @{ 'X-Tenant-Id' = '1' }

function Hit($name, $method, $url, $headers, $body) {
    try {
        $args = @{ Method = $method; Uri = $url; UseBasicParsing = $true }
        if ($headers) { $args.Headers = $headers }
        if ($body)    { $args.ContentType = 'application/json'; $args.Body = $body }
        $r = Invoke-WebRequest @args
        Add-Content $out ("=== {0}`n[{1}] {2}`n" -f $name, [int]$r.StatusCode, $r.Content)
        return $r.Content
    } catch {
        $resp = $_.Exception.Response
        $code = 'ERR'; $txt = $_.Exception.Message
        if ($resp) {
            $code = [int]$resp.StatusCode
            $sr = New-Object IO.StreamReader($resp.GetResponseStream())
            $txt = $sr.ReadToEnd()
        }
        Add-Content $out ("=== {0}`n[{1}] {2}`n" -f $name, $code, $txt)
        return $null
    }
}

# ---- health + reads ----------------------------------------------------------
Hit '01 GET health (no tenant needed)'            GET  "$base/actuator/health"            @{}   $null | Out-Null
Hit '02 GET all shows'                            GET  "$base/api/v1/shows"               $H    $null | Out-Null
Hit '03 GET show 1 (cache MISS - check app log)'  GET  "$base/api/v1/shows/1"             $H    $null | Out-Null
Hit '04 GET show 1 again (cache HIT - no SQL)'    GET  "$base/api/v1/shows/1"             $H    $null | Out-Null

# ---- POST create + capture id ------------------------------------------------
$created = Hit '05 POST booking (userId 7001 -> payment success)' POST "$base/api/v1/bookings" $H '{"showId":1,"userId":7001,"seatNo":"P1"}'
$id = if ($created) { ($created | ConvertFrom-Json).id } else { 0 }
Add-Content $out ">>> created booking id = $id`n"

Hit '06 POST booking (userId 7010 -> payment DECLINED, status FAILED)' POST "$base/api/v1/bookings" $H '{"showId":1,"userId":7010,"seatNo":"P2"}' | Out-Null
Hit '07 POST duplicate seat A1 -> expect 409'     POST "$base/api/v1/bookings" $H '{"showId":1,"userId":7002,"seatNo":"A1"}' | Out-Null
Hit '08 POST missing seatNo -> expect 400 + fieldErrors' POST "$base/api/v1/bookings" $H '{"showId":1,"userId":7003}' | Out-Null

# ---- GET by id + tenant isolation --------------------------------------------
Hit "09 GET booking $id"                          GET  "$base/api/v1/bookings/$id"        $H    $null | Out-Null
Hit "10 GET booking $id as tenant 2 -> expect 404" GET "$base/api/v1/bookings/$id"        @{ 'X-Tenant-Id' = '2' } $null | Out-Null

# ---- pagination: offset, slice, keyset ---------------------------------------
Hit '11 GET offset page (Page - has totals)'      GET  "$base/api/v1/bookings?page=0&size=5&sort=createdAt,desc" $H $null | Out-Null
Hit '12 GET by-status (Slice - hasNext, NO totals)' GET "$base/api/v1/bookings/by-status?status=CONFIRMED&size=5" $H $null | Out-Null

$feed1 = Hit '13 GET keyset feed page 1'          GET  "$base/api/v1/bookings/feed?size=5" $H  $null
$cursor = if ($feed1) { ($feed1 | ConvertFrom-Json).nextCursor } else { '' }
Add-Content $out ">>> nextCursor = $cursor`n"
Hit '14 GET keyset feed page 2 (using cursor)'    GET  "$base/api/v1/bookings/feed?size=5&cursor=$cursor" $H $null | Out-Null

# ---- PUT (full) + PATCH (partial) --------------------------------------------
Hit '15 PUT show 1 (full replacement)'            PUT  "$base/api/v1/shows/1" $H '{"title":"Inception (IMAX Remaster)","venue":"PVR Phoenix Screen 4","totalSeats":220,"priceCents":55000,"startsAt":"2026-07-25T18:30:00Z"}' | Out-Null
Hit '16 PATCH show 1 title only'                  PATCH "$base/api/v1/shows/1/title?title=Inception+Directors+Cut" $H $null | Out-Null
Hit '17 GET show 1 (fresh after evict - new title)' GET "$base/api/v1/shows/1"             $H    $null | Out-Null

# ---- DELETE (soft cancel) + idempotency --------------------------------------
Hit "18 DELETE booking $id (soft cancel)"         DELETE "$base/api/v1/bookings/$id"      $H    $null | Out-Null
Hit "19 DELETE booking $id AGAIN (idempotent - same result)" DELETE "$base/api/v1/bookings/$id" $H $null | Out-Null

# ---- error contract ----------------------------------------------------------
Hit '20 GET bookings with NO tenant header -> expect 400' GET "$base/api/v1/bookings"     @{}   $null | Out-Null
Hit '21 GET bad sort property -> expect 400'      GET  "$base/api/v1/bookings?sort=notAField" $H $null | Out-Null
Hit '22 GET feed size=0 -> expect 400'            GET  "$base/api/v1/bookings/feed?size=0" $H   $null | Out-Null

Add-Content $out "DONE."
