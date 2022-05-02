ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "codecLaws"
  )


libraryDependencies ++= Seq(
  "org.typelevel" %% "discipline-scalatest" % "2.1.5"
)
