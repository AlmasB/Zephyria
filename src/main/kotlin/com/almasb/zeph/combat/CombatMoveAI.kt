package com.almasb.zeph.combat

import com.almasb.fxgl.core.math.FXGLMath

/**
 * @author Almas Baim (https://github.com/AlmasB)
 */
interface CombatMoveAI {

    fun nextMove(previousMoves: List<CombatMove>): CombatMove
}

class RandomCombatMoveAI : CombatMoveAI {

    override fun nextMove(previousMoves: List<CombatMove>): CombatMove {
        return FXGLMath.random(CombatMove.values()).get()
    }
}