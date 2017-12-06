name := """dpa-reservation"""

maintainer := "Swapnil Borse <swapnil.borse@decimalpointanalytics.com>"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean, JDebPackaging, JavaServerAppPackaging)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
"be.objectify"  %% "deadbolt-java"     % "2.3.2",
  javaJdbc,
  cache,
  javaWs
)

resolvers += (
  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
)
 
resolvers += Resolver.file("Local", file( System.getenv("IVY_HOME") + "/whatever/it/is"))(Resolver.ivyStylePatterns)


libraryDependencies += "org.avaje" % "ebean" % "2.7.3"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.37"
libraryDependencies += "org.webjars" % "bootstrap" % "3.2.0"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"