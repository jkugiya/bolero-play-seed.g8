import com.amazonaws.regions.{ Region, Regions }

name := "$name$"
organization := "$organization$"
version := "1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.1"
ThisBuild / resolvers ++= Seq(
  "Artima Maven Repository".at("https://repo.artima.com/releases/"),
  Resolver.sonatypeRepo("releases"))

lazy val ecrSettings = Seq(
  region in Ecr := Region.getRegion(Regions.AP_NORTHEAST_1),
  repositoryName := "$organization$/" + (packageName in Docker).value,
  repositoryTags in Ecr := Seq(
    sys.env
      .get("STAGE")
      .find(_ == "prod")
      .map(_ => version.value.replace("-SNAPSHOT", ""))
      .getOrElse(version.value)
  ),
  localDockerImage in Ecr := "$organization$/" + (packageName in Docker).value + ":" + (version in Docker).value,
  push in Ecr := ((push in Ecr) dependsOn (publishLocal in Docker, login in Ecr)).value
)

lazy val dockerSettings = Seq(
  dockerBaseImage := "amazoncorretto:8u222",
  dockerExposedPorts := Seq(9000),
  dockerUsername := Some("$organization$"),
  daemonUserUid in Docker := None,
  daemonUser in Docker := "daemon",
  bashScriptExtraDefines ++= Seq(
    "addJava -Xms\${JVM_HEAP_MIN:-2048m}",
    "addJava -Xmx\${JVM_HEAP_MAX:-2048m}",
    "addJava -XX:MaxMetaspaceSize=\${JVM_META_MAX:-512M}"
  )
)

lazy val paradoxSettings = Seq(
  Compile /paradoxMaterialTheme := {
    ParadoxMaterialTheme()
      .withLanguage(java.util.Locale.JAPANESE)
      .withSearchLanguage("jp")
  },
  (Compile / paradoxOverlayDirectories) ++= Seq(baseDirectory.value / "doc" / "paradox"),
  makeSite / includeFilter := "*.html" | "*.css" | "*.png" | "*.png" | "*.js" | "*.woff" | "*.woff2" | "*.ttf",
  makeSite / mappings ++= (Compile / paradoxMaterialTheme / mappings).value,
  makeSite := makeSite.dependsOn(Compile / paradox).value,
  siteSourceDirectory := (Compile / paradox / target).value
)

val CatsVersion = "2.0.0"
val CirceVersion = "0.12.3"
val MacwireVersion = "2.3.3"
lazy val root = {
    val proj =
        (project in file("."))
            .enablePlugins(PlayScala, EcrPlugin, ParadoxPlugin, ParadoxMaterialThemePlugin)
            .settings(dockerSettings)
            .settings(ecrSettings)
            .settings(paradoxSettings)
            .settings(
                scalaVersion := "$scalaVersion$",
                addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
                scalacOptions ++= Seq(
                  "-Ymacro-annotations",
                  "-P:bm4:no-filtering:y",
                  "-P:bm4:no-map-id:y"
                ),
                libraryDependencies ++= Seq(
                    // Functional Programming
                    "org.typelevel" %% "cats-core" % CatsVersion,
                    "org.typelevel" %% "alleycats-core" % CatsVersion,
                    "org.typelevel" %% "cats-testkit" % CatsVersion % Test,
                    // Json Library
                    "io.circe"     %% "circe-core"    % CirceVersion,
                    "io.circe"     %% "circe-generic" % CirceVersion,
                    "io.circe"     %% "circe-parser"  % CirceVersion,
                    "de.heikoseeberger" %% "akka-http-circe" % "1.29.1",
                    "com.dripower" %% "play-circe"    % "2712.0",
                    // DI
                    "com.softwaremill.macwire" %% "macros" % MacwireVersion % "provided",
                    "com.softwaremill.macwire" %% "macrosakka" % MacwireVersion % "provided",
                    "com.softwaremill.macwire" %% "util" % MacwireVersion,
                    "com.softwaremill.macwire" %% "proxy" % MacwireVersion,
                    // logging
                    "ch.qos.logback" % "logback-classic" % "1.2.3",
                    // Test
                    "org.scalactic"          %% "scalactic"          % "3.1.0" % Test,
                    "org.scalatest"          %% "scalatest"          % "3.1.0" % Test,
                    "org.scalacheck" %% "scalacheck" % "1.14.1" % Test,
                    "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
                    "org.scalamock"          %% "scalamock"          % "4.4.0" % Test)
            )
    proj
}

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "$organization$.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "$organization$.binders._"
