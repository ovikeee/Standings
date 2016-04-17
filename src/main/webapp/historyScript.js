var teamId;
var teamTitle;
var req;
var tbody;
var allMatches;
var firstMatch;
var rowMatch;

function loadTournaments() {
    var url = "historyOfTeam?type=loadSelectBoxTournament" +
        "&teamId=" + teamId;
    req = new XMLHttpRequest();
    req.open("GET", url, true);
    req.onreadystatechange = fillingTournament;
    req.send(null);

}

function init() {
    setTeam();
    loadTournaments();
    tbody = document.getElementById("tbody_id");
    rowMatch =  document.getElementById("matchRow");
}

function parserUrl() {
    var tmp = new Array();		// два вспомагательных
    var tmp2 = new Array();		// массива
    var param = new Array();
    var get = location.search;	// строка GET запроса
    if (get != '') {
        tmp = (get.substr(1)).split('&');	// разделяем переменные
        for (var i = 0; i < tmp.length; i++) {
            tmp2 = tmp[i].split('=');		// массив param будет содержать
            param[tmp2[0]] = tmp2[1];		// пары ключ(имя переменной)->значение
        }
        return param;
    }
}

function setTeam() {
    var params = parserUrl();
    teamId = params['teamId'];
    teamTitle = params['teamTitle'];
    document.getElementById('teamTitleParagraph').innerHTML = unescape(teamTitle);
}

function action(url) {
    // Создаем объект запроса
    req = new XMLHttpRequest();

    // Указываем метод, адрес и асинхронность
    req.open("GET", url, true);

    // Указываем функцию для обратного вызова
    req.onreadystatechange = callback;

    // Отправляем запрос
    req.send(null);
}

function fillingTournament() {
    if (req.readyState == 4) {
        var tournamentSelector = document.getElementById("selectTournament");
        tournamentSelector.innerHTML = "";
        if (req.status == 200) {
            if (req.responseText == null || req.responseText == "") {
                alert("Эта команда не учавствовала ни в каком турнире");
                return false;
            } else {
                var selectorInfo = JSON.parse(req.responseText);
                var option3;
                if (selectorInfo.length > 0) {
                    for (loop = 0; loop < selectorInfo.length; loop++) {
                        option3 = document.createElement("option");
                        option3.textContent = selectorInfo[loop].tournaments;
                        option3.setAttribute("value", selectorInfo[loop].tournamentId);
                        tournamentSelector.appendChild(option3);
                    }
                }
            }
        }
    }
}

function getMatches() {
    var url = "historyOfTeam?type=getMatches" +
        "&teamId=" + teamId +
        "&tournamentId=" + document.getElementById("selectTournament").options[document.getElementById("selectTournament").options.selectedIndex].value
    action(url);
}

function callback() {
    if (req.readyState == 4) {
        switch (req.status) {
            case 200: //без ошибок. Выводим измененую таблицу
                parseMessages(req.responseText);
                break;
            case 490: //не выполнена ни одна операция
                alert("Запрос к серверу не выполнил никаких действий!");
        }
    }
}

function saveData(match_id, stage, data, owner, guests, result, nextMatchId, state) {
    var match = [match_id, stage, data, owner, guests, result, nextMatchId, state];
    allMatches.push(match);
}

function setMatch(match) {
    rowMatch.getElementsByTagName("td")[0].innerHTML = match[0];
    rowMatch.getElementsByTagName("td")[1].innerHTML = match[1];
    rowMatch.getElementsByTagName("td")[2].innerHTML = match[2];
    rowMatch.getElementsByTagName("td")[3].innerHTML = match[3];
    rowMatch.getElementsByTagName("td")[4].innerHTML = match[4];
    rowMatch.getElementsByTagName("td")[5].innerHTML = match[5];
    rowMatch.getElementsByTagName("td")[6].innerHTML = match[6];
    rowMatch.getElementsByTagName("td")[7].innerHTML = match[7];
}

function parseMessages(responseText) {
    if (responseText == null) {
        return false;
    } else {
        var matchesInfo = JSON.parse(responseText);
        allMatches = [];
        if (matchesInfo.length > 0) {
            for (loop = 0; loop < matchesInfo.length; loop++) {
                saveData(
                    matchesInfo[loop].match_id,
                    matchesInfo[loop].stage,
                    matchesInfo[loop].data,
                    matchesInfo[loop].owner,
                    matchesInfo[loop].guests,
                    matchesInfo[loop].result,
                    matchesInfo[loop].nextMatchId,
                    matchesInfo[loop].state
                );
            }
            //устанавливаем первый матч
            var tmpMatch;
            var flag = false;
            for (var i = 0; i < allMatches.length; i++) {
                tmpMatch = allMatches[i];
                for (var i = 0; i < allMatches.length; i++) {
                    if (allMatches[i][6] == tmpMatch[0]) {//смотрим, есть ли матчи, ссылающиеся на tmpMatch
                        flag = true; //есть
                    }
                }
                if (flag == false){
                    firstMatch = tmpMatch;
                    setMatch(firstMatch);
                    return true;
                }
                flag = false;//сбрасываем флаг
            }
        }
        return true;
    }
}

function setPrevMatch() {
    var currentMatchId = document.getElementById("matchId");
    var prevMatch;
    for (var i = 0; i < allMatches.length; i++) {
        if (allMatches[i][6] == currentMatchId) {//находим предыдущий матч
            prevMatch= allMatches[i];
            setMatch(prevMatch);
        }
    }
    if (prevMatch == null) {
        alert("Это первый матч.");
    }
}

function setNextMatch() {
    var nextMatchId = document.getElementById("nextMatchId");
    var nextMatch;
    for (var i = 0; i < allMatches.length; i++) {
        if (allMatches[i][0] == nextMatchId) {//находим матч
            nextMatch= allMatches[i];
        }
    }
    if (nextMatch != null && nextMatch!="") {
        setMatch(nextMatch);
    }else{
        alert("Это финальный матч.");
    }
}