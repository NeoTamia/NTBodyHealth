plugins {
    id("ntbodyhealth-build")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

extra["localJarRepo"] = true

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation(libs.bundles.ntConfig)
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.11")
    }
}

tasks.processResources {
    val props = mapOf(
        "version" to version,
        "name" to project.name
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
