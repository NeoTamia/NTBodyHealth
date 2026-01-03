package re.neotamia.bodyhealth.core.body.config

import re.neotamia.config.annotation.ConfigVersion
import re.neotamia.bodyhealth.core.body.BodyPart
import re.neotamia.bodyhealth.core.body.BodyPartType
import re.neotamia.nightconfig.core.serde.annotations.SerdeComment

/**
 * Global Configuration for Body system.
 * @param version The configuration version.
 * @param bodyType The body type configuration.
 */
data class Config(
    @ConfigVersion
    @SerdeComment("Never change this value unless you know what you are doing")
    val version: String = "0.1",

    @SerdeComment("""
Body type configuration.
Basic is always enabled.
If you enable Advanced or Realistic, the corresponding body parts will be used in damage calculations.
Options:
 - Basic: Head, Chest, Legs
 - Advanced: Head, Shoulders, Arms, Hands, Chest, Thighs, Knees, Legs, Feet
 - Realistic: Eyes, Left/Right for Shoulder, Arm, Hand, Torso, Thigh, Knee, Leg, Foot
    """)
    val body: BodyConfig = BodyConfig(
        basicParts = listOf(
            BodyPart(BodyPartType.HEAD, 0.4),
            BodyPart(BodyPartType.CHEST, 0.4),
            BodyPart(BodyPartType.LEGS, 0.2),
            BodyPart(BodyPartType.FEET, 0.2)
        ),
        advancedParts = Parts(
            enabled = true,
            parts = listOf(
                BodyPart(BodyPartType.SHOULDERS, 0.5),
                BodyPart(BodyPartType.ARMS, 0.5),
                BodyPart(BodyPartType.HANDS, 0.3),
                BodyPart(BodyPartType.CHEST, 0.5),

                BodyPart(BodyPartType.THIGHS, 0.3),
                BodyPart(BodyPartType.KNEES, 0.2),
                BodyPart(BodyPartType.LEGS, 0.2),
                BodyPart(BodyPartType.FEET, 0.2)
            )
        ),
        realisticParts = Parts(
            enabled = true,
            parts = listOf(
                BodyPart(BodyPartType.LEFT_SHOULDER, 0.3),
                BodyPart(BodyPartType.RIGHT_SHOULDER, 0.3),

                BodyPart(BodyPartType.LEFT_ARM, 0.2),
                BodyPart(BodyPartType.RIGHT_ARM, 0.2),

                BodyPart(BodyPartType.LEFT_HAND, 0.1),
                BodyPart(BodyPartType.RIGHT_HAND, 0.1),

                BodyPart(BodyPartType.TOP_TORSO, 0.4),
                BodyPart(BodyPartType.BOTTOM_TORSO, 0.4),

                BodyPart(BodyPartType.LEFT_THIGH, 0.2),
                BodyPart(BodyPartType.RIGHT_THIGH, 0.2),

                BodyPart(BodyPartType.LEFT_KNEE, 0.1),
                BodyPart(BodyPartType.RIGHT_KNEE, 0.1),

                BodyPart(BodyPartType.LEFT_LEG, 0.1),
                BodyPart(BodyPartType.RIGHT_LEG, 0.1),

                BodyPart(BodyPartType.LEFT_FOOT, 0.1),
                BodyPart(BodyPartType.RIGHT_FOOT, 0.1)
            )
        )
    )
)
