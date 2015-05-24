package idv.xfl03.quesreg.command;

public class CommandRespondSet {
	
	public boolean returnCode=false;
	public String returnText=null;
	
	public final static String CRLF="\r\n";
	public final static String CONSOLE="CONSOLE"+(Math.random()*100000);
	public final static String SPLIT_LINE="--------------------";
	
	public static final String[] SYSTEM_ERROR={"[ERROR] System ERROR."};
	public static final String[] EMPTY_RETURN_TEXT={"[ERROR] Empty return text."};
	 
	public void attendReturnText(String text){
		String CRLF=CommandRespondSet.CRLF;
		if(returnText==null){
			CRLF="";
			returnText="";
		}
		returnText+=CRLF;
		returnText+=text;
	}
	public void attendSplitLineToReturnText(){
		attendReturnText(SPLIT_LINE);
	}
	public void attendBlankLineToReturnText(){
		attendReturnText(" ");
	}
	
	public String[] getReturnText(){
		if(returnText!=null){
			return returnText.split(CommandRespondSet.CRLF);
		}else{
			return EMPTY_RETURN_TEXT;
		}
	}
	
}
