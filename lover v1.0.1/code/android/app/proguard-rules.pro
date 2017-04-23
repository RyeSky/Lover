-dontoptimize
-dontpreverify

#声明第三方jar包，不需要管so文件，之后打包，看有哪些警告（因为jar包会有不严谨的引用）
#-libraryjars libs/gson-2.2.4.jar
#-libraryjars libs/universal-image-loader-1.9.3.jar

#关闭jar中的警告
-dontwarn sun.misc.**
-dontwarn butterknife.internal.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn cn.jpush.**
-dontwarn cn.jiguang.**
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

#rxjava
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#保持已知的不能混淆的类
-keep class okhttp3.** { *;}
-keep class cn.jpush.** { *; }
-keep class cn.jiguang.** { *; }

#v4包
-dontwarn android.support.v4.**
-keep class android.support.v4.** {*;}

#关于gson解析的比较多
#0、关于gson本身的一些类不能混淆，比如注解
#1、org.json.**中关于json基础的类不能混淆
#2、实现java.io.Serializable接口的类不能混淆
#3、有用gson注解的成员变量的类不能混淆，比如艺天下中用到了很多内部类（下面的Begin @Expose---End @Expose）
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe {*;}
#-keep class com.google.gson.stream.** {*;}
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** {*;}
##---------------End: proguard configuration for Gson  ----------

-keep class org.json.** {*;}

#实现Serializable接口的类
-keep class * implements java.io.Serializable {*;}

#实现Parcelable接口的类
-keep class * implements android.os.Parcelable{
    public static final android.os.Parcelable$Creator *;
    *;
}

#保持枚举
-keepclassmembernames enum *{
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#不清楚的第三方，jar包或库工程不要混淆
#打包后一定一定要测试，看功能是否实现正常，比较常见的就是json数据解析不出来（实体类没有实现Serializable接口，或是没有keep内部类），如果有这种情况，实现接口或keep，打包再测
#打包发布后，一旦程序崩溃，crash文件收集到的信息也是混淆后的类路径，可以从工程根目录proguard文件夹里的mapping.txt文件中找对应的源文件，crash文件只能精确到类方法一级，不能精确到哪一行代码，这需要你直接调试，重现bug
#打包之前clean一下工程，否则很可能在打包过程中出错，并不是混淆配置有问题