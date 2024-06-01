import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

val targetJavaVersion: String by rootProject
val mavenGroup: String by rootProject
val projectVersion: String by rootProject

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1") apply(false)
}

group = mavenGroup
version = projectVersion

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks {
        java {
            toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))

            val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()

            if (os.isMacOsX) {
                toolchain.vendor.set(JvmVendorSpec.AZUL)
            }
        }

        compileJava {
            options.encoding = Charsets.UTF_8.name()
        }

        javadoc {
            options.encoding = Charsets.UTF_8.name()
        }

        processResources {
            filteringCharset = Charsets.UTF_8.name()
        }

        test {
            useJUnitPlatform()

            testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://jitpack.io/")
    }


    tasks {
        java {
            withSourcesJar()
        }

        compileJava {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(Integer.valueOf(targetJavaVersion))
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "8.7"
    }
}