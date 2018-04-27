[![Release](https://jitpack.io/v/Valkryst/V2DSprite.svg)](https://jitpack.io/#Valkryst/VTerminal) [![Total alerts](https://img.shields.io/lgtm/alerts/g/Valkryst/V2DSprite.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Valkryst/V2DSprite/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Valkryst/V2DSprite.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Valkryst/V2DSprite/context:java)

## Jar Files & Maven

The Maven dependency is hosted off of JitPack, so you will need to add JitPack as a repository before you add V2DSprite as a dependency.

### Maven

JitPack:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Dependency:

```xml
<dependency>
    <groupId>com.github.Valkryst</groupId>
    <artifactId>V2DSprite</artifactId>
    <version>2020.01.20-FULL_REWRITE.0</version>
</dependency>
```

### Jar

Jar files can be found on the [releases](https://github.com/Valkryst/V2DSprite/releases) page.

### Loading a SpriteSheet

To load a SpriteSheet from the filesystem, you could use the following code.

```java
final Path imagePath = Paths.get("./test_res/image.png");
final Path jsonPath = Paths.get("./test_res/data-valid.json");
final SpriteSheet spriteSheet = new SpriteSheet(imagePath, jsonPath);
```

To load a SpriteSheet from within the JAR, you can use the following code.

```java
final Path imagePath = Paths.get(ClassName.class.getResource("./image.png").toURI());
final Path jsonPath = Paths.get(ClassName.class.getResource("./data-valid.png").toURI());
final SpriteSheet spriteSheet = new SpriteSheet(imagePath, jsonPath);
```

### Defining a SpriteSheet

The Sprites of a SpriteSheet are defined VIA an array of JSON objects.

* name - A unique ID for the sprite.
    * Capital letters are ignored, so the words "Yellow", "YeLLoW", and "yellow" are all considered to be
    the same.
* x - The x-axis position of the Sprite's top-left pixel, within the SpriteSheet.
* y - The y-axis position of the Sprite's top-left pixel, within the SpriteSheet.
* width - The width of the Sprite, in pixels.
* height - The height of the Sprite, in pixels.

```json
[
    {
        "name": "Sprite A",
        "x": 0,
        "y": 0,
        "width": 32,
        "height": 32
    },
    {
        "name": "Sprite B",
        "x": 32,
        "y": 0,
        "width": 32,
        "height": 32
    },
    {
        "name": "Sprite C",
        "x": 0,
        "y": 32,
        "width": 32,
        "height": 32
    },
    {
        "name": "SpriteD",
        "x": 32,
        "y": 32,
        "width": 32,
        "height": 32
    }
]
```