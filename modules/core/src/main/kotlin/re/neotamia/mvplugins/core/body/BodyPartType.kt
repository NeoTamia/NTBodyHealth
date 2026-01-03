package re.neotamia.mvplugins.core.body

import re.neotamia.mvplugins.core.body.config.Config

/**
 * Enumeration of body part types with their associated configuration levels.
 * Each body part type is linked to a specific PartConfiguration level.
 * @property config The configuration level associated with the body part type.
 */
enum class BodyPartType(val config: PartConfiguration) {
    EYES(PartConfiguration.REALISTIC), // Realistic
    HEAD(PartConfiguration.BASIC), // Basic

    SHOULDERS(PartConfiguration.ADVANCED), // Advanced
    LEFT_SHOULDER(PartConfiguration.REALISTIC), // Realistic
    RIGHT_SHOULDER(PartConfiguration.REALISTIC), // Realistic

    ARMS(PartConfiguration.ADVANCED), // Advanced
    LEFT_ARM(PartConfiguration.REALISTIC), // Realistic
    RIGHT_ARM(PartConfiguration.REALISTIC), // Realistic

    HANDS(PartConfiguration.ADVANCED), // Advanced
    LEFT_HAND(PartConfiguration.REALISTIC), // Realistic
    RIGHT_HAND(PartConfiguration.REALISTIC), // Realistic

    CHEST(PartConfiguration.BASIC), // Basic
    TOP_TORSO(PartConfiguration.REALISTIC), // Realistic
    BOTTOM_TORSO(PartConfiguration.REALISTIC), // Realistic

    THIGHS(PartConfiguration.ADVANCED), // Advanced
    LEFT_THIGH(PartConfiguration.REALISTIC), // Realistic
    RIGHT_THIGH(PartConfiguration.REALISTIC), // Realistic

    KNEES(PartConfiguration.ADVANCED), // Advanced
    LEFT_KNEE(PartConfiguration.REALISTIC), // Realistic
    RIGHT_KNEE(PartConfiguration.REALISTIC), // Realistic

    LEGS(PartConfiguration.BASIC), // Basic
    LEFT_LEG(PartConfiguration.REALISTIC), // Realistic
    RIGHT_LEG(PartConfiguration.REALISTIC), // Realistic

    FEET(PartConfiguration.ADVANCED), // Advanced
    LEFT_FOOT(PartConfiguration.REALISTIC), // Realistic
    RIGHT_FOOT(PartConfiguration.REALISTIC), // Realistic

    UNKNOWN(PartConfiguration.BASIC);

    fun isEnabledInConfig(config: Config): Boolean {
        return when (this.config) {
            PartConfiguration.BASIC -> config.body.basicParts.any { it.type == this }
            PartConfiguration.ADVANCED -> config.body.advancedParts.enabled &&
                    config.body.advancedParts.parts.any { it.type == this }
            PartConfiguration.REALISTIC -> config.body.realisticParts.enabled &&
                    config.body.realisticParts.parts.any { it.type == this }
        }
    }

}
