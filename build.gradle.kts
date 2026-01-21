import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `ntbodyhealth-build`
    alias(libs.plugins.paperweight) apply false
    alias(libs.plugins.runPaper)
    alias(libs.plugins.resourceFactoryBukkit) // For plugin.yml generation
    alias(libs.plugins.resourceFactoryPaper) // For paper-plugin.yml generation
}

extra["localJarRepo"] = true

dependencies {
    implementation(projects.modules.ntBodyHealthCore)
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.11")
    }
}

tasks.jar {
    manifest.attributes(
        "paperweight-mappings-namespace" to "mojang"
    )
}

paperPluginYaml {
    name = rootProject.name
    main = "re.neotamia.bodyhealth.core.BodyHealthPlugin"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    apiVersion = "1.21"
    version = project.version.toString()
}

bukkitPluginYaml {
    name = rootProject.name
    main = "re.neotamia.bodyhealth.core.BodyHealthPlugin"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    apiVersion = "1.21"
    version = project.version.toString()
}
