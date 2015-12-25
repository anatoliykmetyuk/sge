package sge

import subscript.language
import subscript.Predef._
import subscript.objectalgebra._

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback

import org.dyn4j.dynamics._


trait Events extends KeyboardEvents
                with CollisionEvents

class TriggerMap[K] {
  private var _triggers: Map[K, Trigger] = Map()

  def triggers = _triggers

  def event(key: K): Trigger = {
    lazy val entry: (K, Trigger) = key -> new Trigger {
      override script lifecycle = @{there.onDeactivate {_triggers -= entry._1}}: super.lifecycle
    }

    _triggers += entry
    entry._2
  }

  def happened(key: K): Unit = triggers.get(key).foreach(_.trigger)
}

class BinaryTriggerMap[K1, K2] extends TriggerMap[(K1, K2)] {
  def event   (k1: K1, k2: K2): Trigger = event   ((k1, k2))
  def happened(k1: K1, k2: K2): Unit    = happened((k1, k2))
}


trait KeyboardEvents {
  val keys = new BinaryTriggerMap[Int, Int]

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

trait CollisionEvents {
  val collisions = new BinaryTriggerMap[Body, Body]

  val collisionListener = new CollisionAdapter {
    override def collision(body1: Body, fixture1: BodyFixture, body2: Body, fixture2: BodyFixture): Boolean = {
      collisions.happened(body1, body2)
      true
    }
  }
}