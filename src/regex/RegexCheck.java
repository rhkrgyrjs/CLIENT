package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCheck 
{
	private RegexCheck() {}
	
	public static boolean isId(String id)
	{
		// 아이디 : 하나 이상의 문자와 하나 이상의 숫자. 8~16자  
		final String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
	}
	
	public static boolean isNickName(String nickName)
	{
		// 닉네임 : 한글, 영어, 숫자 허용 8~16자 
		final String regex = "^[가-힣A-Za-z0-9]{8,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nickName);
        return matcher.matches();
	}

	public static boolean isPw(String pw)
	{
		// 비밀번호 : 문자 하나 이상, 숫자 하나 이상, 특수문자 하나 이상. 8~20 자 
		final String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pw);
        return matcher.matches();
	}
	
	public static boolean isEmail(String email)
	{
		final String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
	}
	
	public static boolean isPhoneNum(String phoneNum)
	{
		final String regex = "^(\\d{3})-(\\d{4})-(\\d{4})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
	}
}
