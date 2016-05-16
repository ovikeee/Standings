/**
 * Created by User on 18.03.2016.
 */
var req;
var req2;
var matchesTable;
var seasonSelector;
var tournamentSelector;
var tbody;
var removeId;
var selectedMatch;
var currentId;
var flag=0;

function start() {
    tournamentSelector = document.getElementById("tournaments-selector");
    seasonSelector = document.getElementById("season-selector");
    matchesTable = document.getElementById("matches-table");
    tbody = document.getElementById("tbody_id");
    loadSelectBox();
    showTable();
}

function loadSelectBox() {
    var url = "matches?type=loadSelectBox";
    req2 = new XMLHttpRequest();
    req2.open("GET", url, true);
    req2.onreadystatechange = filling;
    req2.send(null);

    var url = "matches?type=loadSelectBoxTournament";
    req3 = new XMLHttpRequest();
    req3.open("GET", url, true);
    req3.onreadystatechange = fillingTournament;
    req3.send(null);


}

function clearSelectors() {
    document.getElementById("ownerId").innerHTML = "";
    document.getElementById("guestsId").innerHTML = "";
}

function filling() {
    if (req2.readyState == 4) {
        clearSelectors();
        if (req2.status == 200) {
            if (req2.responseText == null) {
                alert("is null");
                return false;
            } else {
                var selectorInfo = JSON.parse(req2.responseText);
                var ownerSelector = document.getElementById("ownerId");
                var guestsSelector = document.getElementById("guestsId");
                //  var tournamentSelector = document.getElementById("tournamentId");
                var option;
                var option2;
                var option3;
                if (selectorInfo.length > 0) {
                    for (loop = 0; loop < selectorInfo.length; loop++) {
                        option = document.createElement("option");
                        option.textContent = selectorInfo[loop].teams;
                        option.setAttribute("value",selectorInfo[loop].teamId);

                        option2 = document.createElement("option");
                        option2.textContent = selectorInfo[loop].teams;
                        option2.setAttribute("value",selectorInfo[loop].teamId);

                        ownerSelector.appendChild(option);
                        guestsSelector.appendChild(option2);
                    }
                }


                //if (selectorInfo[1].length > 0) {
                //    for (loop = 0; loop < selectorInfo[1].length; loop++) {
                //        option3 = document.createElement("option");
                //        option3.textContent = selectorInfo[loop].tournaments;
                //        option3.setAttribute("value", selectorInfo[loop].tournamentId);
                //
                //        tournamentSelector.appendChild(option3);
                //    }
                //}
            }
        }
    }
}

function fillingTournament() {
    // alert("is null");
    if (req3.readyState == 4) {
        document.getElementById("tournamentId").innerHTML = "";
        if (req3.status == 200) {
            if (req3.responseText == null) {
                alert("is null");
                return false;
            } else {
                var selectorInfo = JSON.parse(req3.responseText);
                var tournamentSelector = document.getElementById("tournamentId");
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

function callback() {
    if (req.readyState == 4) {
        clearTable();
        switch (req.status) {
            case 200: //без ошибок. Выводим измененую таблицу
                parseMessages(req.responseText);
                break;
            case 555: //подтверждение каскадного удаления
                document.getElementById("textInfo").innerText = "Претенденты на удалеие:";
                parseMessages(req.responseText);
                if (confirm("При удалении выбранного матча, удалятся и все матчи, которые ссылаются на него. Хотите ли Вы удалить эти матчи?")) {
                    cascadeDelete(removeId);
                } else {
                    showTable();
                }
                document.getElementById("textInfo").innerText = "Все матчи:";
                break;
            case 444: //удаление не выполнено, такой матч не найден. Возможно его только что кто-то удалил
                alert("Удаление не выполнено! ");
                parseMessages(req.responseText);
                break;
            case 445: //добавление не удалось
                alert("Добавление не выполнено!");
                parseMessages(req.responseText);
                break;
            case 446://изменение не произведено
                alert("Изменение не выполнено!");
                parseMessages(req.responseText);
                break;
            case 490: //не выполнена ни одна операция
                alert("Запрос к серверу не выполнил никаких действий!");
                parseMessages(req.responseText);
                break;
        }

    }
}

function clearTable() {
    tbody.innerHTML = "";
    //$("#myTable").sorte

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
                    matchesInfo[loop].tournament,
                    matchesInfo[loop].data,
                    matchesInfo[loop].owner,
                    matchesInfo[loop].guests,
                    matchesInfo[loop].result,
                    matchesInfo[loop].nextMatchId,
                    matchesInfo[loop].state
                );
            }
        }
        if(flag==0) {
            $("#myTable").tablesorter({sortList: [[0, 0], [1, 0]]});
            flag++;
        }
        $('.tablesorter').trigger('update');
        return true;
    }
}

function appendWeight(match_id, stage, tournament_id, data, owner, guests, result, nextMatchId, state) {
    var row;
    var cell;
    var ref;

    row = document.createElement("tr");

    row.setAttribute("id", match_id);
    row.setAttribute("onClick", "selectRow(this)");


    //cell = document.createElement("td");
    //cell.textContent = match_id;
    //cell.setAttribute("style", "display: none");// style="display: none"
    //row.appendChild(cell);

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
  //ref.setAttribute("style", "color: blue");//   color: #930;
    ref.setAttribute("id", match_id);
    ref.setAttribute("onClick", "removeMatch(this)");
    cell.appendChild(ref);
    row.appendChild(cell);


    tbody.appendChild(row);
}

function showTable() {
    var url = "matches?type=showAllMatches";
    action(url);
}

function addMatch() {
    var tournament = document.getElementById("tournamentId");
    /*    if(tournament.classList.contains("active")){
     tournament.classList.remove("active");
     };
     $('div').removeClass("clName1 clName2")
     */
    if (checkFields()) {
        var url = "matches?type=addMatch" +
            "&tournamentId=" + document.getElementById("tournamentId").options[document.getElementById("tournamentId").options.selectedIndex].value +
            "&stageId=" + document.getElementById("stageId").options[document.getElementById("stageId").options.selectedIndex].text +
            "&dateId=" + document.getElementById("dateId").value +
            "&ownerId=" + document.getElementById("ownerId").options[document.getElementById("ownerId").options.selectedIndex].value +
            "&guestsId=" + document.getElementById("guestsId").options[document.getElementById("guestsId").options.selectedIndex].value +
            "&scoreId=" + document.getElementById("scoreId").value +
            "&next_matchId=" + document.getElementById("next_matchId").value +
            "&statusId=" + document.getElementById("statusId").options[document.getElementById("statusId").options.selectedIndex].text
        action(url);
    }
}

function editMatch() {
    var tournament = document.getElementById("tournamentId");
    /*    if(tournament.classList.contains("active")){
     tournament.classList.remove("active");
     };
     $('div').removeClass("clName1 clName2")
     */
    if (checkFields()) {
        var url = "matches?type=editMatch" +
            "&matchId=" + currentId +
            "&tournamentId=" + document.getElementById("tournamentId").options[document.getElementById("tournamentId").options.selectedIndex].value +
            "&stageId=" + document.getElementById("stageId").options[document.getElementById("stageId").options.selectedIndex].text +
            "&dateId=" + document.getElementById("dateId").value+//.options[document.getElementById("dateId").options.selectedIndex].text +
            "&ownerId=" + document.getElementById("ownerId").options[document.getElementById("ownerId").options.selectedIndex].value +
            "&guestsId=" + document.getElementById("guestsId").options[document.getElementById("guestsId").options.selectedIndex].value +
            "&scoreId=" + document.getElementById("scoreId").value +
            "&next_matchId=" + document.getElementById("next_matchId").value +
            "&statusId=" + document.getElementById("statusId").options[document.getElementById("statusId").options.selectedIndex].text
        action(url);
    }
}

function searchBy() {
    var url = "matches?type=find" +
        "&findType=" + document.getElementById("param").options[document.getElementById("param").options.selectedIndex].value +
        "&value=" + document.getElementById("findField").value;
    action(url);
}

function removeMatch(element) {
    if (confirm("Вы уверены, что хотите удалить эту запись?")) {
        if (element.id != null) {
            removeId = element.id;
            var url = "matches?type=removeMatch" +
                "&matchId=" + removeId;
            action(url);
        }
    }
}

function cascadeDelete() {
    var url = "matches?type=cascadeRemove" +
        "&matchId=" + removeId;
    action(url);

}

function checkFields() {
    ////проверка обязательных полей ввода
    //var touenId = document.getElementById("tournamentId");
    //
    //if (($.isNumeric(touenId.value)) && (touenId.value / Math.floor(touenId.value) == 1)) {
    //    //Целое число
        return true;
    //} else {
    //    //не целое число
    //    //выделяем ячейку
    //    //touenId.addClass("active");
    //    //$('#tournamentId').parent().addClass("active");
    //    alert("Введите целое число в поле tournamentId");
    //    return false;
    //}
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

function setOptionByName(param,title){
    var select = document.getElementById(param);
    for (var i = 0; i < select.options.length; i++) {
        var option = select.options[i];
        if(option.innerText==title) {
            select.selectedIndex = i;
        }
    }
}

function selectRow(row) {

    $(".active").removeClass();
    $(row).addClass("active");
    selectedMatch = row;
    setOptionByName("tournamentId",row.getElementsByTagName("td")[1].innerHTML);
    setOptionByName("stageId",row.getElementsByTagName("td")[0].innerHTML);
    setOptionByName("ownerId",row.getElementsByTagName("td")[3].innerHTML);
    setOptionByName("guestsId",row.getElementsByTagName("td")[4].innerHTML);
    document.getElementById("scoreId").value = row.getElementsByTagName("td")[5].innerHTML;
    document.getElementById("next_matchId").value = row.getElementsByTagName("td")[6].innerHTML;
    currentId = row.id;
}

function copyMatch() {
    // alert(selectedMatch.getElementsByTagName("td")[0].innerHTML);
    var url = "matches?type=copyMatch" +
        "&matchId=" + selectedMatch.getElementsByTagName("td")[0].innerHTML;
    action(url);
}

//Ajax

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

