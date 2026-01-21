package re.neotamia.bodyhealth.core.body

import re.neotamia.nightconfig.core.serde.annotations.SerdeKey
import re.neotamia.nightconfig.core.serde.annotations.SerdeSkip

/**
 * Represents a body part with health and damage factor.
 * @param type The type of the body part.
 * @param damageFactor The damage multiplier for this body part.
 * @param maxHealth The maximum health of the body part (default is 100.0).
 */
data class BodyPart(
    val type: BodyPartType,
    @SerdeKey("damage-factor")
    val damageFactor: Double,
    @SerdeSkip
    val maxHealth: Double = 100.0
) {
    @SerdeSkip
    private var currentHealth: Double

    /**
     * Initializes the body part with full health.
     */
    init {
        currentHealth = maxHealth
    }

    /**
     * Applies healing to the body part.
     * @param healingAmount The amount of healing to apply.
     * @return The actual amount of healing applied.
     */
    fun applyHealing(healingAmount: Double): Double {
        currentHealth = (currentHealth + healingAmount).coerceAtMost(maxHealth)
        return healingAmount
    }

    /**
     * Applies damage to the body part.
     * @param rawDamage The raw damage to apply.
     * @return The actual damage applied after considering the damage factor.
     */
    fun applyDamage(rawDamage: Double): Double {
        val actualDamage = rawDamage * damageFactor
        currentHealth = (currentHealth - actualDamage).coerceAtLeast(0.0)
        return actualDamage
    }

    /**
     * Calculates the current health percentage of the body part.
     * @return The health percentage as a Double.
     */
    fun getHealthPercentage(): Double = (currentHealth / maxHealth) * 100.0

    /**
     * Checks if the body part is broken (health <= 0).
     * @return True if the body part is broken, false otherwise.
     */
    fun isBroken(): Boolean = currentHealth <= 0.0

    /**
     * Determines the current state of the body part based on its health percentage.
     * @return The PartState representing the health state of the body part.
     */
    fun getState(): PartState {
        val healthPercentage = getHealthPercentage()
        return when {
            healthPercentage > 85.0 -> PartState.HEALTHY
            healthPercentage > 60.0 -> PartState.LIGHTY_INJURED
            healthPercentage > 30.0 -> PartState.INJURED
            healthPercentage > 0.0 -> PartState.HEAVYLY_INJURED
            else -> PartState.BROKEN
        }
    }
}
