package com.wceng.wotranslation

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setStrictModePolicy()
    }

    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            StrictMode.setThreadPolicy(
                Builder()
                    .detectAll()
                    .penaltyLog()
                    .build(),
            )
        }
    }

//    private fun setStrictModePolicy() {
//        if (isDebuggable()) {
//            StrictMode.setThreadPolicy(Builder()
//                .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()   // or .detectAll() for all detectable problems
////                .penaltyDialog() //弹出违规提示对话框
//                .penaltyLog() //在Logcat 中打印违规异常信息
//                .penaltyFlashScreen() //API等级11
//                .build());
////            StrictMode.setVmPolicy(Builder()
////                .detectLeakedSqlLiteObjects()
////                .detectLeakedClosableObjects() //API等级11
////                .penaltyLog()
////                .penaltyDeath()
////                .build());
//        }
//    }
}