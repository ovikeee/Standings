<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> Редактор матчей </title>

    <script type="text/JavaScript" src="showAllMatches.js"></script>
    <%--    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>--%>
    <script type="text/javascript" src="jquery-1.12.2.min.js"></script>
    <link rel="stylesheet" href="style.css">
</head>
<body onload="start()">
<form>
    <p><input name="results" onclick="location.href='index.jsp';" type="button" value="Результаты">&nbsp;<input name="tournaments" onclick="location.href='tournament_editor.jsp';" type="button" value="Турниры">&nbsp;<input name="teams" onclick="location.href='team_editor.jsp';" type="button" value="Команды">&nbsp;<input name="matches" onclick="location.href='match_editor.jsp';" type="button" value="Матчи"></p>
    <table align="center" border="1" cellpadding="0" cellspacing="0" class="clickable" style="width: 700px">
        <caption>Заполните обязательные поля и добавьте матч.<br>
            &nbsp;</caption>
        <thead>
        <tr>
            <th bordercolor="red" scope="col">id матча</th>
            <th scope="col">id турнира *</th>
            <th scope="col">стадия турнира</th>
            <th scope="col">дата матча *</th>
            <th scope="col">хозяева</th>
            <th scope="col">гости</th>
            <th scope="col">
                <p>счёт</p>
            </th>
            <th scope="col">следующий матч</th>
            <th scope="col">статус игры</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><input id="match_id" name="match_id" size="3" type="text"></td>
            <td><input id="tournamentId" name="tournament_id" size="4" type="text"></td>
            <td><select id="stageId" name="stage"><option value="1">финал</option><option value="2">1/2</option><option value="4">1/4</option><option value="8">1/8</option></select></td>
            <td><select id="dateId" name="date"><option value="1">02-04-2014 23:37:50</option><option value="2">02-04-2014 23:37:50</option></select></td>
            <td><select id="ownerId" name="owner"><option value="1">Анжи</option><option value="2">2</option><option value="3">3</option><option value="4">Спартак</option><option selected="selected" value="0"></option></select></td>
            <td><select id="guestsId" name="guests"><option value="1">Анжи</option><option value="2">2</option><option value="3">3</option><option value="4">Спартак</option><option selected="selected" value="0"></option></select></td>
            <td><input id="scoreId" name="score" size="5" type="text"></td>
            <td><input id="next_matchId" name="next_match" size="7" type="text"></td>
            <td><select id="statusId" name="status"><option value="1">завершен</option><option value="2">ожидается</option><option value="3">отменен</option><option value="4">тех. поражение</option></select></td>
        </tr>
        </tbody>
    </table>
    <p align="center"><input name="add" onclick="addMatch()" type="button" value="Добавить матч"> <input name="edit" onclick="editMatch()" type="button" value="Изменить">&nbsp;<input name="copy" id="copy" onclick="copyMatch()" type="button" value="Копировать"></p>
    <p align="center"><input name="show_matches" onclick="showTable()" type="button" value="Показать все матчи"></p>
    <p align="center" id="textInfo">Список всех матчей</p>
    <table align="center" border="1" cellpadding="0" cellspacing="0" style="width: 700px">
        <caption><input id="findField" maxlength="24" name="findField" size="25" type="text"> <select id="param" name="param"><option value="id">по id матча</option><option value="stage">по стадии</option><option value="match_data">по дате</option><option value="owner_id">по id хозяев</option><option value="guests_id">по id гостей</option><option value="status">по статусу матча</option><option selected="selected" value="tournament_id">по id турнира</option></select> <input name="find" onclick="searchBy()" type="button" value="Найти"></caption>
        <thead>
        <tr>
            <th scope="col">id матча</th>
            <th scope="col">стадия турнира</th>
            <th scope="col">id турнира</th>
            <th scope="col">дата матча</th>
            <th scope="col">хозяева</th>
            <th scope="col">гости</th>
            <th scope="col">счёт</th>
            <th scope="col">следующий матч</th>
            <th scope="col">статус игры</th>
            <th scope="col">удалить</th>
        </tr>
        </thead>
        <tbody id="tbody_id">
        </tbody>
    </table>
</form>
</body>
</html>

