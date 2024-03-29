package net.slashware.util;

import java.io.*;

public class FileUtil {

    public static int filasEnArchivo (String pArchivo) throws FileNotFoundException, IOException{
        Debug.enterMethod("", "FileUtil.filasEnArchivo", pArchivo);
        //Date starting = new Date();
        File vArchivo = new File(pArchivo);
        BufferedReader inx = new BufferedReader(new FileReader(vArchivo));
        int lines = 0;
        while (inx.readLine() != null) lines++;
        //Date end = new Date();
        //System.out.println("Comienzo: "+starting);
        //System.out.println("Fin: "+end);
        //System.out.println("Lineas: "+lines);
        inx.close();
        Debug.exitMethod(lines+"");
        return lines;
    }

    public static void deleteFile(String what){
    	new File(what).delete();
    }

    public static void copyFile(File origen, File destino) throws Exception {
        FileInputStream fis  = new FileInputStream(origen);
        FileOutputStream fos = new FileOutputStream(destino);
        byte[] buf = new byte[1024];
        int i = 0;
        while((i=fis.read(buf))!=-1) {
          fos.write(buf, 0, i);
          }
        fis.close();
        fos.close();
    }

    public static BufferedReader getReader(String fileName) throws IOException{
        Debug.enterStaticMethod("FileUtil", "getReader");
        BufferedReader x = new BufferedReader(new FileReader(fileName));
		Debug.exitMethod(x);
		return x;
	}

	public static BufferedWriter getWriter(String fileName) throws IOException{
        Debug.enterStaticMethod("FileUtil", "getWriter");
        BufferedWriter x = new BufferedWriter(new FileWriter(fileName));
		Debug.exitMethod(x);
        return x;
	}

	public static boolean fileExists(String filename) {
		return new File(filename).exists();
	}

	
}