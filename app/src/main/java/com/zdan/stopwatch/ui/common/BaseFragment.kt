package com.zdan.stopwatch.ui.common

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.zdan.stopwatch.MainActivity

open class BaseFragment(@LayoutRes resId: Int): Fragment(resId) {
    fun keepScreenOn(isOn: Boolean) {
        (activity as MainActivity).keepScreenOn(isOn)
    }
}