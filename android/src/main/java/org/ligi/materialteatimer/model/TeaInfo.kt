package org.ligi.materialteatimer.model

import android.support.annotation.DrawableRes

data class TeaInfo(val name: String, @DrawableRes val imageRes: Int, val brewTime: Int, val temp: Int, val tempMax: Int, val url: String)