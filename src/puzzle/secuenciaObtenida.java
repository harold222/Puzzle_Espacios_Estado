package puzzle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class secuenciaObtenida {
    
    private ArrayList list;
 
    public secuenciaObtenida() {
        list = new ArrayList();
    }
    
    public ArrayList getSecuencia(){
        return list;
    }
    
    public void setSecuencia(String a){
       this.list.add(a);
    }

    public void dibujar() throws IOException{
        Iterator iter = list.iterator();
        int a [] = null;
        
        int cont = 0;
        while (iter.hasNext()){
          
            String ruta = "C:\\Temp\\secuencia.txt";
            File archivo = new File(ruta);
            BufferedWriter bw;
            
            if(archivo.exists()) {
                bw = new BufferedWriter(new FileWriter(archivo,true));
                bw.write(iter.next().toString()+"\n");
            } else {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write("");
            }
            
            bw.close();
            cont++;
        }
        
    }
}
