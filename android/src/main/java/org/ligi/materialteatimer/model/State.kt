package org.ligi.materialteatimer.model

import com.chibatching.kotpref.KotprefModel

object State : KotprefModel() {

    var lastSelectedTeaName by stringPref()
}