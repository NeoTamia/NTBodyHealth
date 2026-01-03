package re.neotamia.bodyhealth.core.listeners

import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mannequin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import re.neotamia.bodyhealth.core.BodyHealthPlugin
import re.neotamia.bodyhealth.core.body.BodyDetection

class PlayerListener(val plugin: BodyHealthPlugin) : Listener {

    val logger = plugin.logger;

//    @EventHandler
//    fun onPlayerDamage(event: EntityDamageEvent) {
//        plugin.logger.info("Entity damaged event triggered.");
//
//        val player = event.entity;
//
//        if (player !is Player) {
//            return;
//        }
//
//        plugin.logger.info("Player ${player.name} took damage: ${event.damage}");
//    }


    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        if (event.isCancelled) return;

        val target = event.entity;

        if (target !is Player && target !is Mannequin && target !is ArmorStand) {
            return;
        }

        when (event) {
            is EntityDamageByEntityEvent -> {
                logger.info("Receive Damage event by Entity.");
                if (event.damageSource.directEntity is Arrow) {
                    logger.info("Damage caused by Arrow.");
                    val source = event.damageSource.sourceLocation;

                    if (source == null) {
                        logger.info("Source location is null.");
                        return;
                    }

                    val detection = BodyDetection(target as LivingEntity, plugin);
                    detection.detectHitBodyPart(source);
                }

            }

            is EntityDamageByBlockEvent -> {
                logger.info("Receive Damage event by Block.");
                val source = event.damageSource;
                logger.info("Damage source: $source");
            }

            else -> {
                logger.info("Unknown damage source.");
            }
        }




    }

}
