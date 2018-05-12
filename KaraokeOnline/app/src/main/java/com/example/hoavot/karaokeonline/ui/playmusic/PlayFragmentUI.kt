package com.example.hoavot.karaokeonline.ui.playmusic

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.*
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.ui.extensions.fontNomal
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 * @author at-hoavo
 */
class PlayFragmentUI : AnkoComponent<PlayFragment> {
    internal lateinit var imgMusic: ImageView
    internal lateinit var dateMusic: TextView
    internal lateinit var tvNameMusic: TextView
    internal lateinit var tvArtist: TextView
    internal lateinit var mSeekBar: SeekBar
    internal lateinit var currentTime: TextView
    internal lateinit var totalTime: TextView
    internal lateinit var mRecyclerView: RecyclerView

    internal var mImgBtnPlay: ImageButton? = null
    internal var mImgBtnPause: ImageButton? = null
    internal var mImgBtnShuffle: ImageButton? = null
    internal var mImgBtnShuffleSelected: ImageButton? = null
    internal var mImgBtnAutoNext: ImageButton? = null
    internal var mImgBtnAutoNextSelected: ImageButton? = null
    internal var mImgBtnNext: ImageButton? = null
    internal var mImgBtnPrevious: ImageButton? = null

    override fun createView(ui: AnkoContext<PlayFragment>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            relativeLayout {
                lparams(matchParent, dip(300))

                tvNameMusic = textView {
                    id = R.id.recordsFragmentNameMusic
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    textColor = Color.BLACK
                }.lparams {
                    leftMargin = dip(20)
                    topMargin = dip(20)
                }

                tvArtist = textView {
                    id = R.id.recordsFragmentArticsMusic
                    textSize = px2dip(dimen(R.dimen.textSize13))
                    textColor = ContextCompat.getColor(context, R.color.colorDate)
                    fontNomal()
                }.lparams {
                    leftMargin = dip(20)
                    topMargin = dip(10)
                    below(R.id.recordsFragmentNameMusic)
                }

                dateMusic=textView {
                    id = R.id.recordsFragmentDateMusic
                    textSize = px2dip(dimen(R.dimen.textSize12))
                    textColor = ContextCompat.getColor(context, R.color.colorStartPlay)
                }.lparams {
                    leftMargin = dip(20)
                    topMargin = dip(5)
                    below(R.id.recordsFragmentArticsMusic)
                }

                imgMusic = imageView(R.drawable.ic_sound_wave) {
                    id = R.id.recordsFragmentImgMusic
                }.lparams(dip(100), dip(100)) {
                    centerHorizontally()
                    below(R.id.recordsFragmentDateMusic)
                }

                linearLayout {
                    currentTime = textView("0:00:00") {
                        textColor = Color.BLACK
                    }.lparams(dip(0)) {
                        weight = 2f
                        leftMargin = dip(15)
                    }

                    mSeekBar = seekBar {
                        id = R.id.recordsFragmentProgressBarMusic
                        horizontalPadding = dip(5)
                    }.lparams(dip(0)) {
                        weight = 6f
                    }

                    totalTime = textView("0:00:00") {
                        textColor = Color.BLACK
                    }.lparams(dip(0)) {
                        weight = 2f
                        leftMargin = dip(15)
                    }
                }.lparams(matchParent, dip(25)) {
                    topMargin = dip(10)
                    below(R.id.recordsFragmentImgMusic)
                    rightOf(R.id.recordPlayButton)
                }

                relativeLayout {
                    mImgBtnShuffle = imageButton(R.drawable.img_btn_shuffle) {
                        id = R.id.imgBtnShuffle
                        onClick {
                            owner.onButtonClick(mImgBtnShuffle)
                        }
                    }.lparams {
                        rightMargin = dip(15)
                        leftOf(R.id.imgBtnPrevious)
                    }

                    mImgBtnShuffleSelected = imageButton(R.drawable.img_btn_shuffle_selected) {
                        visibility = View.INVISIBLE
                        id = R.id.imgBtnShuffleSelected
                        onClick {
                            owner.onButtonClick(mImgBtnShuffleSelected)
                        }
                    }.lparams {
                        sameLeft(R.id.imgBtnShuffle)
                    }

                    mImgBtnPrevious = imageButton(R.drawable.img_btn_previous) {
                        id = R.id.imgBtnPrevious
                        onClick {
                            owner.onButtonClick(mImgBtnPrevious)
                        }
                    }.lparams {
                        rightMargin = dip(15)
                        leftOf(R.id.imgBtnPlay)
                    }

                    mImgBtnPlay = imageButton(R.drawable.img_btn_play) {
                        id = R.id.imgBtnPlay
                        visibility = View.INVISIBLE
                        onClick {
                            owner.onButtonClick(mImgBtnPlay)
                        }
                    }.lparams {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }

                    mImgBtnPause = imageButton(R.drawable.img_btn_pause) {
                        id = R.id.imgBtnPause
                        onClick {
                            owner.onButtonClick(mImgBtnPause)
                        }
                    }.lparams {
                        sameLeft(R.id.imgBtnPlay)
                        gravity = Gravity.CENTER_HORIZONTAL
                    }

                    mImgBtnNext = imageButton(R.drawable.img_btn_next) {
                        id = R.id.imgBtnNext
                        onClick {
                            owner.onButtonClick(mImgBtnNext)
                        }
                    }.lparams {
                        leftMargin = dip(15)
                        rightOf(R.id.imgBtnPlay)
                    }

                    mImgBtnAutoNext = imageButton(R.drawable.ic_btn_auto_next) {
                        id = R.id.imgBtnAutoNext
                        onClick {
                            owner.onButtonClick(mImgBtnAutoNext)
                        }
                    }.lparams {
                        rightOf(R.id.imgBtnNext)
                        leftMargin = 15
                    }

                    mImgBtnAutoNextSelected = imageButton(R.drawable.ic_btn_auto_next_selected) {
                        id = R.id.imgBtnAutoNextSelected
                        visibility = View.INVISIBLE
                        onClick {
                            owner.onButtonClick(mImgBtnAutoNextSelected)
                        }
                    }.lparams {
                        sameLeft(R.id.imgBtnAutoNext)
                    }

                }.lparams(matchParent, wrapContent) {
                    centerHorizontally()
                    alignParentBottom()
                }
            }

            mRecyclerView = recyclerView {
                layoutManager = LinearLayoutManager(context)
            }.lparams(matchParent, matchParent) {
                topMargin = dip(10)
            }
        }
    }
}