/**
 * Created by User on 18.03.2016.
 */
var req;
var tbody;

function init() {
    tbody = document.getElementById("tbody");
}

function callback() {
    clearTable();
    if (req.readyState == 4) {
        if (req.status == 200) {
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
        var tournamentsInfo = JSON.parse(responseText);
        if (tournamentsInfo.length > 0) {
            for (loop = 0; loop < tournamentsInfo.length; loop++) {
                appendWeight(
                    tournamentsInfo[loop].tournamentId,
                    tournamentsInfo[loop].tournamentTitle,
                    tournamentsInfo[loop].numberOfTeams,
                    tournamentsInfo[loop].season
                );
            }
        }
        return true;
    }
}

function appendWeight(tournamentId, tournamentTitle,numberOfTeams,season) {
    var row;
    var cell;

    row = document.createElement("tr");

    cell = document.createElement("td");
    cell.textContent = tournamentId;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = tournamentTitle;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = numberOfTeams;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = season;
    row.appendChild(cell);

    tbody.appendChild(row);
}


function showTable() {
    // Формируем адрес с параметрами
    var url = "tournaments";

    // Создаем объект запроса
    req = new XMLHttpRequest();

    // Указываем метод, адрес и асинхронность
    req.open("GET", url, true);

    // Указываем функцию для обратного вызова
    req.onreadystatechange = callback;

    // Отправляем запрос
    req.send(null);
}


