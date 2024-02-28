plugins {
    kotlin("multiplatform") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("maven-publish")
    kotlin("plugin.allopen") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
}

group = "com.tsschmi"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        // moduleName = "measures"
        browser {
            useEsModules()
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        compilations["main"].packageJson {
            customField("types", "kotlin/measures.d.ts")
        }
        generateTypeScriptDefinitions()
        binaries.executable()

    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                implementation("org.jetbrains.kotlin:kotlin-allopen")
                implementation("org.glassfish.jaxb:jaxb-runtime:2.3.6")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                implementation("org.glassfish.jaxb:jaxb-runtime:2.3.6")
                implementation("com.vladmihalcea:hibernate-types-52:2.16.1")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            }
        }
        val jsTest by getting
    }

    allOpen {
        annotation("kotlinx.serialization.Serializable")
    }
}