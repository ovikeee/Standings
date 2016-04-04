import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 03.04.2016.
 */
public class Checker {

    public static Integer isInt(String str) {
        Integer result = null;
        try {
            result = Integer.parseInt(str);
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String notEmpty(String checkStr){
        if (checkStr != "" || checkStr != null) {
           return checkStr;
        }
        return null;
    }

    public static Integer getInteger(ResultSet rs, String strColName) throws SQLException {
        int nValue = rs.getInt(strColName);
        return rs.wasNull() ? null : nValue;
    }

    public static void setInt(PreparedStatement statement,int id, Integer val ) throws SQLException {
        if (val != null) {
            statement.setInt(id, val);
        } else {
            statement.setNull(id, java.sql.Types.INTEGER);
        }
    }

    public static void setStr(PreparedStatement statement,int id, String val ) throws SQLException {
        if (val != null) {
            statement.setString(id, val);
        } else {
            statement.setString(id, null);
        }
    }
}
