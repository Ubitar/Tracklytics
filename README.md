### Tracklytics : a tracker library in android
**Important: the library since from https://github.com/orhanobut/tracklytics**
I Suggest everyone try to use Original author's library first

**Fix Bug List:**
1、Unbind `Aspectjx AOP` library
2、fix it doesn't work in multiple library

#### 1、Import
import `aspectjx` Dependency Library  to your `project/build.gradle`
```
buildscript {
    dependencies {
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10'
    }
}
```
apply `aspectjx` plugin in your `module/build.gradle `
```
apply plugin: 'com.hujiang.android-aspectjx'
```
And then , Add Tracklytics library for your module
```
dependencies {
    implementation 'com.ubitar.tracklytics:tracklytics:1.0.3'
}
```
#### 2、Use
Subscribe to all tracked events and send them to your preferred analytic tools.
```
Tracklytics.init(new EventSubscriber() {
  @Override public void onEventTracked(Event event) {
    // Send your events to Mixpanel, Fabric etc
  }
});
```
Add the tracker for your target function
```
@TrackEvent("event_name")
public void foo() {
}
```
#### 3、Problem
**compile error: zip file is empty**
reference  https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx/issues/287.
This is the version problems of Aspectjx，you can search found more solution in the github their  issues .

#### 4、More Use
See the Original author's
https://github.com/orhanobut/tracklytics
