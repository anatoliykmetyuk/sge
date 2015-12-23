scalaVersion := "2.11.7"
libraryDependencies += "org.subscript-lang" %% "subscript-swing" % "2.0.0"
SubscriptSbt.projectSettings

lazy val lwjglVersion = "3.0.0a"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  // Graphics
  "org.lwjgl" % "lwjgl" % lwjglVersion
, "org.lwjgl" % "lwjgl-platform" % lwjglVersion classifier "natives-osx"  // Reminder: add the classifiers for other systems too!

  // Physics
, "org.dyn4j" % "dyn4j" % "3.2.1"
)

fork := true

javaOptions in run += "-XstartOnFirstThread"

initialCommands := """
import org.dyn4j.dynamics._
import org.dyn4j.geometry._

val world     = new World
val floorRect = new Rectangle(15.0, 1.0)
val floor     = new Body()

floor.addFixture(new BodyFixture(floorRect))
floor.setMass(MassType.NORMAL)

floor.translate(0.0, -4.0)
world.addBody(floor)
"""