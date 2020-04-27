# Die JUnit Platform

Oder wie entwickelt man eine eigene `TestEngine`?

## Die Idee

Aus der Notwendigkeit geboren, weil JUnit 4 als Plattform (zu) erfolgreich war.

Es gibt viele Verwendungen von `junit-4.x.jar`, zu viele davon in einer Art und Weise, die das Weiterentwickeln unmöglich machten.
Bereits das Umbennen privater Felder führt zu fehlerhaften Darstellungen in IDEs.

![junit4-serialization-bug](https://raw.githubusercontent.com/marcphilipp/presentations/master/junit5-intro/serialization-bug.png)

Trennung der Belange.

Drei Klassen von Kunden:

- Testautoren

  Entwickler, die `@Test` tippen.

- Frameworks

  Erweiterungen, die das `@Test`-Tippen der Testautoren unterstützen.

- Tools

  Werkzeuge, die Tests ausführen und einen Report erstellen.

In einem Bild zusammengefasst:

![junit5-architecture-big-picture](https://github.com/marcphilipp/presentations/raw/master/junit5-intro/junit5-architecture-big-picture.svg?sanitize=true)

## Für wen ist die `TestEngine` Schnittstelle gedacht?

Für alle, die selber definieren wollen, was ein Test ist und wie solche Tests evaluiert werden.
Und genau das machen wir jetzt: `T³`

### T³ = text transform tester

- `text.input` » `String.transform(Function)` » `text(function).output`

Falls die Transformationsfunktion idempotent ist:

- `text(function).output` » `String.transform(Function)` » `text(function).output`

Beispiel

`hallo.input`
```
Hallo Welt!
Tschö.
```

Transformation: `String::toUppercase`

`hallo(uppercase).output`
```
HALLO WELT!
TSCHÖ.
```

### Pseudo-Code von T³

```java
abstract class T³ implements TestEngine {

  // discover -- anounce tests we want to execute

  // execute -- read input, apply transformation, compare expected with actual output

  public abstract String transform(String text);
  
  public List<Test> mandatedTests() {
    return List.of();
  }

  public static final class Test extends AbstractTestDescriptor {
    private final String input;
    private final String output;
  }
}
```

### Konkrete Implementierung mit Uppercase

```java
public class Uppercase extends T³ {

  @Override
  public String transform(String text) {
    return text.toUppercase();
  }

  @Override
  public List<Test> mandatedTests() {
    return List.of(new Test("Hallo", "HALLO"));
  }
}
```

## Zusammenfassung

Dieser Artikel beschreibt die zugrundeliegende Idee und die Anwendungsmöglichkeiten der JUnit Platform.
Nach einem kurzen historischen Rückblick auf JUnit 4 startet er direkt mit einem Beispiel durch:
es wird eine TestEngine entwickelt, die beliebige Texte in eine normierte Form bringt.
Hier werden einfache Textdateien als Testbeschreibung verwendet.

Danach folgt ein technischer Überblick der Funktionen der JUnit Platform.
Dabei wird der Schwerpunkt auf die Selektoren gelegt, die von TestEninges unterstützt werden können.
Der Artikel schließt mit der Erwähnung von Jupiter und Vintage als zwei vom JUnit Team bereitgestellte TestEngine
Implentierungen, sowie einem Ausblick auf bereits veröffentlichte [3rd-party TestEngines].

[3rd-party TestEngines]: https://github.com/junit-team/junit5/wiki/Third-party-Extensions#junit-platform-test-engines
