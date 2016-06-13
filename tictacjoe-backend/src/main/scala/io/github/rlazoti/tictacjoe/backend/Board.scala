package io.github.rlazoti.tictacjoe.backend

case class Position(val row: Int, val col: Int)

case class Move(val player: Player, val position: Position)

case class Board(
  val id: Int,
  val width: Int = Game.width,
  val opponent: Player = Computer(),
  val user: Player = User("Rodrigo"),
  val currentState: BoardState = InitialState()) {

  private def isEnded() =
    currentState.over(user, opponent)

  private def userWon() =
    currentState.won(user)

  private def opponentWon() =
    currentState.won(opponent)

  private def draw() =
    currentState.draw(user, opponent)

  def addMove(move: Move) = {
    if (isEnded()) this
    else {
      val board = Board(id, width, opponent, user, NextState(currentState, move))
      println(s"next move => ${move.player.name} marks ${move.position}")
      board.printCurrentState()
      board
    }
  }

  def printCurrentState() = {
    val message =
      if (userWon()) s"${user.name} won!"
      else if (opponentWon()) s"${opponent.name} won!"
      else if (draw()) s"Game Draw!"
      else "Game is not over yet!"

    println(s"""
      #=============== Board =================
      #id: $id
      #status: ${message}
      #blanks: ${currentState.blanks}
      #positions:
      #${currentState.positions.map(_.mkString("|")).mkString("\n")}
      #=======================================
      #""".stripMargin('#'))
  }
}
