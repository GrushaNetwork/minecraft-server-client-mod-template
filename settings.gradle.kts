pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Forge"
            url = uri("https://files.minecraftforge.net/maven/")
        }
        maven {
            name = "Fabric-Forge loom"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "JitPack"
            url = uri("https://jitpack.io/")
        }

        gradlePluginPortal()
    }
}

rootProject.name = "ProjectName"

include("common")

include("client")
include("client:api")
include("client:common")
include("client:forge")
include("client:fabric")

findProject(":client:api")?.name = "api"
findProject(":client:common")?.name = "common"
findProject(":client:forge")?.name = "forge"
findProject(":client:fabric")?.name = "fabric"
