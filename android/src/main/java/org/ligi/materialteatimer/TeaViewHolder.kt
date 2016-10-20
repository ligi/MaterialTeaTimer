package org.ligi.materialteatimer

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class TeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val teaName = itemView.findViewById(R.id.tea_name) as TextView
    val teaImage = itemView.findViewById(R.id.teaImage) as ImageView
    val minutes = itemView.findViewById(R.id.tea_mins) as TextView
    val seconds = itemView.findViewById(R.id.tea_secs) as TextView
    val temperature = itemView.findViewById(R.id.tea_temp) as TextView

    val infoIcon = itemView.findViewById(R.id.infoIcon) as ImageView?

    fun bind(teaInfo: TeaInfo) {
        teaName.text = teaInfo.name
        teaImage.setImageResource(teaInfo.imageRes)
        minutes.text = (teaInfo.brewTime / 60).toString() + "m"
        seconds.text = (teaInfo.brewTime % 60).toString() + "s"

        var temp = teaInfo.temp.toString()
        if (teaInfo.tempMax > 0) {
            temp += ".." + teaInfo.tempMax
        }
        temperature.text = temp + "°C"

        if (infoIcon != null) {

            infoIcon.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(teaInfo.url)
                itemView.context.startActivity(intent)
            }

            if (Build.VERSION.SDK_INT >= 21) {
                if (TeaProvider.currentTea.equals(teaInfo)) {
                    teaImage.transitionName = teaImage.context.getString(R.string.tea_transition_from_main)
                } else {
                    teaImage.transitionName = null
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
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, teaImage as View, activity.getString(R.string.tea_transition_to_main))
        activity.startActivity(intent, options.toBundle())
    }
}