![](https://codebuild.us-east-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiWDdWSDd6YlB3MlRqVlQyVGJKWjlRdmUxRGRxbGN1dWZvQVIxWTBXL0w5UzFIOUlZQ2ZlWTkvL3lidmJJV2l3VUlpVGc4aElXTytVZTVkMmlXUGswWFo4PSIsIml2UGFyYW1ldGVyU3BlYyI6IldnaUFTQ05RaWkxWDltc3IiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master) [![Release](https://jitpack.io/v/Valkryst/V2DSprite.svg)](https://jitpack.io/#Valkryst/V2DSprite)

## Jar Files & Maven

The Maven dependency is hosted off of JitPack, so you will need to add JitPack as a repository before you add V2DSprite as a dependency.

### Maven

JitPack ([Example](https://github.com/Valkryst/V2DSprite/blob/master/pom.xml)):

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

Dependency:

    <dependency>
        <groupId>com.github.Valkryst</groupId>
        <artifactId>V2DSprite</artifactId>
        <version>1.0.1</version>
    </dependency>

### Jar

Jar files can be found on the [releases](https://github.com/Valkryst/V2DSprite/releases) page.

## Example Usage

* A *SpriteAtlas* is a collection of *SpriteSheet*s.
* A *SpriteSheet* is a collection of *SpriteAnimation*s.
* A *SpriteAtlas* is loaded using an image and a JSON file.
    * The JSON file defines each *SpriteSheet* in the *SpriteAtlas*.
    * The JSON file defines each *SpriteAnimation* in each *SpriteSheet* of the *SpriteAtlas*.

### SpriteAtlas JSON Format

A *SpriteAtlas* is defined as a collection of *SpriteSheet*s, so we begin by creating an array of *"Sheets"*.

```json
{
    "Sheets": [
    ]
}
```

Within the *Sheets* array, each *SpriteSheet* is defined using a *"Name"* and an array of *"Animations"*. The *"Animations"* define different sets of animations in a *SpriteSheet*.

```json
{
    "Sheets": [
      {
        "Name": "Enemy",
        "Animations": [
        ]
      }
    ]
}
```

We'll now create a *"Standing"* and *"Walking"* set of animations.

```json
{
    "Sheets": [
      {
        "Name": "Enemy",
        "Animations": [
          {
            "Name": "Standing",
            "Frames": [
            ]
          },
          {
            "Name": "Walking",
            "Frames": [
            ]
          }
        ]
      }
    ]
}
```

Imagine our sprite sheet looking like this, where each *Image* is a 32x32 image.

|      | Frame 1 | Frame 2 | Frame 3 |
|----|:------:|:------:|:------:|
| Standing| Image | Empty | Empty |
| Walking | Image | Image | Image |

So, we'll now add each frame to the animations.

```json
{
    "Sheets": [
      {
        "Name": "Enemy",
        "Animations": [
          {
            "Name": "Standing",
            "Frames": [
              {
                "x": 0,
                "y": 0,
                "width": 32,
                "height": 32
              }
            ]
          },
          {
            "Name": "Walking",
            "Frames": [
              {
                "x": 0,
                "y": 32,
                "width": 32,
                "height": 32
              },
              {
                "x": 32,
                "y": 32,
                "width": 32,
                "height": 32
              },
              {
                "x": 64,
                "y": 32,
                "width": 32,
                "height": 32
              }
            ]
          }
        ]
      }
    ]
}
```

That's it, the JSON file is complete. You could add additional sheets, animations, and 
frame if you like. An example JSON file and sprite sheet image can be found in the 
*test_res* directory.  

## Loading & Animating

You can see a rough example of this in the *Driver* class of the *test* directory.

```java
// Load a SpriteAtlas by creating input streams to the image and JSON files that define it.
final FileInputStream atlasImageStream = new FileInputStream("test_res/Atlas.png");
final FileInputStream atlasJSONStream = new FileInputStream("test_res/Atlas.json");
final SpriteAtlas atlas = new SpriteAtlas(atlasImageStream, atlasJSONStream);
```

```java
// Retrieve SpriteAnimations from the SpriteAtlas
final SpriteAnimation enemyA = atlas.getSpriteSheet("Enemy").getAnimation("Standing");
final SpriteAnimation enemyB = atlas.getSpriteSheet("Enemy").getAnimation("Standing");
```

```java
// Within some form of update-render loop, simply increment the animation frame and draw it to the screen.
enemyA.toNextFrame();
enemyB.toNextFrame();

final Graphics2D gc = getSomeGraphicsContext();
enemyA.draw(gc, new Point(0, 32));
enemyB.draw(gc, new Point(64, 128));
```