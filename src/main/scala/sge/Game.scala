package sge

import subscript.language
import subscript.Predef._

import scala.collection.JavaConversions._

import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11

import org.dyn4j.dynamics._

abstract class Game extends LWJGLEngine
                       with GameContext
                       with ShaderEngine {
  
  // Initial properties
  val width : Int = 640
  val height: Int = 480
  val title : String = "SubScript Game Engine Application"


  // Physics
  val world = new World

  def p(tag: Any) = println(s"$tag:\t${GL11.glGetError}")

  /** Start the game */
  def start() {
    initGl()
    compileShaderPrograms()
    body()
    termGl()
  }

  /** Main part of the game */
  def body(): Unit = subscript.DSL._execute(lifecycle)

  script..
    live: Any

    lifecycle = workflow || while(glfwWindowShouldClose(window) != GL11.GL_TRUE) sleep: 15

    workflow = live || render || physics

    render = GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
             {!for (b <- world.getBodyIterator) b.asInstanceOf[GameObject].render()!}
             glfwSwapBuffers: window
             glfwPollEvents()
             sleep: 15   // 60 FPS
             ...

    physics = 
      var t: Long = 0
      [
        {!world.update((System.currentTimeMillis - t) / 1000D)!}
        let t = System.currentTimeMillis
        sleep: 15
        ...
      ]

}
