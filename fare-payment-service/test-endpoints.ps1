$ErrorActionPreference = "Stop"
$baseUrl = "http://localhost:8084/api/payments"

Write-Host "3. TEST CREATE PAYMENT"
$createPayload = @{
  rideId = "RIDE-DEMO-002"
  passengerId = "PASSENGER-DEMO-002"
  driverId = "DRIVER-DEMO-002"
  distanceKm = 10
  durationMinutes = 20
  paymentMethod = "CARD"
} | ConvertTo-Json
$createResponse = Invoke-RestMethod -Uri $baseUrl -Method Post -Body $createPayload -ContentType "application/json"
$paymentId = $createResponse.id
Write-Host "Created Payment ID: $paymentId"
Write-Host "Create Response: $($createResponse | ConvertTo-Json -Depth 5)"

Write-Host "`n4. TEST GET PAYMENT BY ID"
$getById = Invoke-RestMethod -Uri "$baseUrl/$paymentId" -Method Get
Write-Host "Get By ID Response: $($getById | ConvertTo-Json -Depth 5)"

Write-Host "`n5. TEST GET PAYMENT BY RIDE ID"
$getByRide = Invoke-RestMethod -Uri "$baseUrl/ride/RIDE-DEMO-002" -Method Get
Write-Host "Get By Ride Response: $($getByRide | ConvertTo-Json -Depth 5)"

Write-Host "`n6. TEST PASSENGER PAYMENT HISTORY"
$getByPassenger = Invoke-RestMethod -Uri "$baseUrl/passenger/PASSENGER-DEMO-002" -Method Get
Write-Host "Get By Passenger count: $($getByPassenger.Count)"

Write-Host "`n7. TEST DRIVER PAYMENT RECORDS"
$getByDriver = Invoke-RestMethod -Uri "$baseUrl/driver/DRIVER-DEMO-002" -Method Get
Write-Host "Get By Driver count: $($getByDriver.Count)"

Write-Host "`n8. TEST PAYMENT STATUS FILTER"
$getByStatus = Invoke-RestMethod -Uri "$baseUrl/status/PENDING" -Method Get
Write-Host "Get By Status PENDING count: $($getByStatus.Count)"

Write-Host "`n9. TEST UPDATE PAYMENT STATUS"
$updatePayload = @{ paymentStatus = "COMPLETED" } | ConvertTo-Json
$updateResponse = Invoke-RestMethod -Uri "$baseUrl/$paymentId/status" -Method Patch -Body $updatePayload -ContentType "application/json"
Write-Host "Update Response: $($updateResponse | ConvertTo-Json -Depth 5)"

Write-Host "`n10. VERIFY COMPLETED STATUS FILTER"
$getByStatusCompleted = Invoke-RestMethod -Uri "$baseUrl/status/COMPLETED" -Method Get
Write-Host "Get By Status COMPLETED count: $($getByStatusCompleted.Count)"

Write-Host "`n11. TEST REFUND"
$refundPayload = @{ refundAmount = 500.00 } | ConvertTo-Json
$refundResponse = Invoke-RestMethod -Uri "$baseUrl/$paymentId/refund" -Method Post -Body $refundPayload -ContentType "application/json"
Write-Host "Refund Response: $($refundResponse | ConvertTo-Json -Depth 5)"

Write-Host "`n12. VERIFY REFUNDED PAYMENT"
$getRefunded = Invoke-RestMethod -Uri "$baseUrl/$paymentId" -Method Get
Write-Host "Refunded Payment Status: $($getRefunded.paymentStatus), Amount: $($getRefunded.refundAmount)"

Write-Host "`n13. TEST DUPLICATE PAYMENT"
try {
  Invoke-RestMethod -Uri $baseUrl -Method Post -Body $createPayload -ContentType "application/json"
  Write-Host "Duplicate test FAILED - no exception thrown"
} catch {
  Write-Host "Duplicate payment correctly rejected. Status Code: $($_.Exception.Response.StatusCode.value__)"
}

Write-Host "`n14. TEST PAYMENT NOT FOUND"
try {
  Invoke-RestMethod -Uri "$baseUrl/UNKNOWN-PAYMENT-ID" -Method Get
  Write-Host "Not found test FAILED"
} catch {
  Write-Host "Not found correctly rejected. Status Code: $($_.Exception.Response.StatusCode.value__)"
}

Write-Host "`n15. TEST INVALID STATUS"
$invalidStatusPayload = @{ paymentStatus = "INVALID_STATUS" } | ConvertTo-Json
try {
  Invoke-RestMethod -Uri "$baseUrl/$paymentId/status" -Method Patch -Body $invalidStatusPayload -ContentType "application/json"
} catch {
  Write-Host "Invalid status correctly rejected. Status Code: $($_.Exception.Response.StatusCode.value__)"
}

Write-Host "`n16. TEST INVALID CREATE REQUEST"
$invalidCreatePayload = @{ rideId = ""; distanceKm = -1 } | ConvertTo-Json
try {
  Invoke-RestMethod -Uri $baseUrl -Method Post -Body $invalidCreatePayload -ContentType "application/json"
} catch {
  Write-Host "Invalid create correctly rejected. Status Code: $($_.Exception.Response.StatusCode.value__)"
}

Write-Host "`n17. TEST INVALID REFUND"
$invalidRefundPayload = @{ refundAmount = -50 } | ConvertTo-Json
try {
  Invoke-RestMethod -Uri "$baseUrl/$paymentId/refund" -Method Post -Body $invalidRefundPayload -ContentType "application/json"
} catch {
  Write-Host "Invalid refund correctly rejected. Status Code: $($_.Exception.Response.StatusCode.value__)"
}

Write-Host "`nAll tests completed."
