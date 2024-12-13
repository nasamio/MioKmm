## 简介
这是一个针对Android、Web、桌面和服务器的Kotlin多平台项目。

* `/composeApp` 用于共享Compose多平台应用程序中的代码。
  它包含几个子文件夹:
  - `commonMain` 用于所有目标共享的代码。
  - 其他文件夹用于仅为文件夹名称中指示的平台编译的Kotlin代码。
    例如，如果你想在Kotlin应用的iOS部分使用Apple的CoreCrypto，
    iosMain 将是适合此类调用的文件夹。

* `/server` 用于Ktor服务器应用程序。

* `/shared` 用于项目中所有目标之间共享的代码。 最重要的子文件夹是 commonMain。如果需要，你也可以在这里添加平台特定的文件夹中的代码。

## 构建&运行

执行下面的指令，进行各个模块的构建和运行:

| 功能                            | 指令                                                  |
|-------------------------------|-----------------------------------------------------|
| 服务端运行                         | `./gradlew :server:run`                             |
| 运行web网页端(wasm)                | `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` |
| 运行桌面端(jvm)                    | `./gradlew :composeApp:run`                         |
| 安装移动端(android)                | `./gradlew :composeApp:installAndroidAndRun`        |                       | Build the docker image to use with the fat JAR      |               |

## 结尾
本项目用于学习KMP项目,非商业用途.