# Employee-Management-System
## How to run
* Dev
```
.\mvnw.cmd spring-boot:run
```
* Prod
```
$env:DB_URL="jdbc:mysql://localhost:3306/employee_management"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=prod"
```
* Test 
```
.\mvnw.cmd test
```