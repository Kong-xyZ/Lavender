package com.omooo.plugin.task

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.build.gradle.internal.dependency.ArtifactCollectionWithExtraArtifact
import com.android.build.gradle.internal.publishing.AndroidArtifacts
import com.android.build.gradle.internal.tasks.CheckManifest
import com.omooo.plugin.util.writeToJson
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import org.gradle.api.artifacts.result.ResolvedArtifactResult
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.component.local.model.OpaqueComponentArtifactIdentifier
import java.io.File
import java.util.regex.Pattern

/**
 * Author: Omooo
 * Date: 2019/9/27
 * Version: v0.1.0
 * Desc: 输出 app 及其依赖的 aar 权限信息
 * Use: ./gradlew listPermissions
 * Output: projectDir/permissions.json
 */
internal open class ListPermissionTask : DefaultTask() {

    @Input
    lateinit var variant: BaseVariant

    @TaskAction
    fun doAction() {
        println(
            """
                *********************************************
                ********* -- ListPermissionTask -- **********
                ***** -- projectDir/permissions.json -- *****
                *********************************************
            """.trimIndent()
        )

        val map = HashMap<String, List<String>>()

        // 获取 app 模块的权限
        val checkManifestTask = variant.checkManifestProvider.get() as CheckManifest
        if (checkManifestTask.isManifestRequiredButNotPresent()) {
            println("App manifest is missing for variant ${variant.name}")
            getAppModulePermission(map)
        } else {
            val manifest = checkManifestTask.fakeOutputDir.asFile.get()
            if (manifest.exists()) {
                map["app"] = matchPermission(manifest.readText())
            } else {
                println("App manifest is missing for variant ${variant.name}, Expected path: ${manifest.absolutePath}")
                getAppModulePermission(map)
            }
        }

        // 获取 app 依赖的 aar 权限
        val variantData = (variant as ApplicationVariantImpl).variantData
        val manifests = variantData.variantDependencies.getArtifactCollection(
            AndroidArtifacts.ConsumedConfigType.RUNTIME_CLASSPATH,
            AndroidArtifacts.ArtifactScope.ALL,
            AndroidArtifacts.ArtifactType.MANIFEST
        )
        val artifacts = manifests.artifacts
        for (artifact in artifacts) {
            if (!map.containsKey(getArtifactName(artifact))
                && matchPermission(artifact.file.readText()).isNotEmpty()
            ) {
                map[getArtifactName(artifact)] = matchPermission(artifact.file.readText())
            }
        }

        map.writeToJson("${project.parent?.projectDir}/permissions.json")
        calcAarSize()
    }

    /**
     * 统计 app 依赖的 aar 大小
     *
     * 输出：projectDir/aarSize.json
     */
    private fun calcAarSize() {
        val list = mutableListOf<String>()
        val variantData = (variant as ApplicationVariantImpl).variantData
        val aarCollection = variantData.variantDependencies.getArtifactCollection(
            AndroidArtifacts.ConsumedConfigType.RUNTIME_CLASSPATH,
            AndroidArtifacts.ArtifactScope.ALL,
            AndroidArtifacts.ArtifactType.AAR
        )
        aarCollection.artifacts.forEach { artifact ->
            val l = File(artifact.file.absolutePath).length() / 1024
            list.add("${getArtifactName(artifact)}: ${l}kb")
        }
        list.writeToJson("${project.parent?.projectDir}/aarSize.json")
    }

    private fun getAppModulePermission(map: HashMap<String, List<String>>) {
        val file = project.projectDir.resolve("src/main/AndroidManifest.xml")
        if (file.exists()) {
            map["app"] = matchPermission(file.readText())
        } else {
            println("App manifest is missing for path ${file.absolutePath}")
        }
    }

    /**
     * 根据 Manifest 文件匹配权限信息
     */
    private fun matchPermission(text: String): List<String> {
        val list = ArrayList<String>()
        val pattern = Pattern.compile("<uses-permission.+./>")
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            list.add(matcher.group())
        }
        return list
    }

    private fun getArtifactName(artifact: ResolvedArtifactResult): String {
        return when (val id = artifact.id.componentIdentifier) {
            is ProjectComponentIdentifier -> id.projectPath
            is ModuleComponentIdentifier -> id.group + ":" + id.module + ":" + id.version
            is OpaqueComponentArtifactIdentifier -> id.getDisplayName()
            is ArtifactCollectionWithExtraArtifact.ExtraComponentIdentifier -> id.getDisplayName()
            else -> throw RuntimeException("Unsupported type of ComponentIdentifier")
        }
    }
}