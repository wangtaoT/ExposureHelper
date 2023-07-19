package com.wt.exposurehelper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wt.exposurehelper.view.ExposureFrameLayout

class FirstAdapter(private val context: Context?, val data: ArrayList<String>) :
    RecyclerView.Adapter<FirstAdapter.MyViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        mOnItemClickListener = itemClickListener
    }

    //  删除数据
    fun removeData(position: Int) {
        data.removeAt(position)
        //删除动画
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(
                context
            ).inflate(
                R.layout.item_find, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv.text = data[position]
        holder.root.exposureBindData = data[position]

        holder.root.setOnLongClickListener {
            mOnItemClickListener?.onLongClick(it, position)
            return@setOnLongClickListener false
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv: TextView
        var root: ExposureFrameLayout

        init {
            tv = view.findViewById(R.id.item_find_title) as TextView
            root = view.findViewById(R.id.root) as ExposureFrameLayout
        }
    }

    //设置回调接口
    interface OnItemClickListener {
        fun onLongClick(view: View?, position: Int)
    }

}