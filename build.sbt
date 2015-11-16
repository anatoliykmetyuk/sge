scalaVersion := "2.11.7"
libraryDependencies += "org.subscript-lang" %% "subscript-swing" % "2.0.0"
SubscriptSbt.projectSettings

lazy val lwjglVersion = "3.0.0a"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "org.lwjgl" % "lwjgl" % lwjglVersion
, "org.lwjgl" % "lwjgl-platform" % lwjglVersion classifier "natives-osx"
)

fork := true

javaOptions in run += "-XstartOnFirstThread"

excludeFilter := "Main.scala"