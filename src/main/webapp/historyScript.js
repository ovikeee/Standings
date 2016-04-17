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
    var tmp = new Array();		// ��� ���������������
    var tmp2 = new Array();		// �������
    var param = new Array();
    var get = location.search;	// ������ GET �������
    if (get != '') {
        tmp = (get.substr(1)).split('&');	// ��������� ����������
        for (var i = 0; i < tmp.length; i++) {
            tmp2 = tmp[i].split('=');		// ������ param ����� ���������
            param[tmp2[0]] = tmp2[1];		// ���� ����(��� ����������)->��������
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
    // ������� ������ �������
    req = new XMLHttpRequest();

    // ��������� �����, ����� � �������������
    req.open("GET", url, true);

    // ��������� ������� ��� ��������� ������
    req.onreadystatechange = callback;

    // ���������� ������
    req.send(null);
}

function fillingTournament() {
    if (req.readyState == 4) {
        var tournamentSelector = document.getElementById("selectTournament");
        tournamentSelector.innerHTML = "";
        if (req.status == 200) {
            if (req.responseText == null || req.responseText == "") {
                alert("��� ������� �� ������������ �� � ����� �������");
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
            case 200: //��� ������. ������� ��������� �������
                parseMessages(req.responseText);
                break;
            case 490: //�� ��������� �� ���� ��������
                alert("������ � ������� �� �������� ������� ��������!");
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
            //������������� ������ ����
            var tmpMatch;
            var flag = false;
            for (var i = 0; i < allMatches.length; i++) {
                tmpMatch = allMatches[i];
                for (var i = 0; i < allMatches.length; i++) {
                    if (allMatches[i][6] == tmpMatch[0]) {//�������, ���� �� �����, ����������� �� tmpMatch
                        flag = true; //����
                    }
                }
                if (flag == false){
                    firstMatch = tmpMatch;
                    setMatch(firstMatch);
                    return true;
                }
                flag = false;//���������� ����
            }
        }
        return true;
    }
}

function setPrevMatch() {
    var currentMatchId = document.getElementById("matchId");
    var prevMatch;
    for (var i = 0; i < allMatches.length; i++) {
        if (allMatches[i][6] == currentMatchId) {//������� ���������� ����
            prevMatch= allMatches[i];
            setMatch(prevMatch);
        }
    }
    if (prevMatch == null) {
        alert("��� ������ ����.");
    }
}

function setNextMatch() {
    var nextMatchId = document.getElementById("nextMatchId");
    var nextMatch;
    for (var i = 0; i < allMatches.length; i++) {
        if (allMatches[i][0] == nextMatchId) {//������� ����
            nextMatch= allMatches[i];
        }
    }
    if (nextMatch != null && nextMatch!="") {
        setMatch(nextMatch);
    }else{
        alert("��� ��������� ����.");
    }
}