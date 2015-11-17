package sge

import org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl.glfw.{Callbacks, GLFWErrorCallback, GLFWKeyCallback}
import org.lwjgl.glfw.GLFW._

import org.lwjgl.opengl.GLContext
import org.lwjgl.opengl.GL11._


trait LWJGLEngine {this: Game =>
  SharedLibraryLoader.load()

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
  def initGl() {
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
  def termGl() {
    glfwDestroyWindow(window)
    keyCb.release()
    glfwTerminate()
    errorCb.release()
  }


}