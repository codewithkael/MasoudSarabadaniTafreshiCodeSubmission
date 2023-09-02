package com.apex.codeassesment.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ItemMainRecyclerViewBinding
import com.bumptech.glide.RequestManager
@SuppressLint("NotifyDataSetChanged")
class MainRecyclerViewAdapter constructor(private val glide: RequestManager) :
    RecyclerView.Adapter<MainRecyclerViewAdapter.MainRecyclerViewHolder>() {

    private var listener:Listener?=null
    fun setListener(listener: Listener){
        this.listener = listener
    }

    private var userList: List<User> = listOf()
    fun setUserList(list: List<User>) {
        this.userList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        return MainRecyclerViewHolder(
            ItemMainRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            glide
        ){
            listener?.onUserClicked(it)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    inner class MainRecyclerViewHolder(
        private val binding: ItemMainRecyclerViewBinding, private val glide: RequestManager,
        private val onUserClicked:(User)->Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
        fun bind(user: User) {
            binding.apply {
                titleName.text = context.resources.getString(
                    R.string.title_name, user.name?.first, user.name?.last
                )
                titleAge.text =
                    context.resources.getString(R.string.title_age, user.dob?.age.toString())
                titleEmail.text = user.email
                titleLocation.text = context.resources.getString(
                    R.string.title_location,
                    user.location?.coordinates?.latitude.toString(),
                    user.location?.coordinates?.longitude.toString()
                )
                glide.load(user.picture?.large).into(mainImageview)
                root.setOnClickListener { onUserClicked.invoke(user) }
            }
        }
    }

    interface Listener {
        fun onUserClicked(user: User)
    }


}