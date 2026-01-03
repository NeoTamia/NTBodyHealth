package re.neotamia.bodyhealth.core.body.config

import re.neotamia.bodyhealth.core.body.BodyPart
import re.neotamia.nightconfig.core.serde.annotations.SerdeComment

/**
 * Configuration for body types including basic, advanced, and realistic parts.
 * @param basicParts The list of basic body parts.
 * @param advancedParts The configuration for advanced body parts.
 * @param realisticParts The configuration for realistic body parts.
 */
data class BodyConfig(
    @SerdeComment("The sum of every basic-parts factors should be 1.0")
    val basicParts: List<BodyPart> = listOf(),
    @SerdeComment("Each advanced-parts factor is a multiplier applied to the corresponding basic-part")
    val advancedParts: Parts = Parts(
        enabled = false,
        parts = listOf()
    ),
    @SerdeComment("Each realistic-parts factor is a multiplier applied to the corresponding advanced-part")
    val realisticParts: Parts = Parts(
        enabled = false,
        parts = listOf()
    )
)
