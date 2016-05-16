import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Управление таблицой matches в БД
 * CRUD операции, получение объектного представления объектов БД.
 */
public class DaoMatchPostgreSQL extends AbstractDAO<Match, Integer> {
    private class PersistMatch extends Match {
        public void setId(int id) {
            super.setId(id);
        }
    }

    public DaoMatchPostgreSQL(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    @NotNull
    //todo добавить транзакции
    public List<Match> getCascadeMatches(int id) throws PersistException {
        List<Match> list = null;
        String sql = "WITH RECURSIVE r AS ( SELECT a.* FROM matches a  WHERE id = ? UNION ALL SELECT b.* FROM matches b JOIN r  ON b.next_match_id = r.id ) SELECT * FROM r";
        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            PreparedStatement statement = connection.prepareStatement(sql);
//            ////  statement.setInt(1, id);//todo не работает?!!!
//            ResultSet rs = statement.executeQuery();
            list = jdbcTemplate.query(sql, new Object[]{id}, getMapper());
//            list = parseResultSet(rs);
//            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
            throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
        }
        return list;
    }

    @NotNull
    public List<Match> getMatches(int teamId, int tournamentId) throws PersistException {
        List<Match> list = null;
        String sql = "select * from matches where (guests_id = ? OR owner_id = ?) AND tournament_id=?";
        try {
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, teamId);
//            statement.setInt(2, teamId);
//            statement.setInt(3, tournamentId);
//            ResultSet rs = statement.executeQuery();
            list = jdbcTemplate.query(sql, new Object[]{teamId, teamId, tournamentId}, getMapper());
//            list = parseResultSet(rs);
//            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();//todo Логгер
//            try {
//                connection.rollback();
                throw new PersistException("Транзакция не выполнена");
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }

        } finally {
//            try {
//                connection.setAutoCommit(true);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//отключаем поддержку транзакций (устанавливаем по-умолчанию)
//            } catch (SQLException e) {
//                e.printStackTrace();//todo Логгер
//            }
        }
        return list;
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM matches";
    }

    @Override
    public String getInsertQuery() {
        return "insert into matches (stage, tournament_id, match_data,owner_id, guests_id, owner_id_score, guests_id_score," +
                " next_match_id, status ) values(?,?,?,?,?,?,?,?,?)";
    }

    @Override
    public PreparedStatementSetter getPSSInsertQuery(Match match) {
        final Match object = match;
        PreparedStatementSetter result = new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, object.getStage());
                preparedStatement.setInt(2, object.getTournamentId());
                preparedStatement.setDate(3, (Date) object.getMatchData());
                Checker.setInt(preparedStatement, 4, object.getOwnerId());//на случай, если значение null
                Checker.setInt(preparedStatement, 5, object.getGuestsId());
                Checker.setInt(preparedStatement, 6, object.getOwnerScore());
                Checker.setInt(preparedStatement, 7, object.getGuestsScore());
                Checker.setInt(preparedStatement, 8, object.getNextMatchId());
                preparedStatement.setString(9, object.getStatus());
            }
        };
        return result;
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE matches SET" +
                " stage =?," +
                " tournament_id =?," +
                " match_data =?," +
                " owner_id =?," +
                " guests_id =?," +
                " owner_id_score =?," +
                " guests_id_score =?," +
                " next_match_id =?," +
                " status =? WHERE id=?";
    }

    @Override
    public PreparedStatementSetter getPSSUpdateQuery(Match match) {
        final Match object = match;
        PreparedStatementSetter result = new PreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, object.getStage());
                preparedStatement.setInt(2, object.getTournamentId());
                preparedStatement.setDate(3, (Date) object.getMatchData());
                Checker.setInt(preparedStatement, 4, object.getOwnerId());//на случай, если значение null
                Checker.setInt(preparedStatement, 5, object.getGuestsId());
                Checker.setInt(preparedStatement, 6, object.getOwnerScore());
                Checker.setInt(preparedStatement, 7, object.getGuestsScore());
                Checker.setInt(preparedStatement, 8, object.getNextMatchId());
                preparedStatement.setString(9, object.getStatus());
                preparedStatement.setInt(10, object.getId());
            }
        };
        return result;
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM matches WHERE id= ?";
    }


    @Override
    public Match create() throws PersistException {
        Match g = new Match();
        return g;
    }

    @Override
    protected List<Match> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Match> result = new LinkedList();
        try {
            while (rs.next()) {
                PersistMatch match = new PersistMatch();
                match.setId(rs.getInt("id"));
                match.setStage(rs.getString("stage"));
                match.setTournamentId(rs.getInt("tournament_id"));
                match.setMatchData(rs.getDate("match_data"));
                match.setOwnerId(Checker.getInteger(rs, "owner_id"));//Integer или null
                match.setGuestsId(Checker.getInteger(rs, "guests_id"));
                match.setOwnerScore(Checker.getInteger(rs, "owner_id_score"));
                match.setGuestsScore(Checker.getInteger(rs, "guests_id_score"));
                match.setNextMatchId(Checker.getInteger(rs, "next_match_id"));
                match.setStatus(rs.getString("status"));
                result.add(match);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Match object) throws PersistException {
        try {
            statement.setString(1, object.getStage());
            statement.setInt(2, object.getTournamentId());
            statement.setDate(3, (Date) object.getMatchData());
            Checker.setInt(statement, 4, object.getOwnerId());//на случай, если значение null
            Checker.setInt(statement, 5, object.getGuestsId());
            Checker.setInt(statement, 6, object.getOwnerScore());
            Checker.setInt(statement, 7, object.getGuestsScore());
            Checker.setInt(statement, 8, object.getNextMatchId());
            statement.setString(9, object.getStatus());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Match object) throws PersistException {
        try {
            statement.setString(1, object.getStage());
            statement.setInt(2, object.getTournamentId());
            statement.setDate(3, (Date) object.getMatchData());
            Checker.setInt(statement, 4, object.getOwnerId());//на случай, если значение null
            Checker.setInt(statement, 5, object.getGuestsId());
            Checker.setInt(statement, 6, object.getOwnerScore());
            Checker.setInt(statement, 7, object.getGuestsScore());
            Checker.setInt(statement, 8, object.getNextMatchId());
            statement.setString(9, object.getStatus());
            statement.setInt(10, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected RowMapper<Match> getMapper() {
        return new MatchMapper();
    }


}