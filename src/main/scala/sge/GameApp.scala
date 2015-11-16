package sge

trait GameApp {this: Game =>
  def main(args: Array[String]): Unit = start()
}