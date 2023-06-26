//package huffman;
import java.io.*;
public class Huffman
{
    public static void main(String args[])
    {
        byte bytesLeido;
        int frecuencias[]=new int[256];
        try{/*
            FileInputStream f1=new FileInputStream("./pepe.txt");
            for(int i=0;i<256;i++)
                frecuencias[i]=0;
            int leidos=f1.read();
            
            while(leidos>0)
            {
                System.out.println("--> " + leidos);
                bytesLeido=(byte)leidos;
                frecuencias[leidos]+=1;
                System.out.println((int)bytesLeido & 0xFF);
                leidos=f1.read();
            }
            System.out.println("frecuencia de a's:" + frecuencias[97]);
            System.out.println("frecuencia de b's:" + frecuencias[98]);
            System.out.println("frecuencia de c's:" +frecuencias[99]);
            */
            
            
            
            //Aplicar codigos de huffman
            
            // a = 11 b = 0 c = 10
            //11001010 10000000
            String cadena[]={"10001000","10010100","11100010","01000010","00000010"};
            byte b[]=new byte[cadena.length];
            for(int i=0;i<cadena.length;i++)
            {
                b[i]=(byte)Short.parseShort(cadena[i],2);
                System.out.println((int)b[i] & 0xFF);
                System.out.println(Integer.toBinaryString(b[i]&0xFF));
            }
            FileOutputStream f2;
            f2=new FileOutputStream("./salida.txt");
            f2.write(b);
            f2.close();
            //f1.close();
        }
        catch(IOException e)
        {
            System.out.println("error al leer o escribir archivo");
        }
    }
}
