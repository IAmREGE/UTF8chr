
plugins {
    application // <1>
}

repositories {
    mavenCentral()
}

dependencies {}

java {
    sourceCompatibility = JavaVersion.VERSION_1_5
    targetCompatibility = JavaVersion.VERSION_1_5
}

application {
    mainClass.set("rege.rege.utf8chr.test.Test1") // <5>
}