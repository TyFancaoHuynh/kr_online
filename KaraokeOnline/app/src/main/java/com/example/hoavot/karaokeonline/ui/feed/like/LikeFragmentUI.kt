package com.example.hoavot.karaokeonline.ui.feed.like

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.User
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 *
 * @author at-hoavo
 */
class LikeFragmentUI(private val users: MutableList<User>) : AnkoComponent<LikeFragment> {
    internal val likeAdapter = LikeAdapter(users)
    internal lateinit var countLike: TextView
    override fun createView(ui: AnkoContext<LikeFragment>): View {
        return with(ui) {
            relativeLayout {
                lparams(matchParent, matchParent)

                imageView(R.drawable.ic_heart_small) {
                    id = R.id.imgCountLike
                }.lparams(dip(40), dip(40)) {
                    leftMargin = dip(10)
                }

                countLike = textView {
                    id = R.id.tvCountLike
                    textColor = Color.BLACK
                    textSize = px2dip(dimen(R.dimen.textSize15))
                    gravity=Gravity.CENTER
                }.lparams {
                    leftMargin = dip(8)
                    rightOf(R.id.imgCountLike)
                }

                recyclerView {
                    layoutManager = LinearLayoutManager(context)
                    adapter = likeAdapter
                }.lparams(matchParent, matchParent) {
                    below(R.id.imgCountLike)
                }
            }
        }
    }

}