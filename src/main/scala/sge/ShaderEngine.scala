package sge

import org.lwjgl.opengl.{GL11, GL20}

trait ShaderEngine {

  def createShader(shader: Shader): Int = {
    // Compile the shader
    val handle: Int = GL20.glCreateShader(shader.tpe)
    GL20.glShaderSource(handle, shader.source)
    GL20.glCompileShader(handle)

    // Check for errors
    val status = GL20.glGetShaderi(handle, GL20.GL_COMPILE_STATUS)
    if (status == GL11.GL_FALSE) {
      val error = GL20.glGetShaderInfoLog(handle)
      System.err.println(s"Error while compiling shader $shader: $error")
    }

    // Return shader's address in GPU memory
    handle
  }

  def createShaderProgram(shaders: Seq[Int]): Int = {
    // Link shaders into a program
    val handle: Int = GL20.glCreateProgram()
    for (s <- shaders) GL20.glAttachShader(handle, s)
    GL20.glLinkProgram(handle)

    // Check for errors
    val status = GL20.glGetShaderi(handle, GL20.GL_LINK_STATUS)
    if (status == GL11.GL_FALSE) {
      val error = GL20.glGetProgramInfoLog(handle)
      System.err.println(s"Error while linking the program: $error")
    }

    // Detach shaders
    for (s <- shaders) GL20.glDetachShader(handle, s)

    // Reutrn program's address
    handle
  }

  def program(shaders: Seq[Shader]): Int =
    createShaderProgram {shaders.map(createShader)}

}

case class Shader(tpe: Int, source: String)