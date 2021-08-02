import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    idea
    `maven-publish`
    id("fabric-loom") version "0.9.9"
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

base {
    archivesBaseName = "i-am-very-smart"
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://maven.fabricmc.net")
    maven(url = "https://maven.terraformersmc.com/")
}

version = "2.1.0z+mc1.17.1"
group = "me.sargunvohra.mcmods"

minecraft {
}

dependencies {
    minecraft("com.mojang:minecraft:1.17.1")
    mappings("net.fabricmc:yarn:1.17.1+build.32:v2")
    modImplementation("net.fabricmc:fabric-loader:0.11.6")

    modImplementation("net.fabricmc.fabric-api:fabric-api:0.37.1+1.17")

    listOf(
        "me.shedaniel.cloth:config-2:4.5.6",
        "me.sargunvohra.mcmods:autoconfig1u:3.2.0-unstable"
    ).forEach {
        modImplementation(it)
        include(it)
    }

    modCompileOnly("com.terraformersmc:modmenu:2.0.4")
}

val processResources = tasks.getByName<ProcessResources>("processResources") {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        filter { line -> line.replace("%VERSION%", "${project.version}") }
    }
}

val javaCompile = tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val jar = tasks.getByName<Jar>("jar") {
    from("LICENSE")
}

val remapJar = tasks.getByName<RemapJarTask>("remapJar")

curseforge {
    if (project.hasProperty("curseforge_api_key")) {
        apiKey = project.property("curseforge_api_key")!!
    }

    project(closureOf<CurseProject> {
        id = "318163"
        releaseType = "release"
        addGameVersion("1.17.1")
        addGameVersion("Fabric")
        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            embeddedLibrary("cloth-config")
            embeddedLibrary("auto-config-updated-api")
        })
        mainArtifact(file("${project.buildDir}/libs/${base.archivesBaseName}-$version.jar"))
        afterEvaluate {
            mainArtifact(remapJar)
            uploadTask.dependsOn(remapJar)
        }
    })

    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
