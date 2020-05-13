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

package de.sormuras.ttt;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.FileSource;

/** Test-defining data holder. */
public final class /*record*/ Test {

  public static Test of(String displayName, String input, String expectedOutput) {
    return new Test(displayName, input, expectedOutput, null);
  }

  public static Test of(String function, Path input) {
    var inputName = input.getFileName().toString();
    var name = inputName.substring(0, inputName.length() - ".input".length());
    var outputName = name + ".output";
    var output = input.resolveSibling(outputName);
    var displayName = String.format("File %s equals %s file %s.", outputName, function, inputName);
    try {
      var source = FileSource.from(output.toFile());
      return new Test(displayName, Files.readString(input), Files.readString(output), source);
    } catch (IOException e) {
      throw new UncheckedIOException("Creating new test failed", e);
    }
  }

  private final String displayName;
  private final String input;
  private final String output;
  private final TestSource testSource;

  public Test(String displayName, String input, String output, TestSource testSource) {
    this.displayName = displayName;
    this.input = input;
    this.output = output;
    this.testSource = testSource;
  }

  public String displayName() {
    return displayName;
  }

  public String input() {
    return input;
  }

  public String output() {
    return output;
  }

  public TestSource testSource() {
    return testSource;
  }
}
