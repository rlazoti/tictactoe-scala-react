package io.github.rlazoti.tictacjoe.backend

object App extends App {

  val currentBoardId = 1
  implicit val settings = GameSettings()
  val currentBoard = Board(currentBoardId, settings, User("Rodrigo"), Computer(), InitialBoardState())
  var boards = List(currentBoard)

  val moves = List(
    Move(currentBoard.player, Position(0, 2)),
    Move(currentBoard.opponentPlayer, Position(1, 0)),
    Move(currentBoard.player, Position(2, 0)),
    Move(currentBoard.opponentPlayer, Position(1, 2)),
    Move(currentBoard.player, Position(1, 1)),
    Move(currentBoard.opponentPlayer, Position(2, 2))
  )

  currentBoard.printCurrentState()

  moves.foreach { nextMove =>
    boards = boards
      .filter { board => board.id == currentBoardId }
      .map { board => board.addMove(nextMove) }
      //.map { board => board.printCurrentState(); board }
  }

}
