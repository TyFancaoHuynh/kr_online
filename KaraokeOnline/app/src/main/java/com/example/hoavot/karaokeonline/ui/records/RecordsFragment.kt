package com.example.hoavot.karaokeonline.ui.records


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hoavot.karaokeonline.data.model.other.Record
import com.example.hoavot.karaokeonline.ui.base.BaseFragment
import org.jetbrains.anko.AnkoContext

/**
 *
 * @author at-hoavo.
 */
class RecordsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val records = mutableListOf<Record>()
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        records.add(Record("hc", "", "cbhjck", "Record001", 1234567, "12-03-2018"))
        var ui = RecordsUI(records)
        return ui.createView(AnkoContext.Companion.create(context, this, false))
    }

    override fun onBindViewModel() {
        //
    }
}