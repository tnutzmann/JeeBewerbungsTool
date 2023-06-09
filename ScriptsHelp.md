Sicher, hier ist eine detailliertere Erklärung, wie Sie JavaScript-Dateien in Spring Boot mit Thymeleaf zusammenfassen können:

1. Erstellen Sie eine neue HTML-Datei, die Ihre JavaScript-Dateien enthält. Sie können dies mit dem `<script>`-Tag tun, indem Sie den Inhalt Ihrer JavaScript-Dateien in die HTML-Datei einbetten.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Meine Seite</title>
    <script th:inline="javascript">
        /* Inhalt von script1.js */
        /* Inhalt von script2.js */
        /* Inhalt von script3.js */
    </script>
</head>
<body>
    <!-- Inhalt der Seite -->
</body>
</html>
```

2. Erstellen Sie einen Controller, der die HTML-Datei zurückgibt. Sie können dies tun, indem Sie eine Methode in Ihrem Controller erstellen, die die HTML-Datei zurückgibt.

```java
@Controller
public class MyController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
```

3. Konfigurieren Sie Thymeleaf, um die HTML-Datei zu rendern. Sie können dies in Ihrer `application.properties`-Datei tun, indem Sie den Pfad zu Ihrer HTML-Datei angeben.

```properties
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

4. Erstellen Sie eine neue HTML-Datei, die Ihre JavaScript-Dateien enthält. Sie können dies mit dem `<script>`-Tag tun, indem Sie den Inhalt Ihrer JavaScript-Dateien in die HTML-Datei einbetten.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Meine Seite</title>
    <script th:inline="javascript">
        /* Inhalt von script1.js */
        /* Inhalt von script2.js */
        /* Inhalt von script3.js */
    </script>
</head>
<body>
    <!-- Inhalt der Seite -->
</body>
</html>
```

5. Fügen Sie Ihre JavaScript-Dateien in das Verzeichnis `src/main/resources/static/js` ein.

6. Konfigurieren Sie Thymeleaf, um Ihre JavaScript-Dateien zu rendern. Sie können dies tun, indem Sie den Pfad zu Ihren JavaScript-Dateien in Ihrer HTML-Datei angeben.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Meine Seite</title>
    <script th:src="@{/js/script1.js}"></script>
    <script th:src="@{/js/script2.js}"></script>
    <script th:src="@{/js/script3.js}"></script>
</head>
<body>
    <!-- Inhalt der Seite -->
</body>
</html>
```

Durch die Verwendung von Thymeleaf können Sie JavaScript-Dateien in Ihrer HTML-Datei einbetten und sie in Ihrer Spring Boot-Anwendung rendern, ohne dass Sie zusätzliche Tools oder Module benötigen.

Citations:
[1] https://www.dev-insider.de/views-mit-thymeleaf-erstellen-a-976811/
[2] https://www.bennyn.de/programmierung/javascript/javascript-und-css-dateien-zusammenfassen-verkleinern-und-verschleiern.html
[3] https://www.seo-suedwest.de/8133-google-gibt-6-tipps-fuer-schnellere-javascript-seiten.html
[4] https://sjmp.de/html-und-css/javascript-dateien-mit-google-buendeln/
[5] https://www.modified-shop.org/forum/index.php?topic=11667.0

By Perplexity at https://www.perplexity.ai/search/df271efe-7684-40b6-9808-70f43dbb57f1