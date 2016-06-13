package io.github.rlazoti.tictacjoe.backend

trait BoardState {
  def blanks: Int
  def positions: Game.Positions

  def over(player: Player, opponent: Player) =
    won(player) || won(opponent) || draw(player, opponent)

  def draw(player: Player, opponent: Player) =
    !won(player) && !won(opponent) && blanks == 0

  def won(player: Player) =
    winningCol(player) || winningRow(player) || winningDiagonal(player) || winningReverseDiagonal(player)

  private def checkValues(player: Player, values: Array[String]) =
    values.foldLeft(true) { (acc, value) => acc && value == player.mark }

  private def winningDiagonal(player: Player) = {
    val values =
      for (index <- 0 to Game.width - 1)
      yield positions(index)(index)

    checkValues(player, values.toArray)
  }

  private def winningReverseDiagonal(player: Player) = {
    val values =
      for (index <- 0 to Game.width - 1)
      yield positions(Game.width - 1 - index)(index)

    checkValues(player, values.toArray)
  }

  private def winningRow(player: Player) =
    positions
      .map { row => checkValues(player, row) }
      .count { result => result == true } > 0

  private def winningCol(player: Player) = {
    def getColumn(columnIndex: Int) =
      positions.map { row => row(columnIndex) }

    val columns =
      for (index <- 0 to Game.width - 1)
      yield getColumn(index)

    columns
      .map { row => checkValues(player, row) }
      .count { result => result == true } > 0
  }

}

case class InitialState() extends BoardState {
  val blanks = Game.width * Game.width
  val positions = Array.fill(Game.width, Game.width)(Game.EmptyPosition)
}

case class NextState(currentState: BoardState, move: Move) extends BoardState {
  val blanks = currentState.blanks - 1
  val positions = buildPositions()

  private def buildPositions() = {
    val newPositions = currentState.positions
    newPositions(move.position.row)(move.position.col) = move.player.mark
    newPositions
  }
}
