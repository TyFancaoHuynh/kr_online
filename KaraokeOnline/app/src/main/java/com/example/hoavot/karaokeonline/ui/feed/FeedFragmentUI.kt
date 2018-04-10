package com.example.hoavot.karaokeonline.ui.feed

import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.example.hoavot.karaokeonline.R
import com.example.hoavot.karaokeonline.data.model.other.Comment
import com.example.hoavot.karaokeonline.data.model.other.Feed
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 *
 * @author at-hoavo.
 */
class FeedFragmentUI(private val feeds: MutableList<Feed>, private var updateCommentsAdapter: (DiffUtil.DiffResult) -> Unit) : AnkoComponent<FeedFragment> {
    internal lateinit var add: ImageView
    internal val feedsAdapter = FeedAdapter(feeds, updateCommentsAdapter)
    override fun createView(ui: AnkoContext<FeedFragment>): View {
        return with(ui) {
            relativeLayout {
                lparams(matchParent, matchParent)
                backgroundColor = ContextCompat.getColor(context, R.color.colorItemFeed)
                relativeLayout {
                    id = R.id.feedFragmentAreaCaption
                    lparams(matchParent, dip(130))

                    linearLayout {
                        id = R.id.feedFragmentAreaEdt
                        editText {
                            id = R.id.feedFragmentAreaEdtCaption
                            backgroundResource = R.drawable.custom_item_feed
                            hint = "What do you think?..."
                            textSize = px2dip(dimen(R.dimen.feedTitleTextSize))
                            hintTextColor = ContextCompat.getColor(context, R.color.colorGrayLight)
                            maxLines = 4
                            leftPadding = dip(10)
                        }.lparams(dip(250), matchParent) {
                            leftMargin = dip(30)
                            rightMargin = dip(10)
                        }

                        add = imageView(R.drawable.ic_plus)
                    }.lparams(matchParent, dip(40)) {
                        topMargin = dip(10)
                    }

                    imageView(R.mipmap.ic_file_audio) {

                    }.lparams(dip(25), dip(25)) {
                        below(R.id.feedFragmentAreaEdt)
                        topMargin = dip(5)
                        leftMargin = dip(30)
                    }
                }

                recyclerView {
                    layoutManager = LinearLayoutManager(context)
                    adapter = feedsAdapter
                }.lparams(matchParent, matchParent) {
                    below(R.id.feedFragmentAreaCaption)
                    topMargin = dip(10)
                }
            }
        }
    }

}