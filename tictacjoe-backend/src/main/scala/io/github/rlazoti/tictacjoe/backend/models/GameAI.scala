package io.github.rlazoti.tictacjoe.backend.models

import util.Random

trait GameAI {
  def generateMove(board: Board): Option[Move]
}

case class EasyGameAI() extends GameAI {
  private val preferredMoves =
    Random.shuffle(List((1,1), (0,0), (0,2), (2,0), (2,2), (0,1), (1,0), (1,2), (2,1)))

  def generateMove(board: Board): Option[Move] =
    preferredMoves
      .find { case (row, col) =>
        board.currentState.positions(row)(col) == board.settings.emptyPositionValue
      }
      match {
        case Some((row, col)) => Some(Move(row, col))
        case _ => None
      }
}

case class HardGameAI() extends GameAI {

  def generateMove(board: Board): Option[Move] = {
    None
  }

  private def score(board: Board, depth: Int) =
    if (board.userWon()) 10 - depth
    else if (board.computerWon()) depth - 10
    else 0

  private def minimax(board: Board, depth: Int) = {
    if (board.isEnded()) score(board, depth)

    val newDepth = depth + 1
    var scores = List()
    var moves = List()

    // Populate the scores array, recursing as needed
    game.get_available_moves.each do |move|
        possible_game = game.get_new_state(move)
        scores.push minimax(possible_game, depth)
        moves.push move
    end

    # Do the min or the max calculation
    if game.active_turn == @player
        # This is the max calculation
        max_score_index = scores.each_with_index.max[1]
        @choice = moves[max_score_index]
        return scores[max_score_index]
    else
        # This is the min calculation
        min_score_index = scores.each_with_index.min[1]
        @choice = moves[min_score_index]
        return scores[min_score_index]
    end
end
}
