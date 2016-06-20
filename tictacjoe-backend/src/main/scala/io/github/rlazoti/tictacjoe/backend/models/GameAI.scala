package io.github.rlazoti.tictacjoe.backend.models

trait GameAI {
  def generateMove(board: Board): Option[Move]
}

case class EasyGameAI() extends GameAI {

  private val preferredMoves = List((1,1), (0,0), (0,2), (2,0), (2,2), (0,1), (1,0), (1,2), (2,1))

  def generateMove(board: Board): Option[Move] =
    preferredMoves
      .find {
        case (row, col) => board.currentState.positions(row)(col) == board.settings.emptyPositionValue
      }
      match {
        case Some((row, col)) => Some(Move(row, col))
        case None => None
      }

}
