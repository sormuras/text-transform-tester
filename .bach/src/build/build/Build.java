package build;

import de.sormuras.bach.Bach;
import de.sormuras.bach.Configuration;
import de.sormuras.bach.Project;

class Build {
  public static void main(String... args) {
    var configuration = Configuration.ofSystem();

    var project =
        Project.ofCurrentDirectory()
            .name("ttt")
            .version("1-ea")
            .withLibraryRequires("org.junit.platform.console");

    new Bach(configuration, project).build();
  }
}
