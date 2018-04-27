package com.valkryst.V2DSprite;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SuiteHelper {
    private static final String RESOURCES_PATH = "src/test/resources/";

    public static Path getResourcePath() {
        return Paths.get(RESOURCES_PATH);
    }

    public static Path getResourcePath(final String resource) {
        return Paths.get(RESOURCES_PATH + resource);
    }
}
