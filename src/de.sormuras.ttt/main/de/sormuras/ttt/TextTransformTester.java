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

import java.util.List;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.opentest4j.AssertionFailedError;

/** An abstract {@link TestEngine} to be extended by transformation-providing implementations. */
public abstract class TextTransformTester implements TestEngine {

  @Override
  public String getId() {
    return getClass().getName();
  }

  @Override
  public org.junit.platform.engine.TestDescriptor discover(
      EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
    var engine = new EngineDescriptor(uniqueId, getClass().getSimpleName());
    var iterator = tests().listIterator();
    while (iterator.hasNext()) {
      var id = uniqueId.append("test", "#" + iterator.nextIndex());
      var test = iterator.next();
      var descriptor = new TestDescriptor(id, test.displayName(), test, test.testSource());
      engine.addChild(descriptor);
    }
    return engine;
  }

  protected abstract List<Test> tests();

  @Override
  public void execute(ExecutionRequest executionRequest) {
    var engine = executionRequest.getRootTestDescriptor();
    var listener = executionRequest.getEngineExecutionListener();
    listener.executionStarted(engine);
    for (var child : engine.getChildren()) {
      if (child instanceof TestDescriptor) {
        var descriptor = (TestDescriptor) child;
        listener.executionStarted(descriptor);
        var actual = transform(descriptor.getTest().input());
        var expected = descriptor.getTest().output();
        if (expected.equals(actual)) {
          listener.executionFinished(descriptor, TestExecutionResult.successful());
          continue;
        }
        var message = "Mismatch of expected and actual output";
        var error = new AssertionFailedError(message, expected, actual);
        listener.executionFinished(descriptor, TestExecutionResult.failed(error));
      }
    }
    listener.executionFinished(engine, TestExecutionResult.successful());
  }

  protected abstract String transform(String input);
}
