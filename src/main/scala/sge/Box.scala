package sge

import subscript.language
import subscript.Predef._

import scala.collection.JavaConversions._

import subscript.objectalgebra._

import org.dyn4j.dynamics._
import org.dyn4j.geometry._

trait BoxComponent {this: Game =>

  abstract class Box(x: Double, y: Double, width: Double, height: Double, color: Array[Float]) extends GameObject {
    initBody()
    
    def initBody() {
      val rect = new Rectangle(width, height)
      addFixture(rect)
      setMass(MassType.NORMAL)
      translate(x, y)
    }


    override def render() {
      for (fixture <- getFixtures) renderFixture(fixture)
    }

    def renderFixture(fixture: BodyFixture) {
      val poly     = polygon(fixture)
      val program  = shaderPrograms(SGE_SHADER_DEFAULT).handle

      GlUtils.renderRectangle(poly, program, color)
    }

    def polygon(fixture: BodyFixture): Array[Float] = {
      var vertices = fixture.getShape.asInstanceOf[Polygon].getVertices.map(_.copy())
      vertices.foreach(getTransform.transform)  // Moving the vertices in the physical space

      // Scaling so that there's 10 units across the screen
      for (v <- vertices) {
        v.x /= 10
        v.y /= 10
      }

      vertices ++= Seq(vertices.head.copy, vertices(2).copy)
      vertices.flatMap(v => Seq(v.x, v.y)).map(_.toFloat)
    }
  }

}