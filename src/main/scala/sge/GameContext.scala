package sge

import subscript.language
import subscript.Predef._

import subscript.objectalgebra._

import org.dyn4j.dynamics._

trait GameContext {this: Game =>

  abstract class GameObject extends Body with SSProcess {
    var tag = ""

    val destroyed = new Trigger

    override script lifecycle = world.addBody: this
                                super.lifecycle / destroyed
                                {!world.removeBody(this)!}

    def render(): Unit

    def collision(that: GameObject): Trigger = collisions.event(this, that)

    def collision(f   : GameObject => Boolean): Trigger = collisions.event {(b1, b2) =>
      val one     = if (this == b1) b1 else b2
      val another = if (one  == b1) b2 else b1

      one == this && f(another)
    }

    def collision(tag : String): Trigger = collision(_.tag == tag)
  }

}