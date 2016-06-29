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
    computerTurn: computerTurn
  };

};

var GameTitle = React.createClass({
  render : function() {
    return (<h1 className="text-primary">{this.props.labels.gameName}</h1>);
  }
});

var GameLanguage = React.createClass({

  clickHandler : function(language) {
    var labels = new Labels(language);

    ReactDOM.render(
      <GameTitle labels={labels} />,
      document.getElementById("gameTitle")
    );

    ReactDOM.render(
      <GameSettingsFirstStep labels={labels} />,
      document.getElementById("game-container")
    );
  },

  render : function() {
    return (
      <div className="row">
        <div className="col-xs-8 col-xs-offset-2 col-md-4 col-md-offset-4 text-center">
          <button type="button" className="btn btn-lg btn-default btn-block" onClick={this.clickHandler.bind(this, "pt-br")}>
            Português
          </button>
          <button type="button" className="btn btn-lg btn-warning btn-block" onClick={this.clickHandler.bind(this, "en")}>
            English
          </button>
        </div>
      </div>
    );
  }

});

var GameSettingsFirstStep = React.createClass({

  clickHandler : function(level) {
    ReactDOM.render(
      <GameSettingsSecondStep labels={this.props.labels} level={level} />,
      document.getElementById("game-container")
    );
  },

  render : function() {
    return (
      <div className="row">
        <div className="row row-title">
          <div className="col-xs-12 text-center">
            <h2>{this.props.labels.difficulty}</h2>
          </div>
        </div>
        <div className="row">
          <div className="col-xs-8 col-xs-offset-2 col-md-4 col-md-offset-4 text-center">
            <button type="button" className="btn btn-lg btn-block btn-default" onClick={this.clickHandler.bind(this, "easy")}>
              {this.props.labels.easy}
            </button>
            <button type="button" className="btn btn-lg btn-block btn-warning" onClick={this.clickHandler.bind(this, "normal")}>
              {this.props.labels.normal}
            </button>
            <button type="button" className="btn btn-lg btn-block btn-danger" onClick={this.clickHandler.bind(this, "hard")}>
              {this.props.labels.hard}
            </button>
          </div>
        </div>
      </div>
    );
  }

});

var GameSettingsSecondStep = React.createClass({

  clickHandler : function(mark) {
    ReactDOM.render(
      <GameSettingsThirdStep labels={this.props.labels} level={this.props.level} playerMark={mark} />,
      document.getElementById("game-container")
    );
  },

  render : function() {
    return (
      <div className="row">
        <div className="row row-title">
          <div className="col-xs-12 text-center">
            <h2>{this.props.labels.pieceType}</h2>
          </div>
        </div>
        <div className="row">
          <div className="col-xs-8 col-xs-offset-2 col-md-4 col-md-offset-4 text-center">
            <button type="button" className="btn btn-lg btn-block btn-success" onClick={this.clickHandler.bind(this, "O")}>
              &nbsp;&nbsp;O&nbsp;&nbsp;
            </button>
            <button type="button" className="btn btn-lg btn-block btn-primary" onClick={this.clickHandler.bind(this, "X")}>
              &nbsp;&nbsp;X&nbsp;&nbsp;
            </button>
          </div>
        </div>
      </div>
    );
  }

});

var GameSettingsThirdStep = React.createClass({

  clickHandler : function(whoStarts) {
    var labels = this.props.labels;

    var onSuccess = function(_result, _labels) {
      ReactDOM.render(
        <NewGame labels={_labels} gameData={_result} />,
        document.getElementById("game-container")
      );
    };

    var onError = function(jqXHR, textStatus, errorThrown) {
      console.log(jqXHR);
    };

    var formdata = {
      "level" : this.props.level,
      "playerMark" : this.props.playerMark,
      "whoStarts" : whoStarts
    };

    $.ajax({
      type: 'POST',
      url: '/game/new',
      dataType: 'json',
      contentType: "application/json",
      data: JSON.stringify(formdata),
      beforeSend: function() {}
    })
     .done(function(data) { onSuccess(data, labels); })
     .fail(onError);
  },

  render : function() {
    return (
      <div className="row">
        <div className="row row-title">
          <div className="col-xs-12 text-center">
            <h2>{this.props.labels.whoWillStart}</h2>
          </div>
        </div>
        <div className="row">
          <div className="col-xs-8 col-xs-offset-2 col-md-4 col-md-offset-4 text-center">
            <button type="button" className="btn btn-lg btn-block btn-success" onClick={this.clickHandler.bind(this, "Computer")}>
              {this.props.labels.computer}
            </button>
            <button type="button" className="btn btn-lg btn-block btn-primary" onClick={this.clickHandler.bind(this, "User")}>
              {this.props.labels.iWillStart}
            </button>
          </div>
        </div>
      </div>
    );
  }

});

var NewGame = React.createClass({

  getInitialState: function() {
    return { status: this.statusHandler(this.props.gameData.status), waitResult : false };
  },

  isPositionAvailable : function(row, col) {
    return this.props.gameData.userMark !== this.props.gameData.positions[row][col] &&
           this.props.gameData.computerMark !== this.props.gameData.positions[row][col];
  },

  classHandler : function(context, row, col) {
    if (context.props.gameData.positions[row][col] === context.props.gameData.computerMark)
      return "piece piece-computer";

    else if (context.props.gameData.positions[row][col] === context.props.gameData.userMark)
      return "piece piece-user";

    else if (context.props.gameData.status === "active")
      return "piece piece-free";

    else return "piece";
  },

  statusHandler : function(status) {
    if (status === "user-won") return this.props.labels.youWin;
    else if (status === "computer-won") return this.props.labels.youLose;
    else if (status === "draw") return this.props.labels.draw;
    else if (status === "computer-turn") return this.props.labels.computerTurn;
    else return this.props.labels.yourTurn;
  },

  leaveHandler : function(row, col) {
    if (this.props.gameData.status === "active" && this.isPositionAvailable(row, col))
      $("#r" + row + "-c" + col).text("-").removeClass("piece-free-hover");
  },

  enterHandler : function(row, col) {
    if (this.props.gameData.status === "active" && this.isPositionAvailable(row, col))
      $("#r" + row + "-c" + col).text(this.props.gameData.userMark).addClass("piece-free-hover");
  },

  clickHandler : function(row, col) {
    if (!this.isPositionAvailable(row, col)) return false;
    else if (this.props.gameData.status !== "active") return false;
    else if (this.state.waitResult) return false;

    this.setState({ status: this.statusHandler("computer-turn") });

    var onSuccess = function(context) {
      return function(result) {
        context.setState({ status: context.statusHandler(result.status) });
        context.setState({ waitResult : false });
        ReactDOM.render(
          <NewGame labels={context.props.labels} gameData={result} />,
          document.getElementById("game-container")
        );
      };
    }(this);

    var onError = function(context) {
      return function(jqXHR, textStatus, errorThrown) {
        context.setState({ status : textStatus });
        context.setState({ waitResult : false });
      };
    }(this);

    var gameMustWaitAjaxResult = function(context) {
      return function() {
        context.setState({ waitResult : true });
      };
    }(this);

    var formdata = {
      "row":row,
      "col":col,
      "board": {
         "level": this.props.gameData.level,
         "userMark": this.props.gameData.userMark,
         "computerMark": this.props.gameData.computerMark,
         "positions": this.props.gameData.positions,
         "status": this.props.gameData.status
      }
    };

    $.ajax({
      type: 'POST',
      url: '/game/move',
      dataType: 'json',
      contentType: "application/json",
      data: JSON.stringify(formdata),
      beforeSend: gameMustWaitAjaxResult
    })
     .done(onSuccess)
     .fail(onError);

    return false;
  },

  render : function() {
    var generateColumn = function(context, positions, row) {
      return positions.map(function(value, col) {
        var id = "r" + row + "-c" + col;

        return <td key={"r" + id} className="text-center">
                 <div id={id} key={"d" + id}
                      onClick={context.clickHandler.bind(context, row, col)}
                      onMouseEnter={context.enterHandler.bind(context, row, col)}
                      onMouseLeave={context.leaveHandler.bind(context, row, col)}
                      className={context.classHandler(context, row, col)}>
                   {value}
                 </div>
               </td>;
      });
    };

    var generateRows = function(context, positions) {
      return positions.map(function(value, row) {
        return <tr key={row}>{generateColumn(context, value, row)}</tr>;
      });
    };

    return (
      <div className="row">
        <div className="row">
          <div className="col-xs-8 col-xs-offset-2 col-md-4 col-md-offset-4">
            <table id="board">
              <tbody>
                {generateRows(this, this.props.gameData.positions)}
              </tbody>
            </table>
          </div>
        </div>
        <div className="row">
          <div id="game-status" className="col-xs-8 col-xs-offset-2 col-md-4 col-md-offset-4">
            <div className="row row-title">
              <div className="col-xs-12 text-center bg-warning">
                <h2 className="text-warning">{this.state.status}</h2>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

});

$(function() {
  ReactDOM.render(
    <GameLanguage />,
    document.getElementById("game-container")
  );
});
