package com.testfirestoresort

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

/**
 * Created by Mohanraj Karatadipalayam on 18/12/17.
 */


/**
 * RecyclerView adapter for a list of Devs.
 */
open class DevelopersRVAdapter(query: Query):
        FirestoreAdapter<DevelopersRVAdapter.ViewHolder>(query) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.developer_cell, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvDeveloperName: TextView by lazy {
            itemView.findViewById<TextView>(R.id.tv_name_label)
        }


        private val tvLastCommitDate: TextView by lazy {
            itemView.findViewById<TextView>(R.id.tv_commit_date_value)
        }



        fun bind(snapshot: DocumentSnapshot) {

            // val commodityViewModel = snapshot.toObject(CommodityViewModel::class.java)

            tvDeveloperName.text =  snapshot.data["name"].toString()
            tvLastCommitDate.text = snapshot.data["date"].toString()


            // Click listener
            itemView.setOnClickListener {

            }
        }

    }
}
