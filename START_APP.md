# How to Start the BrainBridge Backend Application

## Prerequisites
- Java 21 (as specified in pom.xml)
- Maven (or use the included Maven wrapper)

## Method 1: Using Maven Wrapper (Recommended)

Navigate to the project directory:
```bash
cd Backend/brainbridge/brainbridge
```

### On Windows:
```bash
.\mvnw.cmd spring-boot:run
```

### On Linux/Mac:
```bash
./mvnw spring-boot:run
```

## Method 2: Using Maven (if installed)

```bash
cd Backend/brainbridge/brainbridge
mvn spring-boot:run
```

## Method 3: Build and Run JAR

First, build the project:
```bash
cd Backend/brainbridge/brainbridge
.\mvnw.cmd clean package
```

Then run the JAR:
```bash
java -jar target/brainbridge-0.0.1-SNAPSHOT.jar
```

## Method 4: Run from IDE (IntelliJ IDEA / Eclipse / VS Code)

1. **IntelliJ IDEA:**
   - Open the project: `File` → `Open` → Select `Backend/brainbridge/brainbridge`
   - Right-click on `BrainbridgeApplication.java`
   - Select `Run 'BrainbridgeApplication.main()'`

2. **Eclipse:**
   - Import as Maven project
   - Right-click on `BrainbridgeApplication.java`
   - Select `Run As` → `Java Application`

3. **VS Code:**
   - Install Java Extension Pack
   - Open the project folder
   - Press `F5` or click `Run` → `Start Debugging`

## Verify the Application is Running

Once started, you should see output like:
```
Started BrainbridgeApplication in X.XXX seconds
```

The application will be available at:
- **API Base URL:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **H2 Database Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:brainbridgedb`
  - Username: `sa`
  - Password: (leave empty)

## Test the API

### Using Swagger UI (Easiest)
1. Open http://localhost:8080/swagger-ui.html in your browser
2. You'll see all available endpoints organized by tags (Users, Projects, Comments, Analytics)
3. Click on any endpoint to expand and see details
4. Click "Try it out" to test the endpoint
5. Fill in the required parameters and click "Execute"

### Using cURL Examples

**Register a new user:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "username": "testuser",
    "password": "password123",
    "confirmPassword": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "emailOrUsername": "test@example.com",
    "password": "password123"
  }'
```

**Get all users:**
```bash
curl http://localhost:8080/api/users
```

## Troubleshooting

### Port 8080 already in use
If port 8080 is already in use, you can change it in `application.properties`:
```properties
server.port=8081
```

### Java version issues
Make sure you have Java 21 installed:
```bash
java -version
```

If you have a different version, either:
1. Install Java 21
2. Or change the Java version in `pom.xml` (line 30) to match your installed version

### Maven dependencies not downloading
Try:
```bash
.\mvnw.cmd clean install
```

## Application Properties

The application uses H2 in-memory database by default. All data will be lost when you stop the application.

To persist data, you can configure MySQL in `application.properties` (currently commented out in pom.xml).

