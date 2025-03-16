# Production Configuration Documentation

## Core Settings

### Profile Configuration
```yaml
spring:
  config:
    activate:
      on-profile: prod
```
- Activates the production profile

### JMX and JVM Settings
```yaml
  jmx:
    enabled: false  # Disables Java Management Extensions to reduce overhead
  jvm:
    gc:
      log-file-rotation: false  # Disables GC log rotation
      log-detail: false        # Minimizes GC logging details
```

### Main Application Settings
```yaml
  main:
    web-application-type: servlet    # Defines as a servlet-based web application
    lazy-initialization: true        # Reduces startup time and initial memory usage
    banner-mode: off                 # Disables Spring banner for cleaner logs
    allow-bean-definition-overriding: false  # Prevents bean definition conflicts
    allow-circular-references: false         # Prevents circular dependencies
    cloud-platform: none                     # Disables cloud platform features
    register-shutdown-hook: false            # Optimizes shutdown process
```

### Auto-Configuration Exclusions
```yaml
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration
      - org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
      - org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration
      - org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
      - org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
      - org.springframework.boot.autoconfigure.jta.JtaAutoConfiguration
      - org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration
```
- Disables unnecessary auto-configurations to reduce startup time and memory usage

### Resource and MVC Settings
```yaml
  resources:
    add-mappings: false  # Disables automatic resource mapping
  mvc:
    throw-exception-if-no-handler-found: true  # Improves error handling
```

### Jackson Configuration
```yaml
  jackson:
    time-zone: Asia/Ho_Chi_Minh           # Sets default timezone
    default-property-inclusion: non_null   # Excludes null values from JSON
    serialization:
      fail-on-empty-beans: false          # Prevents serialization errors
    deserialization:
      fail-on-unknown-properties: false    # Ignores unknown JSON properties
    parser:
      allow-single-quotes: true           # Allows single quotes in JSON
```

## Security Settings

### Basic Security
```yaml
  security:
    filter:
      dispatcher-types: REQUEST  # Limits security filter to REQUEST type only
```

### CORS Configuration
```yaml
  cors:
    allowed-origins: "*"
    allowed-methods: "*"
    allowed-headers: "*"
    exposed-headers: "*"
    allow-credentials: true
    max-age: 3600  # Cache preflight requests for 1 hour
```

### Rate Limiting
```yaml
app:
  security:
    rate-limit:
      enabled: true
      max-requests: 100    # Maximum requests allowed
      time-window: 60000   # Time window in milliseconds (1 minute)
```

## Database Configuration

### DataSource Settings
```yaml
  datasource:
    url: ${DATABASE_URL}?serverTimezone=Asia/Ho_Chi_Minh&cachePrepStmts=true&useServerPrepStmts=true&rewriteBatchedStatements=true&useConfigs=maxPerformance&elideSetAutoCommits=true&maintainTimeStats=false
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### HikariCP Connection Pool
```yaml
    hikari:
      auto-commit: false                 # Manual transaction control
      pool-name: HikariCP               # Pool identifier
      minimum-idle: 1                    # Minimum idle connections
      maximum-pool-size: 2               # Maximum pool size
      idle-timeout: 300000              # 5 minutes idle timeout
      max-lifetime: 1800000             # 30 minutes maximum lifetime
      connection-timeout: 20000         # 20 seconds connection timeout
      validation-timeout: 5000          # 5 seconds validation timeout
      keepalive-time: 0                 # Disabled keepalive
      leak-detection-threshold: 0       # Disabled leak detection
      register-mbeans: false            # Disabled JMX monitoring
      allow-pool-suspension: false      # Prevents pool suspension
      initialization-fail-timeout: 1     # Fast fail on initialization
      validation-query: SELECT 1        # Connection validation query
```

### MySQL Optimizations
```yaml
      data-source-properties:
        cachePrepStmts: true            # Enable statement caching
        prepStmtCacheSize: 250          # Statement cache size
        prepStmtCacheSqlLimit: 2048     # Maximum statement length to cache
        useServerPrepStmts: true        # Use server-side prepared statements
        useLocalSessionState: true      # Reduce client/server communication
        rewriteBatchedStatements: true  # Optimize batch operations
        cacheResultSetMetadata: true    # Cache result set metadata
        cacheServerConfiguration: true   # Cache server configuration
        maintainTimeStats: false        # Disable timing statistics
        elideSetAutoCommits: true       # Optimize autocommit handling
```

## JPA/Hibernate Configuration

### Naming Strategy
```yaml
    naming:
      physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
```

### Basic JPA Settings
```yaml
    hibernate:
      ddl-auto: update                # Database schema update strategy
    open-in-view: false              # Disables OSIV pattern
    show-sql: false                  # Disables SQL logging
```

### Connection Management
```yaml
    properties:
      hibernate:
        physical_naming_strategy: com.healthy.backend.init.PascalCaseNamingStrategy
        connection:
          handling_mode: DELAYED_ACQUISITION_AND_RELEASE_AFTER_TRANSACTION
          provider_disables_autocommit: true
          release_mode: after_statement
```

### JDBC Settings
```yaml
        jdbc:
          batch_size: 10              # Batch size for operations
          fetch_size: 50              # Number of rows fetched at once
          batch_versioned_data: false # Disable versioned data batching
```

### Query Optimization
```yaml
        query:
          in_clause_parameter_padding: true     # Optimize IN clause queries
          plan_cache_max_size: 32              # Limit query plan cache
          plan_parameter_metadata_max_size: 16  # Limit parameter metadata cache
          conventional_java_constants: true     # Use Java constant values
          fail_on_pagination_over_collection_fetch: true  # Prevent N+1 issues
          immutable_entity_update_query_handling_mode: warning
          startup_check: false                 # Disable startup checks
```

### Collection Settings
```yaml
        max_fetch_depth: 2               # Limit fetch depth for associations
        default_batch_fetch_size: 10     # Batch size for collections
        collection:
          fetch:
            batch_size: 10               # Collection fetch batch size
```

### Memory and Performance Optimizations
```yaml
        order_inserts: true              # Order insert operations
        order_updates: true              # Order update operations
        boot:
          allow_jdbc_metadata_access: false  # Disable JDBC metadata access
        session:
          events:
            log: false                   # Disable session event logging
        generate_statistics: false       # Disable statistics generation
        cache:
          use_second_level_cache: false  # Disable second level cache
          use_query_cache: false         # Disable query cache
```

### Transaction Settings
```yaml
        transaction:
          auto_close_session: true       # Automatically close sessions
          coordinator_class: jdbc        # Use JDBC transaction coordinator
```

### Additional Optimizations
```yaml
        bytecode:
          provider: none                 # Disable bytecode provider
        event:
          merge:
            entity_copy_observer: allow  # Allow entity copy during merge
        integrate:
          envers:
            enabled: false               # Disable Hibernate Envers
```

### JPA Specific Settings
```yaml
      jakarta:
        persistence:
          persistence-unit:
            transaction-type: RESOURCE_LOCAL  # Local transaction management
          flush:
            mode: AUTO                       # Auto flush mode
          sharedCache:
            mode: NONE                       # Disable shared cache
          validation:
            mode: NONE                       # Disable validation
          lock:
            timeout: 1000                    # Lock timeout in milliseconds
```

## Mail Configuration
```yaml
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
```

## Server Configuration

### Basic Server Settings
```yaml
server:
  address: 0.0.0.0
  port: 8080
```

### Undertow Settings
```yaml
  undertow:
    io-threads: 1                # IO thread count
    worker-threads: 2            # Worker thread count
    buffer-size: 512            # Buffer size in bytes
    websocket:
      buffer-pool: 512          # WebSocket buffer pool size
    direct-buffers: true        # Use direct buffers
    accesslog:
      enabled: false            # Disable access logging
    max-http-post-size: 2MB     # Maximum POST size
    always-set-keep-alive: false # Optimize keep-alive handling
    preserve-path-on-forward: false
    record-request-start-time: false
    max-headers: 20             # Maximum number of headers
    max-parameters: 50          # Maximum number of parameters
    url-charset: UTF-8          # URL character encoding
    http2:
      enabled: true             # Enable HTTP/2
    options:
      server-name-size: 128
      max-concurrent-requests-per-connection: 30
      max-queued-requests: 50
      enable-http2: true
```

### Compression
```yaml
  compression:
    enabled: true
    mime-types: application/json,text/html,text/xml,text/plain,application/javascript,text/css
    min-response-size: 2048    # Minimum size to trigger compression
```

## Task Execution
```yaml
  task:
    execution:
      pool:
        core-size: 1           # Core thread pool size
        max-size: 5            # Maximum thread pool size
        queue-capacity: 100    # Task queue capacity
        keep-alive: 60s        # Thread keep-alive time
        allow-core-thread-timeout: true  # Allow core threads to timeout
      thread-name-prefix: task-  # Thread naming pattern
      shutdown:
        await-termination: true
        await-termination-period: 20s
```

## Transaction Management
```yaml
  transaction:
    default-timeout: 30s       # Default transaction timeout
    rollback-on-commit-failure: true  # Rollback on commit failures
```

## File Upload Settings
```yaml
  servlet:
    multipart:
      enabled: true
      max-file-size: 2MB       # Maximum file size
      max-request-size: 2MB    # Maximum request size
      file-size-threshold: 0   # In-memory threshold
```

## Swagger/SpringDoc Configuration
```yaml
  springdoc:
    swagger-ui:
      customJs: /static/custom-swagger.js
      customCss: /static/style.css
      defaultModelsExpandDepth: -1
      disable-swagger-default-response-messages: true
      lazy-load: true
      docExpansion: none
      filter: true
      use-root-path: true
      enabled: false           # Disabled in production
    override-with-generic-response: false
    cache:
      disabled: true
    writer-with-default-pretty-printer: false
    api-docs:
      enabled: false
```

## Logging Configuration
```yaml
logging:
  level:
    root: INFO
    org.springframework: INFO
    com.zaxxer.hikari: INFO
    org.hibernate: INFO
    com.healthy.backend: INFO
    io.undertow.websockets.jsr: ERROR
    org.springframework.security.config.annotation.authentication.configuration: ERROR
    io.undertow: WARN
    org.xnio: WARN
  pattern:
    console: "%d{HH:mm:ss.SSS} %-5level [%thread] %logger{36} : %msg%n"
  file:
    max-size: 10MB
    max-history: 3
    name: logs/application.log
```

## JWT Configuration
```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000         # 24 hours
  refresh-token:
    expiration: 604800000      # 7 days
```

## Application Specific Settings
```yaml
app:
  url: https://api.cybriadev.com/
  cache:
    enabled: false             # Application-level cache disabled
  compression:
    enabled: true
    mime-types: application/json,text/html,text/xml,text/plain
    min-response-size: 2048
```

## Optimization Recommendations

1. **Memory Usage**:
    - Current settings optimize for ~200MB idle memory
    - HikariCP pool size is minimal (1-2 connections)
    - Disabled unnecessary features and caching
    - Lazy initialization enabled
    - Minimal thread pools

2. **Performance Tuning**:
    - Batch operations are optimized
    - Connection handling is efficient
    - Query planning is optimized
    - HTTP/2 enabled
    - Compression enabled for responses

3. **Security**:
    - Rate limiting enabled
    - CORS configured
    - JWT with reasonable expiration times
    - Maximum request sizes limited
    - Swagger UI disabled in production

4. **Monitoring**:
    - Structured logging enabled
    - Performance statistics disabled
    - JMX disabled
    - Access logging disabled

## Maintenance Notes

1. **Log Management**:
    - Logs are kept for 3 days
    - Each log file limited to 10MB
    - Console pattern optimized for readability
    - Different log levels for different components

2. **Connection Pool**:
    - Monitor connection usage
    - Adjust pool size if needed (currently 1-2)
    - Connection timeouts set to reasonable values
    - Leak detection disabled for performance

3. **Performance Monitoring**:
    - Watch for connection timeouts
    - Monitor task queue capacity
    - Check compression effectiveness
    - Monitor thread pool utilization

4. **Security Considerations**:
    - JWT tokens expire after 24 hours
    - Refresh tokens expire after 7 days
    - Rate limiting set to 100 requests per minute
    - File uploads limited to 2MB

5. **Database Optimization**:
    - Statement caching enabled
    - Batch operations optimized
    - Connection release optimized
    - Query plan caching configured

6. **Server Tuning**:
    - Minimal thread pools
    - Direct buffers enabled
    - HTTP/2 support
    - Optimized keep-alive handling 