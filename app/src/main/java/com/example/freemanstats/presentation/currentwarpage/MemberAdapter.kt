package com.example.freemanstats.presentation.currentwarpage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.freemanstats.R
import com.example.freemanstats.domain.model.Attack
import com.example.freemanstats.domain.model.Member

class MemberAdapter(
    private var members: List<Member>,
    private var opponentMembers: List<Member>
) : RecyclerView.Adapter<MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]

        holder.playerPositionTextView.text = "${member.mapPosition}."
        holder.playerNameTextView.text = "${member.name}"
        setImageTownHall(holder.townHallImageView, member.townhallLevel)
        setDefenseData(holder.defenseTextView, member.bestOpponentAttack)

        if (member.attacks.isNullOrEmpty()) {
            // Если атак нет, отображаем "Нет атак"
            holder.percentAttack1TextView.text = "Нет атак"
            holder.percentAttack2TextView.text = "Нет атак"
            resetStars(holder.starConstraintLayout1)
            resetStars(holder.starConstraintLayout2)
        } else {

            if (member.attacks.size >= 1) {
                setAttackData(
                    holder.starConstraintLayout1,
                    holder.percentAttack1TextView,
                    member.attacks,
                    0
                )
            } else {
                holder.percentAttack1TextView.text = "Нет атак"
                resetStars(holder.starConstraintLayout1)
            }

            // Вторая атака
            if (member.attacks.size == 2) {
                setAttackData(
                    holder.starConstraintLayout2,
                    holder.percentAttack2TextView,
                    member.attacks,
                    1
                )
            } else {
                holder.percentAttack2TextView.text = "Нет атак"
                resetStars(holder.starConstraintLayout2)
            }
        }
    }

    override fun getItemCount(): Int {
        return members.size
    }

    private fun setAttackData(
        starContainer: ConstraintLayout,
        textView: TextView,
        attacks: List<Attack>?,
        index: Int
    ) {
        val defenderTextView = starContainer.getChildAt(3) as TextView
        // Получаем ImageView для каждой звезды
        val star1 = starContainer.getChildAt(0) as ImageView
        val star2 = starContainer.getChildAt(1) as ImageView
        val star3 = starContainer.getChildAt(2) as ImageView

        val attack = attacks?.getOrNull(index)
        textView.text = if (attack != null) {
            "${attack.destructionPercentage.toInt()}% "
        } else {
            return
        }
        val defenderPosition =
            getOpponentPosition(attack?.defenderTag ?: throw RuntimeException("Attack is null"))
        val defenderName = getOpponentName(attack.defenderTag)
        defenderTextView.text = ("$defenderPosition.$defenderName")

        star1.setImageResource(if (attack.stars >= 1) R.drawable.yellow_star else R.drawable.gray_star)
        star2.setImageResource(if (attack.stars >= 2) R.drawable.yellow_star else R.drawable.gray_star)
        star3.setImageResource(if (attack.stars >= 3) R.drawable.yellow_star else R.drawable.gray_star)
    }

    private fun setDefenseData(textView: TextView, bestOpponentAttack: Attack?) {
        textView.text = if (bestOpponentAttack != null) {
            "Оборона: ${bestOpponentAttack.destructionPercentage}% разрушено"
        } else {
            "Оборона: Нет данных"
        }
    }

    private fun getOpponentPosition(defenderTag: String): Int {
        return opponentMembers.find { it.tag == defenderTag }?.mapPosition ?: 0
    }

    private fun getOpponentName(defenderTag: String): String {
        return opponentMembers.find { it.tag == defenderTag }?.name ?: "Имя не найдено"
    }

    private fun setImageTownHall(imageView: ImageView, townhallLevel: Int) {
        val resource = when (townhallLevel) {
            3 -> R.drawable.th3
            4 -> R.drawable.th4
            5 -> R.drawable.th5
            6 -> R.drawable.th6
            7 -> R.drawable.th7
            8 -> R.drawable.th8
            9 -> R.drawable.th9
            10 -> R.drawable.th10
            11 -> R.drawable.th11
            12 -> R.drawable.th12
            13 -> R.drawable.th13
            14 -> R.drawable.th14
            15 -> R.drawable.th15
            16 -> R.drawable.th16
            17 -> R.drawable.th17
            else -> throw IllegalArgumentException("Unknown townhall level: $townhallLevel")
        }
        imageView.setImageResource(resource)
    }

    private fun resetStars(starContainer: ConstraintLayout) {
        val star1 = starContainer.getChildAt(0) as ImageView
        val star2 = starContainer.getChildAt(1) as ImageView
        val star3 = starContainer.getChildAt(2) as ImageView
        val defence = starContainer.getChildAt(3) as TextView

        star1.setImageResource(R.drawable.gray_star)
        star2.setImageResource(R.drawable.gray_star)
        star3.setImageResource(R.drawable.gray_star)
        defence.text = ""
    }

    fun updateData(newMembers: List<Member>, newOpponentMembers: List<Member>) {
        Log.d("Adapter", """
            Updating data:
            Members: ${newMembers.size} items
            ${newMembers.joinToString("\n") { "${it.mapPosition}. ${it.name} (${it.tag})" }}
            
            Opponents: ${newOpponentMembers.size} items
            ${newOpponentMembers.joinToString("\n") { "${it.mapPosition}. ${it.name} (${it.tag})" }}
        """.trimIndent())
        this.members = newMembers.sortedBy { it.mapPosition } // Сортируем по mapPosition
        this.opponentMembers = newOpponentMembers
        notifyDataSetChanged()
        Log.d("Adapter", "Данные обновлены")
    }
}