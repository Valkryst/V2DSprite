[![Java CI with Maven](https://github.com/Valkryst/V2DSprite/actions/workflows/maven.yml/badge.svg)](https://github.com/Valkryst/V2DSprite/actions/workflows/maven.yml)

I will eventually publish a sample project, which demonstrates the full use of
this library.

## Table of Contents

* [Installation](https://github.com/Valkryst/V2DSprite#installation)
	* [Gradle](https://github.com/Valkryst/V2DSprite#-gradle)
	* [Maven](https://github.com/Valkryst/V2DSprite#-maven)
	* [sbt](https://github.com/Valkryst/V2DSprite#-scala-sbt)
* [Terminology](https://github.com/Valkryst/V2DSprite#terminology)
* [Folder Structure](https://github.com/Valkryst/V2DSprite#folder-structure)
* [File Structure](https://github.com/Valkryst/V2DSprite#file-structure)
* [Supported Image Formats](https://github.com/Valkryst/V2DSprite#supported-image-formats)
* [Credits & Inspiration](https://github.com/Valkryst/V2DSprite#credits--inspiration)

## Installation

V2DSprite is hosted on the [JitPack package repository](https://jitpack.io/#Valkryst/V2DSprite)
which supports Gradle, Maven, and sbt.

### ![Gradle](https://i.imgur.com/qtc6bXq.png?1) Gradle

Add JitPack to your `build.gradle` at the end of repositories.

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add V2DSprite as a dependency.

```
dependencies {
	implementation 'com.github.Valkryst:V2DSprite:2023.02.24-break'
}
```

### ![Maven](https://i.imgur.com/2TZzobp.png?1) Maven

Add JitPack as a repository.

``` xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Add V2DSprite as a dependency.

```xml
<dependency>
    <groupId>com.github.Valkryst</groupId>
    <artifactId>V2DSprite</artifactId>
    <version>2023.02.24-break</version>
</dependency>
```

### ![Scala SBT](https://i.imgur.com/Nqv3mVd.png?1) Scala SBT

Add JitPack as a resolver.

```
resolvers += "jitpack" at "https://jitpack.io"
```

Add V2DSprite as a dependency.

```
libraryDependencies += "com.github.Valkryst" % "V2DSprite" % "2023.02.24-break"
```

## Terminology

* A [SpriteSheet](https://github.com/Valkryst/V2DSprite/blob/master/src/main/java/com/valkryst/V2DSprite/SpriteSheet.java) contains one or more [Animations](https://github.com/Valkryst/V2DSprite/blob/master/src/main/java/com/valkryst/V2DSprite/Animation.java).
* An [Animation](https://github.com/Valkryst/V2DSprite/blob/master/src/main/java/com/valkryst/V2DSprite/Animation.java) contains one or more [Frames](https://github.com/Valkryst/V2DSprite/blob/master/src/main/java/com/valkryst/V2DSprite/Frame.java).
* A [Frame](https://github.com/Valkryst/V2DSprite/blob/master/src/main/java/com/valkryst/V2DSprite/Frame.java) can have a [CollisionBox](https://github.com/Valkryst/V2DSprite/blob/master/src/main/java/com/valkryst/V2DSprite/CollisionBox.java).
* A [Frame](https://github.com/Valkryst/V2DSprite/blob/master/src/main/java/com/valkryst/V2DSprite/Frame.java) can have a [Hitbox](https://github.com/Valkryst/V2DSprite/blob/master/src/main/java/com/valkryst/V2DSprite/Hitbox.java).

## Folder Structure

V2DSprite assumes that all of your data is located within the `sprites` folder,
within your `.jar` file.

Each subfolder of the `sprites` folder must contain one sprite sheet, named
`image.extension` and an `animations` folder.

The `animations` folder must contain all of the `_collisionbox.tsv`,
`_frame.tsv`, and `_hitbox.tsv` files for each animation in the sprite sheet.

See the following heirarchy, as an example:

* sprites
  * slime_green
    * image.png
    * animations
	    * idle_collisionbox.tsv
	    * idle_frame.tsv
	    * idle_hitbox.tsv
	    * walk_collisionbox.tsv
	    * walk_frame.tsv
	    * walk_hitbox.tsv
  * slime_purple
    * image.tiff
    * animations
	    * idle_collisionbox.tsv
	    * idle_frame.tsv
	    * idle_hitbox.tsv
	    * walk_collisionbox.tsv
	    * walk_frame.tsv
	    * walk_hitbox.tsv
  * skeleton
    * image.jpeg
    * animations
      * attack_collisionbox.tsv
      * attack_frame.tsv
      * attack_hitbox.tsv
      * idle_collisionbox.tsv
      * idle_frame.tsv
      * idle_hitbox.tsv
      * walk_collisionbox.tsv
      * walk_frame.tsv
      * walk_hitbox.tsv

## File Structure

Each row of the `.tsv` files correspond with one another. So, the first row in
`_frame.tsv` defines a _Frame_ and the first row in `_collisionbox.tsv` defines
the _Collision Box_ for that _Frame_.

You are not required to use the `_collisionbox.tsv` or `_hitbox.tsv` files, but
they are a useful feature for loading/working with that data in your projects.

Please remember that the values are seperated by _tabs_, not spaces.

### frame.tsv

A _Frame_ is defined by:

* X-Axis offset, from the top-left pixel of the _Sprite Sheet_.
* Y-Axis offset, from the top-left pixel of the _Sprite Sheet_.
* Width of the _Frame_ (sprite).
* Height of the _Frame_ (sprite).
* Duration, in milliseconds, that the _Frame_ should be displayed during an _Animation_.

e.g. `xOffset yOffset width height duration`

### collisionbox.tsv

A _Collision Box_ is defined by:

* X-Axis offset, from the top-left pixel of the _Frame_.
* Y-Axis offset, from the top-left pixel of the _Frame_.
* Width of the _Frame_ (sprite).
* Height of the _Frame_ (sprite).

e.g. `xOffset yOffset width height`

### hitbox.tsv

A _Hitbox_ is defined by:

* X-Axis offset, from the top-left pixel of the _Frame_.
* Y-Axis offset, from the top-left pixel of the _Frame_.
* Width of the _Frame_ (sprite).
* Height of the _Frame_ (sprite).

e.g. `xOffset yOffset width height`

## Supported Image Formats

This library uses `javax.imageio` to load images, and it supports the following
formats:

* `bmp`
* `gif`
* `jpeg`/`jpg`
* `png`
* `tiff`/`tif`
* `wbmp`

## Credits & Inspiration

* [LionEngine](https://github.com/b3dgs/lionengine)
* [LITIENGINE](https://github.com/gurkenlabs/litiengine)