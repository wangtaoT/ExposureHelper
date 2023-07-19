package com.wt.exposurehelper.view

import com.wt.exposurehelper.IProvideExposureData


import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * 作为ItemView根布局使用的FrameLayout
 */
class ExposureFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : FrameLayout(context, attrs, style), IProvideExposureData {
    var exposureBindData: Any? = null

    override fun provideData(): Any? = exposureBindData
}