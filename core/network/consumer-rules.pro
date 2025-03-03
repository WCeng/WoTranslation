# ========================================================
# Retrofit 核心规则
# ========================================================
# 允许混淆 Call 接口的实现类，但保留接口自身结构
-keep,allowshrinking,allowobfuscation interface retrofit2.Call

# 允许混淆 Response 类，但保留类结构（防止字段/方法被移除）
-keep,allowshrinking,allowobfuscation class retrofit2.Response

# 保留 Retrofit 注解（如 @GET, @Query）的元数据
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# ========================================================
# Kotlin 协程支持（suspend 函数和 Continuation）
# ========================================================
# 允许混淆 Continuation 实现类，但保留类定义
-keep,allowshrinking,allowobfuscation class kotlin.coroutines.Continuation

# 保留 suspend 函数的签名（防止泛型擦除导致类型错误）
-keepattributes Signature

# ========================================================
# 数据类与 Gson 反序列化
# ========================================================
# 保留数据类 NetworkTranslate 及其字段（确保 Gson 正确映射）
-keep class com.wceng.network.model.NetworkTranslate {
    @com.google.gson.annotations.SerializedName <fields>;
    <init>(...); # 保留所有构造函数
}

# 全局保留 @SerializedName 注解的字段
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ========================================================
# 通用 Kotlin 规则
# ========================================================
# 保留 Kotlin Metadata（反射和序列化依赖）
-keepclassmembers class ** { 
    @kotlin.Metadata *; 
}

# 保留 Kotlin 数据类的 copy 方法（防止潜在问题）
-keepclassmembers class com.wceng.network.model.NetworkTranslate {
    public ** copy(...);
}