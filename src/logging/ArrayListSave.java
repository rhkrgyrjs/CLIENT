package logging;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import form.GameBoardInfoForm;

public class ArrayListSave 
{
	public static void saveArrayListToFile(ArrayList<GameBoardInfoForm> toSave)
	{
		// 게임 로그를 저장함. 위치는 CLIENT/myGames/[날짜시간분].hglog 
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("myGames/" + geTimeString() + ".hglog")))
	    {
	        oos.writeObject(toSave);
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	private static ArrayList<GameBoardInfoForm> loadArrayListFromFile(String filePath) 
	{
        ArrayList<GameBoardInfoForm> loadedArrayList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) 
        {
            loadedArrayList = (ArrayList<GameBoardInfoForm>) ois.readObject();
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
        return loadedArrayList;
    }
	
	public static String geTimeString() 
	{// 현재 날짜와 시간 정보 가져오기
        LocalDateTime now = LocalDateTime.now();

        // DateTimeFormatter를 사용하여 원하는 형식으로 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // 포맷에 맞게 날짜와 시간을 문자열로 변환
        String formattedDateTime = now.format(formatter);

        return formattedDateTime;
    }
}
