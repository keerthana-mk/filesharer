package mk.learning.fileshare.constants;

public class Queries {

	public static String AUTH_QUERY = "select * from FileSharer_UserId where Username=? and password=?";

	public static String FUNC_QUERY = "select func from FileSharer_UserId where Username=?";

	public static String ENCRYPT_QUERY = "SELECT PASSWORD FROM FileSharer_UserId WHERE USERNAME=?";

	public static String INSERT_SQL = "INSERT INTO FileSharer_UserId values(?,?,?,?,?,?)";

	public static String FIRST_LOGIN_QUERY = "select firstloginflag from FileSharer_UserId where username=?";

	public static String UPDATE_PASSWORD_QUERY = "update FileSharer_UserId set password=?,firstloginflag=?,create_date=?,expiry_date=? where username=?";

	public static String DELETE_USER_QUERY = "delete from FileSharer_UserId where username=?";

	public static String UPDATE_PASSWORD_EXPIRY = "update FileSharer_UserId set firstloginflag=-1 where Username=? and to_date(expiry_date)<?";
}
