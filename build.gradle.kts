plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "io.github.lumine1909"
version = "1.1"

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}