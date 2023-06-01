# Dokumentation von SpringBoot funktionalitäten
Im folgenden werden die wichtigsten Funktionen von SpringBoot erläutert und wie Sie in diesem Projekt vewendet werden.

Es ist gegliedert in folgende Bereiche:
 - Lombok
 - Dependency Injection
 - Database
 - Business Logic
 - Controller
 - Security

## Lombok
### Basics
Lombok ist ein Plugin, welches die Erstellung von Getter, Setter, Konstruktoren und anderen Funktionen automatisiert.
### Getter und Setter
Um Getter und Setter automatisch generieren zu lassen, muss die Annotation `@Getter` bzw. `@Setter` verwendet werden.
Das kann an einem Feld oder für die ganze Klasse verwendet werden:

```java
@Getter
@Setter
public class User {
    private String name;
}
```
oder
```java
public class User {
    @Getter
    @Setter
    private String name;
}
```

### Konstruktoren
Um Konstruktoren automatisch generieren zu lassen, gibt es drei verschiedene Annotationen:
 - `@NoArgsConstructor`: Erstellt einen Konstruktor ohne Parameter.
 - `@AllArgsConstructor`: Erstellt einen Konstruktor mit allen Parametern.
 - `@RequiredArgsConstructor`: Erstellt einen Konstruktor mit allen Parametern, die mit `@NonNull` gekennzeichnet sind bzw. `final` sind.

```java
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    private final String name;
}
```
## Dependency Injection
### Basics
Dependency Injection ist ein Entwurfsmuster, welches die Abhängigkeiten zwischen Klassen verringert. Dabei werden die Abhängigkeiten
nicht in der Klasse selbst erstellt, sondern von außen in die Klasse injiziert. Dadurch wird die Klasse unabhängiger und kann
einfacher getestet werden.

Alle Klassen, die von SpringBoot verwaltet werden sollen, müssen mit der Annotation `@Component` gekennzeichnet werden. Dabei sind Klassen mit folgenden
Annotationen auch Komponenten:
    - `@Service`: Services, die die Business Logic enthalten.
    - `@Repository`: Repositories, die die Datenbankzugriffe enthalten.
    - `@Controller`: Controller, die die REST-Endpunkte enthalten.
    - `@Configuration`: Konfigurationsklassen, die die Konfiguration der App enthalten.

Um sonst eine Klasse in den Injection Kontext zu bringen, muss eine Funktion mit der Annotation `@Bean` gekennzeichnet werden.
### Beispiele
#### Komponente
```java
@Component
public class Test {
    public void test() {
        System.out.println("Test");
    }
}
```
#### Bean
```java
@Configuration
public class TestConfig {
    @Bean
    public Test test() {
        return new Test();
    }
}
```
#### Injection
```java
@Component
@RequiredArgsConstructor
public class TestInjection {
    private final Test test;
}
```


## Database
### Entity
#### Basics

Entities sind die Objekte die in der Datenbank gespeichert werden. Sie werden mit der Annotation `@Entity` gekennzeichnet.

Jede Entity benötigt hierbei eine ID, die mit der Annotation `@Id` gekennzeichnet wird. 
Diese ID kann entweder automatisch generiert werden, oder manuell gesetzt werden. Um sie automatisch 
generieren zu lassen, muss die Annotation `@GeneratedValue` verwendet werden. Dabei können verschiedene
Generationsstrategien verwendet werden. Die wichtigsten sind:
 - `AUTO`: Die ID wird automatisch generiert. Dabei wird die Strategie von der Datenbank bestimmt.
 - `UUID`: Die ID wird automatisch generiert. Dabei wird eine UUID generiert.

#### Columns

Die Entity kann neben seiner ID auch noch weitere Attribute haben. Diese müssen lediglich als weiter Felder deklartiert
werden. Dabei können diese Felder mit der Annotation `@Column` gekennzeichnet werden. Diese Annotation kann verschiedene
Standarteinstellungen ändern. So kann beispielsweise der Spaltenname durch `@Column(name = "spaltenname")` geändert werden.
Oder es kann festgelegt werden, dass das Feld nicht null sein darf, durch `@Column(nullable = false)`.

#### Relations

Entities können auch Beziehungen zu anderen Entities haben. Für diese gibt es 4 verschiedene Annontations:
 - `@OneToOne`: Die Entity hat eine Beziehung zu einer anderen Entity.
 - `@OneToMany`: Die Entity hat eine Beziehung zu mehreren anderen Entities.
 - `@ManyToOne`: Mehrere Entities haben eine Beziehung zu einer anderen Entity.
 - `@ManyToMany`: Mehrere Entities haben eine Beziehung zu mehreren anderen Entities.

#### Auto-Generierung

Entities werden, sofern in der `application.properties` die Einstellung `spring.jpa.hibernate.ddl-auto` gesetzt ist und
in der StartApp die Annontation `@SpringBootApplication` verwendet wird, automatisch generiert. Dabei wird die Datenbank
mit den Entities synchronisiert. Dabei werden die Tabellen erstellt, die Spalten hinzugefügt und die Beziehungen zwischen
den Entities festgelegt. 

Valide werte für `spring.jpa.hibernate.ddl-auto` sind:
- `none`: Es wird nichts automatisch generiert.
- `create`: Die Datenbank wird bei jedem Start der App neu generiert. Dabei werden alle Daten gelöscht.
- `create-drop`: Die Datenbank wird bei jedem Start der App neu generiert. Dabei werden alle Daten gelöscht, wenn die App beendet wird.
- `update`: Die Datenbank wird bei jedem Start der App nach den momentanen Entities geupdated. Dabei werden die Daten nicht gelöscht.

#### Beispiel

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToOne
    private Address address;
    @OneToMany
    private List<Phone> phones;
    @ManyToOne
    private Company company;
    @ManyToMany
    private List<Department> departments;
}
```

### Repository
#### Basics

Repositories sind die Schnittstelle zwischen der Datenbank und dem Code. Sie werden mit der Annotation `@Repository` gekennzeichnet. 
Mit ihnen können in der Datenbank gespeicherte Entities abgefragt, erstellt, geändert und gelöscht werden. Dabei muss die 
Abfragelogik nicht selbst geschrieben werden, sondern kann mit Hilfe von SpringBoot automatisch generiert werden. Dafür muss
das Repository (als Interface) lediglich von der Klasse `JpaRepository` erben. Dieses Interface bietet bereits die wichtigsten Funktionen an.

#### Weitere Funktionen

Um weitere Abfragen zu ermöglichen, können weitere Funktionen in das Repository geschrieben werden. Dabei muss lediglich die
Funktion mit der Annotation `@Query` gekennzeichnet werden. In dieser Funktion kann dann die Abfrage geschrieben werden. 

Zudem gibt es eine Standardsyntax für Funktionennamen, die automatisch in eine Abfrage umgewandelt werden. Diese Syntax ist beispielsweise:
 - `findBy<Attributname>`: Sucht nach einer Entity mit dem Attribut `<Attributname>`.

Um das nicht selbst machen zu müssen, gibt es das Plugin JPABuddy, welche diese Funktionen durch clicken generiert.

#### Beispiel

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.name = :name")
    List<User> findByName(@Param("name") String name);
    
    User findByAddress(Address address);
}
```

## Business Logic
### Basics

Die Business Logic ist der Code, der die Logik der Anwendung enthält. Dabei wird dieser Code in `Services` geschrieben.
Um einen Service zu erstellen, muss lediglich die Klasse mit der Annotation `@Service` gekennzeichnet werden.
### Beispiel

```java
@Service
public class UserService {
    
    public int add(int a, int b) {
        return a + b;
    }
}
```
## Controller
### Basics

Controller sind die Schnittstelle zwischen der Business Logic und dem User. Sie werden mit der Annotation `@Controller` gekennzeichnet.
In ihnen werden verschiedene Routen definiert, die aufgerufen werden können. Dabei wird die Funktion, die aufgerufen werden soll, mit 
den Annontationen:
- `@GetMapping`: Die Funktion wird aufgerufen, wenn die Route mit GET aufgerufen wird.
- `@PostMapping`: Die Funktion wird aufgerufen, wenn die Route mit POST aufgerufen wird.
- `@PutMapping`: Die Funktion wird aufgerufen, wenn die Route mit PUT aufgerufen wird.
- `@DeleteMapping`: Die Funktion wird aufgerufen, wenn die Route mit DELETE aufgerufen wird.

gekennzeichnet. In dieser Annotation kann die Route angegeben werden. Dabei können auch Platzhalter verwendet werden. Diese werden
mit `{}` gekennzeichnet. In der Funktion können dann die Platzhalter mit der Annotation `@PathVariable` als Parameter übergeben werden.

#### Beispiel

```java
@Controller
public class UserController {
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Long id) {
        return id.toString();
    }
}
```

### Templates
#### Basics

Da wir hier mit Thymleaf arbeiten können wir auch Templates verwenden. Diese werden in dem Ordner `resources/templates` gespeichert.
Um ein Template zu verwenden, muss lediglich der Name des Templates zurückgegeben werden. Dabei muss der Name ohne die Endung `.html`
angegeben werden.

#### Daten übergeben
Um Daten an das Template zu übergeben, muss lediglich ein `Model` als Parameter in die Controller-Funktion übergeben werden. 
In diesem können dann die Daten gespeichert werden. Dabei wird der Name des Attributes als Key und der Wert als Value gespeichert.
Im Template kann dann auf die Daten mit dem Key zugegriffen werden.

#### Beispiel

```java
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "user";
    }
}
```

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>User</title>
</head>
<body>
    <h1 th:text="${user.name}"></h1>
</body>
</html>
```
#### Redirects

##### Basics

Um einen Redirect an einen andere Contoller-Funktion zu machen, muss lediglich `redirect:` vor den Namen des Templates geschrieben werden.
Dabei können auch Model-Attribute übergeben werden. 

##### Beispiel

```java

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Long id, RedirectAttributes model) {
        User user = userService.getUser(id);
        model.addFlashAttribute("user", user);
        return "redirect:/user";
    }
    @GetMapping("/user")
    public String getUser(Model model) {
        return "user";
    }
}
```

##### Erweitert

Wir haben bei uns in der Anwendung einen `ModelConfigurer`. Dieser erleichtert es uns Daten ans Model zu übergeben. Vorallem mit
redirects. Denn bei Duplikaten werden die Values nicht überschrieben, so verhindert man, dass Daten im redirect verloren gehen.
Zudem wird zwischen einem Model und RedirectAttributes unterschieden. 

__Usage:__

```java
import de.jinba.server.util.ModelConfigurer;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Long id, RedirectAttributes model) {
        User user = userService.getUser(id);
        ModelConfigurer.of(model)
                .with("user", user); // Da wir RedirectAttributes verwenden, wird das als FlashAttribute gespeichert.
        return "redirect:/user";
    }
    @GetMapping("/user")
    public String getUser(Model model) {
        ModelConfigurer.of(model)
                .with("user", "lol"); // Bei einem redirect von /user/{id} wird das gesetzte FlashAttribute nicht überschrieben.
        return "user";
    }
}
```

#### Formulare
##### Basics
In Thymeleaf lassen sich auch Formulare verwenden.
Dabei gibt es mehrere Steps:
1. Wir überlegen uns, welche Daten wir brauchen. Und erstellen dafür eine Klasse:
```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {
    private String name;
    private String password;
}
```
2. Wir fügen die Formulardaten zum Model im Controller hinzu:
```java
@Controller
public class UserController {
    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "form";
    }
}
```
3. Wir erstellen das Formular im Template. Hier können wir mittels th:object die Daten aus dem Model verwenden: 
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Form</title>
</head>
<body>
    <!-- Wir geben hier die Action an, welche Variable verwenden werden soll hier userForm, 
    welches wir im Model hinzugefügt haben und welche methode verwendet werden soll (get oder post)-->
    <form th:action="@{/form}" th:object="${userForm}" method="post">
        <!-- Um fields in der Klasse zu 'binden' verwenden wir th:field mit der *{} syntax-->
        <input type="text" th:field="*{name}" />
        <input type="password" th:field="*{password}" />
        <input type="submit" value="Submit" />
    </form>
</body>
</html>
```
4. Wir erstellen eine Controller-Funktion, welche die Daten entgegen nimmt:
```java
@Controller
public class UserController {
    @PostMapping("/form")
    public String form(@ModelAttribute UserForm userForm) {
        System.out.println(userForm.getName());
        System.out.println(userForm.getPassword());
        return "form";
    }
}
```
5. Wir können auch Validierungen hinzufügen. Dafür müssen wir lediglich die Klasse mit der Annotation `@Valid` kennzeichnen und 
das BindingResult als Parameter hinzufügen.

```java
import org.springframework.validation.BindingResult;

@Controller
public class UserController {
    @PostMapping("/form")
    public String form(@ModelAttribute @Valid UserForm userForm, BindingResult bindingResult) {
        // Hier gucken wir, ob es Fehler gibt. Wenn ja, dann geben wir das Formular zurück.
        // Um validitäten zu setzen, müssen wir in der Klasse die Annotationen hinzufügen.
        // Bsp: @NotNull, @NotEmpty, @Size(min = 3, max = 10), etc.
        if (bindingResult.hasErrors()) {
            return "form";
        }
        System.out.println(userForm.getName());
        System.out.println(userForm.getPassword());
        return "form";
    }
}
```
#### Fragments
Um html-Code zu wiederverwenden, können wir Fragments verwenden. Diese werden in dem Ordner `resources/templates/fragments` gespeichert.
__Beispiel:__
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Fragment</title>
</head>
<body>
    <div th:fragment="header">
        <h1>Header</h1>
    </div>
    <div th:fragment="footer">
        <h1>Footer</h1>
    </div>
</body>
</html>
```
Um diese Fragments zu verwenden, müssen wir sie in dem Template einbinden:
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Fragment</title>
</head>
<body>
    <div th:replace="fragments/header :: header"></div>
    <div th:replace="fragments/footer :: footer"></div>
</body>
</html>
```
## Security
### Basics
Damit wir Security in unserer Anwendung verwenden können, müssen wir die Abhängigkeit `spring-boot-starter-security` hinzufügen.
Dadurch können wir den Zugriff auf unsere Anwendung einschränken und Nutzer authentifizieren und autorisieren. (Und vieles mehr)
### Sessions
Um Sessions zu verwenden, müssen wir folgende Abhängigkeit hinzufügen:
```xml
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-core</artifactId>
</dependency>
```
Im folgenden wir davon ausgegangen, dass wir Sessions verwenden und eine Datenbankverbindung haben.
### UserDetails
Damit SpringBoot weiß, welche Daten wir für einen Nutzer verwenden, müssen wir eine (User-)Klasse erstellen, welche das Interface `UserDetails` implementiert.
```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;

    // Funktion gibt die Rollen des Nutzers zurück.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
    // Andere funktionen hier weglassen
}
```
### UserDetailsService
Damit SpringBoot weiß, wie es die User-Daten aus der Datenbank holen soll, müssen wir einen Service erstellen, welche das Interface `UserDetailsService` implementiert.

Es wird dabei davon ausgegangen, dass wir eine UserRepository haben, welche die Daten aus der Datenbank holt.
```java
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    // Funktion gibt die User-Daten aus der Datenbank zurück.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
```
### SecurityConfig
Damit wir nun die Security Funktionen verwenden können müssen wir ein bisschen was konfigurieren:
1. Wie soll das Passwort gehashed werden?
```
@Bean 
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
2. Einen AuthenticationManager konfigurieren:
```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
    return auth.getAuthenticationManager();
}
```
Hier wird von Spring einiges automatisch konfiguriert:
- `PasswordEncoder`: Da wir eine Bean erstellt haben, wird diese automatisch verwendet.
- `UserDetailsService`: Da wir einen Service erstellt haben, wird dieser automatisch verwendet.

Und mehr wird nicht benötigt!

3. Die `SecurityFilterChain` konfigurieren:
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors() // Cross-Origin Resource Sharing wird aktiviert
            .and()
            .csrf().disable() // Cross-Site Request Forgery cookies werden deaktiviert
            .formLogin() // Formular Login wird aktiviert
            .defaultSuccessUrl("/dashboard", true) // Wenn login erfolgreich, dann wird auf /dashboard weitergeleitet
            .and()
            .logout() // Logout wird aktiviert
            .logoutSuccessUrl("/login?logout=true") // Wenn logout erfolgreich, dann wird auf /login weitergeleitet
            .invalidateHttpSession(true) // Session wird invalidiert
            .deleteCookies("SESSION") // Session-Cookie wird gelöscht
            .and()
            .sessionManagement() // Session-Management wird aktiviert
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Session wird nur erstellt, wenn benötigt
            .and()
            .authorizeHttpRequests() // Authorisierung wird aktiviert
            .requestMatchers("/profile").authenticated() // Wenn auf /profile zugegriffen wird, dann muss der Nutzer authentifiziert sein
            .requestMatchers("/**").permitAll(); // Alle anderen Anfragen sind erlaubt
        return http.build(); // FilterChain wird zurückgegeben
}
```
