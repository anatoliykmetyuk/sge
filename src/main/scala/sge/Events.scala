package sge

import subscript.language
import subscript.Predef._
import subscript.objectalgebra._

import org.lwjgl.glfw.GLFW

trait Events {

  var keyTriggers: Map[(Int, Int), Trigger] = Map()

  def keyIntEvent(key: Int, evt: Int): Trigger = {
    lazy val entry: ((Int, Int), Trigger) = (key, evt) -> new Trigger {
      override script lifecycle = @{there.onDeactivate {keyTriggers -= entry._1}}: super.lifecycle
    }

    keyTriggers += entry
    entry._2
  }

  def keyCharEvent(char: Char, evt: Int) =
    keyIntEvent(char.toString.toUpperCase.head.toInt, evt)  // Casting char to upper case

  def press  (key: Char): Trigger = keyCharEvent(key, GLFW.GLFW_PRESS  )
  def press  (key: Int ): Trigger = keyIntEvent (key, GLFW.GLFW_PRESS  )
  def release(key: Char): Trigger = keyCharEvent(key, GLFW.GLFW_RELEASE)
  def release(key: Int ): Trigger = keyIntEvent (key, GLFW.GLFW_RELEASE)

}