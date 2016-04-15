<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактор команд</title>
    <script type="text/javascript" src="showAllTeams.js"></script>
</head>
<body onload="init()">
<p><input class="button" name="results" onclick="location.href='index.jsp';" type="button" value="Результаты">&nbsp;<input
        class="button" name="tournaments" onclick="location.href='tournament_editor.jsp';" type="button" value="Турниры">&nbsp;<input
        class="button" name="teams" onclick="location.href='team_editor.jsp';" type="button" value="Команды">&nbsp;<input
        class="button" name="matches" onclick="location.href='match_editor.jsp';" type="button" value="Матчи"></p>
<p align="center">РЕДАКТОР КОМАНД</p>
<table align="center" border="1" cellpadding="0" cellspacing="0" style="width: 50px">
    <tbody>
    <tr>
        <td>Id команды</td>
        <td>Название&nbsp;</td>
    </tr>
    <tr>
        <td><input name="team_id" type="text"></td>
        <td><input name="team_title" type="text"></td>
    </tr>
    </tbody>
</table>
<p align="center"><input name="add" type="button" value="Добавить">&nbsp;<input name="edit" type="button"
                                                                                value="Изменить"></p>

<p align="center"><input name="show_all_teams" type="button" onclick="showTable()" value="Показать все команды"></p>

<p align="center"><input name="find_txt" size="20" type="text">&nbsp;<select name="selector">
    <option value="1">id</option>
    <option selected="selected" value="2">Название</option>
</select>&nbsp;<input name="find" type="button" value="Найти"></p>
<table align="center" border="1" cellpadding="0" cellspacing="0" style="width: 350px">
    <caption>Все команды</caption>
    <thead>
    <tr>
        <td style="text-align: center;">Id Команды</td>
        <td style="text-align: center;">Название</td>
        <td style="text-align: center;">Удалить</td>
    </tr>
    </thead>
    <tbody id="tbody">
    <tr>
        <td style="text-align: center;">1</td>
        <td style="text-align: center;">Ajax</td>
        <td style="text-align: center;">X</td>
    </tr>
    <tr>
        <td style="text-align: center;">2</td>
        <td style="text-align: center;">Спартак</td>
        <td style="text-align: center;">X</td>
    </tr>
    </tbody>
</table>
<p>&nbsp;</p>

</body>
</html>