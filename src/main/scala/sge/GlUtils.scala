package sge

import org.lwjgl.BufferUtils

import org.lwjgl.opengl.{GL11, GL15, GL20, GL30}
import org.lwjgl.glfw.GLFW._

object GlUtils {

  def renderRectangle(vertices: Array[Float], shaderProgram: Int) {
    val dataBuffer = BufferUtils.createFloatBuffer(vertices.size)
    dataBuffer.put(vertices)
    dataBuffer.flip()

    // VAO - Vertex Array Object
    val vao = GL30.glGenVertexArrays()
    GL30.glBindVertexArray(vao)

    // VBO - Vertex Buffer Object
    val dataHandle = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, dataHandle)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW)

    // Prepare the shader program
    GL20.glUseProgram(shaderProgram)
    val lPosition = GL20.glGetAttribLocation(shaderProgram, "position")

    // Connect the data to the shader
    GL20.glEnableVertexAttribArray(lPosition)
    GL20.glVertexAttribPointer(lPosition, 2, GL11.GL_FLOAT, false, 0, 0)

    // Draw
    GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 6);

    // Clean up
    GL20.glUseProgram(0);
    GL20.glDisableVertexAttribArray(lPosition);
    GL30.glBindVertexArray(0)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
  }

}
