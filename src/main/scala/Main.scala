import subscript.language
import subscript.Predef._

import subscript.swing._
import subscript.vm.executor._

import scala.swing.Swing

import org.lwjgl.opengl.GL11
import org.lwjgl.glfw.GLFW._

import sge._

object Main extends Game with GameApp {

  override def body() {
    while(glfwWindowShouldClose(window) != GL11.GL_TRUE) {
      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  }

  script live = println: "Live start!"
                sleep: 3000
                println: "Live end!"

}
