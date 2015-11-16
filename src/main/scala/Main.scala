import subscript.language

import subscript.swing._
import subscript.vm.executor._

import scala.swing.Swing

import sge._

object Main extends Game with GameApp {

  script live = println: "Live start!"
                sleep: 3000
                println: "Live end!"

}
