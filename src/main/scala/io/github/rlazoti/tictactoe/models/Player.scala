package io.github.rlazoti.tictactoe.models

object Piece {
  def getByType(pieceType: String): Piece =
    pieceType match {
      case "X" | "x" => Cross()
      case _ => Nought()
    }

  def getOpponentPiece(playerPiece: Piece) =
    playerPiece match {
      case Cross() => Nought()
      case _ => Cross()
    }
}

sealed trait Piece {
  def get: String
}

case class Cross() extends Piece {
  val get = "X"
}

case class Nought() extends Piece {
  val get = "O"
}

sealed trait Player {
  def name: String
  def getPiece: String
}

case class Computer(piece: Piece) extends Player {
  val name = "Computer"
  val getPiece = piece.get
}

case class User(piece: Piece) extends Player {
  val name = "User"
  val getPiece = piece.get
}
