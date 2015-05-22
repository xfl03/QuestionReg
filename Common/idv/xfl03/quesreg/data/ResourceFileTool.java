package idv.xfl03.quesreg.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceFileTool {
	
	private InputStream getResourceFileStream(String fileName){
		return this.getClass().getResourceAsStream(fileName);
	}
	
	public void resourceFileCopy(String name, File to) {
		InputStream is = null;
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		try {
			is=getResourceFileStream(name);
			bis =new BufferedInputStream(is);
			bos=new BufferedOutputStream(new FileOutputStream(to));
			
	        byte[] b=new byte[2048];
	        int i=0;
	        while((i=bis.read(b))!=-1){
	        	bos.write(b, 0, i);
	        }
	        bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				is.close();
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
