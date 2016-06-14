package io.github.rlazoti.tictacjoe.backend

trait BoardState {

  def blanks: Int
  def positions: Array[Array[String]]
  def lastMovePlayer: Option[Player]
  def settings: GameSettings

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
      for (index <- 0 to settings.boardWidth - 1)
      yield positions(index)(index)

    checkValues(player, values.toArray)
  }

  private def winningReverseDiagonal(player: Player) = {
    val values =
      for (index <- 0 to settings.boardWidth - 1)
      yield positions(settings.boardWidth - 1 - index)(index)

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
      for (index <- 0 to settings.boardWidth - 1)
      yield getColumn(index)

    columns
      .map { row => checkValues(player, row) }
      .count { result => result == true } > 0
  }

}

case class InitialBoardState(implicit settings: GameSettings) extends BoardState {
  val blanks = settings.boardWidth * settings.boardWidth
  val positions = Array.fill(settings.boardWidth, settings.boardWidth)(settings.emptyPositionValue)
  val lastMovePlayer = None
}

case class NextBoardState(currentState: BoardState, move: Move) extends BoardState {
  currentState.lastMovePlayer.map { player =>
    if (currentState.over(player, move.player))
      throw new UnsupportedOperationException(s"No one can perform a new move ater the end of game.")
  }

  val lastMovePlayer = currentState.lastMovePlayer match {
    case Some(currentPlayer) if (currentPlayer.equals(move.player)) =>
      throw new IllegalArgumentException(s"User '${move.player.name}' cannot perform two moves in a row.")
    case _ => Some(move.player)
  }

  val blanks = currentState.blanks - 1
  val settings = currentState.settings
  val positions = buildPositions()

  private def buildPositions() = {
    val newPositions = currentState.positions.map { values => values.map(identity) }
    newPositions(move.position.row)(move.position.col) = move.player.mark
    newPositions
  }
}
