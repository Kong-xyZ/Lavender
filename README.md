---
概览
---

#### 一、Lavener 是什么

Lavener 是一个 Gradle Plugin，提供一系列静态检测的能力，其目标主要是为了解决随着 APP 复杂度的提升而带来的性能、稳定性、包体积等一系列质量问题。

#### 二、为什么以此命名？

Lavender 直译为薰衣草。薰衣草的香味能够帮助我们缓解压力、减少沮丧。

希望这个 Gradle Plugin 也能够帮助我们减少重复的劳动工作，使我们的工作更加轻松顺畅。

#### 三、功能列表

目前包含包体积瘦身和安全合规检查相关功能。

##### 包体积瘦身

| 功能                                  | 使用文档                                                     |
| ------------------------------------- | ------------------------------------------------------------ |
| 重复资源监测                          | [重复资源检测](/wiki/包体积优化/重复资源检测.md)             |
| 输出 AAR 大小                         | [输出 App 依赖的 AAR 大小](/wiki/包体积优化/输出%20App%20依赖的%20AAR%20大小.md) |
| 打包时自动 png 转 webp、webp 图片压缩 | [图片压缩使用文档](/wiki/包体积优化/图片压缩使用文档.md)     |
| 无用资源监测                          | [无用资源监测](/wiki/包体积优化/无用资源监测.md)             |
| 无用 Assets 资源监测                  | [无用 Assets 资源检测](/wiki/包体积优化/无用%20Assets%20资源检测.md) |
| 输出 App 依赖的 AAR 下的 assets 资源  | [输出 App 依赖的所有 assets 资源](/wiki/包体积优化/输出%20App%20依赖%20AAR%20下的%20assets%20资源.md) |
| 删除无用 Assets 资源                  | [删除无用 Assets 资源](/wiki/包体积优化/删除无用%20Assets%20资源.md) |
| APK 增量分析                          | [APK 增量分析](/wiki/包体积优化/APK%20增量分析.md)           |
| 输出图片列表                          | [输出图片列表](/wiki/包体积优化/输出图片列表.md)             |

##### 静态分析

| 功能                                                 | 使用                                                |
| ---------------------------------------------------- | --------------------------------------------------- |
| 输出 App 及其依赖的 AAR 权限                         | [依赖权限检测]()                                    |
| 检测 Manifest 注册组件是否声明 android:exported 属性 | [exported 属性检测]()                               |
| 类、方法、常量、字段调用检测                         | [方法调用检测]()                                    |
| 输出无用类文件                                       | [输出无用类文件]()                                  |
| 输出类及所属负责人的映射                             | [输出类及所属的负责人的映射]()                      |
| 输出 Manifest 定义的 scheme                          | [输出 Manifest 定义的 scheme ]()                    |
| 检测透明 Activity 设置了 screenOrientation 属性      | [检测透明 Activity 设置了 screenOrientation 属性]() |
| 输出依赖的包名列表                                   | [输出依赖的包名列表]()                              |