package re.neotamia.bodyhealth.core.body.config

import re.neotamia.bodyhealth.core.body.BodyPart

/**
 * Configuration for a set of body parts.
 * @param enabled Indicates if this set of body parts is enabled.
 * @param parts The list of body parts in this set.
 */
data class Parts(
    val enabled: Boolean = false,
    val parts: List<BodyPart> = listOf()
)
