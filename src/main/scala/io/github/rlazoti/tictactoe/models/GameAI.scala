package io.github.rlazoti.tictactoe.models

import util.Random

sealed trait GameAI {
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

case class NormalGameAI() extends GameAI {

  private val easyAI = EasyGameAI()
  private val hardAI = HardGameAI()

  def generateMove(board: Board): Option[Move] =
    if (board.availablePositions().size % 3 == 0) hardAI.generateMove(board)
    else easyAI.generateMove(board)

}

case class HardGameAI() extends GameAI {

  def generateMove(board: Board): Option[Move] = {
    val (_, bestMove) = minimax(board, 2, None, Int.MinValue, Int.MaxValue)
    bestMove
  }

  private def minimax(board: Board, depth: Int, move: Option[Move], alpha: Int, beta: Int): (Int, Option[Move]) = {
    var score: Int = 0
    var currentBeta = beta
    var currentAlpha = alpha
    var bestMove: Option[Move] = move

    if (board.isEnded() || depth == 0) (evaluate(board), bestMove)

    else {
      board.availablePositions().foreach { move =>
        if (currentAlpha < currentBeta) {
          val newBoard = Board(board.settings, board.userPlayer, NextBoardState(board.currentState, move))
          score = minimax(newBoard, depth -1, Some(move), currentAlpha, currentBeta)._1

          if (board.currentState.opponentPlayer == board.computerPlayer) {
            if (score > currentAlpha) {
              currentAlpha = score
              bestMove = newBoard.currentState.lastMove
            }
          }

          else if (score < currentBeta) {
            currentBeta = score
            bestMove = newBoard.currentState.lastMove
          }
        }
      }

      (if (board.currentState.opponentPlayer == board.computerPlayer) currentAlpha else currentBeta, bestMove)
    }
  }

  private def evaluate(board: Board): Int =
    List(
      (0, 0, 0, 1, 0, 2), // row 0
      (1, 0, 1, 1, 1, 2), // row 1
      (2, 0, 2, 1, 2, 2), // row 2
      (0, 0, 1, 0, 2, 0), // col 0
      (0, 1, 1, 1, 2, 1), // col 1
      (0, 2, 1, 2, 2, 2), // col 2
      (0, 0, 1, 1, 2, 2), // diagonal
      (0, 2, 1, 1, 2, 0)  // reverse diagonal
    ).map { case (r1, c1, r2, c2, r3, c3) => evaluateLine(board, r1, c1, r2, c2, r3, c3) }
     .sum

  private def evaluateLine(board: Board, row1: Int, col1: Int, row2: Int, col2: Int, row3: Int, col3: Int): Int = {
    val score1 =
      if (board.currentState.positions(row1)(col1).equals(board.computerPlayer.getMark)) 1
      else if (board.currentState.positions(row1)(col1).equals(board.userPlayer.getMark)) -1
      else 0

    val score2 =
      if (board.currentState.positions(row2)(col2).equals(board.computerPlayer.getMark)) {
        if (score1 == 1) 10 else if (score1 == -1) 0 else 1
      }
      else if (board.currentState.positions(row2)(col2).equals(board.userPlayer.getMark)) {
        if (score1 == -1) -10 else if (score1 == 1) 0 else -1
      }
      else score1

    if (board.currentState.positions(row3)(col3).equals(board.computerPlayer.getMark)) {
      if (score2 > 0) score2 * 10 else if (score2 < 0) 0 else 1
    }
    else if (board.currentState.positions(row3)(col3).equals(board.userPlayer.getMark)) {
      if (score2 < 0) score2 * 10 else if (score2 > 1) 0 else -1
    }
    else score2
  }

}
