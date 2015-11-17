package sge

import java.nio.ByteBuffer

import org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl.glfw.{Callbacks, GLFWErrorCallback, GLFWKeyCallback, GLFWvidmode}
import org.lwjgl.glfw.GLFW._

import org.lwjgl.opengl.GLContext
import org.lwjgl.opengl.{GL11, GL15, GL20, GL32}


trait LWJGLEngine {this: Game =>
  SharedLibraryLoader.load()

  // LWJGL window handle
  var window: Long = _

  // Callbacks
  val errorCb = Callbacks.errorCallbackPrint(System.err)

  val keyCb = new GLFWKeyCallback {
    override def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
        glfwSetWindowShouldClose(window, GL11.GL_TRUE)
    }
  }

  // Initializes OpenGL
  def initGl() {
    // Setting the error callback, initializing OpenGL
    glfwSetErrorCallback(errorCb)
    if (glfwInit() != GL11.GL_TRUE) throw new IllegalStateException("Unable to initialize GLFW")
    
    // Creating the window
    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE)
    window = glfwCreateWindow(width, height, title, NULL, NULL)

    if (window == NULL) {
      glfwTerminate()
      throw new RuntimeException("Failed to create a window")
    }

    // Setting the keyboard callback
    glfwSetKeyCallback(window, keyCb)

    // Centering the window
    val vidmode: ByteBuffer = glfwGetVideoMode(glfwGetPrimaryMonitor())

    glfwSetWindowPos(
        window,
        (GLFWvidmode.width(vidmode) - width) / 2,
        (GLFWvidmode.height(vidmode) - height) / 2
    )

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)

    // Enable v-sync
    glfwSwapInterval(1)

    // Make the window visible
    glfwShowWindow(window)
    GLContext.createFromCurrent()

    // Set the clear color to blue
    GL11.glClearColor(0.0f, 0.0f, 0.5f, 1.0f)
  }

  // Terminates OpenGL
  def termGl() {
    // Destroying the window, releasing its keyboard callback
    glfwDestroyWindow(window)
    keyCb.release()

    // Destroying GLFW, releasing its error callback
    glfwTerminate()
    errorCb.release()
  }


}