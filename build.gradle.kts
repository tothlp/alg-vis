plugins {
    `java-library`
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

