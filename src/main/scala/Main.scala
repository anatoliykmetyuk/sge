import subscript.language
import subscript.Predef._
import subscript.objectalgebra._

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

  class SimpleBox(x: Double, y: Double, width: Double, height: Double) extends Box(x, y, width, height, randomColor) {
    override script live = {..}
  }

  class Wall(x: Double, y: Double, width: Double, height: Double) extends Box(x, y, width, height, randomColor) {
    setMass(MassType.INFINITE)
    override script live = {..}
  }

  class Bullet(x: Double, y: Double, direction: Vector2) extends Box(x, y, 2, 0.5, randomColor) {
    tag = "bullet"
    override script live = {:applyImpulse(direction):} {..}
  }

  class Plane(x: Double, y: Double, direction: Vector2) extends Box(x, y, 3, 0.5, randomColor) {

    val forward  = direction
    val backward = forward.getNegative
    val upward   = forward.copy.rotate(math.Pi / 2).multiply(5)
    
    val gameOver = new Trigger

    override script live = [|| [controls   ...]
                               [collisions ...]] / gameOverProcedure

    script..
      controls = [  moveOnKey: 'a', backward
                  + moveOnKey: 'd', forward]
                 || moveOnKey: 'w', upward

      moveOnKey(key: Char, dir: Vector2) =
        press: key [{!applyForce(dir)!} sleep: 15 ...] / release: key

      collisions = collision: "bullet" gameOver.trigger

      gameOverProcedure = gameOver

                          let setMass(MassType.INFINITE)
                          setLinearVelocity : 0, 0
                          setAngularVelocity: 0

                          sleep: 1000
  }


  script..
    live = || // Walls
              new Wall(0, -7, 18 , 0.5)
              new Wall(7, 0, 0.5, 18  )

              // Target
              new SimpleBox(3, 0, 1, 5)

              // Bullets
              new Bullet(-5, 0, new Vector2(20, 0))
              new Bullet(-8, 2, new Vector2(30, 0))

              // Plane
              new Plane(-9, 5, new Vector2(25, 0))

}
