package re.neotamia.bodyhealth.core

import org.bukkit.plugin.java.JavaPlugin
import re.neotamia.config.NTConfig
import re.neotamia.bodyhealth.core.body.config.Config
import re.neotamia.bodyhealth.core.listeners.PlayerListener
import re.neotamia.nightconfig.core.serde.NamingStrategy
import re.neotamia.nightconfig.yaml.YamlFormat


class BodyHealthPlugin: JavaPlugin() {

    val c: NTConfig = NTConfig();

    val configFilename: String = "body-config.yml";

    var config: Config = Config();

    override fun onEnable() {
        if (!this.dataFolder.exists()) this.dataFolder.mkdirs();

        logger.info("Loading configuration...");

        c.registerFormat(YamlFormat.defaultInstance(), "yaml", "yml");
        c.setNamingStrategy(NamingStrategy.KEBAB_CASE);


//        val result = config.load(
//            this.dataFolder.toPath().resolve(configFilename),
//            bodyConfig
//        );

//        println(result);

        c.save(
            this.dataFolder.toPath().resolve(configFilename),
            config
        );

//        val result = config.loadWithMigration<BodyConfig>(
//            this.dataFolder.toPath().resolve(configFilename),
//            BodyConfig::class.java,
//            bodyConfig
//        );

//        logger.info(result.toString());

        logger.info("Enabling plugins...");

        server.pluginManager.registerEvents(PlayerListener(this), this);
    }

    override fun onDisable() {
        logger.info("Disabling plugins...");
    }

}
