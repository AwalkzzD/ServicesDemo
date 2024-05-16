package com.example.servicesdemo.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.servicesdemo.BR
import com.example.servicesdemo.base.viewmodel.BaseViewModel
import com.example.servicesdemo.data.dto.alarm.Alarm

class GenericDataAdapter<T : Any>(
    private var dataList: List<T>,
    @LayoutRes val layoutID: Int,
    private val onItemClick: (T) -> Unit,
) : RecyclerView.Adapter<GenericDataAdapter<T>.ViewHolder>() {

    lateinit var viewModel: BaseViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutID, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

    }

    /*fun setVM(viewModel: BaseViewModel) {
        this.viewModel = viewModel
    }

    fun filterList(filterList: List<T>) {
        dataList = filterList
        notifyDataSetChanged()
    }*/

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            when (item) {
                is Alarm -> {
                    binding.setVariable(BR.alarm, item)
                }
            }
            binding.executePendingBindings()
        }
    }
}