package com.example.hamid.gitRepo.presentation.ui.adaptar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hamid.gitRepo.R
import com.hamid.domain.model.model.RepoPresentationModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.repository_item.view.*

class RepoListAdapter : RecyclerView.Adapter<RepoListAdapter.ViewHolder>() {

    private var repoList: List<RepoPresentationModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_item, parent, false)

        return ViewHolder(
            view,
            object :
                ViewHolder.IClickListener {

                override fun onClick(caller: View, position: Int) {

                }

            })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repoList!![position]
        holder.tvName.text = repo.name
        holder.tvDescription.text = repo.description

        Picasso.get()
            .load(repo.avatar)
            .into(holder.ivAvatar)
    }

    override fun getItemCount(): Int {
        return if (repoList != null)
            repoList!!.size
        else
            0
    }

    fun setAdapterList(repoList: List<RepoPresentationModel>) {
        this.repoList = repoList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, private val clickListener: IClickListener) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var tvName: TextView = view.tv_name
        var tvDescription: TextView = view.tv_description
        var ivAvatar: ImageView = view.iv_avatar


        override fun onClick(view: View) {
            clickListener.onClick(view, layoutPosition)
        }

        interface IClickListener {
            fun onClick(caller: View, position: Int)
        }

    }

}
