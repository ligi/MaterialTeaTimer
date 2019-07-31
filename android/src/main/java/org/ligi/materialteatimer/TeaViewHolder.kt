package org.ligi.materialteatimer

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.tea_card.view.*
import org.ligi.materialteatimer.model.State
import org.ligi.materialteatimer.model.TeaInfo

class TeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(teaInfo: TeaInfo) {
        itemView.tea_name.text = teaInfo.name
        itemView.teaImage.teaImage.setImageResource(teaInfo.imageRes)
        itemView.tea_mins.text = (teaInfo.brewTime / 60).toString() + "m"
        itemView.tea_secs.text = (teaInfo.brewTime % 60).toString() + "s"

        var temp = teaInfo.temp.toString()
        if (teaInfo.tempMax > 0) {
            temp += ".." + teaInfo.tempMax
        }
        itemView.tea_temp.text = temp + "°C"

        if (itemView.infoIcon != null) {

            itemView.infoIcon.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(teaInfo.url)
                itemView.context.startActivity(intent)
            }

            if (Build.VERSION.SDK_INT >= 21) {
                itemView.teaImage.transitionName  = if (TeaProvider.currentTea == teaInfo) {
                    itemView.teaImage.context.getString(R.string.tea_transition_from_main)
                } else {
                    null
                }
            }

            itemView.setOnClickListener {
                Timer.resetAndPause()
                State.lastSelectedTeaName = teaInfo.name
                TeaProvider.currentTea = teaInfo

                val activity = itemView.context as Activity
                val intent = Intent(activity, MainActivity::class.java)

                if (Build.VERSION.SDK_INT >= 21) {
                    makeTransition(activity, intent)
                } else {
                    activity.startActivity(intent)
                }

                itemView.postDelayed({
                    activity.finish()
                }, 1000)
            }
        }
    }

    @TargetApi(21)
    private fun makeTransition(activity: Activity, intent: Intent) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, itemView.teaImage as View, activity.getString(R.string.tea_transition_to_main))
        activity.startActivity(intent, options.toBundle())
    }
}