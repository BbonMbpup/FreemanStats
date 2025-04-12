package com.example.freemanstats.presentation

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.freemanstats.R

class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val playerPositionTextView: TextView = itemView.findViewById(R.id.tv_playerPosition)
    val playerNameTextView: TextView = itemView.findViewById(R.id.playerNameTextView)
    val townHallImageView: ImageView = itemView.findViewById(R.id.iv_town_hall)
    val percentAttack1TextView: TextView = itemView.findViewById(R.id.attack1_percent)
    val target1TextView: TextView = itemView.findViewById(R.id.attack1_target)
    val percentAttack2TextView: TextView = itemView.findViewById(R.id.attack2_percent)
    val target2TextView: TextView = itemView.findViewById(R.id.attack2_target)

    val starConstraintLayout1: ConstraintLayout = itemView.findViewById(R.id.star_container1)
    val starConstraintLayout2: ConstraintLayout = itemView.findViewById(R.id.star_container2)


    val defenseTextView: TextView = itemView.findViewById(R.id.defenseTextView)
}