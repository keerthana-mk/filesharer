package mk.learning.fileshare.constants;

public class Queries {

	public static String AUTH_QUERY = "select * from FileSharer_UserId where Username=? and password=?";

	public static String FUNC_QUERY = "select func from FileSharer_UserId where Username=?";
}
