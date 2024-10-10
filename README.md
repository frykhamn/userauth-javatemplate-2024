
# UserAuth Template

Detta projekt är en mall för användarautentisering byggd med Java och Spring Boot. Projektet ger funktionalitet för användarregistrering, inloggning, lösenordskryptering och JWT-baserad autentisering. Projektet är strukturerat enligt Spring-principer och har omfattande tester för att säkerställa robusthet.

## Använda Tekniker

- **Java**: Huvudspråk för projektet.
- **Spring Boot**: Används för snabb konfiguration och uppsättning av applikationen.
- **Spring Security**: Hanterar autentisering och auktorisering samt kryptering av lösenord.
- **PostgreSQL**: Relationsdatabas för att lagra användarinformation.
- **JWT (JSON Web Token)**: Används för statslös autentisering mellan klient och server.
- **Maven**: Byggverktyg för att hantera beroenden och projektets livscykel.
- **JUnit & Mockito**: Används för enhets- och integrationstester.

## Nyckelfunktioner

- **Användarregistrering**: Nya användare kan registrera sig med användarnamn, lösenord och andra detaljer. Lösenorden krypteras och lagras säkert.
- **Användarinloggning**: Användare kan logga in och få en JWT-token som används för framtida autentisering.
- **JWT-autentisering**: Varje begäran till skyddade endpoints kräver en JWT-token som autentiseras av servern.
- **Felhantering**: Systemet hanterar och loggar olika typer av fel, som felaktig inloggning eller försök att registrera en redan existerande användare.

## Controllers

### AuthController
`AuthController` hanterar användarrelaterade operationer som inloggning och registrering.

- **Login (POST /auth/login)**:
  Användare skickar in sina inloggningsuppgifter (användarnamn och lösenord) och får tillbaka en JWT-token om autentiseringen lyckas.

  - **Begäran**:
    ```json
    {
      "username": "string",
      "password": "string"
    }
    ```

  - **Svar**:
    ```json
    {
      "token": "jwt-token"
    }
    ```

  Exempel:
  ```bash
  curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username": "user1", "password": "password123"}'
  ```

- **Registrera användare (POST /auth/register)**:
  Registrerar en ny användare med användarnamn, lösenord och andra nödvändiga uppgifter. Om registreringen lyckas får användaren ett framgångsmeddelande, annars kastas ett undantag om användaren redan existerar.

  - **Begäran**:
    ```json
    {
      "username": "string",
      "password": "string",
      "email": "string",
      "firstName": "string",
      "lastName": "string",
      "phoneNumber": "string"
    }
    ```

  Exempel:
  ```bash
  curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d '{"username": "newuser", "password": "password123", "email": "newuser@example.com", "firstName": "John", "lastName": "Doe", "phoneNumber": "1234567890"}'
  ```

- **Hämta alla användare (GET /auth/users)**:
  Returnerar en lista över alla registrerade användare (endast för teständamål).

### Säkerhet och autentisering

Efter att en användare har autentiserats via `/auth/login` får denne en **JWT-token**. Denna token ska inkluderas i varje framtida begäran till skyddade endpoints i `Authorization`-huvudet:
```bash
Authorization: Bearer <jwt_token>
```
Servern kommer att validera JWT-token vid varje begäran för att säkerställa att användaren är autentiserad.

## Testning

### Enhetstester

Enhetstester använder **JUnit** och **Mockito** för att testa tjänster och controllers isolerat.

- **Tjänstetester**: `UserServiceTest` verifierar logik kring användarregistrering och hantering, inklusive fall som redan existerande användare eller misslyckad användarregistrering.
- **Controller-tester**: `AuthControllerTest` testar inloggnings- och registreringsfunktionalitet samt JWT-generering med mockade beroenden.

### Integrationstester

Integrationstester utförs med **MockMvc** för att simulera HTTP-begäran mot faktiska endpoints. Dessa tester validerar hela flödet för användarregistrering, inloggning och JWT-token-hantering.

- **Exempeltester**:
  - **Login-tester**: Testar att en användare kan logga in och få en JWT-token.
  - **Registrerings-tester**: Testar att en användare kan registrera sig framgångsrikt, och att en användare inte kan registrera sig med ett redan existerande användarnamn.

```bash
mvn test
```

## Projektstruktur

- **Controller**: Hanterar HTTP-begäran och definierar endpoints för autentisering och användarhantering.
- **Service**: Affärslogik för registrering och inloggning samt hantering av JWT-tokens.
- **Repository**: Kommunicerar med PostgreSQL för att lagra och hämta användarinformation.
- **Security**: Hanterar lösenordskryptering och autentisering via JWT.

## Hur du kör projektet

1. **Installera PostgreSQL** och skapa en databas.
2. **Klona detta repository**:
   ```bash
   git clone <repo-url>
   ```
3. **Uppdatera `application.properties`**:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```
4. **Bygg projektet**:
   ```bash
   mvn clean install
   ```
5. **Starta applikationen**:
   ```bash
   mvn spring-boot:run
   ```

## Hur du kör testerna

Kör alla enhets- och integrationstester med Maven:
```bash
mvn test
```
