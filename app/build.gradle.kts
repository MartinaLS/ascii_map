
plugins {
    application // <1>
}

repositories {
    mavenCentral() // <2>
}

dependencies {
    testImplementation("junit:junit:4.13.1") // <3>

    implementation("com.google.guava:guava:30.0-jre") // <4>
}

application {
    mainClass.set("demo.App") // <5>
}
