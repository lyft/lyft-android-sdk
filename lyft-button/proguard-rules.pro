# Gson
-keepclassmembers class ** {
  @com.google.gson.annotation.SerializedName *;
}

# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# Lyft Button
-keep class com.lyft.**{ *; }
-keepclassmembers class com.lyft.** {
    *;
}
