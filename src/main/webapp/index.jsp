<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="showAllMatches.js"></script>
</head>
<body onload="init()">
<form action="">
    <p><input name="results" onclick="location.href='index.jsp';" type="button" value="Результаты">&nbsp;<input
            name="tournaments" onclick="location.href='tournament_editor.jsp';" type="button"
            value="Турниры">&nbsp;<input name="teams" onclick="location.href='team_editor.jsp';" type="button"
                                         value="Команды">&nbsp;<input name="matches"
                                                                      onclick="location.href='match_editor.jsp';"
                                                                      type="button" value="Матчи"></p>
    <table align="center" border="0" cellpadding="0" cellspacing="0">
        <tbody>
        <tr>
            <td style="text-align: center;">Выберите турнир</td>
            <td><select id="tournaments-selector" name="choice">
                <option value="1">La Liga</option>
                <option value="2">Cup of America</option>
                <option selected="selected" value="3">ЧМ 2018</option>
                <option value="4">Russian Cup</option>
                <option value=""></option>
            </select></td>
        </tr>
        <tr>
            <td>Выберете сезон</td>
            <td><select id="season-selector">
                <option value="1">2015</option>
                <option value="2">2020</option>
                <option selected="selected" value="3">2018</option>
                <option value="4">2011/2012</option>
                <option value="">2015/2016</option>
            </select></td>
        </tr>
        </tbody>
    </table>
    <table id="matches-table" align="center" border="1" cellpadding="0" cellspacing="0" style="width: 700px">
        <caption><br>
            <input name="show" type="button" onclick="showTable()" value="Показать турнирную таблицу"><br>
            <br>
            <input maxlength="24" name="find" size="25" type="text"> <select name="param">
                <option value="1">id матча</option>
                <option selected="selected" value="2">стадия</option>
                <option value="3">дата</option>
                <option value="4">команда</option>
                <option value="5">статус матча</option>
            </select> <input name="find_match" type="submit" id="find_match" value="Найти"><br>
            <br>
            Турнирная таблица: Кубок России
        </caption>
        <thead>
        <tr>
            <th scope="col">id матча</th>
            <th scope="col">стадия</th>
            <th scope="col">дата матча</th>
            <th scope="col">хозяева</th>
            <th scope="col">гости</th>
            <th scope="col">счёт</th>
            <th scope="col">следующий матч</th>
            <th scope="col">статус игры</th>
        </tr>
        </thead>
        <tbody id="tbody_id">
        <tr>
            <td style="text-align: center;">4</td>
            <td style="text-align: center;">1/4</td>
            <td style="text-align: center;">2015-06-15</td>
            <td style="text-align: center;">Ростов</td>
            <td style="text-align: center;">ЦСКА</td>
            <td style="text-align: center;">2-0</td>
            <td style="text-align: center;"><a href="http://google.com" target="_blank">Ростов-Динамо</a></td>
            <td style="text-align: center;">завершен</td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>