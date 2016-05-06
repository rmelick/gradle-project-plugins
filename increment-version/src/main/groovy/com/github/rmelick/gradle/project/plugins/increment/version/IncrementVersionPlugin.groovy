package com.github.rmelick.gradle.project.plugins.increment.version

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

/**
 *
 */
class IncrementVersionPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    project.extensions.create("versioning", IncrementVersionPluginExtension)
    project.task("incrementVersion") {
      versionFile = { project.versioning.versionFile }
    }
  }
}

class IncrementVersionPluginExtension {
  def versionFile
}

class IncrementVersionTask extends DefaultTask {
  private static final VERSION_PROP_NAME = "VERSION"
  def versionFile

  private File getVersionFile() {
    project.file(versionFile)
  }

  @TaskAction
  def increment() {
    def versionFile = getVersionFile()
    def versionProps = loadProperties(versionFile)
    def existingVersion = versionProps.getProperty(VERSION_PROP_NAME)
    versionProps.setProperty(VERSION_PROP_NAME, incrementVersion(existingVersion))
    writeProperties(versionProps, versionFile)
  }

  private static Properties loadProperties(File file) {
    Properties properties = new Properties()
    file.withInputStream {
      properties.load(it)
    }
    return properties
  }

  private static def writeProperties(Properties properties, File file) {
    file.withOutputStream {
      properties.store(it, "")
    }
  }

  private static String incrementVersion(String oldVersion) {
    println("Incrementing ${oldVersion}")
    return "NewVersion"
  }
}