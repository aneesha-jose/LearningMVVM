
#Models
-keep class com.aneeshajose.trending.models.**{*;}

#Joda Time
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

-dontwarn org.slf4j.**
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}