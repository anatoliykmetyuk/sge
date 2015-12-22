import subscript.language
import subscript.Predef._

import subscript.swing._
import subscript.vm.executor._

import scala.swing.Swing

import org.lwjgl.BufferUtils

import org.lwjgl.opengl.{GL11, GL15, GL20, GL30}
import org.lwjgl.glfw.GLFW._

import sge._

object Main extends Game with GameApp {

  override def body() {
    GL11.glClearColor(0.0f, 0.0f, 0.5f, 1.0f)
    while(glfwWindowShouldClose(window) != GL11.GL_TRUE) {
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
      renderSquare()
      glfwSwapBuffers(window)
      glfwPollEvents()
    }
  }

  def renderSquare() {
    // Prepare data
    val data = Array[Float](
      -0.5f, -0.5f, 
      -0.5f,  0.5f,
       0.5f, -0.5f,
       0.5f,  0.5f,
      -0.5f,  0.5f,
       0.5f, -0.5f
    )
    val dataBuffer = BufferUtils.createFloatBuffer(data.size)
    dataBuffer.put(data)
    dataBuffer.flip()

    // VAO - Vertex Array Object
    val vao = GL30.glGenVertexArrays()
    GL30.glBindVertexArray(vao)

    // VBO - Vertex Buffer Object
    val dataHandle = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, dataHandle)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW)

    // Prepare the shader program
    val program = shaderPrograms(SGE_SHADER_DEFAULT).handle
    GL20.glUseProgram(program)
    val lPosition = GL20.glGetAttribLocation(program, "position")

    // Connect the data to the shader
    GL20.glEnableVertexAttribArray(lPosition)
    GL20.glVertexAttribPointer(lPosition, 2, GL11.GL_FLOAT, false, 0, 0)

    // Draw
    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

    // Clean up
    GL20.glUseProgram(0);
    GL20.glDisableVertexAttribArray(0);
  }

  script live = println: "Live start!"
                sleep: 3000
                println: "Live end!"

}
