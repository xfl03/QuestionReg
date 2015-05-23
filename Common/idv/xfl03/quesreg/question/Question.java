package idv.xfl03.quesreg.question;

import idv.xfl03.quesreg.config.ConfigTool;

import java.io.File;

public class Question {
	public String question="Nothing";
	public String a="Choose me!";
	public String b ="Nothing";
	public String c ="Nothing";
	public String d ="Nothing";
	public String key ="a";
	public int type=1;
	
	//Create A Default
	public Question(int type){
		this.type=type;
	}
	public Question(File questionFile){
		ConfigTool ct=new ConfigTool(questionFile);
		question=ct.getStringConfig("question", question);
		a=ct.getStringConfig("a", a);
		b=ct.getStringConfig("b", b);
		c=ct.getStringConfig("c", c);
		d=ct.getStringConfig("d", d);
		key=ct.getStringConfig("key", key);
		type=ct.getIntConfig("type", type);
		if(type<1||type>4){
			System.out.println(type);
			type=1;
		}
	}
}
