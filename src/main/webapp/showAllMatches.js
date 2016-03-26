/**
 * Created by User on 18.03.2016.
 */
var req;
var matchesTable;
var seasonSelector;
var tournamentSelector;
var tbody;

function init() {
    tournamentSelector = document.getElementById("tournaments-selector");
    seasonSelector = document.getElementById("season-selector");
    matchesTable = document.getElementById("matches-table");
    tbody = document.getElementById("tbody_id");
}

function callback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            clearTable();
            parseMessages(req.responseText);
        }else if (req.status == 555){
            clearTable();
            alert("you wanna delete cascade next match?");
            parseMessages(req.responseText);
        }
    }
}

function clearTable() {
    tbody.innerHTML = "";
}

function parseMessages(responseText) {
    if (responseText == null) {
        return false;
    } else {
        var matchesInfo = JSON.parse(responseText);
        if (matchesInfo.length > 0) {
            for (loop = 0; loop < matchesInfo.length; loop++) {
                appendWeight(
                    matchesInfo[loop].match_id,
                    matchesInfo[loop].stage,
                    matchesInfo[loop].tournamentId,
                    matchesInfo[loop].data,
                    matchesInfo[loop].owner,
                    matchesInfo[loop].guests,
                    matchesInfo[loop].result,
                    matchesInfo[loop].nextMatchId,
                    matchesInfo[loop].state
                );
            }
        }
        return true;
    }
}

function appendWeight(match_id, stage, tournament_id, data, owner, guests, result, nextMatchId, state) {
    var row;
    var cell;
    var ref;

    row = document.createElement("tr");

    cell = document.createElement("td");
    cell.textContent = match_id;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = stage;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = tournament_id;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = data;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = owner;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = guests;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = result;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = nextMatchId;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = state;
    row.appendChild(cell);

    cell = document.createElement("td");
    ref = document.createElement("a");
    ref.textContent = "X";
   // ref.setAttribute("href", "")
    ref.setAttribute("id", match_id)
    ref.setAttribute("onClick", "removeMatch(this)")
    cell.appendChild(ref);
    row.appendChild(cell);


    tbody.appendChild(row);
}


function showTable() {
    var url = "matches?type=showAllMatches";
    action(url);
}

function addMatch() {
    if (checkFields()) {
        var url = "matches?type=addMatch" +
            "&tournamentId=" + document.getElementById("tournamentId").value +
            "&stageId=" + document.getElementById("stageId").options[document.getElementById("stageId").options.selectedIndex].text +
            "&dateId=" + document.getElementById("dateId").options[document.getElementById("dateId").options.selectedIndex].text +
            "&ownerId=" + document.getElementById("ownerId").options[document.getElementById("ownerId").options.selectedIndex].text +
            "&guestsId=" + document.getElementById("guestsId").options[document.getElementById("guestsId").options.selectedIndex].text +
            "&scoreId=" + document.getElementById("scoreId").value +
            "&next_matchId=" + document.getElementById("next_matchId").value +
            "&statusId=" + document.getElementById("statusId").options[document.getElementById("statusId").options.selectedIndex].text
        action(url);
    }
}

function removeMatch(element) {
    if (element.id != null) {
        var url = "matches?type=removeMatch" +
            "&matchId=" + element.id;
        action(url);
    }
}

function checkFields() {
    //проверка обязательных полей ввода
    return true;
}

function action(url) {
// Формируем адрес с параметрами
// Создаем объект запроса
    req = new XMLHttpRequest();

// Указываем метод, адрес и асинхронность
    req.open("GET", url, true);

// Указываем функцию для обратного вызова
    req.onreadystatechange = callback;

// Отправляем запрос
    req.send(null);
}
//function sendToServ() {
//    $.ajax({
//        type: "POST",
//        url: "matches",
//        dataType: "json",
//        data: {
//            stageId: document.getElementById("stageId").options[document.getElementById("stageId").options.selectedIndex].text
//        },
//        success: function (data) {
//            alert(data[0].stage);
//        }
//    })
//    ;
//}


//function sendToServ2() {
//
//    req = new XMLHttpRequest();
//
//    var json = JSON.stringify({
//        name: "Виктор",
//        surname: "Цой"
//    });
//
//    req.open("POST", 'matches', true)
//    req.setRequestHeader('Content-type', 'application/json; charset=utf-8');
//
//    req.onreadystatechange = callback;
//
//// Отсылаем объект в формате JSON и с Content-Type application/json
//// Сервер должен уметь такой Content-Type принимать и раскодировать
//    req.send(json);
//}

