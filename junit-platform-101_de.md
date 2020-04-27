# Die JUnit Platform

Oder wie entwickelt man eine eigene `TestEngine`?

## Die Idee

Aus der Notwendigkeit geboren, weil JUnit 4 als Plattform (zu) erfolgreich war.

Es gibt viele Verwendungen von `junit-4.x.jar`, zu viele davon in einer Art und Weise, die das Weiterentwickeln unmöglich machten.
Bereits das Umbennen privater Felder führt zu fehlerhaften Darstellungen in IDEs.

![junit-4-serialization-bug](https://raw.githubusercontent.com/marcphilipp/presentations/master/junit5-intro/serialization-bug.png)

Trennung der Belange.

Drei Klassen von Kunden:

- Testautoren

  Entwickler, die `@Test` tippen.

- Frameworks

  Erweiterungen, die das `@Test`-Tippen der Testautoren unterstützen.

- Tools

  Werkzeuge, die Tests ausführen und einen Report erstellen.

## Für wen ist die `TestEngine` Schnittstelle gedacht?

Für alle, die selber definieren wollen, was ein Test ist und wie solche Tests evaluiert werden.

### T³ = text transform tester

- `text.input` » `String.transform(Function)` » `text.output`
- `text.output` » `String.transform(Function)` » `text.output`, falls die Transformationsfunktion idempotent ist.

Beispiel

`hallo.input`
```
Hallo Welt!
Tschö.
```

Transformation: `String::toUppercase`

`hallo.output`
```
HALLO WELT!
TSCHÖ.
```

### Grobe Architektur von T³

```java
abstract class T³ implements TestEngine {

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
