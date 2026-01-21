package re.neotamia.bodyhealth.core.body

import org.bukkit.Location
import org.bukkit.entity.Interaction
import org.bukkit.entity.LivingEntity
import re.neotamia.bodyhealth.core.BodyHealthPlugin
import re.neotamia.bodyhealth.core.body.config.Config
import java.util.logging.Logger
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Class responsible for detecting which body part of a player was hit.
 * @constructor Creates a BodyDetection instance with the specified target player and source entity.
 * @param livingEntity The living entity (player) being targeted.
 * @param config The body configuration settings.
 */
class BodyDetection {
    private val livingEntity: LivingEntity

    private val config: Config
    private val logger: Logger

    /**
     * Initializes a new instance of BodyDetection.
     * @param livingEntity The living entity (player) being targeted.
     * @param plugin The plugin instance containing configuration and logger.
     */
    constructor(livingEntity: LivingEntity, plugin: BodyHealthPlugin) {
        this.livingEntity = livingEntity
        this.config = plugin.config
        this.logger = plugin.logger
    }

    private fun combineSides(lateral: LocationType, frontal: LocationType): LocationType =
        when {
            // 1. Check for Corners (Both Lateral and Frontal are non-center)
            lateral == LocationType.LEFT && frontal == LocationType.FRONT -> LocationType.FRONT_LEFT
            lateral == LocationType.LEFT && frontal == LocationType.BACK -> LocationType.BACK_LEFT
            lateral == LocationType.RIGHT && frontal == LocationType.FRONT -> LocationType.FRONT_RIGHT
            lateral == LocationType.RIGHT && frontal == LocationType.BACK -> LocationType.BACK_RIGHT

            // 2. Check for pure Lateral Sides (Frontal is CENTER)
            lateral == LocationType.LEFT -> LocationType.LEFT
            lateral == LocationType.RIGHT -> LocationType.RIGHT

            // 3. Check for pure Frontal Sides (Lateral is CENTER)
            frontal == LocationType.FRONT -> LocationType.FRONT
            frontal == LocationType.BACK -> LocationType.BACK

            // 5. Fallback to CENTER (should not happen)
            else -> LocationType.CENTER
        }

    /**
     * Detects which side of the body was hit based on the source location.
     * /!\ Don't touch at the divider if you don't know what you are doing /!\
     * @param source The location of the source that caused the hit.
     * @param isBody Whether to use body detection (true) or feet detection (false).
     * @return The type of body location that was hit.
     */
    fun detectBodySide(source: Location, isBody: Boolean = true): LocationType {
        // 1. Get the direction vector from player to source
        val playerLoc = livingEntity.location
        val dx = source.x - playerLoc.x
        val dz = source.z - playerLoc.z

        val maxDist = max(abs(dx), abs(dz))
        val nx = if (maxDist > 0) dx / maxDist else 0.0
        val nz = if (maxDist > 0) dz / maxDist else 0.0

        val yawRad = Math.toRadians(-livingEntity.bodyYaw.toDouble())
        val cosYaw = cos(yawRad)
        val sinYaw = sin(yawRad)

        // Standard 2D rotation for Minecraft's coordinate system
        val localX = nx * cosYaw - nz * sinYaw
        val localZ = nx * sinYaw + nz * cosYaw

        // 3. Define sensitivity based on a normalized scale (-1.0 to 1.0)
        // Instead of using width, we use a threshold (e.g., 0.3)
        val threshold = if (isBody) 0.6 else 0.0

        var lateral =
            when {
                localX < -threshold -> LocationType.RIGHT
                localX > threshold -> LocationType.LEFT
                else -> LocationType.CENTER
            }

        if (!isBody) {
            lateral = if (localX >= 0) LocationType.LEFT else LocationType.RIGHT
        }

        val frontal =
            when {
                localZ < -threshold -> LocationType.BACK
                localZ > threshold -> LocationType.FRONT
                else -> LocationType.CENTER
            }

        // 4. Combine (Logic remains the same)
        return combineSides(lateral, frontal)
    }

    fun basicDetection(source: Location): BodyPartType {
        val sy = source.y
        val ly = livingEntity.location.y
        val isCrouch = livingEntity.isSneaking
        val totalHeight = livingEntity.height

        return when {
            sy > ly + BodyPartType.HEAD.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.HEAD
            sy > ly + BodyPartType.CHEST.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.CHEST
            sy > ly + BodyPartType.LEGS.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.LEGS
            sy > ly + BodyPartType.FEET.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.FEET
            else -> BodyPartType.UNKNOWN
        }
    }

    fun advancedDetection(source: Location): BodyPartType {
        val sy = source.y
        val ly = livingEntity.location.y
        val isCrouch = livingEntity.isSneaking
        val totalHeight = livingEntity.height

        val side = detectBodySide(source)

        logger.info("Advanced detection side: $side")

        return when {
            sy > ly + BodyPartType.HEAD.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.HEAD
            sy > ly + BodyPartType.SHOULDERS.calculatePartHeight(totalHeight, isCrouch) && (side.isLeft() || side.isRight()) -> BodyPartType.SHOULDERS
            sy > ly + BodyPartType.ARMS.calculatePartHeight(totalHeight, isCrouch) && (side.isLeft() || side.isRight()) -> BodyPartType.ARMS
            sy > ly + BodyPartType.HANDS.calculatePartHeight(totalHeight, isCrouch) && (side.isLeft() || side.isRight()) -> BodyPartType.HANDS
            sy > ly + BodyPartType.CHEST.calculatePartHeight(totalHeight, isCrouch) && side.isFacing() -> BodyPartType.CHEST
            sy > ly + BodyPartType.THIGHS.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.THIGHS
            sy > ly + BodyPartType.KNEES.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.KNEES
            sy > ly + BodyPartType.LEGS.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.LEGS
            sy > ly + BodyPartType.FEET.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.FEET
            else -> BodyPartType.UNKNOWN
        }
    }

    fun realisticDetection(source: Location): BodyPartType {
        val sy = source.y
        val ly = livingEntity.location.y
        val totalHeight = livingEntity.height
        val isCrouch = livingEntity.isSneaking

        val bSide = detectBodySide(source)
        val fSide = detectBodySide(source, false)

        logger.info("Realistic detection body: $bSide")
        logger.info("Realistic detection feet: $fSide")

        return when {
            sy > ly + BodyPartType.HEAD.calculatePartHeight(totalHeight, isCrouch) -> BodyPartType.HEAD

            sy > ly + BodyPartType.TOP_TORSO.calculatePartHeight(totalHeight, isCrouch) && bSide.isFacing() -> BodyPartType.TOP_TORSO
            sy > ly + BodyPartType.BOTTOM_TORSO.calculatePartHeight(totalHeight, isCrouch) && bSide.isFacing() -> BodyPartType.BOTTOM_TORSO

            sy > ly + BodyPartType.LEFT_SHOULDER.calculatePartHeight(totalHeight, isCrouch) && bSide.isLeft() -> BodyPartType.LEFT_SHOULDER
            sy > ly + BodyPartType.RIGHT_SHOULDER.calculatePartHeight(totalHeight, isCrouch) && bSide.isRight() -> BodyPartType.RIGHT_SHOULDER

            sy > ly + BodyPartType.LEFT_ARM.calculatePartHeight(totalHeight, isCrouch) && bSide.isLeft() -> BodyPartType.LEFT_ARM
            sy > ly + BodyPartType.RIGHT_ARM.calculatePartHeight(totalHeight, isCrouch) && bSide.isRight() -> BodyPartType.RIGHT_ARM

            sy > ly + BodyPartType.LEFT_HAND.calculatePartHeight(totalHeight, isCrouch) && bSide.isLeft() -> BodyPartType.LEFT_HAND
            sy > ly + BodyPartType.RIGHT_HAND.calculatePartHeight(totalHeight, isCrouch) && bSide.isRight() -> BodyPartType.RIGHT_HAND

            sy > ly + BodyPartType.LEFT_THIGH.calculatePartHeight(totalHeight, isCrouch) && fSide.isLeft() -> BodyPartType.LEFT_THIGH
            sy > ly + BodyPartType.RIGHT_THIGH.calculatePartHeight(totalHeight, isCrouch) && fSide.isRight() -> BodyPartType.RIGHT_THIGH

            sy > ly + BodyPartType.LEFT_KNEE.calculatePartHeight(totalHeight, isCrouch) && fSide.isLeft() -> BodyPartType.LEFT_KNEE
            sy > ly + BodyPartType.RIGHT_KNEE.calculatePartHeight(totalHeight, isCrouch) && fSide.isRight() -> BodyPartType.RIGHT_KNEE

            sy > ly + BodyPartType.LEFT_LEG.calculatePartHeight(totalHeight, isCrouch) && fSide.isLeft() -> BodyPartType.LEFT_LEG
            sy > ly + BodyPartType.RIGHT_LEG.calculatePartHeight(totalHeight, isCrouch) && fSide.isRight() -> BodyPartType.RIGHT_LEG

            sy > ly + BodyPartType.LEFT_FOOT.calculatePartHeight(totalHeight, isCrouch) && fSide.isLeft() -> BodyPartType.LEFT_FOOT
            sy > ly + BodyPartType.RIGHT_FOOT.calculatePartHeight(totalHeight, isCrouch) && fSide.isRight() -> BodyPartType.RIGHT_FOOT

            // Fallbacks but should not happen
            else -> BodyPartType.UNKNOWN
        }
    }

    /**
     * Detects which body part was hit based on the location and body configuration.
     * @param source The entity that caused the hit.
     * @return The type of body part that was hit.
     */
    fun detectHitBodyPart(source: Location): BodyPartType {
        logger.info("---------------------------------")
        logger.info("Player ${livingEntity.name} (${livingEntity.uniqueId})")
        logger.info("Width: ${livingEntity.width} | Height: ${livingEntity.height}")
        logger.info("---------------------------------")
        logger.info("Source (${source.x}, ${source.y}, ${source.z})")
        logger.info("---------------------------------")

        livingEntity.world.spawn(source, Interaction::class.java) {
            it.interactionHeight = 0.1f
            it.interactionWidth = 0.1f
            it.isResponsive = false
        }

        var part: BodyPartType

        if (config.body.realisticParts.enabled) {
            part = realisticDetection(source)
            logger.info("Realistic detection: Hit body part = ${part.name}")
        } else if (config.body.advancedParts.enabled) {
            part = advancedDetection(source)
            logger.info("Advanced detection: Hit body part = ${part.name}")
        } else {
            part = basicDetection(source)
            logger.info("Basic detection: Hit body part = ${part.name}")
        }
        return part
    }
}
