package org.ligi.materialteatimer

import com.chibatching.kotpref.KotprefModel

object State : KotprefModel() {

    var lastSelectedTeaName by stringPrefVar()
}