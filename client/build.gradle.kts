import net.fabricmc.loom.api.LoomGradleExtensionAPI

val mavenGroup: String by rootProject
val modVersion: String by rootProject
val minecraftVersion: String by rootProject

plugins {
    id("java")
    id("architectury-plugin") version("3.4-SNAPSHOT")
    id("dev.architectury.loom") version("1.6-SNAPSHOT") apply(false)
    id("com.github.johnrengelman.shadow") version("8.1.1") apply(false)
}

group = "$mavenGroup.client"
version = modVersion

architectury {
    compileOnly()

    injectInjectables = false
    minecraft = minecraftVersion
}

subprojects {
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "com.github.johnrengelman.shadow")

    var mappingsDependency: Dependency? = null

    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()

        mappingsDependency = layered {
            officialMojangMappings()
        }
    }

    dependencies {
        "minecraft"("net.minecraft:minecraftt:${minecraftVersion}")
        mappingsDependency?.let { "mappings"(it) }
    }
}

allprojects {
    apply(plugin = "java")

    group = "$mavenGroup.client"
    version = modVersion

    base {
        archivesName = "${rootProject.name}Modification"
    }
}