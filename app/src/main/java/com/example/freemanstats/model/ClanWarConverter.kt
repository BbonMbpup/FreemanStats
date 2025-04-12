package com.example.freemanstats.model

object ClanWarConverter {
    fun ClanWar.toWarLogItems(): List<WarLogItem> {
        val logItems = mutableListOf<WarLogItem>()

        // Обрабатываем атаки нашего клана
        clan.members.forEach { member ->
            member.attacks?.forEach { attack ->
                val defender = opponent.members.find { it.tag == attack.defenderTag }
                defender?.let {
                    logItems.add(
                        WarLogItem(
                            playerTag = member.tag,
                            playerName = member.name,
                            playerNumber = member.mapPosition,
                            isAttack = true,
                            opponentTag = defender.tag,
                            opponentName = defender.name,
                            opponentNumber = defender.mapPosition,
                            stars = attack.stars,
                            points = calculateAttackPoints(attack.stars),
                            order = attack.order
                        )
                    )
                }
            }
        }

        // Обрабатываем защиты нашего клана
        clan.members.forEach { member ->
            member.bestOpponentAttack?.let { attack ->
                val attacker = opponent.members.find { it.tag == attack.attackerTag }
                attacker?.let {
                    logItems.add(
                        WarLogItem(
                            playerTag = member.tag,
                            playerName = member.name,
                            playerNumber = member.mapPosition,
                            isAttack = false,
                            opponentTag = attacker.tag,
                            opponentName = attacker.name,
                            opponentNumber = attacker.mapPosition,
                            stars = attack.stars,
                            points = calculateDefensePoints(attack.stars),
                            order = attack.order
                        )
                    )
                }
            }
        }

        return logItems.sortedByDescending { it.order }
    }

    private fun calculateAttackPoints(stars: Int): Int {
        return if (stars == 3) 2 else 0
    }

    private fun calculateDefensePoints(stars: Int): Int {
        return if (stars < 3) 1 else -1
    }
}