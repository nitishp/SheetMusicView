# SheetMusicView
This is a simple library to help incorporate sheet music quickly into your android application. It is compatible with all API versions 8 and higher.

# Example

The following picture shows SheetMusicView in action:
![Example of sheetmusicview](/images/noteExample.png)

# Usage

Currently, the fastest way to include the view into your project is to include it into one of your layout XML files. The ```MusicBarView``` represents the seven lines that make up an empty music bar, and every single note that you want to insert into this bar is called a ```NoteView```. Make sure to include every ```NoteView``` nested inside of a ```MusicBarView```. An example of this can be seen by the following code snippet:

```
<com.nitishp.sheetmusic.MusicBarView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/view">
    <com.nitishp.sheetmusic.NoteView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		app:noteValue="LOWER_B"
		app:noteDuration="HALF"/>
	<com.nitishp.sheetmusic.NoteView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		app:noteValue="LOWER_C"
		app:noteDuration="HALF"/>
	<com.nitishp.sheetmusic.NoteView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		app:noteValue="HIGHER_E"
		app:noteDuration="FOURTH"/>
	<com.nitishp.sheetmusic.NoteView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		app:noteValue="HIGHER_B"
		app:noteDuration="FOURTH"/>
	<com.nitishp.sheetmusic.NoteView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		app:noteValue="HIGHER_C"
		app:noteDuration="FOURTH"/>
</com.nitishp.sheetmusic.MusicBarView>
```

# Installation

This library is deployed with the help of jitpack. Add the following library dependency to your app's ```build.grade``` file:

```
dependencies {
    compile 'com.github.nitishp:sheetmusicview:v1.2.0'
}
```

Also, add the following to your project's ```build.gradle``` file:

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```


# Contributing

This is a library that I hope to grow into something that is in a lot better of a state than it is currently in. Some of the features that I hope to add include:

* The ability to drag and drop notes into the view.
* Making the view look more like a bar and possibly include treble and clef symbols.
* Add easier support dynamically for changing the value of a note in code
* Any other feature that you think would be useful for any user in the future!

This is also the first Android library that I have ever written. If you find some style mistakes or other inefficiencies, please feel free to make a pull request or inform me on how I can make this library better!

# License

```
Copyright 2016 Nitish Paradkar

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