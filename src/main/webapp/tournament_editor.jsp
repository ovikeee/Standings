<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактор турниров</title>
    <script type="text/javascript" src="showAllTournaments.js"></script>
    <script type="text/javascript" src="jquery-1.12.2.min.js"></script>
    <link rel="stylesheet" href="style.css">
</head>
<body onload="init()">
<p><input class="button" name="results" onclick="location.href='index.jsp';" type="button"
          value="Результаты">&nbsp;<input
        class="button" name="tournaments" onclick="location.href='tournament_editor.jsp';" type="button"
        value="Турниры">&nbsp;<input
        class="button" name="teams" onclick="location.href='team_editor.jsp';" type="button"
        value="Команды">&nbsp;<input
        class="button" name="matches" onclick="location.href='match_editor.jsp';" type="button" value="Матчи"></p>
<p align="center">
    РЕДАКТОР ТУРНИРОВ</p>
<table align="center" border="1" cellpadding="1" cellspacing="1" style="width: 500px">
    <thead>
    <tr>
        <th scope="col">Id турнира</th>
        <th scope="col">Название турнира</th>
        <th scope="col">Количество команд</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td><input name="tournamentId" id="tournamentId" type="text"></td>
        <td><input name="tournamentTitle" id="tournamentTitle" type="text"></td>
        <td><input name="teamNumber" id="teamNumber" type="text"></td>
    </tr>
    </tbody>
</table>
<p align="center"><input name="add" onclick="createTournament()" type="button" value="Создать">&nbsp;<input name="edit"
                                                                                                            onclick="editTournament()"
                                                                                                            type="button"
                                                                                                            value="Изменить">
</p>

<p align="center"><input name="show_tournaments" onclick="showTable()" type="button" value="Показать все турниры"></p>

<div style="text-align: center;">&nbsp;<input id="findField" name="findField" size="20" type="text">&nbsp;<select
        id="param" name="selector">
    <option value="id">id турнира</option>
    <option value="title">Название турнира</option>
    <option value="number_of_teams">Кол-во команд</option>
</select>&nbsp;<input name="find" onclick="searchBy()" type="button" value="Найти"></div>
<div style="text-align: center;">&nbsp;</div>
<table align="center" border="1" cellpadding="1" cellspacing="1" style="width: 500px">
    <thead>
    <tr>
        <th scope="col" style="text-align: center;">Id турнира</th>
        <th scope="col" style="text-align: center;">Название турнира</th>
        <th scope="col" style="text-align: center;">Количество команд</th>
        <th scope="col" style="text-align: center;">Удалить</th>
    </tr>
    </thead>
    <tbody id="tbody">
    <tr>
        <td style="text-align: center;">5</td>
        <td style="text-align: center;">Кубок России</td>
        <td style="text-align: center;">32</td>
        <td style="text-align: center;">X</td>
    </tr>
    </tbody>
</table>
</body>
</html>
