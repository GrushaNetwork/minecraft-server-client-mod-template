import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val mavenGroup: String by rootProject
val targetJavaVersion: String by rootProject

val modId: String by rootProject
val modVersion: String by rootProject
val modAuthors: String by rootProject
val modLicense: String by rootProject
val modDescription: String by rootProject

val minecraftVersion: String by rootProject
val fabricLoaderVersion: String by rootProject
val fabricApiVersion: String by rootProject

plugins {
    id("java")
}

group = "$mavenGroup.client.fabric"
version = modVersion

configurations {
    create("shadowCommon")
}

architectury {
    compileOnly()
    injectInjectables = false

    platformSetupLoomIde()
    fabric()
}

repositories {
    mavenCentral()
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion}")

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

    "shadowCommon"(project(":client:common", "transformProductionFabric")) {
        isTransitive = false
    }
}

tasks {
    jar {
        archiveClassifier = "dev"
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "name" to rootProject.name,
                    "modId" to modId,
                    "version" to modVersion,
                    "modLicense" to modLicense,
                    "modAuthors" to modAuthors,
                    "modDescription" to modDescription,
                    "fabricLoaderVersion" to fabricLoaderVersion,
                    "fabricApiVersion" to fabricApiVersion,
                    "minecraftVersion" to minecraftVersion,
                    "targetJavaVersion" to targetJavaVersion
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

        archiveBaseName.set("${rootProject.name}Modification-Fabric")
    }

    build {
        doLast {
            shadowJar.get().archiveFile.get().asFile.delete()

            remapJar.get().archiveFile.get().asFile
                .copyTo(rootProject.buildDir.resolve("libs/" + remapJar.get().archiveFile.get().asFile.name), true)
        }
    }
}