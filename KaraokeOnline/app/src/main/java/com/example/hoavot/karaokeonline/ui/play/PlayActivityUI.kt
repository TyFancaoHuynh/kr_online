package com.example.hoavot.karaokeonline.ui.play

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.nomal.Item
import com.example.hoavot.karaokeonline.ui.play.fab.FloatingMenuButton
import com.example.hoavot.karaokeonline.ui.play.fab.floatingButton
import com.example.hoavot.karaokeonline.ui.utils.ShowVideoAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 *
 * @author at-hoavo.
 */
class PlayActivityUI(private val items: MutableList<Item>, private val context: Context) : AnkoComponent<PlayActivity> {
    internal var adapterShowVideo = ShowVideoAdapter(context, items, Color.WHITE)
    internal lateinit var thumnailVideo: ImageView
    internal lateinit var nameVideo: TextView
    internal lateinit var channelName: TextView
    internal lateinit var dayPublish: TextView
    internal lateinit var viewCount: TextView
    internal lateinit var like: TextView
    internal lateinit var dislike: TextView
    internal lateinit var recyclerviewListVideo: RecyclerView
    lateinit var fabMenuGroup: FloatingMenuButton

    override fun createView(ui: AnkoContext<PlayActivity>): View = with(ui) {
        relativeLayout {
            backgroundColor = Color.BLACK
            frameLayout {
                id = R.id.play_activity_framelayout_play
            }.lparams(matchParent, dip(200))

            relativeLayout {
                id = R.id.play_activity_relativeLayout_show_info_video
                backgroundColor = Color.BLACK
                lparams(matchParent, dip(100)) {
                    below(R.id.play_activity_framelayout_play)
                }

                nameVideo = textView {
                    id = R.id.play_activity_tv_name_video
                    textSize = px2dip(dimen(R.dimen.play_activity_tv_name_video_text_size))
                    textColor = Color.WHITE
                    typeface = Typeface.DEFAULT_BOLD
                }

                thumnailVideo = imageView {
                    id = R.id.play_activity_img_thumnail
                }.lparams(dip(50), dip(50)) {
                    below(R.id.play_activity_tv_name_video)
                }

                channelName = textView {
                    id = R.id.play_activity_tv_name_channel
                    textSize = px2dip(dimen(R.dimen.play_activity_tv_channel_video_text_size))
                    textColor = Color.WHITE
                }.lparams {
                    leftMargin = dip(10)
                    below(R.id.play_activity_tv_name_video)
                    rightOf(R.id.play_activity_img_thumnail)
                }

                dayPublish = textView {
                    id = R.id.play_activity_tv_day_publish
                    textSize = px2dip(dimen(R.dimen.play_activity_tv_day_publish_video_text_size))
                    textColor = Color.WHITE
                }.lparams {
                    leftMargin = dip(10)
                    below(R.id.play_activity_tv_name_channel)
                    rightOf(R.id.play_activity_img_thumnail)
                }

                viewCount = textView {
                    id = R.id.play_activity_tv_view_count
                    textSize = px2dip(dimen(R.dimen.play_activity_tv_view_count_video_text_size))
                    textColor = Color.WHITE
                }.lparams {
                    below(R.id.play_activity_tv_name_video)
                    alignParentRight()
                    topMargin = dip(20)
                    rightMargin = dip(10)
                }

                dislike = textView {
                    id = R.id.play_activity_tv_unlike
                    textColor = Color.WHITE
                    textSize = px2dip(dimen(R.dimen.play_activity_tv_like_video_text_size))
                }.lparams {
                    below(R.id.play_activity_tv_view_count)
                    alignParentRight()
                    rightMargin = dip(20)
                    topMargin = dip(5)
                }

                imageView {
                    id = R.id.play_activity_img_unlike
                    backgroundResource = R.mipmap.ic_dislike
                }.lparams(dip(30), dip(30)) {
                    leftOf(R.id.play_activity_tv_unlike)
                    below(R.id.play_activity_tv_view_count)
                }

                like = textView {
                    id = R.id.play_activity_tv_like
                    textColor = Color.WHITE
                    textSize = px2dip(dimen(R.dimen.play_activity_tv_like_video_text_size))
                }.lparams {
                    topMargin = dip(5)
                    leftOf(R.id.play_activity_img_unlike)
                    below(R.id.play_activity_tv_view_count)
                    rightMargin = dip(10)
                }

                imageView {
                    id = R.id.play_activity_img_like
                    backgroundResource = R.mipmap.ic_like
                }.lparams(dip(30), dip(30)) {
                    leftOf(R.id.play_activity_tv_like)
                    below(R.id.play_activity_tv_view_count)
                }
            }

            recyclerviewListVideo = recyclerView {
                backgroundColor = Color.BLACK
                layoutManager = LinearLayoutManager(context)
                adapter = adapterShowVideo
            }.lparams(matchParent, matchParent) {
                below(R.id.play_activity_relativeLayout_show_info_video)
            }

            fabMenuGroup = floatingButton(object : FloatingMenuButton.OnMenuClickListener {
                override fun eventItemMenuClicked(view: View) {
                    owner.eventOnClickItemMenu(view)
                }
            }) {
            }.lparams {
                alignParentBottom()
                alignParentRight()
            }

        }
    }
}
