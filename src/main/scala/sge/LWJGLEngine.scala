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
      keyTriggers.get((key, action)).foreach(_.trigger)
    }
  }

  // Initializes OpenGL
  def initGl() {
    // Setting the error callback, initializing OpenGL
    glfwSetErrorCallback(errorCb)
    if (glfwInit() != GL11.GL_TRUE) throw new IllegalStateException("Unable to initialize GLFW")
    
    // Window hints
    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE)

    // Use OpenGL 3
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)

    // Use Core profile on OS X instead of default Legacy profile - to be able to use OpenGL 2 and higher
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE)

    // Creating the window
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