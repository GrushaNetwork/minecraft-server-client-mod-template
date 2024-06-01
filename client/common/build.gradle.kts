val mavenGroup: String by rootProject
val modVersion: String by rootProject
val minecraftVersion: String by rootProject
val fabricLoaderVersion: String by rootProject

plugins {
    id("java")
}

group = "$mavenGroup.client.common"
version = modVersion

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")

    implementation(project(":common"))
    implementation(project(":client:api"))
}

architectury {
    compileOnly()
    injectInjectables = false

    common("fabric", "forge")
}

configurations {
    create("dev")
}

tasks {
    artifacts {
        add("dev", jar)
    }
}