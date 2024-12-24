plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.mio"
version = "1.0.0"
application {
    mainClass.set("com.mio.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
//    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
//    cors
    implementation(libs.ktor.server.cors)
//    gson
    implementation(libs.gson)

    // 请求
    implementation(libs.ktor.client.desktop)
}