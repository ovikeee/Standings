/**
 * Created by User on 18.03.2016.
 */
var req;
var tbody;

function init() {
    tbody = document.getElementById("tbody");
    showTable();
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
                    tournamentsInfo[loop].numberOfTeams
                );
            }
        }
        return true;
    }
}

function appendWeight(tournamentId, tournamentTitle,numberOfTeams) {
    var row;
    var cell;
    var ref;

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
    ref = document.createElement("a");
    ref.textContent = "X";
    // ref.setAttribute("href", "")
    ref.setAttribute("id", tournamentId);
    ref.setAttribute("onClick", "removeTournament(this)");
    cell.appendChild(ref);
    row.appendChild(cell);

    tbody.appendChild(row);
}

function callback() {
    clearTable();
    if (req.readyState == 4) {
        switch (req.status) {
            case 200: //без ошибок. Выводим измененую таблицу
                break;
            case 444: //удаление не выполнено, такой Турнир не найден. Возможно его только что кто-то удалил
                alert("Удаление не выполнено! ");
                break;
            case 445: //добавление не удалось
                alert("Добавление не выполнено!");
                break;
            case 446://изменение не произведено
                alert("Изменение не выполнено!");
                break;
            case 490: //не выполнена ни одна операция
                alert("Запрос к серверу не выполнил никаких действий!");
        }
        parseMessages(req.responseText);
    }
}

function clearTable() {
    tbody.innerHTML = "";
}


function showTable() {
    // Формируем адрес с параметрами
    var url ="tournaments?type=showAllTournament";

    action(url);
}

function action(url){
    // Создаем объект запроса
    req = new XMLHttpRequest();

    // Указываем метод, адрес и асинхронность
    req.open("GET", url, true);

    // Указываем функцию для обратного вызова
    req.onreadystatechange = callback;

    // Отправляем запрос
    req.send(null);
}

function checkFields() {
    //проверка обязательных полей ввода
    var teamNum = document.getElementById("teamNumber");
    if(document.getElementById("tournamentTitle").value=="") return false; //количество команд
    if(teamNum=="") return true; //количество команд
    if (($.isNumeric(teamNum.value)) && (teamNum.value / Math.floor(teamNum.value) == 1)) {
        //Целое число
    return true;
    } else {
        //не целое число
        //выделяем ячейку
        //touenId.addClass("active");
        //$('#tournamentId').parent().addClass("active");
        alert("Введите целое число в поле teamNum");
        return false;
    }
}

function createTournament() {
    if (checkFields()) {
        var url = "tournaments?type=createTournament" +
            "&tournamentTitle=" + document.getElementById("tournamentTitle").value +
            "&teamNumber=" + document.getElementById("teamNumber").value;
            action(url);
    }
}

function editTournament() {
    if (checkFields()) {
        var url = "tournaments?type=editTournament" +
            "&tournamentId=" + document.getElementById("tournamentId").value +
            "&tournamentTitle=" + document.getElementById("tournamentTitle").value +
            "&teamNumber=" + document.getElementById("teamNumber").value;
        action(url);
    }
}

function searchBy() {
    var url = "tournaments?type=find" +
        "&findType=" + document.getElementById("param").options[document.getElementById("param").options.selectedIndex].value +
        "&value=" + document.getElementById("findField").value;
    action(url);
}

function removeTournament(element){
    if (confirm("Вы уверены, что хотите удалить этот турнир?")) {
        if (element.id != null) {
            removeId = element.id;
            var url = "tournaments?type=removeTournament" +
                "&tournamentId=" + removeId;
            action(url);
        }
    }
}