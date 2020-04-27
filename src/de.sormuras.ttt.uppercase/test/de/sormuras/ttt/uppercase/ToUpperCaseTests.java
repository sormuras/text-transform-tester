package de.sormuras.ttt.uppercase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class ToUpperCaseTests {

  @Test
  void checkEngineProperties() {
    var engine = new ToUpperCase();
    assertEquals("de.sormuras.ttt.uppercase.ToUpperCase", engine.getId());
    assertEquals(Optional.empty(), engine.getGroupId());
    assertEquals(engine.getClass().getModule().getName(), engine.getArtifactId().orElseThrow());
    assertEquals("DEVELOPMENT", engine.getVersion().orElseThrow());
  }
}
