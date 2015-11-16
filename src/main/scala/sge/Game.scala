package sge

import subscript.language
import subscript.Predef._

import scala.collection.JavaConversions._

import java.nio.ByteBuffer
import java.nio.IntBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GLContext
import org.lwjgl.glfw.GLFWvidmode

import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil.NULL

import org.dyn4j.dynamics._

abstract class Game extends GameContext {
  SharedLibraryLoader.load()
  
  // Initial properties
  val width : Int = 640
  val height: Int = 480
  val title : String = "SubScript Game Engine Application"


  // Physics
  val world = new World


  // LWJGL window handle
  var window: Long = _

  // Callbacks
  val errorCb = Callbacks.errorCallbackPrint(System.err)

  val keyCb = new GLFWKeyCallback {
    override def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
        glfwSetWindowShouldClose(window, GL_TRUE)
    }
  }

  // Initializes OpenGL
  def init() {
    glfwSetErrorCallback(errorCb)
    if (glfwInit() != GL_TRUE) throw new IllegalStateException("Unable to initialize GLFW")
    
    window = glfwCreateWindow(width, height, title, NULL, NULL)
    if (window == NULL) {
      glfwTerminate()
      throw new RuntimeException("Failed to create a window")
    }
    glfwSetKeyCallback(window, keyCb)
    
    glfwMakeContextCurrent(window)
    GLContext.createFromCurrent()
  }

  // Terminates OpenGL
  def term() {
    glfwDestroyWindow(window)
    keyCb.release()
    glfwTerminate()
    errorCb.release()
  }

  /** Start the game */
  def start() {
    init()
    subscript.DSL._execute(lifecycle)
    term()
  }

  script..
    live: Any

    lifecycle = workflow || while(glfwWindowShouldClose(window) != GL_TRUE) sleep: 15

    workflow = live || render

    render = {!for (b <- world.getBodyIterator) b.asInstanceOf[GameObject].render()!}
             glfwSwapBuffers: window
             glfwPollEvents()
             sleep: 15   // 60 FPS
             ...

}
