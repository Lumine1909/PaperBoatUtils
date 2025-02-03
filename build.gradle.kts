plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
}

group = "io.github.lumine1909"
version = "1.2.1"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}