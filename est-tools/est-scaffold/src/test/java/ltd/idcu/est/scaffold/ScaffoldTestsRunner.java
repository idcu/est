package ltd.idcu.est.scaffold;

import ltd.idcu.est.test.Tests;

public class ScaffoldTestsRunner {
    public static void main(String[] args) {
        Tests.run(
            ProjectConfigTest.class,
            ProjectTypeTest.class,
            TemplateFileSystemTest.class
        );
    }
}
