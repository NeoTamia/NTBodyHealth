plugins {
    id("ntbodyhealth-build")
}

extra["localJarRepo"] = true

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation(libs.bundles.ntConfig)
}
