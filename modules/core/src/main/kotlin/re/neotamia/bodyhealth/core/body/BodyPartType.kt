package re.neotamia.bodyhealth.core.body

import re.neotamia.bodyhealth.core.body.config.Config

/**
 * Enumeration of body part types with their associated configuration levels.
 * Each body part type is linked to a specific PartConfiguration level.
 * @property config The configuration level associated with the body part type.
 */
enum class BodyPartType(val config: PartConfiguration) {
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

    fun calculatePartHeight(totalHeight: Double, isCrouch: Boolean): Double {
        return when (this) {
            HEAD -> if (!isCrouch) totalHeight * 0.7727230538 else 0.0;
            SHOULDERS, LEFT_SHOULDER, RIGHT_SHOULDER -> if (!isCrouch) totalHeight * 0.714370381 else 0.0;
            TOP_TORSO -> if (!isCrouch) totalHeight * 0.5581918394 else 0.0;
            ARMS, LEFT_ARM, RIGHT_ARM -> if (!isCrouch) totalHeight * 0.4805437296 else 0.0;
            HANDS, LEFT_HAND, RIGHT_HAND -> if (!isCrouch) totalHeight * 0.3846965965 else 0.0;
            CHEST, BOTTOM_TORSO -> if (!isCrouch) totalHeight * 0.3846965965 else 0.0;
            THIGHS, LEFT_THIGH, RIGHT_THIGH -> if (!isCrouch) totalHeight * 0.2353702323 else 0.0;
            KNEES, LEFT_KNEE, RIGHT_KNEE -> if (!isCrouch) totalHeight * 0.2093453735 else 0.0;
            LEGS, LEFT_LEG, RIGHT_LEG -> if (!isCrouch) totalHeight * 0.1201870994 else 0.0;
            FEET, LEFT_FOOT, RIGHT_FOOT -> 0.0;
            else -> 0.0
        }
    }

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
