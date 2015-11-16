package sge

import subscript.language

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

abstract class Game {
  SharedLibraryLoader.load()
  

  val width : Int = 640
  val height: Int = 480
  val title : String = "SubScript Game Engine Application"


  var window: Long = _

  val errorCb = Callbacks.errorCallbackPrint(System.err)

  val keyCb = new GLFWKeyCallback {
    override def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
        glfwSetWindowShouldClose(window, GL_TRUE)
    }
  }

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

  def term() {
    glfwDestroyWindow(window)
    keyCb.release()
    glfwTerminate()
    errorCb.release()
  }

  def start() {
    init()
    subscript.DSL._execute(lifecycle)
    term()
  }

  script..
    live: Any

    sleep(t: Long) = {* Thread sleep t *}

    lifecycle = workflow || while(glfwWindowShouldClose(window) != GL_TRUE) sleep: 15

    workflow = live || render

    render = glfwSwapBuffers: window
             glfwPollEvents()
             sleep: 15
             ...

}
