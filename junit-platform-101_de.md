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

## Unter der Haube einer `TestEngine`

Platform als Mittler zwischen Tools und TestEngines.

### EngineDiscoveryRequest

EngineDiscoveryRequest provides a TestEngine access to the information necessary to discover tests and containers.
A request is comprised of selectors and filters. While the former select resources that engines can use to discover tests, the latter specify how such resources are to be filtered. All of the filters have to include a resource for it to end up in the test plan.

In addition, the supplied configuration parameters can be used to influence the discovery process.

### Discovery Selektoren

Im Module `org.junit.platform.engine` ist im gleichnamigen Package das Interface `DiscoverySelector` deklariert.
Ein `DiscoverySelector` definiert auf welchem Weg eine `TestEngine` ihre Tests finden kann.
Zum Beispiel über den Namen einer Javaklasse, 
— for example, the name of a Java class, the path to a file or directory, etc.

Das Package `org.junit.platform.engine.discovery` enthält in der Version 1.7 der JUnit Platform folgende konkrete Selektoren.

1. `ClasspathResourceSelector` seit 1.0

    A DiscoverySelector that selects the name of a classpath resource so that TestEngines can load resources from the classpath — for example, to load XML or JSON files from the classpath, potentially within JARs.

1. `ClasspathRootSelector` seit 1.0

    A DiscoverySelector that selects a classpath root so that TestEngines can search for class files or resources within the physical classpath — for example, to scan for test classes.

1. `ClassSelector` seit 1.0

    A DiscoverySelector that selects a Class or class name so that TestEngines can discover tests or containers based on classes.
    If a Java Class reference is provided, the selector will return that Class and its class name accordingly. If a class name is provided, the selector will only attempt to lazily load the Class if getJavaClass() is invoked.

    In this context, Java Class means anything that can be referenced as a Class on the JVM — for example, classes from other JVM languages such Groovy, Scala, etc.

1. `DirectorySelector` seit 1.0

    A DiscoverySelector that selects a directory so that TestEngines can discover tests or containers based on directories in the file system.

1. `FileSelector` seit 1.0

    A DiscoverySelector that selects a file so that TestEngines can discover tests or containers based on files in the file system.

1. `MethodSelector` seit 1.0

    A DiscoverySelector that selects a Method or a combination of class name, method name, and parameter types so that TestEngines can discover tests or containers based on methods.
    If a Java Method is provided, the selector will return that method and its method name, class name, and parameter types accordingly. If a Class and method name, a class name and method name, or simply a fully qualified method name is provided, this selector will only attempt to lazily load the Class and Method if getJavaClass() or getJavaMethod() is invoked.

    In this context, a Java Method means anything that can be referenced as a Method on the JVM — for example, methods from Java classes or methods from other JVM languages such Groovy, Scala, etc.

1. `ModuleSelector` seit 1.1

    A DiscoverySelector that selects a module name so that TestEngines can discover tests or containers based on modules.

1. `NestedClassSelector` seit 1.6

    A DiscoverySelector that selects a nested Class or class name enclosed in other classes so that TestEngines can discover tests or containers based on classes.
    If Java Class references are provided for the nested class or the enclosing classes, the selector will return these Class and their class names accordingly. If class names are provided, the selector will only attempt to lazily load the Class if getEnclosingClasses() or getNestedClass() are invoked.
    
    In this context, Java Class means anything that can be referenced as a Class on the JVM — for example, classes from other JVM languages such Groovy, Scala, etc.

1. `NestedMethodSelector` seit 1.6

    A DiscoverySelector that selects a nested Method or a combination of enclosing classes names, class name, method name, and parameter types so that TestEngines can discover tests or containers based on methods.
    If a Java Method is provided, the selector will return that method and its method name, class name, enclosing classes names and parameter types accordingly. If class or methods names are provided, this selector will only attempt to lazily load the Class and Method if getEnclosingClasses(), getNestedClass() or getMethod() is invoked.
    
    In this context, a Java Method means anything that can be referenced as a Method on the JVM — for example, methods from Java classes or methods from other JVM languages such Groovy, Scala, etc.

1. `PackageSelector` seit 1.0

    A DiscoverySelector that selects a package name so that TestEngines can discover tests or containers based on packages.

1. `UniqueIdSelector` seit 1.0

    A DiscoverySelector that selects a UniqueId so that TestEngines can discover tests or containers based on unique IDs.

1. `UriSelector` seit 1.0

    A DiscoverySelector that selects a URI so that TestEngines can discover tests or containers based on URIs.

### Discovery Filter

A DiscoveryFilter is applied during test discovery to determine if a given container or test should be included in the test plan.
TestEngines should apply DiscoveryFilters during the test discovery phase.

### ExecutionRequest

Provides a single TestEngine access to the information necessary to execute its tests.
A request contains an engine's root TestDescriptor, the EngineExecutionListener to be notified of test execution events, and ConfigurationParameters that the engine may use to influence test execution.

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
