ThisBuild / version      := "0.1.1"
ThisBuild / scalaVersion := "3.6.3"

lazy val versions = new {
    val scalaTest  = "3.2.19"
    val scalaCheck = "1.18.1"
    val scalactic  = "3.2.19"
    val sourcecode = "0.4.3"
    val osLib      = "0.11.4"
    val mainargs   = "0.7.6"
    val gson       = "2.11.0"
    val kyo        = "0.16.2"
  }

lazy val commonDependencies = Seq(
  "com.lihaoyi"         %% "os-lib"   % versions.osLib,
  "com.lihaoyi"         %% "mainargs" % versions.mainargs,
  "com.google.code.gson" % "gson"     % versions.gson
)

lazy val kyoDependencies = Seq(
  "io.getkyo" %% "kyo-prelude" % versions.kyo,
  "io.getkyo" %% "kyo-core"    % versions.kyo,
  "io.getkyo" %% "kyo-data"    % versions.kyo,
  "io.getkyo" %% "kyo-direct"  % versions.kyo
)

lazy val testDependencies = Seq(
  "org.scalacheck"    %% "scalacheck"         % versions.scalaCheck,
  "org.scalactic"     %% "scalactic"          % versions.scalactic         % Test,
  "org.scalatestplus" %% "scalacheck-1-18"    % s"${versions.scalaTest}.0" % Test,
  "org.scalatest"     %% "scalatest"          % versions.scalaTest         % Test,
  "org.scalatest"     %% "scalatest-funsuite" % versions.scalaTest         % Test
)

// Common settings for all projects
lazy val commonSettings = Seq(
  libraryDependencies ++= commonDependencies ++ kyoDependencies ++ testDependencies
)

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _                        => MergeStrategy.first
}

lazy val commonScalacOptions = Seq("-feature")

lazy val joinActors =
  (project in file("."))
    .aggregate(benchmarks)
    .dependsOn(core)
    .settings(
      name                       := "joinActors",
      version                    := version.value,
      scalaVersion               := scalaVersion.value,
      assembly / mainClass := Some("core.Main"),
      assembly / assemblyJarName := "joinActors.jar"
    )

lazy val core =
  (project in file("core"))
    .settings(
      name := "core",
      commonSettings,
      scalacOptions ++= commonScalacOptions ++ Seq("Xcheck-macros"),
    )

lazy val benchmarks =
  (project in file("benchmarks"))
    .dependsOn(core % "compile->compile;test->test")
    .settings(
      name := "benchmarks",
      commonSettings,
      scalacOptions ++= commonScalacOptions,
      publish / skip := true
    )
