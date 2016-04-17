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
    var ref;

    row = document.createElement("tr");

    cell = document.createElement("td");
    cell.textContent = teamId;
    row.appendChild(cell);

    cell = document.createElement("td");
    ref = document.createElement("a");
    ref.textContent = teamTitle;
    // ref.setAttribute("href", "")
    ref.setAttribute("id", teamId);
    ref.setAttribute("onClick", "gotoHistoryOfTeam("+teamId+",'"+teamTitle+"')");
    cell.appendChild(ref);
    row.appendChild(cell);

    cell = document.createElement("td");
    ref = document.createElement("a");
    ref.textContent = "X";
    // ref.setAttribute("href", "")
    ref.setAttribute("id", teamId);
    ref.setAttribute("onClick", "removeTeam(this)");
    cell.appendChild(ref);
    row.appendChild(cell);

    tbody.appendChild(row);
}

function gotoHistoryOfTeam(id, title) {
    if (id!="") {
        window.open("historyOfTeam.html?teamId=" + id +"&teamTitle="+title, "JS"); //отправляем в адресной строке message
    }
}

function callback() {
    clearTable();
    if (req.readyState == 4) {
        switch (req.status) {
            case 200: //без ошибок. Выводим измененую таблицу
                break;
            case 444: //удаление не выполнено, такая команда не найдена. Возможно её только что кто-то удалил
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
    var url ="teams?type=showAllTeam";

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





function createTeam() {
    if(document.getElementById("teamTitle").value==""){
        alert("Введите название команды");
        return false;
    }else{
    var url = "teams?type=createTeam" +
            "&teamTitle=" + document.getElementById("teamTitle").value;
        action(url);
    }
}

function editTeam() {
    if(document.getElementById("teamTitle").value==""){
        alert("Введите название команды");
        return false;
    }else{
        var url = "teams?type=editTeam" +
            "&teamId=" + document.getElementById("teamId").value +
            "&teamTitle=" + document.getElementById("teamTitle").value;
        action(url);
    }
}

function searchBy() {
    var url = "teams?type=find" +
        "&findType=" + document.getElementById("param").options[document.getElementById("param").options.selectedIndex].value +
        "&value=" + document.getElementById("findField").value;
    action(url);
}

function removeTeam(element){
    if (confirm("Вы уверены, что хотите удалить эту команду?")) {
        if (element.id != null) {
            removeId = element.id;
            var url = "teams?type=removeTeam" +
                "&teamId=" + removeId;
            action(url);
        }
    }
}
