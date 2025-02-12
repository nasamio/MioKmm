import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                entry?.let {
                    println("entry: ${it.absolutePath}")
                }
                // 实现本地调试的时候 如果已经有该网页打开 就不开新的；如果没有就开一个
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    port = 3579
                    static = (static ?: mutableListOf()).apply {
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                    open = false
                }
                progressReporter = true
                showProgress = true
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
            // 路由
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
            // Enables FileKit without Compose dependencies
            implementation("io.github.vinceglb:filekit-core:0.8.8")
//            // Enables FileKit with Composable utilities
//            implementation("io.github.vinceglb:filekit-compose:0.8.8")
            implementation(libs.coil.compose.core)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.compose)
            // ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.serialization.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.io)
            // ai
            implementation ("com.aallam.openai:openai-client:4.0.0-beta01")
            implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            // gson
            implementation("com.google.code.gson:gson:2.10.1")
            implementation(libs.ktor.client.desktop)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
//            implementation(libs.androidx.navigation.compose)
            implementation(libs.navigation.compose)
        }
    }
}

android {
    namespace = "com.mio"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mio"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    implementation(libs.androidx.navigation.compose)
    implementation(libs.core)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.mio.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.mio"
            packageVersion = "1.0.0"
        }
    }
}

tasks.register("installAndroidAndRun") {
    // 使这个任务依赖于 installDebug
    dependsOn("installDebug")



    doLast {
        println("app已成功安装,正在启动...")

        // 启动应用的命令
        exec {
            commandLine("adb", "shell", "am", "start", "-n", "com.mio/.MainActivity")
            standardOutput = System.out
            errorOutput = System.err
        }
    }
}


