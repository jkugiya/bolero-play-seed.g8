package $organization;format="package"$

import sbt.Keys._
import sbt._

import scala.sys.process._

object Bolero extends AutoPlugin {
  object autoImport {
    lazy val buildClient = taskKey[Unit]("Build client project")
    val clientDir = settingKey[String]("Defines directory of bolero client.")
    val consoleEncoding = settingKey[String]("Defines terminal's output encoding.")
  }
  override def trigger = allRequirements
  import autoImport._

  override lazy val buildSettings = Seq(
    clientDir := "Client",
    consoleEncoding := "Shift_JIS",
    buildClient := {
      val baseDir = baseDirectory.value
      val encoding = consoleEncoding.value
      println(baseDir.getCanonicalPath)
      val dir = new File(baseDir, clientDir.value)
      buildDotnet(dir)
    })

  private def buildDotnet(dotNetProjectDir: File) = {
    val command = Seq("dotnet", "build")
    val process = Process(command, dotNetProjectDir)
    val exitCode: Int = process.!
    if (exitCode != 0)  {
      sys.error("Failed to build.")
    }
  }
}