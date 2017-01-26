-dontoptimize
-dontpreverify

#声明第三方jar包，不需要管so文件，之后打包，看有哪些警告（因为jar包会有不严谨的引用）
#-libraryjars libs/gson-2.2.4.jar
#-libraryjars libs/universal-image-loader-1.9.3.jar

#关闭jar中的警告
-dontwarn butterknife.internal.**
-dontwarn okio.**
-dontwarn cn.jpush.**
-dontwarn cn.jiguang.**

#保持已知的不能混淆的类
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
-keep class * implements java.io.Serializable {*;}

##---------------Begin @Expose-----------------------------------
##---------------com.skye.lover.model.*;--------------------------------
-keep class com.skye.lover.model.BaseResponse {*;}
-keep class com.skye.lover.model.FilePaths {*;}
-keep class com.skye.lover.model.ListResponse {*;}
-keep class com.skye.lover.model.User {*;}
-keep class com.skye.lover.model.Comment {*;}
-keep class com.skye.lover.model.FollowPillowTalk {*;}
-keep class com.skye.lover.model.Message {*;}
-keep class com.skye.lover.model.PillowTalkProperties {*;}
-keep class com.skye.lover.model.PrivateMessageSession {*;}
##---------------com.skye.lover.activity.*;-----------------
-keep class com.skye.lover.activity.pillowtalk.PillowTalkDetailActivity$Data {*;}
-keep class com.skye.lover.activity.pillowtalk.BroadcastDetailActivity$Data {*;}
-keep class com.skye.lover.activity.pillowtalk.comment.CommentListActivity$Data {*;}
-keep class com.skye.lover.activity.pillowtalk.OthersPillowTalkListActivity$Data {*;}
-keep class com.skye.lover.activity.my.CollectedPillowTalkListActivity$Data {*;}
-keep class com.skye.lover.activity.my.PraisedPillowTalkListActivity$Data {*;}
-keep class com.skye.lover.activity.my.CommentedPillowTalkListActivity$Data {*;}
-keep class com.skye.lover.activity.my.MessageListActivity$Data {*;}
-keep class com.skye.lover.activity.privatemessage.PrivateMessageActivity$Data {*;}
##---------------com.skye.lover.fragment.*;-----------------
-keep class com.skye.lover.fragment.HoneyWordFragment$Data {*;}
-keep class com.skye.lover.fragment.FindFragment$Data {*;}
##---------------End @Expose-------------------------------------

#不清楚的第三方，jar包或库工程不要混淆
#打包后一定一定要测试，看功能是否实现正常，比较常见的就是json数据解析不出来（实体类没有实现Serializable接口，或是没有keep内部类），如果有这种情况，实现接口或keep，打包再测
#打包发布后，一旦程序崩溃，crash文件收集到的信息也是混淆后的类路径，可以从工程根目录proguard文件夹里的mapping.txt文件中找对应的源文件，crash文件只能精确到类方法一级，不能精确到哪一行代码，这需要你直接调试，重现bug
#打包之前clean一下工程，否则很可能在打包过程中出错，并不是混淆配置有问题