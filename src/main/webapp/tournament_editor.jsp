<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактор турниров</title>
    <script type="text/javascript" src="showAllTournaments.js"></script>
</head>
<body onload="init()">
<p><input name="results" onclick="location.href='index.jsp';" type="button" value="Результаты">&nbsp;<input name="tournaments" onclick="location.href='tournament_editor.jsp';" type="button" value="Турниры">&nbsp;<input name="teams" onclick="location.href='team_editor.jsp';" type="button" value="Команды">&nbsp;<input name="matches" onclick="location.href='match_editor.jsp';" type="button" value="Матчи"></p>
<p align="center">РЕДАКТОР ТУРНИРОВ</p>
<table align="center" border="1" cellpadding="1" cellspacing="1" style="width: 500px">
    <tbody>
    <tr>
        <td>Id турнира</td>
        <td>Название турнира</td>
        <td>Количество команд</td>
        <td>Сезон</td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    </tbody>
</table>
<p align="center"><input name="add" type="button" value="Добавить">&nbsp;<input name="edit" type="button" value="Изменить"></p>
<p align="center"><input name="show_tournaments" onclick="showTable()" type="button" value="Показать все турниры"></p>
<div style="text-align: center;">&nbsp;<input name="find_txt" size="20" type="text">&nbsp;<select name="selector"><option value="1">id турнира</option><option value="2">Название турнира</option><option value="3">Кол-во команд</option><option value="4">Сезон</option></select>&nbsp;<input name="find" type="button" value="Найти"></div>
<div style="text-align: center;">&nbsp;</div>
<table align="center" border="1" cellpadding="1" cellspacing="1" style="width: 500px">
    <tbody id="tbody">
    <tr>
        <td style="text-align: center;">Id турнира</td>
        <td style="text-align: center;">Название турнира</td>
        <td style="text-align: center;">Количество команд</td>
        <td style="text-align: center;">Сезон</td>
        <td style="text-align: center;">Удалить</td>
    </tr>
    <tr>
        <td style="text-align: center;">5</td>
        <td style="text-align: center;">Кубок России</td>
        <td style="text-align: center;">32</td>
        <td style="text-align: center;">2010/2011</td>
        <td style="text-align: center;">X</td>
    </tr>
    </tbody>
</table>
</body>
</html>
