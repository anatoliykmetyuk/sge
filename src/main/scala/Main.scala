import java.nio.ByteBuffer
import java.nio.IntBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.GLContext
import org.lwjgl.glfw.GLFWvidmode

import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL33._
import org.lwjgl.system.MemoryUtil.NULL

object Main extends Game {

  override def update(deltaTime: Double, width: Int, height: Int) {
    val ratio = width / height.toFloat

    /* Set viewport and clear screen */
    glViewport(0, 0, width, height);
    glClear(GL_COLOR_BUFFER_BIT);

    /* Set ortographic projection */
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(-ratio, ratio, -1f, 1f, 1f, -1f);
    glMatrixMode(GL_MODELVIEW);

    /* Rotate matrix */
    glLoadIdentity();
    glRotatef(glfwGetTime().toFloat * 50f, 0f, 0f, 1f);

    /* Render triangle */
    glBegin(GL_TRIANGLES);
    glColor3f(1f, 0f, 0f);
    glVertex3f(-0.6f, -0.4f, 0f);
    glColor3f(0f, 1f, 0f);
    glVertex3f(0.6f, -0.4f, 0f);
    glColor3f(0f, 0f, 1f);
    glVertex3f(0f, 0.6f, 0f);
    glEnd();
  }

  def main(args: Array[String]): Unit = {
    lifecycle()
  }
}
