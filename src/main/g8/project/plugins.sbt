addSbtPlugin("com.typesafe.play"        % "sbt-plugin"          % "$playVersion$")
addSbtPlugin("com.typesafe.sbt"         % "sbt-native-packager" % "1.4.0")
// formatter
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.3")
// dotenv tool
addSbtPlugin("au.com.onegeek" %% "sbt-dotenv" % "2.0.117")
// code style checker
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
// compiler plugin for scalatest
resolvers += "Artima Maven Repository".at("https://repo.artima.com/releases/")
addSbtPlugin("com.artima.supersafe" %% "sbtplugin" % "1.1.8")
// ecr
// ecr:push task package application as a docker image and push it to ECR
addSbtPlugin("com.mintbeans" % "sbt-ecr" % "0.14.1")
// check library's update
// dependencyUpdates task shows a list of project dependencies
// dependencyUpdatesReport task writes list of project dependencies to a file.
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.0")
//  Document
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.6.8")
addSbtPlugin("com.wanari" % "sbt-paradox-diagrams" % "0.1.3")
addSbtPlugin("com.github.jkugiya" % "sbt-paradox-material-theme" % "0.6.0-fork3")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.4.0")
