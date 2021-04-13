apply<GroovyPlugin>()

plugins {
    application // <1>
}

repositories {
    mavenCentral() // <2>
}

dependencies {
    testImplementation("org.spockframework:spock-core:2.0-M5-groovy-3.0")

    implementation("com.google.guava:guava:30.0-jre") // <4>
}

application {
    mainClass.set("application.App") // <5>
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
