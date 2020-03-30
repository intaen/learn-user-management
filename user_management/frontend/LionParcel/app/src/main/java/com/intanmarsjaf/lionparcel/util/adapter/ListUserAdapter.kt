package com.intanmarsjaf.bengongapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import com.intanmarsjaf.bengongapps.model.User
import com.intanmarsjaf.lionparcel.R
import com.intanmarsjaf.lionparcel.model.ListUser

class ListUserAdapter(@NonNull context: Context, @LayoutRes layoutRes: Int = 0, var userList: MutableList<ListUser>)
    : ArrayAdapter<ListUser>(context, layoutRes, userList) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)

        val user = userList.get(position)
        itemView.findViewById<TextView>(R.id.item_name).setText(user.name)
        itemView.findViewById<TextView>(R.id.item_username).setText(user.username)
        itemView.findViewById<TextView>(R.id.item_email).setText(user.email)
        return itemView
    }
}