package re.neotamia.bodyhealth.core.body

/**
 * Types of body locations for hit detection.
 */
enum class LocationType {
    LEFT,
    FRONT_LEFT,
    BACK_LEFT,
    RIGHT,
    FRONT_RIGHT,
    BACK_RIGHT,

    FRONT,
    BACK,
    CENTER;

    /**
     * Checks if the location is on the left side.
     */
    fun isLeft(): Boolean {
        return this == LEFT || this == FRONT_LEFT || this == BACK_LEFT;
    }

    /**
     * Checks if the location is on the right side.
     */
    fun isRight(): Boolean {
        return this == RIGHT || this == FRONT_RIGHT || this == BACK_RIGHT;
    }

    /**
     * Checks if the location is facing front or back.
     */
    fun isFacing(): Boolean {
        return this == FRONT || this == BACK;
    }

    /**
     * Checks if the location is on the front side.
     */
    fun isFront(): Boolean {
        return this == FRONT || this == FRONT_LEFT || this == FRONT_RIGHT;
    }

    /**
     * Checks if the location is on the back side.
     */
    fun isBack(): Boolean {
        return this == BACK || this == BACK_LEFT || this == BACK_RIGHT;
    }
}
