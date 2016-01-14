[![Build Status](https://travis-ci.org/Data5tream/emoji-lib.svg?branch=master)](https://travis-ci.org/Data5tream/emoji-lib) [![Coverity Scan](https://scan.coverity.com/projects/7409/badge.svg)](https://scan.coverity.com/projects/data5tream-emoji-lib)

# Emoji Support Lib

Add Emoji support to your Android app!

This library combines the works of [@rocboronat](https://github.com/rocboronat/emojicon), [@ankushsachdeva](https://github.com/ankushsachdeva/emojicon) and [@peibumur](https://github.com/pepibumur/emojize) and my one continuation of this project.

![Screenshot](http://i.imgur.com/rI6hfXA.png)

## Installation

Gradle:

```
repositories {
    ...
    maven { url "https://jitpack.io" }
  }

...

dependencies {
    compile 'com.github.Data5tream:emoji-lib:0.0.2.1'
   }


```
Maven:
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

...

  <dependency>
    <groupId>com.github.data5tream</groupId>
    <artifactId>emoji-lib</artifactId>
    <version>0.0.2.1</version>
  </dependency>
```

## Basic usage

Use `com.github.data5tream.emojilib.EmojiTextView` and `com.github.data5tream.emojilib.EmojiEditText` in your layout XMLs to automatically represent unicode emojis.

Use `EmojiParser` to convert Emoji Cheat Sheet codes into unicode characters:
```java
String formattedAsCheatCode = ":smile:";
String formattedAsUnicode = EmojiParser.parseEmojis(formattedAsCheatCode);
```
`formattedAsUnicode` will be: :smile:

You can also use `EmojiParser` to convert unicode emojis into Emoji Cheat Sheet codes:
```java
String formattedAsUnicode = "😄";
String formattedAsCheatCode = EmojiParser.convertToCheatCode(formattedAsUnicode);
```
`formattedAsCheatCode` will be: `:smile:`

To use the emoji-keyboard, take a look at the [example code](https://github.com/Data5tream/emoji-lib/blob/master/example/src/main/java/com/github/data5tream/emojilib/example/MainActivity.java).

## Acknowledgements

Emoji Support Lib uses emoji graphics from [emoji-cheat-sheet.com](https://github.com/arvida/emoji-cheat-sheet.com/tree/master/public/graphics/emojis).

This project has a dependency on [Guava](https://github.com/google/guava).

## License

```
Copyright 2016 Simon Barth

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
