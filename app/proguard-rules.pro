# Proguard rules for app
-keepclassmembers class kotlin.Metadata { *; }
-keepattributes Signature,InnerClasses,AnnotationDefault,RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepclassmembers enum * { public static **[] values(); public static ** valueOf(java.lang.String); }
# Add further rules as required by libraries you use
