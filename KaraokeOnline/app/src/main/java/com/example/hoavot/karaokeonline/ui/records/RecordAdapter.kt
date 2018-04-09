package com.example.hoavot.karaokeonline.ui.records

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.other.Record
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class RecordAdapter(private val records: MutableList<Record>) : RecyclerView.Adapter<RecordAdapter.RecordHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
        val ui = RecordUI()
        return RecordHolder(ui, ui.createView(AnkoContext.Companion.create(parent.context, parent, false)))
    }

    override fun onBindViewHolder(holder: RecordHolder?, position: Int) {
        holder?.onBind()
    }

    override fun getItemCount() = records.size

    inner class RecordHolder(private val ui: RecordUI, private val item: View) : RecyclerView.ViewHolder(item) {

        fun onBind() {
            ui.nameRecord.text = records[layoutPosition].nameRecord
            ui.timeRecord.text = records[layoutPosition].timeRecord.toString()
            ui.dateRecord.text = records[layoutPosition].dateRecord
        }
    }
}
