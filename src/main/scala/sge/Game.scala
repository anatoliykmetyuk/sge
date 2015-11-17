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

    workflow = live || render

    render = {!for (b <- world.getBodyIterator) b.asInstanceOf[GameObject].render()!}
             glfwSwapBuffers: window
             glfwPollEvents()
             sleep: 15   // 60 FPS
             ...

}
