package sge

import org.lwjgl.opengl.{GL11, GL20}

trait ShaderEngine {

  val SGE_SHADER_DEFAULT = "sge_shader_default"


  val defaultShaderPrograms = Map[String, ShaderProgram](
    /**
     * Accepts positions of the vertices in "position" and
     * the color of whatever is rendered in "color".
     * Renders the figure as is with specified color.
     */
    SGE_SHADER_DEFAULT -> ShaderProgram(Seq(
      Shader(
        tpe = GL20.GL_VERTEX_SHADER
      , source =
          """#version 330
            |in vec2 position;
            |void main() {
            |  gl_Position = vec4(position, 0., 1.);
            |}
            |""".stripMargin
      )
    
    , Shader(
        tpe = GL20.GL_FRAGMENT_SHADER
      , source =
          """#version 330
            |out vec4 out_color;
            |void main() {
            |  out_color = vec4(0., 1., 1., 1.);  
            |}
            |""".stripMargin
      )
    ))
  )


  val shaderPrograms = collection.mutable.Map[String, ShaderProgram]() ++ defaultShaderPrograms


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
    val status = GL20.glGetProgrami(handle, GL20.GL_LINK_STATUS)
    if (status == GL11.GL_FALSE) {
      val error = GL20.glGetProgramInfoLog(handle)
      System.err.println(s"Error while linking the program: $error")
    }

    // Detach shaders
    for (s <- shaders) GL20.glDetachShader(handle, s)

    // Reutrn program's address
    handle
  }


  def compileShaderPrograms() {
    for (p <- shaderPrograms.values) p.handle
  }


  case class Shader(tpe: Int, source: String)

  case class ShaderProgram(shaders: Seq[Shader]) {
    lazy val handle = createShaderProgram(shaders.map(createShader))
  }
}
