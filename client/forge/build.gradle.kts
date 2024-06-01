import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.api.LoomGradleExtensionAPI

val mavenGroup: String by rootProject
val targetJavaVersion: String by rootProject

val modId: String by rootProject
val modVersion: String by rootProject
val modAuthors: String by rootProject
val modLicense: String by rootProject
val modDescription: String by rootProject

val minecraftVersion: String by rootProject
val forgeVersion: String by rootProject
val forgeVersionRange: String by rootProject
val minecraftVersionRange: String by rootProject

plugins {
    id("java")
}

group = "$mavenGroup.client.forge"
version = modVersion

configurations {
    create("shadowCommon")
}

architectury {
    compileOnly()
    injectInjectables = false

    platformSetupLoomIde()
    forge()
}

configure<LoomGradleExtensionAPI> {
    forge.apply {
        mixinConfig(
            "common.mixins.json",
            "forge.mixins.json"
        )
    }
}

repositories {
    mavenCentral()
}

dependencies {
    "forge"("net.minecraftforge:forge:${forgeVersion}")

    compileOnly(project(":common", "dev")) {
        isTransitive = false
    }

    compileOnly(project(":client:api", "dev")) {
        isTransitive = false
    }

    compileOnly(project(":client:common", "dev")) {
        isTransitive = false
    }

    "shadowCommon"(project(":common", "dev")) {
        isTransitive = false
    }

    "shadowCommon"(project(":client:api", "dev")) {
        isTransitive = false
    }

    "shadowCommon"(project(":client:common", "transformProductionForge")) {
        isTransitive = false
    }
}

tasks {
    jar {
        archiveClassifier = "dev"
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand(
                mutableMapOf(
                    "name" to rootProject.name,
                    "modId" to modId,
                    "version" to modVersion,
                    "modLicense" to modLicense,
                    "modAuthors" to modAuthors,
                    "modDescription" to modDescription,
                    "forgeVersion" to forgeVersion,
                    "forgeVersionRange" to forgeVersionRange,
                    "minecraftVersion" to minecraftVersion,
                    "minecraftVersionRange" to minecraftVersionRange,
                    "targetJavaVersion" to targetJavaVersion
                )
            )
        }

        filesMatching("pack.mcmeta") {
            expand(
                mutableMapOf(
                    "name" to rootProject.name,
                    "modId" to modId,
                    "version" to modVersion
                )
            )
        }
    }

    shadowJar {
        archiveClassifier = "dev-shadow"

        configurations = listOf(project.configurations.getByName("shadowCommon"))
    }

    remapJar {
        dependsOn(getByName<ShadowJar>("shadowJar"))
        input.set(shadowJar.get().archiveFile)

        archiveBaseName.set("${rootProject.name}Modification-Forge")
    }

    build {
        doLast {
            shadowJar.get().archiveFile.get().asFile.delete()

            remapJar.get().archiveFile.get().asFile
                .copyTo(rootProject.buildDir.resolve("libs/" + remapJar.get().archiveFile.get().asFile.name), true)
        }
    }
}