name := "search"

version := "0.1"

scalaVersion := "2.12.3"

resolvers += "Maven2 Repository" at "https://repo1.maven.org/maven2/"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "net.liftweb" %% "lift-json" % "3.1.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
