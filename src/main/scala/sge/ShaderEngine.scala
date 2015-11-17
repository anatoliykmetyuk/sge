package sge

import org.lwjgl.opengl.{GL11, GL20}

trait ShaderEngine {

  def createShader(shader: Shader): Int = {
    val handle: Int = GL20.glCreateShader(shader.tpe)
    GL20.glShaderSource(handle, shader.source)
    GL20.glCompileShader(handle)

    val status = GL20.glGetShaderi(handle, GL20.GL_COMPILE_STATUS)
    if (status == GL11.GL_FALSE) {
      val error = GL20.glGetShaderInfoLog(handle)
      System.err.println(s"Error while compiling shader $shader: $error")
    }

    handle
  }

}

case class Shader(tpe: Int, source: String)