package chukstem.tech4dev.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import chukstem.tech4dev.Models.User
import chukstem.tech4dev.R.layout
import chukstem.tech4dev.R.id

class UsersAdapter(context: Context, private val dataSource: ArrayList<User>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cItem = getItem(position) as User
        val rowView = inflater.inflate(layout.listview_users, parent, false)

        // Get name
        val name = rowView.findViewById(id.lvName) as TextView
        name.text = cItem.NAME


        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}