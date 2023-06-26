import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Descoprimir {
    public static void main(String[] args) {
        Map<String, String> codemap = new HashMap<>();
        int width = 0;
        int height = 0;
        String fileName = "Datos.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean readingMapData = true;

            while ((line = reader.readLine()) != null) {
                if (readingMapData) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String key = parts[0];
                        String value = parts[1];
                        codemap.put(key, value);
                    } else {
                        readingMapData = false;
                        width = Integer.parseInt(line);
                    }
                } else {
                    height = Integer.parseInt(line);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[][] valores = new int[height][width];
        int j=0, k=0;
        String cadena="", aux="";
        try (FileInputStream fis = new FileInputStream("Resultado.txt")) {
            byte[] buffer = new byte[8];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                cadena += convertirBytesACadena(buffer, bytesRead);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (char caracter: cadena.toCharArray()) {
            aux += caracter;
            for (Map.Entry<String, String> entry : codemap.entrySet()) {
                if (entry.getValue().equals(aux)) {
                    valores[j][k] = Integer.parseInt(entry.getKey());
                    k++;
                    if (k == width) {
                        k = 0;
                        j++;
                    }
                    aux = "";
                    break;
                }
            }
        }
        Imagen img = new Imagen(valores, 8);
        img.escribeImagen("ImagenDescomprimida.bmp");
    }
    private static String convertirBytesACadena(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            String cadenaBinaria = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
            sb.append(cadenaBinaria);
        }

        return sb.toString();
    }
}
