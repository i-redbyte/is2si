package ru.is2si.sisi.base.navigation

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ScreenNavigator(application: Application) {

    private var currentActivity: Activity? = null
    private var backActivityClazz: Class<*>? = null

    init {
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks())
    }

    fun backTo(clazz: Class<*>) {
        if (backActivityClazz == clazz) {
            backActivityClazz = null
        } else {
            backActivityClazz = clazz
            currentActivity?.finish()
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun activityLifecycleCallbacks() = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
            currentActivity = null
        }

        override fun onActivityResumed(activity: Activity) {
            currentActivity = activity
            if (backActivityClazz == activity::class.java) {
                backActivityClazz = null
            } else if (backActivityClazz != null && backActivityClazz != activity::class.java) {
                activity.finish()
            }
        }

        override fun onActivityStarted(activity: Activity?) =
                Unit

        override fun onActivityDestroyed(activity: Activity?) =
                Unit

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) =
                Unit

        override fun onActivityStopped(activity: Activity?) =
                Unit

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) =
                Unit
    }

}