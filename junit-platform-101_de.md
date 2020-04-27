# Die JUnit Platform

Oder wie entwickelt man eine eigene `TestEngine`?

## Klappentext

Dieser Artikel beschreibt die zugrundeliegende Idee und die Anwendungsmöglichkeiten der JUnit Platform.
Nach einem kurzen historischen Rückblick auf JUnit 4 startet er direkt mit einem Beispiel durch:
es wird eine TestEngine entwickelt, die beliebige Texte in eine normierte Form bringt.
Hier werden einfache Textdateien als Testbeschreibung verwendet.

Danach folgt ein technischer Überblick der Funktionen der JUnit Platform.
Dabei wird der Schwerpunkt auf die Selektoren gelegt, die von TestEninges unterstützt werden können.
Der Artikel schließt mit der Erwähnung von Jupiter und Vintage als zwei vom JUnit Team bereitgestellte TestEngine
Implentierungen, sowie einem Ausblick auf bereits veröffentlichte [3rd-party TestEngines].

## Die Idee

Aus der Notwendigkeit geboren, weil JUnit 4 als Plattform (zu) erfolgreich war.

## Für wen ist die TestEngine gedacht?

Für alle, die selber definieren wollen, was ein Test ist und wie solche Tests evaluiert werden.

## `T³ = text transform tester`

- `test1.input` » `String.transform(Function)` » `test1.output`
- `test1.output` » `String.transform(Function)` » `test1.output`, falls die Transformationsfunktion idempotent ist.

Beispiel

`test-hallo.input`
```
Hallo Welt!
Tschö.
```

Transformation: `String::toUppercase`

`test-hallo.output`
```
HALLO WELT!
TSCHÖ.
```

[3rd-party TestEngines]: https://github.com/junit-team/junit5/wiki/Third-party-Extensions#junit-platform-test-engines
