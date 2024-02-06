import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import org.scalajs.jsdependencies.sbtplugin.JSDependenciesPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

lazy val root = project.in(file("."))
  .aggregate(backend, frontend)
  .settings(
    name := s"${BuildConfig.appName} root"
  )

lazy val frontend =
  project.in(file("frontend"))
    .settings(commonSettings: _*)
    .enablePlugins(ScalaJSPlugin, JSDependenciesPlugin)
    .settings(
      scalaJSUseMainModuleInitializer := true,
      mainClass in Compile := Some("com.iloonga.kids.client.Client"),
      libraryDependencies ++= Dependencies.client.scalaJsDependencies.value,
      jsDependencies ++= Dependencies.client.jsClientDependencies.value
    )
    .dependsOn(sharedJs)

lazy val backend =
  project.in(file("backend"))
    .settings(commonSettings: _*)
    .enablePlugins(JavaAppPackaging)
    .enablePlugins(DockerPlugin)
    .settings(
      libraryDependencies ++= Seq(scalaOrganization.value        % "scala-compiler"        % BuildConfig.scalaVersion)
        ++ Dependencies.server,
      mainClass := Some("com.iloonga.kids.Iloonga"),
      resourceGenerators in Compile += Def.task {
        val f1 = (fastOptJS in Compile in frontend).value.data
        Seq(f1, new File(f1.getPath + ".map"))
      }.taskValue,
      watchSources ++= (watchSources in frontend).value,
      dockerExposedPorts ++= Seq(1026)
    )
    .dependsOn(sharedJvm)

lazy val shared = crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure).in(file("shared"))
  .settings(
    scalaVersion := BuildConfig.scalaVersion,
    LogConfig.logDirKey := LogConfig.logDir,
    // The build info plugin writes these values into a BuildInfo object in the build info package
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, LogConfig.logDirKey),
    // The logback.groovy config file uses this build info to configure logging, for example
    buildInfoPackage := "appbuildinfo",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "upickle" % Dependencies.versions.uPickleVersion,
      "io.circe" %% "circe-core" % Dependencies.versions.circeVersion,
      "io.circe" %% "circe-generic" % Dependencies.versions.circeVersion,
      "io.circe" %% "circe-parser" % Dependencies.versions.circeVersion
    )
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

def commonSettings = Seq(
  scalaVersion := BuildConfig.scalaVersion,
  version := BuildConfig.appVersion,
  scalacOptions ++= Seq("-deprecation", "-feature", "-encoding", "utf8", "-unchecked", "-Xlint"),
  resolvers += "staging" at "https://dl.bintray.com/akka/maven"
)