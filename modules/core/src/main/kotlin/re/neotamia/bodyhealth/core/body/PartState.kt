package re.neotamia.bodyhealth.core.body

/**
 * Represents the health state of a body part.
 * HEALTHY: 100% to 85%
 * LIGHTY_INJURED: 84% to 60%
 * INJURED: 59% to 30%
 * HEAVYLY_INJURED: 29% to 1%
 * BROKEN: 0%
 */
enum class PartState {
    HEALTHY, // 100 => 85%
    LIGHTY_INJURED, // 84% => 60%
    INJURED, // 59% => 30%
    HEAVYLY_INJURED, // 29% => 1%
    BROKEN // 0%
}
