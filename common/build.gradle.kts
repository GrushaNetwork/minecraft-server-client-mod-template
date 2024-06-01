val mavenGroup: String by rootProject
val projectVersion: String by rootProject

plugins {
    id("java")
}

group = "$mavenGroup.common"
version = projectVersion

repositories {
    mavenCentral()
}

dependencies {

}

configurations {
    create("dev")
}

tasks {
    artifacts {
        add("dev", jar)
    }
}
