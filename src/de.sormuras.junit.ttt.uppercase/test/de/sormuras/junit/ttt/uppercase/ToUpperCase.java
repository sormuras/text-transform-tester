/*
 * Copyright (C) 2020 Christian Stein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.sormuras.junit.ttt.uppercase;

import de.sormuras.junit.ttt.Test;
import de.sormuras.junit.ttt.TextTransformTester;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ToUpperCase extends TextTransformTester {

  @Override
  protected List<Test> tests() {
    var tests = new ArrayList<Test>();
    // hand-crafted tests
    tests.add(Test.of("Abc toUpperCase yields ABC", "Abc", "ABC"));
    // tests loaded from directory
    var directory = Path.of("text");
    try (var stream = Files.newDirectoryStream(directory, "*.input")) {
      stream.forEach(input -> tests.add(Test.of("to-upper-cased", input)));
    } catch (IOException e) {
      throw new UncheckedIOException("Loading text files failed", e);
    }
    return List.copyOf(tests);
  }

  @Override
  protected String transform(String input) {
    return input.toUpperCase();
  }
}
