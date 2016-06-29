var Labels = function(language) {
  var portuguese = "pt-br";

  var gameName = language === portuguese ? "Jogo da Velha" : "Tic Tac Toe";
  var difficulty = language === portuguese ? "Escolha a Dificuldade" : "Choose the Difficulty";
  var easyMode = language === portuguese ? "Muito Fácil" : "Piece of Cake";
  var normalMode = "Normal";
  var hardMode = language === portuguese ? "Invencível" : "Impossible";
  var pieceType = language === portuguese ? "OK! Agora escolha a sua Peça" : "OK! Now choose your Piece";
  var whoWillStart = language === portuguese ? "Ótimo! Quem irá começar?" : "Great! Who will Start?";
  var computer = language === portuguese ? "Computador" : "Computer";
  var iWillStart = language === portuguese ? "Eu começo!" : "I'll Start!";
  var youWin = language === portuguese ? "Você Ganhou!" : "You Win!";
  var youLose = language === portuguese ? "Você Perdeu!" : "You Lose!";
  var draw = language === portuguese ? "Empatou!" : "Draw!";
  var yourTurn = language === portuguese ? "Sua vez de jogar" : "It's your turn";
  var computerTurn = language === portuguese ? "Computador vai jogar" : "Computer's turn";
  var playAgain = language === portuguese ? "Novo Jogo" : "New Game";

  return {
    gameName: gameName,
    difficulty: difficulty,
    easy: easyMode,
    normal: normalMode,
    hard: hardMode,
    pieceType: pieceType,
    whoWillStart: whoWillStart,
    computer: computer,
    iWillStart: iWillStart,
    youWin: youWin,
    youLose: youLose,
    draw: draw,
    yourTurn: yourTurn,
    computerTurn: computerTurn,
    playAgain: playAgain
  };

};

var postCall = function(jqueryInstance) {
  return function(uri, data, beforeSend) {
    return jqueryInstance.ajax({
      type: 'POST',
      url: uri,
      dataType: 'json',
      contentType: "application/json",
      data: JSON.stringify(data),
      beforeSend: beforeSend
    });
  };
};
