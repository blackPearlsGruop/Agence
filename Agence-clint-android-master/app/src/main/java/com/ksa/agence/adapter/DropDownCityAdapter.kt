package com.ksa.agence.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ksa.agence.R
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.entity.cityResponse.DataCityResponse

class DropDownCityAdapter(val context: Context, var dataSource: List<DataCityResponse>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.custom_spinner_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }

        vh.label.text = dataSource.get(position).title

        if (position == 0) {
            // Set the hint text color gray
            vh.label.setTextColor(Color.GRAY)
        } else {
            vh.label.setTextColor(Color.BLACK)
        }
        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView

        init {
            label = row?.findViewById(R.id.text) as TextView
        }
    }

}