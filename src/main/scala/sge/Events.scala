package sge

import subscript.language
import subscript.Predef._
import subscript.objectalgebra._

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback

import org.dyn4j.dynamics._


trait Events extends KeyboardEvents
                with CollisionEvents {this: Game =>}

class Triggers[A] {
  private var triggers: Map[A => Boolean, Trigger] = Map()

  def event(filter: A => Boolean): Trigger = {
    lazy val entry: (A => Boolean, Trigger) = filter -> new Trigger {
      override script lifecycle = @{there.onDeactivate {triggers -= entry._1}}: super.lifecycle
    }

    triggers += entry
    entry._2
  }

  def event(key: A): Trigger = event(_ == key)

  def happened(key: A): Unit = triggers.find {case (f, t) => f(key)}.foreach(_._2.trigger)
}

class BinaryTriggers[K1, K2] extends Triggers[(K1, K2)] {
  def event   (filter: (K1, K2) => Boolean): Trigger = event   {k => filter(k._1, k._2)}
  def event   (k1: K1, k2: K2             ): Trigger = event   ((k1, k2))
  def happened(k1: K1, k2: K2             ): Unit    = happened((k1, k2))
}


trait KeyboardEvents {this: Game =>
  val keys = new BinaryTriggers[Int, Int]

  def keyCharEvent(key: Char, evt: Int) = keys.event(key.toString.toUpperCase.head.toInt, evt)  // Casting char to upper case

  def press  (key: Char): Trigger = keyCharEvent(key, GLFW.GLFW_PRESS  )
  def press  (key: Int ): Trigger = keys.event  (key, GLFW.GLFW_PRESS  )
  def release(key: Char): Trigger = keyCharEvent(key, GLFW.GLFW_RELEASE)
  def release(key: Int ): Trigger = keys.event  (key, GLFW.GLFW_RELEASE)

  val keyCallback = new GLFWKeyCallback {
    override def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
      keys.happened(key, action)
    }
  }
}

trait CollisionEvents {this: Game =>
  val collisions = new BinaryTriggers[GameObject, GameObject]

  val collisionListener = new CollisionAdapter {
    override def collision(body1: Body, fixture1: BodyFixture, body2: Body, fixture2: BodyFixture): Boolean = {
      collisions.happened(body1.asInstanceOf[GameObject], body2.asInstanceOf[GameObject])
      true
    }
  }
}