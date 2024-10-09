# Mall för Användarautentisering

Detta projekt är en mall för användarautentisering byggd med Java och Spring Boot. Det ger grundläggande funktionalitet för användarregistrering och inloggning, komplett med lösenordskryptering, JWT-token-generering och omfattande felhantering för fall som "Användare hittades inte" och "Användare finns redan". Projektet är strukturerat med standardpraxis för Spring och innehåller både JUnit- och integrationstester för att säkerställa robusthet och tillförlitlighet.

## Använda Tekniker

- **Java**: Projektet är skrivet i Java, och använder MVC-mönster för att separera affärslogik, dataåtkomst och presentationsskikt.

- **Spring Boot**: Projektet använder Spring Boot för att förenkla konfiguration och uppsättning. 

- **Spring Security**: Projektet använder Spring Security för att hantera lösenordskryptering och autentiseringsflöden. Spring Security säkerställer att lösenord är säkert hashas innan de lagras och hanterar autentiseringsprocessen säkert.

- **PostgreSQL**: PostgreSQL används som relationsdatabas för att lagra användarinformation som användarnamn, hashade lösenord, e-post och andra profiluppgifter.

- **JWT (JSON Web Token)**: JWT används för statslös autentisering i detta projekt. När en användare har autentiserats framgångsrikt genereras en JWT-token som returneras till användaren, vilken sedan används för att auktorisera framtida förfrågningar.

- **Maven**: Maven används som byggverktyg för att hantera beroenden, projektets livscykel och paketering.

## Nyckelfunktioner

- **Användarregistrering**: Nya användare kan registrera sig med sitt användarnamn, lösenord, e-post och andra detaljer. Lösenordet krypteras med Spring Security innan det lagras i PostgreSQL-databasen.

- **Användarinloggning**: Registrerade användare kan logga in genom att tillhandahålla sina inloggningsuppgifter. Om autentiseringen lyckas, genereras en JWT-token som kan användas för att autentisera framtida förfrågningar.

- **Lösenordshantering**: Lösenord hashas säkert med hjälp av Spring Securitys `PasswordEncoder`, vilket säkerställer att de aldrig lagras i klartext.

- **JWT-autentisering**: Efter framgångsrik inloggning får användaren en JWT-token. Denna token används för att autentisera framtida förfrågningar. Servern validerar token vid varje begäran för att säkerställa att användaren är autentiserad.

- **Felhantering**:
    - **Användare hittades inte**: Om en användare försöker logga in med ett användarnamn som inte finns, kastas och hanteras en `UserNotFoundException` korrekt.
    - **Användare finns redan**: När en användare försöker registrera sig med ett användarnamn som redan existerar, förhindrar systemet dubbletter genom att kasta en `UserAlreadyExistsException`.

## Testning

### Enhetstestning
Projektet inkluderar **JUnit-tester** för att testa individuella komponenter isolerat, såsom tjänster och verktyg. Dessa tester säkerställer att varje enhet av applikationen fungerar som förväntat.

### Integrationstestning
Omfattande **integrationstester** inkluderas för att verifiera interaktionen mellan olika komponenter som controllers, tjänster och repositories. Dessa tester simulerar verkliga användningsfall såsom att registrera en användare, logga in med giltiga/ogiltiga uppgifter, och validering av token. Tester täcker även felhanteringsscenarier som när användare inte existerar eller när dubblettregistreringar försöks.

### Felhanteringstester
- **Användare hittades inte**: Tester säkerställer att när ett ogiltigt användarnamn används vid inloggning, kastar systemet korrekt och hanterar en `UserNotFoundException`.
- **Användare finns redan**: Tester verifierar att försök att registrera ett användarnamn som redan finns i databasen avvisas med en `UserAlreadyExistsException`.

## Hur du Kör Projektet

1. Klona repot till din lokala maskin.
2. Säkerställ att PostgreSQL är installerat och igång på din maskin, och skapa en databas för projektet.
3. Uppdatera din `application.properties` med rätt PostgreSQL-anslutningsdetaljer:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```
4. Kör `mvn clean install` för att bygga projektet och installera alla beroenden.
5. Starta applikationen med kommandot `mvn spring-boot:run`.
6. Applikationen kommer att köras på `http://localhost:8080`.

## Endpoints

### Publika Endpoints
- `POST /register`: Registrerar en ny användare. Begäran ska innehålla följande:
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
   curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d '{"username": "user1", "password": "password123", "email": "user1@example.com", "firstName": "John", "lastName": "Doe", "phoneNumber": "1234567890"}'
   ```

- `POST /login`: Loggar in en användare. Begäran ska innehålla:
   ```json
   {
     "username": "string",
     "password": "string"
   }
   ```
  Exempel:
   ```bash
   curl -X POST http://localhost:8080/login -H "Content-Type: application/json" -d '{"username": "user1", "password": "password123"}'
   ```

  Vid framgångsrik inloggning returneras en JWT-token som ska inkluderas i framtida förfrågningar i `Authorization`-huvudet:
   ```bash
   Authorization: Bearer <jwt_token>
   ```

## Projektstruktur

- **Controller**: Hanterar HTTP-förfrågningar och ansvarar för användarregistrering och inloggning.
- **Service**: Innehåller affärslogiken, inklusive lösenordskryptering, användarvalidering och token-generering.
- **Repository**: Kommunicerar med PostgreSQL-databasen för att lagra och hämta användardata.
- **Security**: Hanterar autentisering och auktorisering med hjälp av Spring Security och JWT.

## Hur du Kör Tester

1. Kör alla enhetstester och integrationstester med Maven:
   ```bash
   mvn test
   ```
2. Tester täcker både enhetstester för individuella tjänster och integrationstester för hela autentiseringsflödet, inklusive hantering av JWT-tokens och felhanteringsscenarier.

