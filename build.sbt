name := "ProjektArchWiki"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "1.0.0"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)


fork in run := true 