# simple-discord (library)

Simple-discord is a library for Discord JDA which aims to make a lot of general tasks easier and more efficient out of the box.
The main contributer to the code is still Hyfe but this is listed under a different repo so it's not directly visible.

> How do I include the library with Maven?
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.ZakShearman</groupId>
    <artifactId>simple-discord</artifactId>
    <version>Tag</version>
</dependency>
  ```

> How do I include the library with Gradle?
```gradle
allprojects {
    repositories {
	    maven { url 'https://jitpack.io' }
    }
}
dependencies {
         implementation 'com.github.ZakShearman:simple-discord:Tag'
}
```
