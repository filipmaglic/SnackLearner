package com.example.snacklearner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private var userList: List<Triple<String, String, String>>,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var isAdmin = false

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val roleTextView: TextView = itemView.findViewById(R.id.roleTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val (email, role, uid) = userList[position]
        holder.emailTextView.text = email
        holder.roleTextView.text = role

        // Ako je admin, prikaži gumb za brisanje, inače sakrij
        holder.deleteButton.visibility = if (isAdmin) View.VISIBLE else View.GONE
        holder.deleteButton.setOnClickListener {
            if (isAdmin) onDeleteClicked(uid)
        }
    }

    override fun getItemCount(): Int = userList.size

    fun updateData(newList: List<Triple<String, String, String>>) {
        userList = newList
        notifyDataSetChanged()
    }

    //Omogućuje postavljanje admin mode-a
    fun setAdminMode(admin: Boolean) {
        isAdmin = admin
        notifyDataSetChanged()
    }
}
