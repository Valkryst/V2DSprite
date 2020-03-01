package com.valkryst.V2DSprite;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SuiteHelper {
    private static final String RESOURCES_PATH = "src/test/resources/";
    private static final String ENVIRONMENT = System.getProperty("environment");

    public static boolean isEnvironmentCircleCI() {
        if (ENVIRONMENT == null) {
            return false;
        } else {
            return ENVIRONMENT.equalsIgnoreCase("circleci");
        }
    }

    public static Path getResourcePath() {
        return Paths.get(RESOURCES_PATH);
    }

    public static Path getResourcePath(final String resource) {
        return Paths.get(RESOURCES_PATH + resource);
    }
}
