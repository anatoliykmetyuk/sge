import subscript.language
import subscript.Predef._

import subscript.swing._
import subscript.vm.executor._

import scala.swing.Swing

import org.lwjgl.BufferUtils

import org.lwjgl.opengl.{GL11, GL15, GL20, GL30}
import org.lwjgl.glfw.GLFW._

import org.dyn4j.dynamics._
import org.dyn4j.geometry._

import sge._

object Main extends Game with GameApp
                         with BoxComponent {
  
  def randomColor = {
    def range: Float = (math.random * 0.5 + 0.5).toFloat
    Array(
      range
    , range
    , range
    , 1f
    )
  }

  class SimpleBox(x: Double, y: Double, width: Double, height: Double, static: Boolean = false) extends Box(x, y, width, height, randomColor) {
    if (static) setMass(MassType.INFINITE)
    override script live = {..}
  }


  script..
    live =
      var table = new SimpleBox(0, -7, 18 , 0.5, true)
      var wall  = new SimpleBox(7,  0, 0.5, 18 , true)

      var b1    = new SimpleBox(0 , 0, 1, 1  )
      var b2    = new SimpleBox(-5, 2, 2, 0.5)

      let b1.applyImpulse(new Vector2(10, 0))
      let b2.applyImpulse(new Vector2(30, 0))

      table || wall || b1 || b2 || new SimpleBox(5, 0, 1, 5)

}
