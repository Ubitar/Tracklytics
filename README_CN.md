### Tracklytics  一个安卓埋点库
**声明：该库是由 https://github.com/orhanobut/tracklytics 作者的埋点库修改而来**
推荐先看下原作者的文档先，他写的更详细，我的仅仅是修复了问题及导入方式不同而已，使用上依然是一样的。

修复了如下问题：
1、解耦了`Aspectjx AOP`库
2、修复无法跨库使用的问题

#### 1、导入
在你的项目主` build.gradle `中加入`aspectjx`依赖
```
buildscript {
    dependencies {
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10'
    }
}
```
在所需要用到埋点的模块`module/build.gradle`中引用`aspectjx`的插件
```
apply plugin: 'com.hujiang.android-aspectjx'
```
以及引用依赖
```
dependencies {
    implementation 'com.ubitar.tracklytics:tracklytics:1.0.3'
}
```
#### 2、使用
注册埋点监听器，每当出发埋点时将会调用
```
Tracklytics.init(new EventSubscriber() {
  @Override public void onEventTracked(Event event) {
    // Send your events to Mixpanel, Fabric etc
  }
});
```
添加埋点事件
```
@TrackEvent("event_name")
public void foo() {
}
```
#### 3、常见问题
**编译时出现 zip file is empty**
参考https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx/issues/287 这个是Aspectjx的版本问题，多去issues里搜搜解决办法

#### 4、更多使用功能（从这里我就直接搬运了，太多了）
### @TrackEvent

***Scope:** Method*

Track an event and notify the subscriber(EventSubscriber) on each method invocation.

```source-java
@TrackEvent("event_name")
public void foo() {
}
```
### [](https://github.com/orhanobut/tracklytics#attribute)@Attribute

***Scope:** Method, Method parameters*

There are multiple ways to add an attribute to the corresponding event. Assigns the values in runtime dynamically.

By using method parameters: Parameter value will be used as attribute value.

```source-java
@TrackEvent("event_name")
public void foo(@Attribute("attribute_key") String name) {
  // something
}
```

By using the return value of the method as attribute value.

```source-java
@TrackEvent("event_name")
@Attribute("attribute_key")
public String foo() {
  // something
  return "attribute_value";
}
```

Set a default value when the expected value is null.

```source-java
@TrackEvent("event_name")
public void foo(@Attribute(value="attribute_key", defaultValue="defaultValue") String name) {
  // something
}
```

### [](https://github.com/orhanobut/tracklytics#fixedattribute)@FixedAttribute

***Scope:** Method, Class*

If the attribute values are constant, use FixedAttribute.

On method: Only this event will have this fixed attribute.

```source-java
@TrackEvent("eventName")
@FixedAttribute(key="Login", value="Success")
public void foo(){
}
```

On class: These attributes will be added to each event that is triggered within this class. For example: Following `foo()` method will also have `screen_name` attribute.

```source-java
@FixedAttribute(key="screen_name", value="Login")
public class LoginPresenter{

  @TrackEvent("login")
  public void onLoggedIn(){
  }
}
```

### [](https://github.com/orhanobut/tracklytics#fixedattributes)@FixedAttributes

***Scope:** Method, Class*

Prior to Java 8, repeated annotations are not available. Sometimes you may need more fixed attributes. Use this annotation to add multiple attributes

```source-java
@TrackEvent("event_name")
@FixedAttributes({
  @FixedAttribute(key="name", value="Something"),
  @FixedAttribute(key="last_name", value="Something")
})
public void foo(){
}
```

### [](https://github.com/orhanobut/tracklytics#filters)Filters

Use filters to differentiate some events. You may only want to send specific events to a specific analytics tool. ie: Send login event to Fabric.

```source-java
@TrackEvent(value="event_name",filters=100)
public void foo() {
}
```

### [](https://github.com/orhanobut/tracklytics#tags)Tags

You can use tags to send more information about the tracked event. For example: Adjust requires token for their events.

```source-java
@TrackEvent(value="event",filters=100, tags="abc123")
public void trackNoValues() {
}
```

### [](https://github.com/orhanobut/tracklytics#super-attributes)Super Attributes

Some attributes might be used for every event within the app such as device id. Tracklytics call them as super attributes. These attributes will be automatically added to each event. You only need to set them once and Tracklytics will do the rest.

Access super attributes via Event class

```source-java
Tracklytics.init(new EventSubscriber() {
  @Override public void onEvent(Event event) {
    // event.superAttributes
  }
});
```

Set any attribute as super

```source-java
@Attribute(value="key", isSuper=true)
```

Set any fixed attribute as super

```source-java
@FixedAttribute(key="key", value="value",  isSuper=true)
```

Add super attribute directly

```source-java
tracklytics.addSuperAttribute("key","value");
```

Remove super attribute

```source-java
tracklytics.removeSuperAttribute("key");
```

### [](https://github.com/orhanobut/tracklytics#trackableattribute--trackable)@TrackableAttribute / @Trackable

You can make the class trackable in order to provide preset values. Imagine your domain models, they contain a lot of information and you can re-use them as a source for tracking. For the following use case: When `event` is triggered, attributes from `Foo.getTrackableAttributes()` will be added to this event.

```source-java
class Foo implements Trackable {
  String name;

  @Override public Map<String, String> getTrackableAttributes() {
    Map<String,String> values = new HashMap<>();
    values.put("name", name);
    return values;
  }
}
```

```source-java
@TrackEvent("event")
void something(@TrackableAttribute FooTrackable foo){
}
```

### [](https://github.com/orhanobut/tracklytics#transformattribute--transformattributemap)@TransformAttribute / @TransformAttributeMap

Sometimes values are not in the correct form, such as position or index is in Integer type. This might not be clear if you track them as raw. You may want to send a more understandable value to the analytic tools. Use TransformAttribute to solve this issue.

For example: In the following example, index is represented by integer and you want to have a String value which represent exact value such as menu item.

```source-java
class Foo {
  @TrackEvent("event")
  @TransformAttributeMap(
    keys = {0, 1},
    values = {"value0", "value1"}
  )
  public void foo(@TransformAttribute("key") int index) {
  }
}

// foo(0) : event -> [{"key","value0}]
// foo(1) : event -> [{"key","value1}]
```

### [](https://github.com/orhanobut/tracklytics#log)Log

Tracklytics provides a stream for logs which sends formatted event messages.

```source-java
Tracker tracklytics = Tracker.init(...);
tracklytics.setEventLogListener(new EventLogListener() {
  @Override public void log(String message) {
    // Set your logger here. ie: Logger or Timber
    Log.d("Tracker", message);
  }
});
```

Log output sample

```
eventName:{key=value}, super attrs: {key=value}, tags={100,200}

```

### [](https://github.com/orhanobut/tracklytics#more-api-options)More API options

You can also track event directly without annotations.

```source-java
tracklytics.trackEvent(String eventName)

tracklytics.trackEvent(String eventName, Map attributes)
```
