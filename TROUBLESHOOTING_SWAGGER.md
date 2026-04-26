# Troubleshooting Swagger 500 Error

If you're still getting a 500 error when accessing Swagger, follow these steps:

## Step 1: Check Application Logs

The most important step is to check the **console/terminal output** where you started the application. Look for error messages that contain:
- `Exception`
- `Error`
- `Failed`
- Stack traces

**Common errors you might see:**
- `LazyInitializationException` - Entity lazy loading issue
- `ClassNotFoundException` - Missing dependency
- `BeanCreationException` - Configuration issue
- `SchemaGenerationException` - Swagger schema generation issue

## Step 2: Test if Application is Running

Before accessing Swagger, test if the basic API is working:

1. **Health Check Endpoint:**
   ```
   http://localhost:8080/api/health
   ```
   Should return: `{"status":"UP","message":"BrainBridge API is running"}`

2. **Direct API Docs Endpoint:**
   ```
   http://localhost:8080/v3/api-docs
   ```
   This will show you the raw JSON error if Swagger is failing

## Step 3: Common Fixes

### Fix 1: Restart the Application
```bash
# Stop the application (Ctrl+C)
# Then restart:
cd Backend\brainbridge\brainbridge
.\mvnw.cmd spring-boot:run
```

### Fix 2: Clear Maven Cache and Rebuild
```bash
cd Backend\brainbridge\brainbridge
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

### Fix 3: Check Java Version
Make sure you're using Java 21:
```bash
java -version
```
Should show: `openjdk version "21"` or similar

### Fix 4: Check for Port Conflicts
If port 8080 is already in use, change it in `application.properties`:
```properties
server.port=8081
```

### Fix 5: Disable Swagger Temporarily
If you need to test the API without Swagger, add this to `application.properties`:
```properties
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```

## Step 4: Check Specific Issues

### Issue: LazyInitializationException
If you see this error, it means entities are being accessed outside a transaction. This is already handled in our service layer with `@Transactional`, but if you see this error, check:
- All service methods that access entity relationships have `@Transactional`
- Repository methods use proper `@Query` annotations

### Issue: Schema Generation Error
If Swagger can't generate schemas, try:
1. Remove `@ElementCollection` temporarily and use a simple String field
2. Or add `@Schema(hidden = true)` to problematic fields

### Issue: Circular Reference
We've already added `@JsonIgnore` to entity relationships. If you still see circular reference errors:
1. Check that all `@ManyToOne` and `@OneToMany` relationships have `@JsonIgnore`
2. Verify DTOs don't have circular references

## Step 5: Get Detailed Error Information

Add this to `application.properties` to see more detailed errors:
```properties
logging.level.org.springdoc=DEBUG
logging.level.org.springframework.web=DEBUG
```

Then check the console output for detailed error messages.

## Step 6: Alternative - Use Postman or cURL

If Swagger continues to fail, you can still test your API using:
- **Postman** - Import the OpenAPI spec (if you can generate it)
- **cURL** - Command line tool
- **Browser** - For GET requests

## Still Not Working?

If none of these work, please:
1. Copy the **full error message** from your console/terminal
2. Check what happens when you visit: `http://localhost:8080/v3/api-docs`
3. Share the error details so we can fix the specific issue

