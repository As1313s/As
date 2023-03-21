buildscript {
    repositories {
        addDevRepositoryIfEnabled(this, project)
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

plugins {
    id("kotlinx.team.infra") version infra_version
}

infra {
    teamcity {
        libraryStagingRepoDescription = project.name
    }
    publishing {
        include(":kotlinx-collections-immutable")

        libraryRepoUrl = "https://github.com/Kotlin/kotlinx.collections.immutable"
        sonatype {}
    }
}

allprojects {
    logger.info("Using Kotlin $kotlin_version for project $this")
    repositories {
        addDevRepositoryIfEnabled(this, project)
        mavenCentral()
    }

    // TODO: enable after https://youtrack.jetbrains.com/issue/KT-46257 gets fixed
//    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
//        kotlinOptions.allWarningsAsErrors = true
//    }
}