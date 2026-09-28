plugins {
    id("net.fabricmc.fabric-loom") version("1.15-SNAPSHOT")
}

val minecraft_version = "26.4-snapshot-1"

group = "org.embeddedt"
version = "5.26.0+mc26.4-snapshot-1"

base.archivesName = "modernfix-fabric"

loom {
    accessWidenerPath = file("src/main/resources/modernfix.accesswidener")
}

java {
    val curSourceCompatLevel = JavaVersion.VERSION_25
    sourceCompatibility = curSourceCompatLevel
    targetCompatibility = curSourceCompatLevel
}

repositories {
    maven("https://maven.fabricmc.net/")
    mavenCentral()
}

val embed by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = true
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    implementation("net.fabricmc:fabric-loader:0.18.5")
    implementation("net.fabricmc.fabric-api:fabric-api:0.161.1+26.4")

    implementation(project(":annotations"))
    embed(project(":annotations"))
    annotationProcessor(project(path = ":annotation-processor", configuration = "shadow"))
}

tasks.named<Jar>("jar") {
    from(embed.map { if (it.isDirectory) it else zipTree(it) })
    manifest.attributes(mapOf(
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to version
    ))
}

tasks.withType<JavaCompile>().configureEach {
    if (!name.lowercase().contains("test")) {
        options.compilerArgs.addAll(
            listOf(
                "-ArootProject.name=${rootProject.name}",
                "-Aproject.name=${project.name}"
            )
        )
    }
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "1000"))
}

sourceSets {
    main {
        resources.srcDir(
            layout.buildDirectory.dir("generated/sources/annotationProcessor/java/main/resources")
        )
    }
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(tasks.named("compileJava"))
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}
