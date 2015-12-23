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


  def renderSquare(k: Float) {
    // Prepare data
    val data = Array[Float](
      -0.5f, -0.5f, 
      -0.5f,  0.5f,
       0.5f, -0.5f,
       0.5f,  0.5f,
      -0.5f,  0.5f,
       0.5f, -0.5f
    ).map(_ * k)
    val program = shaderPrograms(SGE_SHADER_DEFAULT).handle

    GlUtils.renderRectangle(data, program)
  }

  // class Box(val step: Float, val delay: Long) extends GameObject {
  //   var size: Float = 0
    
  //   override def render() {
  //     renderSquare(size)
  //   }

  //   override script live =
  //     while(size <= 1)
  //     let size += step
  //     sleep: delay
  // }

  class SimpleBox(x: Double, y: Double, width: Double, height: Double) extends Box(x, y, width, height) {
    override script live = {..}
  }


  script live = new SimpleBox(0, 0, 1, 1)

}
