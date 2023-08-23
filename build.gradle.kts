plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "algvis.ui.AlgVisStandalone"
    }
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/algvis"))
        }
        resources {
            setSrcDirs(listOf("src"))
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}