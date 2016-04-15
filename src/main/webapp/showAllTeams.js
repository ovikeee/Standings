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
        var teamsInfo = JSON.parse(responseText);
        if (teamsInfo.length > 0) {
            for (loop = 0; loop < teamsInfo.length; loop++) {
                appendWeight(
                    teamsInfo[loop].teamId,
                    teamsInfo[loop].teamTitle
                );
            }
        }
        return true;
    }
}

function appendWeight(teamId, teamTitle) {
    var row;
    var cell;

    row = document.createElement("tr");

    cell = document.createElement("td");
    cell.textContent = teamId;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.textContent = teamTitle;
    row.appendChild(cell);

    cell = document.createElement("td");
    cell.setAttribute("href","https://www.google.ru/");
    cell.textContent = "X";
    row.appendChild(cell);

    tbody.appendChild(row);
}


function showTable() {
    // Формируем адрес с параметрами
    var url = "teams";

    // Создаем объект запроса
    req = new XMLHttpRequest();

    // Указываем метод, адрес и асинхронность
    req.open("GET", url, true);

    // Указываем функцию для обратного вызова
    req.onreadystatechange = callback;

    // Отправляем запрос
    req.send(null);
}


