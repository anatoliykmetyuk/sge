package sge

import subscript.language
import subscript.Predef._

import subscript.objectalgebra._

import org.dyn4j.dynamics._

trait GameContext {this: Game =>

  val world: World

  abstract class GameObject extends Body with SSProcess {
    override script lifecycle = world.addBody: this
                                super.lifecycle
                                {!world.removeBody(this)!}
  }

}