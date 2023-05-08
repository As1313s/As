buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0-dev-6976")
    }
}

plugins {
    id("kotlinx.team.infra") version "0.3.0-dev-64"
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
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
    }

    // TODO: enable after https://youtrack.jetbrains.com/issue/KT-46257 gets fixed
//    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
//        kotlinOptions.allWarningsAsErrors = true
//    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xwasm-enable-array-range-checks"
        )
    }
}

with(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin.apply(rootProject)) {
    //canary nodejs that supports wasm GC M6
    nodeVersion = "20.0.0-v8-canary2023041819be670741"
    nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
}


// Node with canary V8 version is not parsed correctly by NPM, producing errors like
//
//          error typescript@4.7.4: The engine "node" is incompatible with this module.
//                                  Expected version ">=4.2.0". Got "20.0.0-v8-canary***"
//
allprojects.forEach {
    it.tasks.withType<org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask>().configureEach {
        args.add("--ignore-engines")
    }
}