<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> Редактор матчей </title>

    <script type="text/JavaScript" src="showAllMatches.js"></script>
    <%--    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>--%>
    <script type="text/javascript" src="jquery-1.12.2.min.js"></script>


 <%-- <link rel="stylesheet" href="style.css"> --%>
</head>
<form action="">
<body onload="init()">
<p><input name="results" onclick="location.href='index.jsp';" type="button" value="Результаты">&nbsp;<input name="tournaments" onclick="location.href='tournament_editor.jsp';" type="button" value="Турниры">&nbsp;<input name="teams" onclick="location.href='team_editor.jsp';" type="button" value="Команды">&nbsp;<input name="matches" onclick="location.href='match_editor.jsp';" type="button" value="Матчи"></p>
<table align="center" border="1" cellpadding="0" cellspacing="0" style="width: 700px">
    <caption>Заполните обязательные поля и добавьте матч.<br>
        &nbsp;</caption>
    <thead>
    <tr>
        <th scope="col">id матча</th>
        <th scope="col">id турнира</th>
        <th scope="col">стадия</th>
        <th scope="col">дата матча</th>
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
        <td style="text-align: center;">&nbsp;</td>
        <td style="text-align: center;"><input name="tournament_id" id="tournamentId" size="3" type="text"></td>
        <td style="text-align: center;"><select name="stage" id="stageId"><option value="1">финал</option><option value="2">1/2</option><option value="4">1/4</option><option value="8">1/8</option></select></td>
        <td style="text-align: center;"><select name="date" id="dateId"><option value="1">02-04-2014 23:37:50</option><option value="2">02-04-2014 23:37:50</option></select></td>
        <td style="text-align: center;"><select name="owner" id="ownerId"><option value="1">Анжи</option><option value="2">2</option><option value="3">3</option><option value="4">Спартак</option><option selected="selected" value="0"></option></select></td>
        <td style="text-align: center;"><select name="guests" id="guestsId"><option value="1">Анжи</option><option value="2">2</option><option value="3">3</option><option value="4">Спартак</option><option selected="selected" value="0"></option></select></td>
        <td style="text-align: center;"><input name="score" id="scoreId" size="5" type="text"></td>
        <td style="text-align: center;"><input name="next_match" id="next_matchId" size="7" type="text"></td>
        <td style="text-align: center;"><select name="status" id="statusId"><option value="1">завершен</option><option value="2">ожидается</option><option value="3">отменен</option><option value="4">тех. поражение</option></select></td>
    </tr>
    </tbody>
</table>
<p align="center"><input name="add" type="button" onclick="addMatch()" value="Добавить матч"> <input name="edit" type="button" value="Изменить"></p>
<p align="center"><input name="show_matches" onclick="showTable()" type="button" value="Показать все матчи"></p>
<table align="center" border="1" cellpadding="0" cellspacing="0" style="width: 700px">
    <caption><input maxlength="24" name="find" size="25" type="text"> <select name="param"><option value="1">id матча</option><option value="2">стадия</option><option value="3">дата</option><option value="4">команда</option><option value="5">статус матча</option><option selected="selected" value="0">id турнира</option></select> <input name="find" type="button" value="Найти"><br>
        <br>
        Турнирная таблица: Кубок России</caption>
    <thead>
    <tr>
        <th scope="col">id матча</th>
        <th scope="col">id турнира</th>
        <th scope="col">стадия</th>
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
    <tr>
        <td style="text-align: center;">4</td>
        <td style="text-align: center;">4</td>
        <td style="text-align: center;">1/4</td>
        <td style="text-align: center;">2015-06-15</td>
        <td style="text-align: center;">Ростов</td>
        <td style="text-align: center;">ЦСКА</td>
        <td style="text-align: center;">2-0</td>
        <td style="text-align: center;"><a href="http://google.com" target="_blank">Ростов-Динамо</a></td>
        <td style="text-align: center;">завершен</td>
        <td style="text-align: center;"><a value=10 onclick="removeMatch(this)">Х</a></td>
    </tr>
    <tr >
        <td style="text-align: center;">5</td>
        <td style="text-align: center;">4</td>
        <td style="text-align: center;">1/4</td>
        <td style="text-align: center;">2015-06-15</td>
        <td style="text-align: center;">Динамо</td>
        <td style="text-align: center;">Анжи</td>
        <td style="text-align: center;">2-1</td>
        <td style="text-align: center;"><a href="http://google.com" target="_blank">Ростов-Динамо</a></td>
        <td style="text-align: center;">завершен</td>
        <td style="text-align: center;">Х</td>
    </tr>
    </tbody>
</table>
<input type="button" name="myButton" id="myButtonId" onclick=" myAction()" value="button" />
</body>
</form>
</html>