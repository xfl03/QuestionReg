package idv.xfl03.quesreg.command;

public class CommandSet {
	
	public String sender="";
	public boolean returnCode=false;
	public String[] args=null;
	public String returnText=null;
	
	public final static String CRLF="\r\n";
	public final static String CONSOLE="CONSOLE"+(Math.random()*100000);
	
	public static final String[] SYSTEM_ERROR={"[ERROR] System ERROR."};
	public static final String[] EMPTY_RETURN_TEXT={"[ERROR] Empty return text."};
	
	public void attendReturnText(String text){
		String CRLF=CommandSet.CRLF;
		if(returnText==null){
			CRLF="";
			returnText="";
		}
		returnText+=CRLF;
		returnText+=text;
	}
	
	public String[] getReturnText(){
		if(returnText!=null){
			return returnText.split(CommandSet.CRLF);
		}else{
			return EMPTY_RETURN_TEXT;
		}
	}
	
}
