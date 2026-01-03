package re.neotamia.mvplugins.core.body

import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Interaction
import org.bukkit.entity.LivingEntity
import re.neotamia.mvplugins.core.MVPlugins
import re.neotamia.mvplugins.core.body.config.Config
import java.util.logging.Logger
import kotlin.div
import kotlin.math.cos
import kotlin.math.sin
import kotlin.unaryMinus

/**
 * Class responsible for detecting which body part of a player was hit.
 * @constructor Creates a BodyDetection instance with the specified target player and source entity.
 * @param livingEntity The living entity (player) being targeted.
 * @param config The body configuration settings.
 */
class BodyDetection {

    private val livingEntity: LivingEntity;

    private val config: Config;
    private val logger: Logger;

    /**
     * Initializes a new instance of BodyDetection.
     * @param livingEntity The living entity (player) being targeted.
     * @param plugin The MVPlugins instance containing configuration and logger.
     */
    constructor(livingEntity: LivingEntity, plugin: MVPlugins) {
        this.livingEntity = livingEntity;
        this.config = plugin.config;
        this.logger = plugin.logger;
    };

    fun getScale(): Double {
        val playerScaleAttribute = livingEntity.getAttribute(Attribute.SCALE);
        var scale: Double = 1.0;

        if (playerScaleAttribute?.value is Double) {
            scale = playerScaleAttribute.value;
        }

        return scale;
    }

    /**
     * Detects which side of the body was hit based on the source location.
     * /!\ Don't touch at the divider if you don't know what you are doing /!\
     * @param source The location of the source that caused the hit.
     * @param divider The number of divisions to determine side sensitivity (default is 5.0).
     * @return The type of body location that was hit.
      */
    fun detectBodySide(source: Location, divider: Double = 5.0): LocationType {

        if (divider <= 0.0) {
            throw IllegalArgumentException("Divider must be greater than 0.0");
        }

        val scale = getScale();
        val playerWidth = livingEntity.width * scale;
        val playerRadius = playerWidth / 2.0;
        val centerOfPlayer = livingEntity.location;

        // Convert bodyYaw to radians
        // Minecraft yaw: 0° = south (+Z), 90° = west (-X), 180° = north (-Z), 270° = east (+X)
        val bodyYawRadians = Math.toRadians(livingEntity.bodyYaw.toDouble())

        // Calculate the vector from player to source in world coordinates
        val dx = source.x - centerOfPlayer.x
        val dz = source.z - centerOfPlayer.z

        // Rotate the vector to player's local coordinate system
        // We need to rotate by -bodyYaw to align with player's facing direction
        // In Minecraft's coordinate system, we need to adjust the rotation
        val localX = dx * cos(bodyYawRadians) + dz * sin(bodyYawRadians)
        val localZ = -dx * sin(bodyYawRadians) + dz * cos(bodyYawRadians)

        // Define boundaries for left/right and front/back
        val rightOrBackBoundary = -playerRadius + (playerWidth / divider);
        val leftOrFrontBoundary = playerRadius - (playerWidth / divider);

        // Determine lateral position (left/center/right)
        val lateral = when {
            localX < rightOrBackBoundary -> LocationType.RIGHT
            localX > leftOrFrontBoundary -> LocationType.LEFT
            else -> LocationType.CENTER
        }

        // Determine frontal position (front/center/back)
        val frontal = when {
            localZ < rightOrBackBoundary -> LocationType.BACK
            localZ > leftOrFrontBoundary -> LocationType.FRONT
            else -> LocationType.CENTER
        }

        // Combine to detect corners and sides
         val side = when {
            lateral == LocationType.LEFT && frontal == LocationType.FRONT -> LocationType.FRONT_LEFT
            lateral == LocationType.LEFT && frontal == LocationType.BACK -> LocationType.BACK_LEFT
            lateral == LocationType.LEFT -> LocationType.LEFT

            lateral == LocationType.RIGHT && frontal == LocationType.FRONT -> LocationType.FRONT_RIGHT
            lateral == LocationType.RIGHT && frontal == LocationType.BACK -> LocationType.BACK_RIGHT
            lateral == LocationType.RIGHT -> LocationType.RIGHT

            frontal == LocationType.FRONT -> LocationType.FRONT
            frontal == LocationType.BACK -> LocationType.BACK
            else -> LocationType.CENTER
        }

        logger.info("Hit side: $side (localX: ${"%.2f".format(localX)}, localZ: ${"%.2f".format(localZ)}, bodyYaw: ${"%.1f".format(livingEntity.bodyYaw)})")

        return side;
    }

//    fun detectBodySide(source: Location) {
//        val playerWidth = livingEntity.width;
//        val playerRadius = playerWidth / 2.0;
//        val centerOfPlayer = livingEntity.location;
//
//        // Convert bodyYaw to radians (bodyYaw is in degrees)
//        val bodyYawRadians = Math.toRadians(livingEntity.bodyYaw.toDouble());
//
//        // Calculate perpendicular offset (90 degrees to the left/right)
//        // Left side: add 90 degrees (π/2 radians)
//        val leftX = livingEntity.location.x + playerRadius * sin(bodyYawRadians + Math.PI / 2)
//        val leftZ = livingEntity.location.z - playerRadius * cos(bodyYawRadians + Math.PI / 2)
//
//        // Right side: subtract 90 degrees (-π/2 radians)
//        val rightX = livingEntity.location.x + playerRadius * sin(bodyYawRadians - Math.PI / 2)
//        val rightZ = livingEntity.location.z - playerRadius * cos(bodyYawRadians - Math.PI / 2)
//
//        // Front: direction player is facing
//        val frontX = livingEntity.location.x - playerRadius * sin(bodyYawRadians)
//        val frontZ = livingEntity.location.z + playerRadius * cos(bodyYawRadians)
//
//        // Back: opposite of front
//        val backX = livingEntity.location.x + playerRadius * sin(bodyYawRadians)
//        val backZ = livingEntity.location.z - playerRadius * cos(bodyYawRadians)
//
//        val distLeft = kotlin.math.sqrt((source.x - leftX).pow(2) + (source.z - leftZ).pow(2))
//        val distRight = kotlin.math.sqrt((source.x - rightX).pow(2) + (source.z - rightZ).pow(2))
//        val distFront = kotlin.math.sqrt((source.x - frontX).pow(2) + (source.z - frontZ).pow(2))
//        val distBack = kotlin.math.sqrt((source.x - backX).pow(2) + (source.z - backZ).pow(2))
//
//        val minDistLateral = minOf(distLeft, distRight);
//        val minDistFace = minOf(distFront, distBack);
//
//        var side = "";
//
//        side += when (minDistLateral) {
//            distLeft -> "LEFT"
//            distRight -> "RIGHT"
//            else -> "UNKNOWN"
//        }
//        side += " - "
//        side += when (minDistFace) {
//            distFront -> "FRONT"
//            distBack -> "BACK"
//            else -> "UNKNOWN"
//        }
//
//        logger.info("Hit side: $side");
//
//
////
////        logger.info("Location X=${livingEntity.location.x}, Z=${livingEntity.location.z}")
////        logger.info("Source location: X=${source.x}, Z=${source.z}")
////        logger.info("Left side: X=$leftX, Z=$leftZ | ")
////        logger.info("Right side: X=$rightX, Z=$rightZ")
//    }

    fun basicDetection(source: Location): BodyPartType {
        val sy = source.y;
        val ly = livingEntity.location.y;
        val scale = getScale();

        return when {
            sy > ly + (1.35 * scale) -> BodyPartType.HEAD
            sy > ly + (0.65 * scale) -> BodyPartType.CHEST
            sy > ly + (0.2 * scale) -> BodyPartType.LEGS
            else -> BodyPartType.FEET
        }
    }

    fun advancedDetection(source: Location): BodyPartType {
        val sy = source.y;
        val ly = livingEntity.location.y;
        val scale = getScale();

        val side = detectBodySide(source);

        return when {
            sy > ly + (1.35 * scale) -> BodyPartType.HEAD
            sy > ly + (1.3 * scale) && (side.isLeft() || side.isRight()) -> BodyPartType.SHOULDERS
            sy > ly + (1.0 * scale) && (side.isLeft() || side.isRight()) -> BodyPartType.ARMS
            sy > ly + (0.66 * scale) && (side.isLeft() || side.isRight()) -> BodyPartType.HANDS
            sy > ly + (0.8 * scale) && side.isFacing() -> BodyPartType.CHEST
            sy > ly + (0.35 * scale) -> BodyPartType.THIGHS
            sy > ly + (0.28 * scale) -> BodyPartType.KNEES
            sy > ly + (0.2 * scale) -> BodyPartType.LEGS
            else -> BodyPartType.FEET
        }
    }

    fun realisticDetection(source: Location): BodyPartType {
        val sy = source.y;
        val ly = livingEntity.location.y;
        val scale = getScale();

        val bSide = detectBodySide(source);

        val fSide = detectBodySide(source, divider = 2.0);

        return when {
//            sy > ly + (1.5 * scale) -> BodyPartType.EYES
            sy > ly + (1.35 * scale) -> BodyPartType.HEAD

            sy > ly + (1.0 * scale) && bSide.isFacing() -> BodyPartType.TOP_TORSO
            sy > ly + (0.66 * scale) && bSide.isFacing() -> BodyPartType.BOTTOM_TORSO

            sy > ly + (1.25 * scale) && bSide.isLeft() -> BodyPartType.LEFT_SHOULDER
            sy > ly + (1.25 * scale) && bSide.isRight() -> BodyPartType.RIGHT_SHOULDER

            sy > ly + (0.88 * scale) && bSide.isLeft() -> BodyPartType.LEFT_ARM
            sy > ly + (0.88 * scale) && bSide.isRight() -> BodyPartType.RIGHT_ARM

            sy > ly + (0.66 * scale) && bSide.isLeft() -> BodyPartType.LEFT_HAND
            sy > ly + (0.66 * scale) && bSide.isRight() -> BodyPartType.RIGHT_HAND

            sy > ly + (0.35 * scale) && fSide.isLeft() -> BodyPartType.LEFT_THIGH
            sy > ly + (0.35 * scale) && fSide.isRight() -> BodyPartType.RIGHT_THIGH

            // @TODO: Fix KNEE Y detection also transforms every value by a percentage of the height
            sy > ly + (0.28 * scale) && fSide.isLeft() -> BodyPartType.LEFT_KNEE
            sy > ly + (0.28 * scale) && fSide.isRight() -> BodyPartType.RIGHT_KNEE

            sy > ly + (0.2 * scale) && fSide.isLeft() -> BodyPartType.LEFT_LEG
            sy > ly + (0.2 * scale) && fSide.isRight() -> BodyPartType.RIGHT_LEG

            sy > ly + (0.0 * scale) && fSide.isLeft() -> BodyPartType.LEFT_FOOT
            sy > ly + (0.0 * scale) && fSide.isRight() -> BodyPartType.RIGHT_FOOT

            // @TODO: Fix UNKNOWN detection for lateral center hits
            else -> BodyPartType.UNKNOWN
        }
    }

    /**
     * Detects which body part was hit based on the location and body configuration.
     * @param source The entity that caused the hit.
     * @return The type of body part that was hit.
     */
    fun detectHitBodyPart(source: Location) {
        logger.info("---------------------------------")
        logger.info("Player ${livingEntity.name} (${livingEntity.uniqueId})");
        logger.info("Width: ${livingEntity.width} | Height: ${livingEntity.height} | Scale: ${"%.2f".format(getScale())}");
        logger.info("---------------------------------")
        logger.info("Source (${source.x}, ${source.y}, ${source.z})")
        logger.info("---------------------------------")

        livingEntity.world.spawn(source, Interaction::class.java) {
            it.interactionHeight = 0.1f;
            it.interactionWidth = 0.1f;
            it.isResponsive = false;
        }

        if (config.body.realisticParts.enabled) {
            val part = realisticDetection(source);
            logger.info("Realistic detection: Hit body part = ${part.name}");
            return;
        } else if (config.body.advancedParts.enabled) {
            val part = advancedDetection(source);
            logger.info("Advanced detection: Hit body part = ${part.name}");
            return;
        } else {
            val part = basicDetection(source);
            logger.info("Basic detection: Hit body part = ${part.name}");
            return;
        }

    }
}
