package com.example.hoavot.karaokeonline.ui.records

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Record
import com.example.hoavot.karaokeonline.ui.extensions.fontNomal
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 *
 * @author at-hoavo.
 */
class RecordsUI(private val records: MutableList<Record>) : AnkoComponent<RecordsFragment> {
    internal lateinit var imgMusic: ImageView
    internal lateinit var imgShareMusic: ImageView
    internal lateinit var tvNameMusic: TextView
    internal lateinit var tvDateMusic: TextView
    internal lateinit var progressBar: ProgressBar
    internal lateinit var currentTime: TextView
    internal lateinit var totalTime: TextView
    internal lateinit var play: ImageView
    internal lateinit var pause: ImageView

    override fun createView(ui: AnkoContext<RecordsFragment>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            relativeLayout {
                lparams(matchParent, dip(220))

                tvNameMusic = textView("djbfdsjkbdskbd") {
                    id = R.id.recordsFragmentNameMusic
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    textColor = Color.BLACK
                }.lparams {
                    leftMargin = dip(20)
                    topMargin = dip(20)
                }

                tvDateMusic = textView("25-01-2018") {
                    id = R.id.recordsFragmentDateMusic
                    textSize = px2dip(dimen(R.dimen.textSize13))
                    textColor = ContextCompat.getColor(context, R.color.colorDate)
                    fontNomal()
                }.lparams {
                    leftMargin = dip(20)
                    topMargin = dip(10)
                    below(R.id.recordsFragmentNameMusic)
                }

                imgMusic = imageView(R.drawable.ic_sound_wave) {
                    id = R.id.recordsFragmentImgMusic
                }.lparams(dip(100), dip(100)) {
                    centerHorizontally()
                    below(R.id.recordsFragmentDateMusic)
                }

                imgShareMusic = imageView {
                    id = R.id.recordsFragmentImgShareMusic
                }.lparams {
                    below(R.id.recordsFragmentImgMusic)
                    rightOf(R.id.recordPlayButton)
                }

                relativeLayout {
                    id = R.id.recordPlayButton
                    play = imageView(R.drawable.play_ic) {
                        visibility = View.GONE
                    }.lparams(matchParent, matchParent)

                    pause = imageView(R.drawable.pause_ic) {

                    }.lparams(matchParent, matchParent)
                }.lparams(dip(35), dip(35)) {
                    leftMargin = dip(10)
                    alignParentBottom()
                }

                linearLayout {
                    currentTime = textView("00:00") {
                        textColor = Color.BLACK
                    }.lparams(dip(0)) {
                        weight = 1f
                        leftMargin = dip(15)
                    }

                    progressBar = horizontalProgressBar {
                        id = R.id.recordsFragmentProgressBarMusic
                        horizontalPadding = dip(5)
                    }.lparams(dip(0)) {
                        weight = 6f
                    }

                    totalTime = textView("00:00") {
                        textColor = Color.BLACK
                    }.lparams(dip(0)) {
                        weight = 1f
                        rightMargin = dip(15)
                    }
                }.lparams(matchParent, dip(25)) {
                    alignParentBottom()
                    rightOf(R.id.recordPlayButton)
                }

            }

            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = RecordAdapter(records)
            }.lparams(matchParent, matchParent) {
                topMargin = dip(10)
            }
        }
    }
}