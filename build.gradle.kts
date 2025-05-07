plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
    id("com.gradleup.shadow") version "9.0.0-beta11"
}

group = "io.github.lumine1909"
version = "1.3.0"
description = "A plugin that provide server side support for OpenBoatUtils mod"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation("io.github.lumine1909:messageutil:1.0.3")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveFileName.set("PaperBoatUtils-${version}-MC-1.21.4.jar")
        minimize()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.21"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}