import subscript.language
import subscript.Predef._

import subscript.swing._
import subscript.vm.executor._

import scala.swing.Swing

import org.lwjgl.opengl.{GL11, GL20}
import org.lwjgl.glfw.GLFW._

import sge._

object Main extends Game with GameApp {

  // val vert = Shader(
  //   GL20.GL_VERTEX_SHADER,
  //   "#version 330                                \n"+
  //   "in vec2 position;                           \n"+
  //   "void main(){                                \n"+
  //   "    gl_Position= vec4(position, 0., 1.);    \n"+
  //   "}                                           \n"
  // )

  // val frag = Shader(
  //   GL20.GL_FRAGMENT_SHADER,
  //   "#version 330                                \n"+
  //   "out vec4 out_color;                         \n"+
  //   "void main(){                                \n"+
  //   "    out_color= vec4(0., 1., 1., 1.);        \n"+
  //   "}                                           \n"
  // )

  override def body() {
    println(shaderPrograms(SGE_SHADER_DEFAULT).handle)
    while(glfwWindowShouldClose(window) != GL11.GL_TRUE) {
      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  }

  script live = println: "Live start!"
                sleep: 3000
                println: "Live end!"

}
