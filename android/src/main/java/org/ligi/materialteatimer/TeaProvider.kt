package org.ligi.materialteatimer

import org.ligi.materialteatimer.model.State
import org.ligi.materialteatimer.model.TeaInfo

object TeaProvider {

    val teas = arrayOf(
            TeaInfo("Green Tea", R.drawable.greentea, 2 * 60 + 30, 81, 87, "https://en.wikipedia.org/wiki/Green_tea"),
            TeaInfo("Black Tea", R.drawable.blacktea, 3 * 60 + 30, 100, 0, "https://en.wikipedia.org/wiki/Black_tea"),
            TeaInfo("Oolong", R.drawable.oolong, 4 * 60, 85, 95, "https://en.wikipedia.org/wiki/Oolong"),
            TeaInfo("Rooibos", R.drawable.rooibos, 7 * 60, 100, 0, "https://en.wikipedia.org/wiki/Rooibos"),
            TeaInfo("Peppermint", R.drawable.peppermint, 3 * 60, 90, 95, "https://en.wikipedia.org/wiki/Peppermint")
    )

    var currentTea = teas.firstOrNull { it.name == State.lastSelectedTeaName } ?: teas[0]

}
