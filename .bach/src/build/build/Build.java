package build;

import de.sormuras.bach.Bach;
import de.sormuras.bach.Configuration;
import de.sormuras.bach.Project;

class Build {
  public static void main(String... args) {
    var configuration = Configuration.ofSystem();
    var project = Project.ofCurrentDirectory();
    new Bach(configuration, project).build();
  }
}
