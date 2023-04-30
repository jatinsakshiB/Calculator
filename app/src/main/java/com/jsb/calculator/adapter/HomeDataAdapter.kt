package com.jsb.calculator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jsb.calculator.R
import com.jsb.calculator.databinding.ItemHomeDataBinding
import com.jsb.calculator.modules.HomeData

class HomeDataAdapter : BaseAdapter() {

    private val data: MutableList<HomeData> = mutableListOf()

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_home_data, parent, false)

        val item: HomeData = data[position]

        val text: TextView = view.findViewById(R.id.text)
        val image: ImageView = view.findViewById(R.id.image)

        text.text = item.name
        image.setImageResource(item.image)
        view.setOnClickListener {
            item.onClick()
        }

        return view
    }

    fun addItem(homeData: HomeData){
        data.add(homeData)
        notifyDataSetChanged()
    }

    fun update(name: String, image: Int) {
        for (i in data.indices) {
            if (data[i].name == name) {
                data[i].image = image
                notifyDataSetChanged()
                break
            }
        }
    }

}
