package sge

import subscript.language
import subscript.Predef._
import subscript.objectalgebra._

import org.lwjgl.glfw.GLFW

trait Events {

  var keyTriggers: Map[(Int, Int), Trigger] = Map()

  def press(char: Char): Trigger = {
    lazy val entry: ((Int, Int), Trigger) = (char.toInt, GLFW.GLFW_PRESS) -> new Trigger {
      override script lifecycle = @{there.onDeactivate {keyTriggers -= entry._1}}: super.lifecycle
    }

    keyTriggers += entry
    entry._2
  }

  // def release(char: Char): Trigger

}