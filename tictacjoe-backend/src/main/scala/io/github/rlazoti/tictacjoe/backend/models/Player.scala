package io.github.rlazoti.tictacjoe.backend.models

object Mark {

  def getByType(markType: String): Mark =
    markType match {
      case "X" | "x" => MarkX()
      case _ => MarkO()
    }

  def getOpponentMark(playerMark: Mark) =
    playerMark match {
      case MarkX() => MarkO()
      case _ => MarkX()
    }

}

trait Mark {
  def get: String
}

case class MarkX() extends Mark {
  val get = "X"
}

case class MarkO() extends Mark {
  val get = "O"
}

trait Player {
  def name: String
  def getMark: String
}

case class Computer(mark: Mark) extends Player {
  val name = "Computer"
  val getMark = mark.get
}

case class User(mark: Mark) extends Player {
  val name = "You"
  val getMark = mark.get
}
