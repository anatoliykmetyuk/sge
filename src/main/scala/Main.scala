import subscript.language
import subscript.Predef._

import subscript.swing._
import subscript.vm.executor._

import scala.swing.Swing

import org.lwjgl.BufferUtils

import org.lwjgl.opengl.{GL11, GL15, GL20, GL30}
import org.lwjgl.glfw.GLFW._

import sge._

object Main extends Game with GameApp
                         with BoxComponent {

  class SimpleBox(x: Double, y: Double, width: Double, height: Double) extends Box(x, y, width, height) {
    override script live = {..}
  }


  script live = new SimpleBox(0, 0, 1, 1) || new SimpleBox(2, 2, 1, 1)

}
