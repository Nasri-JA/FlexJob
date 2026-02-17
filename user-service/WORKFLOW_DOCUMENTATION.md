# User Service - Kompletter Workflow zwischen Schichten

## 🔄 Wie arbeiten die Schichten zusammen?

Diese Dokumentation erklärt **GENAU**, wie die Clean Architecture Schichten miteinander kommunizieren.

---

## 📊 Die 3 Schichten und ihre Verantwortlichkeiten

```
┌─────────────────────────────────────────────────────────────┐
│  INFRASTRUCTURE (Äußere Schicht)                            │
│  - REST Controller (empfängt HTTP Requests)                 │
│  - JPA Repositories (spricht mit Datenbank)                 │
│  - Kafka Publisher (sendet Events)                          │
│  - JWT Provider (erstellt Tokens)                           │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  APPLICATION (Mittlere Schicht)                       │  │
│  │  - Use Cases (Geschäftslogik Orchestrierung)         │  │
│  │  - Ports (Interfaces für Input/Output)               │  │
│  │  - Commands & Responses (DTOs)                       │  │
│  │  ┌─────────────────────────────────────────────────┐ │  │
│  │  │  DOMAIN (Innere Schicht - Kern)                │ │  │
│  │  │  - Entities (User, UserProfile)                │ │  │
│  │  │  - Value Objects (Email, UserId)               │ │  │
│  │  │  - Domain Services (Validierungen)             │ │  │
│  │  │  - Business Rules (activate, canLogin, etc.)   │ │  │
│  │  └─────────────────────────────────────────────────┘ │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 BEISPIEL: User Registration Flow

### Schritt-für-Schritt Ablauf:

```
1. HTTP Request kommt rein
   POST /api/auth/register
   Body: { email, password, userType, firstName, lastName }

   ↓

2. INFRASTRUCTURE - REST Adapter (AuthController)
   - Empfängt HTTP Request
   - Validiert Eingabe (@Valid RegisterRequest)
   - Mapped REST DTO → Application Command
   - Ruft Use Case auf

   ↓

3. APPLICATION - Use Case (RegisterUserService)
   - Orchestriert den Ablauf
   - Prüft ob Email existiert (LoadUserPort)
   - Erstellt Domain Entity (User.createNew())
   - Validiert Business Rules (UserDomainService)
   - Speichert User (SaveUserPort)
   - Publiziert Event (PublishEventPort)
   - Gibt Response zurück

   ↓

4. DOMAIN - Business Logic
   - Email.of() validiert Email-Format
   - User.createNew() erstellt User mit Business Rules
   - UserDomainService prüft Business-Regeln

   ↓

5. APPLICATION - Output Ports aufrufen
   - LoadUserPort.existsByEmail()
   - SaveUserPort.save()
   - PublishEventPort.publishUserRegistered()

   ↓

6. INFRASTRUCTURE - Adapter implementieren Ports
   - UserPersistenceAdapter.save()
     → Mapped Domain → JPA Entity
     → Ruft UserJpaRepository auf
     → Speichert in Datenbank

   - UserEventPublisher.publishUserRegistered()
     → Sendet Event zu Kafka

   ↓

7. Response zurück zum Client
   UserResponse wird zurückgegeben
```

---

## 📝 Detaillierter Workflow mit Code

### 1️⃣ **INFRASTRUCTURE → APPLICATION** (HTTP Request)

```java
// File: AuthController.java
// Wird verwendet in: REST API Endpunkt /api/auth/register
// Zweck: Empfängt HTTP Request, validiert Input, ruft Use Case auf

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Wird verwendet in: Dependency Injection von Spring
    // Zweck: Use Case wird von Spring injiziert
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {  // ← REST DTO (Infrastructure)

        // 1. REST DTO → Application Command
        // Wird verwendet in: Trennung zwischen REST und Application Layer
        RegisterUserCommand command = mapper.toCommand(request);

        // 2. Use Case aufrufen
        // Wird verwendet in: Ausführung der Business Logic
        UserResponse response = registerUserUseCase.execute(command);

        // 3. Response zurück
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

**Wichtig:**
- `RegisterRequest` = REST DTO (nur für HTTP)
- `RegisterUserCommand` = Application DTO (für Use Case)
- **Trennung** zwischen HTTP und Business Logic!

---

### 2️⃣ **APPLICATION** - Use Case Orchestrierung

```java
// File: RegisterUserService.java
// Wird verwendet in: AuthController ruft execute() auf
// Zweck: Orchestriert User-Registrierung, koordiniert alle Schritte

@Service
@Transactional
public class RegisterUserService implements RegisterUserUseCase {

    // Output Ports - Interfaces!
    // Wird verwendet in: Dependency Inversion - wir hängen von Interfaces ab
    private final LoadUserPort loadUserPort;       // ← Interface
    private final SaveUserPort saveUserPort;        // ← Interface
    private final PublishEventPort publishEventPort; // ← Interface
    private final UserDomainService domainService;   // ← Domain Service
    private final PasswordEncoder passwordEncoder;   // ← Spring Bean

    @Override
    public UserResponse execute(RegisterUserCommand command) {

        // SCHRITT 1: Command → Domain Value Object
        // Wird verwendet in: Type Safety, Validation
        // Zweck: Email validieren und als Value Object erstellen
        Email email = Email.of(command.getEmail());  // ← Domain Value Object

        // SCHRITT 2: Prüfen ob Email existiert
        // Wird verwendet in: Business Rule - keine doppelten Emails
        // Zweck: LoadUserPort (Interface) aufrufen
        boolean exists = loadUserPort.existsByEmail(email);

        // SCHRITT 3: Domain Validation
        // Wird verwendet in: Business Rules prüfen
        domainService.validateEmailAvailability(email, exists);

        // SCHRITT 4: Domain Entity erstellen
        // Wird verwendet in: Factory Method Pattern
        // Zweck: User mit Business Rules erstellen
        String encryptedPassword = passwordEncoder.encode(command.getPassword());
        User user = User.createNew(email, encryptedPassword, command.getUserType());

        // SCHRITT 5: Domain Validierung
        // Wird verwendet in: Business Rules
        domainService.validateNewUser(user);

        // SCHRITT 6: User speichern
        // Wird verwendet in: SaveUserPort (Interface)
        // Zweck: Persistierung über Port
        User savedUser = saveUserPort.save(user);  // ← Port aufgerufen!

        // SCHRITT 7: Profile erstellen und speichern
        UserProfile profile = UserProfile.builder()
            .userId(savedUser.getId())
            .firstName(command.getFirstName())
            .lastName(command.getLastName())
            .phone(command.getPhone())
            .build();
        UserProfile savedProfile = saveUserPort.saveProfile(profile);

        // SCHRITT 8: Event publishen
        // Wird verwendet in: PublishEventPort (Interface)
        // Zweck: Kafka Event über Port
        publishEventPort.publishUserRegistered(savedUser);

        // SCHRITT 9: Domain → Response DTO
        // Wird verwendet in: Trennung Domain/Application
        return mapToResponse(savedUser, savedProfile);
    }
}
```

**Wichtig:**
- Use Case kennt nur **Interfaces** (Ports)
- Use Case kennt **keine** Implementierungen
- Use Case orchestriert Domain-Objekte

---

### 3️⃣ **APPLICATION → DOMAIN** - Business Logic

```java
// File: User.java (Domain Entity)
// Wird verwendet in: RegisterUserService für Business Logic
// Zweck: Zentrale Business Rules für User

public class User {

    // Value Objects statt primitiver Typen
    // Wird verwendet in: Type Safety, Domain-Driven Design
    private UserId id;        // ← Value Object (kein Long!)
    private Email email;      // ← Value Object (kein String!)
    private String password;  // encrypted
    private UserType userType;
    private Boolean active;
    private Integer failedLoginAttempts;

    // Factory Method - Business Rule
    // Wird verwendet in: RegisterUserService.execute()
    // Zweck: User mit korrekten Initialwerten erstellen
    public static User createNew(Email email, String encryptedPassword, UserType userType) {
        return User.builder()
            .email(email)
            .password(encryptedPassword)
            .userType(userType)
            .active(true)              // ← Business Rule: Neue User sind aktiv
            .failedLoginAttempts(0)    // ← Business Rule: Start mit 0
            .createdAt(LocalDateTime.now())
            .build();
    }

    // Business Logic Methoden
    // Wird verwendet in: LoginUserService
    // Zweck: Business Rule - User kann nur login wenn aktiv und nicht gesperrt
    public boolean canLogin() {
        return this.active && this.failedLoginAttempts < 3;
    }

    // Wird verwendet in: LoginUserService bei fehlgeschlagenem Login
    // Zweck: Business Rule - Nach 3 Fehlversuchen deaktivieren
    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 3) {
            this.deactivate();  // ← Business Rule!
        }
    }

    // Wird verwendet in: LoginUserService bei erfolgreichem Login
    // Zweck: Fehlversuche zurücksetzen
    public void recordSuccessfulLogin() {
        this.failedLoginAttempts = 0;
    }

    private void deactivate() {
        this.active = false;
    }
}
```

**Wichtig:**
- Domain Entity hat **Business Logic**
- Keine Framework-Abhängigkeiten
- Value Objects statt primitive Typen

---

### 4️⃣ **APPLICATION → INFRASTRUCTURE** - Port Implementierung

```java
// File: LoadUserPort.java (Interface in APPLICATION)
// Wird verwendet in: RegisterUserService, LoginUserService
// Zweck: Definiert WAS die Application braucht (nicht WIE!)

public interface LoadUserPort {
    Optional<User> loadById(UserId userId);
    Optional<User> loadByEmail(Email email);
    boolean existsByEmail(Email email);
}

// ────────────────────────────────────────────────────

// File: UserPersistenceAdapter.java (Implementation in INFRASTRUCTURE)
// Wird verwendet in: Spring injiziert als Bean für LoadUserPort
// Zweck: Implementiert WIE User geladen werden (JPA)

@Component  // ← Spring erstellt Bean
public class UserPersistenceAdapter implements LoadUserPort {  // ← Implementiert Interface!

    // Wird verwendet in: JPA Zugriff
    private final UserJpaRepository jpaRepository;

    // Wird verwendet in: Domain ↔ JPA Mapping
    private final UserPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadByEmail(Email email) {
        // 1. Email Value Object → String
        // Wird verwendet in: JPA braucht String
        String emailString = email.getValue();

        // 2. JPA Repository aufrufen
        // Wird verwendet in: Datenbank Query
        Optional<UserJpaEntity> jpaEntity = jpaRepository.findByEmail(emailString);

        // 3. JPA Entity → Domain Entity
        // Wird verwendet in: Trennung Persistence/Domain
        return jpaEntity.map(mapper::toDomain);  // ← Mapper!
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }
}
```

**Wichtig:**
- Interface in **APPLICATION**
- Implementation in **INFRASTRUCTURE**
- Dependency Inversion Principle!

---

### 5️⃣ **INFRASTRUCTURE** - JPA Layer

```java
// File: UserJpaEntity.java
// Wird verwendet in: UserPersistenceAdapter für DB-Zugriff
// Zweck: Datenbank-Tabelle repräsentieren

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Boolean active;
    private Integer failedLoginAttempts;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

// ────────────────────────────────────────────────────

// File: UserJpaRepository.java
// Wird verwendet in: UserPersistenceAdapter
// Zweck: Spring Data JPA Repository

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    // Wird verwendet in: LoadUserPort.loadByEmail()
    Optional<UserJpaEntity> findByEmail(String email);

    // Wird verwendet in: LoadUserPort.existsByEmail()
    boolean existsByEmail(String email);
}
```

---

### 6️⃣ **Mapper zwischen Schichten**

```java
// File: UserPersistenceMapper.java
// Wird verwendet in: UserPersistenceAdapter
// Zweck: Konvertierung zwischen Domain und JPA

@Component
public class UserPersistenceMapper {

    // Wird verwendet in: LoadUserPort.loadByEmail()
    // Zweck: JPA Entity → Domain Entity
    public User toDomain(UserJpaEntity entity) {
        return User.builder()
            .id(UserId.of(entity.getId()))          // ← Long → Value Object
            .email(Email.of(entity.getEmail()))     // ← String → Value Object
            .password(entity.getPassword())
            .userType(entity.getUserType())
            .active(entity.getActive())
            .failedLoginAttempts(entity.getFailedLoginAttempts())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    // Wird verwendet in: SaveUserPort.save()
    // Zweck: Domain Entity → JPA Entity
    public UserJpaEntity toEntity(User domain) {
        return UserJpaEntity.builder()
            .id(domain.getId() != null ? domain.getId().getValue() : null)  // ← Value Object → Long
            .email(domain.getEmail().getValue())    // ← Value Object → String
            .password(domain.getPassword())
            .userType(domain.getUserType())
            .active(domain.getActive())
            .failedLoginAttempts(domain.getFailedLoginAttempts())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .build();
    }
}
```

---

## 🔑 Warum diese Architektur?

### 1. **Dependency Inversion Principle**

```
❌ FALSCH (direkte Abhängigkeit):
RegisterUserService → UserJpaRepository

✅ RICHTIG (Interface dazwischen):
RegisterUserService → LoadUserPort (Interface) ← UserPersistenceAdapter → UserJpaRepository
```

**Vorteil:**
- Use Case kennt keine JPA
- Leicht testbar (Mock das Interface)
- JPA austauschbar (MongoDB, etc.)

---

### 2. **Separation of Concerns**

```
REST DTO (RegisterRequest)        ← HTTP Layer
    ↓ mapper
Application Command               ← Application Layer
    ↓ factory
Domain Entity (User)              ← Domain Layer
    ↓ mapper
JPA Entity (UserJpaEntity)        ← Persistence Layer
```

**Vorteil:**
- Jede Schicht hat eigene DTOs
- Änderungen in einer Schicht betreffen andere nicht
- Klare Verantwortlichkeiten

---

### 3. **Testability**

```java
// Domain Tests - KEINE Frameworks!
@Test
void user_should_deactivate_after_3_failed_logins() {
    User user = User.createNew(...);
    user.recordFailedLogin();
    user.recordFailedLogin();
    user.recordFailedLogin();
    assertThat(user.getActive()).isFalse();
}

// Use Case Tests - Nur Interfaces mocken
@Test
void should_register_user() {
    // Mock ports
    when(loadUserPort.existsByEmail(any())).thenReturn(false);

    // Test
    UserResponse response = registerUserService.execute(command);

    // Verify
    verify(saveUserPort).save(any(User.class));
}

// Infrastructure Tests - Spring Context
@SpringBootTest
@Test
void should_save_to_database() {
    User user = ...;
    User saved = adapter.save(user);
    assertThat(userJpaRepository.findById(saved.getId())).isPresent();
}
```

---

## 📊 Dependency Flow

```
┌─────────────────────────────────────────────────┐
│         HTTP Request (POST /register)           │
└──────────────────┬──────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────┐
│  INFRASTRUCTURE: AuthController                 │
│  - @RestController                              │
│  - Empfängt RegisterRequest                     │
│  - Mapped zu RegisterUserCommand                │
└──────────────────┬──────────────────────────────┘
                   │ calls
                   ▼
┌─────────────────────────────────────────────────┐
│  APPLICATION: RegisterUserService               │
│  - @Service                                     │
│  - implements RegisterUserUseCase               │
│  - Orchestriert Workflow                        │
│  │                                              │
│  ├─► calls: LoadUserPort.existsByEmail()       │
│  ├─► creates: User.createNew()   ◄─┐          │
│  ├─► calls: UserDomainService     ─┤ DOMAIN   │
│  ├─► calls: SaveUserPort.save()     │          │
│  └─► calls: PublishEventPort      ◄─┘          │
└──────────────────┬──────────────────────────────┘
                   │
        ┌──────────┴───────────┐
        │                      │
        ▼                      ▼
┌──────────────────┐  ┌─────────────────────┐
│ UserPersistence  │  │ UserEventPublisher  │
│ Adapter          │  │                     │
│ - implements     │  │ - implements        │
│   LoadUserPort   │  │   PublishEventPort  │
│ - implements     │  │                     │
│   SaveUserPort   │  │ - Kafka Producer    │
│                  │  └─────────────────────┘
│ ├─► Mapper      │
│ └─► JpaRepo     │
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│   PostgreSQL     │
│   Database       │
└──────────────────┘
```

---

## ✅ Zusammenfassung

### Wer macht was?

| Schicht | Verantwortung | Kennt |
|---------|---------------|-------|
| **DOMAIN** | Business Rules | Nur sich selbst |
| **APPLICATION** | Orchestrierung, Workflow | Domain + Ports (Interfaces) |
| **INFRASTRUCTURE** | Framework, DB, HTTP, Kafka | Alles (implementiert Ports) |

### Kommunikation:

```
INFRASTRUCTURE ruft APPLICATION auf (Use Case Interface)
APPLICATION nutzt DOMAIN (Entities, Services)
APPLICATION definiert Ports (Interfaces)
INFRASTRUCTURE implementiert Ports (Adapters)
```

### Warum so kompliziert?

**Nicht kompliziert - sondern strukturiert!**

✅ **Testbar** - Domain ohne Framework testbar
✅ **Wartbar** - Klare Verantwortlichkeiten
✅ **Flexibel** - Austauschbare Adapters
✅ **Skalierbar** - Klare Grenzen
✅ **Verständlich** - Selbstdokumentierend

---

**Nächster Schritt:** Schaue dir die Dateien mit den Kommentaren an! 🚀
