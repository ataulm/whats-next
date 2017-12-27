package com.ataulm.whatsnext.film

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.ataulm.whatsnext.R

class PeopleWidget constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    @BindView(R.id.people_widget_label) lateinit var labelTextView: TextView
    @BindView(R.id.people_widget_list) lateinit var list: RecyclerView

    private val label: String

    init {
        orientation = VERTICAL
        label = extractCustomAttributes(context, attrs)
    }

    private fun extractCustomAttributes(context: Context, attrs: AttributeSet): String {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PeopleWidget)
        try {
            return typedArray.getString(R.styleable.PeopleWidget_label)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_people_widget, this)
        ButterKnife.bind(this)

        labelTextView.text = label
        list.layoutManager = LinearLayoutManager(context)
        ViewCompat.setNestedScrollingEnabled(list, false);
    }

    fun bind(people: List<PersonViewModel>) {
        list.adapter = PeopleAdapter(people)
    }

    private class PeopleAdapter(private val people: List<PersonViewModel>) : Adapter<PersonViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PersonViewHolder {
            return PersonViewHolder.create(parent!!)
        }

        override fun onBindViewHolder(holder: PersonViewHolder?, position: Int) {
            holder?.bind(people[position])
        }

        override fun getItemCount(): Int {
            return people.size
        }
    }

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {

            fun create(parent: ViewGroup): PersonViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_people, parent, false)
                return PersonViewHolder(view)
            }
        }

        @BindView(R.id.item_people_text_primary) lateinit var primaryTextView: TextView
        @BindView(R.id.item_people_text_secondary) lateinit var secondaryTextView: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(person: PersonViewModel) {
            primaryTextView.text = person.primaryLabel
            secondaryTextView.text = person.secondaryLabel
        }
    }

    data class PersonViewModel(val primaryLabel: String, val secondaryLabel: String?)
}
