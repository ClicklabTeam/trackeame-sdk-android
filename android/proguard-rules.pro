# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/matiascalvo/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();  }

 -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
        public static final *** NULL;      }

 -keepnames @com.google.android.gms.common.annotation.KeepName class *
 -keepclassmembernames class * {
        @com.google.android.gms.common.annotation.KeepName *;
    }

 -keepnames class * implements android.os.Parcelable {
        public static final ** CREATOR;
    }

 -keep class com.google.android.gms.ads.** { *; }
 -dontwarn okio.**

-keepattributes **

-keep class com.clicklab.sdk.model.event.** {public protected *;}
-keep class com.clicklab.sdk.ActivityCallbacks {public protected *;}
-keep class com.clicklab.sdk.ClickLab {public protected *;}
-keep class com.clicklab.sdk.ClicklabClient {public protected *;}
-keep class com.clicklab.sdk.Configuration {public protected *;}
-keep class com.clicklab.sdk.ConnectivityReciver {public protected *;}
-keep class com.clicklab.sdk.SyncJobService {public protected *;}
