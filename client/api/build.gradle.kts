val mavenGroup: String by rootProject
val modVersion: String by rootProject
val minecraftVersion: String by rootProject

plugins {
    id("java")
}

group = "$mavenGroup.client.api"
version = modVersion

configurations {
    create("dev")
}

tasks {
    artifacts {
        add("dev", jar)
    }

    remapJar {
        archiveClassifier = "dev"
        archiveBaseName.set("${rootProject.name}Modification-API")
    }
}