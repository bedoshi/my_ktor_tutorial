# my ktor tutorial
This is my learning ktor. Below is my memo to run something.

## How to blah
- how to build
  - `./gradlew build`
- how to run this
  - `./gradlew run`
- how to send request for my local
  - with curl
    - `curl https://localhost:8080/users`
  - with curl.exe
    - `curl.exe -X GET http://localhost:8080/users`
    - ```powershell
      curl.exe -X POST http://localhost:8080/users `
        -H "Content-Type: application/json" `
        -d '{\"name\": \"山田太郎\", \"email\": \"taro@example.com\", \"age\": 30}'
      ```
  - with Invoke-WebRequest
    - ```powershell
      Invoke-WebRequest -Uri "https://localhost:8080/users"
      ```
    - ```powershell
      # GETリクエスト
      Invoke-RestMethod -Uri "http://localhost:8080/users" -Method Get

      # POSTリクエスト
      $body = @{
        name = "山田太郎"
        email = "taro@example.com"
        age = 30
      } | ConvertTo-Json
    
      Invoke-RestMethod -Uri "http://localhost:8080/users" -Method Post -ContentType "application/json" -Body $body
    
      # PUTリクエスト
      $body = @{
        name = "山田太郎"
        email = "newtaro@example.com"
        age = 31
      } | ConvertTo-Json
    
      Invoke-RestMethod -Uri "http://localhost:8080/users/1" -Method Put -ContentType "application/json" -Body $body
    
      # DELETEリクエスト
      Invoke-RestMethod -Uri "http://localhost:8080/users/1" -Method Delete
      ```
      
