package com.ataulm.whatsnext.film

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.ataulm.whatsnext.R
import kotlinx.android.synthetic.main.merge_people_widget.view.*
import kotlinx.android.synthetic.main.view_item_people.view.*

class PeopleWidget constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val label: String

    init {
        orientation = VERTICAL
        label = extractCustomAttributes(context, attrs)
    }

    private fun extractCustomAttributes(context: Context, attrs: AttributeSet): String {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PeopleWidget)
        try {
            return typedArray.getString(R.styleable.PeopleWidget_label)!!
        } finally {
            typedArray.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_people_widget, this)
        people_widget_label.text = label
        people_widget_list.layoutManager = LinearLayoutManager(context)
        ViewCompat.setNestedScrollingEnabled(people_widget_list, false);
    }

    fun bind(people: List<PersonViewModel>) {
        people_widget_list.adapter = PeopleAdapter(people)
    }

    private class PeopleAdapter(private val people: List<PersonViewModel>) : Adapter<PersonViewHolder>() {

        init {
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            return PersonViewHolder.create(parent)
        }

        override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
            holder.bind(people[position])
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

        fun bind(person: PersonViewModel) {
            itemView.item_people_text_primary.text = person.primaryLabel
            itemView.item_people_text_secondary.text = person.secondaryLabel
        }
    }

    data class PersonViewModel(val primaryLabel: String, val secondaryLabel: String?)
}
