/*
Una imagen se representa como una matriz de tamaño n x m
donde cada elemento de la matriz es un pixel en la imagen.

 En el caso de imágenes en escala de grises cada pixel toma valores
 entero entre 0-255

 Suponga una imagen en escala de grises de tamaño 5 X 5. Un ejemplo de matriz
 que representa imagen es la siguiente:

	200 75 87  200 75
	129 75 87  240 240
	58  75 75  75  240
	58  58 210 210 210
	200 75 26  240 26

 Cada valor de pixel diferente sería un símbolo que codificaremos

 La tabla de frecuencias para esta matriz sería:
 PIXEL FRECUENCIA
 200 | 3
 210 |  3
 240 | 4
 75  | 7
 26  | 2
 87  | 2
 58  | 3
 129 | 1

 Suponga que al aplicar el algoritmo de Códigos de Huffman se obtienen los valores
 para cada pixel como sigue:
 200 = 001
 210 = 010
 240 = 01
 75 = 0
 26 = 111
 87 = 100
 58 = 10
 129 = 101

 El código de Huffman reduce en:

 imagen Normal 8*25 = 200 bits

 3*3 + 3*3 + 4*2 + 7*1 + 2*3 + 2*3 + 3*2 + 1*3 = 48 bits

 REDUJO 152 BITS, más del 50%

 Bits con la codificación de Huffman por cada fila
 200 75 87  200 75       => 001  0   100  001  0
 129 75 87  240 240		 => 101  0   100  01   01
 58  75 75  75  240		 => 10   0   0    0    01
 58  58 210 210 210		 => 10   10  010  010  010
 200 75 26  240 26		 => 001  0   111  01   111

 Podemos ver la codificacion como una sola cadena como sigue:

 001010000101010100010110000011010010010010001011101111

 Cada 8 bits definen un entero por ejemplo 00101000 = 40

 Para crear el archivo comprimido de la imagen definimos el siguiente formato

 int0 {SIMBOLO FRECUENCIA} int1 int2 cadena_bits

 Donde:
	int0            es el número de símbolos (pixeles diferentes) en el archivo
	SIMBOLO         es un valor de pixel
	FRECUENCIA      el número de veces que se encontró ese pixel en la imagen
	int1			es el ancho de la imagen
	int2			es el alto de la imagen
	cadena_bits		es la cadena con la codificación


 Ejemplo de la salida del archivo codificado de la imagen anterior

 Escribir en el archivo los siguientes enteros

 8 200 3 210 3 140 4 75 7 26 2 87 2 58 3 129 1 5 5

 Escribir en el archivo los siguientes bits
 00101000 01010101 00010110 00001101 00100100 10001011 101111

 (El último bloque de bits puede completarse con ceros o unos, por la información
 que define la imagen,  estos no será leídos)

 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Principal
{
	public static void main(String [] args)
	{
		//crea Imagen de prueba
		/*
		int pxl[][] = {
			{200, 75, 87,  200, 75},
			{129, 75, 87,  240, 240},
			{58,  75, 75,  75,  240},
			{58,  58, 210, 210, 210},
			{200, 25, 26,  240, 26}
		};

		Imagen img = new Imagen(pxl, 8);
		img.escribeImagen("Prueba.bmp");*/
		try {
			// Ruta de la imagen BMP
			File file = new File("Prueba.bmp");

			// Leer la imagen BMP
			BufferedImage image = ImageIO.read(file);

			// Convertir la imagen a espacio de color RGB
			BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			rgbImage.createGraphics().drawImage(image, 0, 0, null);

			// Obtener el ancho y alto de la imagen
			int width = rgbImage.getWidth();
			int height = rgbImage.getHeight();

			// Crear la matriz para almacenar los valores de los componentes de color
			int[][] colorMatrix = new int[height][width];

			// Recorrer cada píxel de la imagen
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					// Obtener el valor del píxel en formato RGB
					int rgb = rgbImage.getRGB(x, y);

					// Obtener los componentes rojo, verde y azul
					int red = (rgb >> 16) & 0xFF;
					int green = (rgb >> 8) & 0xFF;
					int blue = rgb & 0xFF;

					// Guardar los valores en la matriz
					colorMatrix[y][x] = red;

				}
			}

			// Imprimir la matriz de valores de los componentes de color
			/*for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					System.out.print(colorMatrix[y][x]+"\t");
				}
				System.out.println();
			}*/
			Map<String, Integer> countMap = new HashMap<>();

			// Recorrer la matriz colorMatrix
			for (int[] row : colorMatrix) {
				for (int pixel : row) {
					// Convertir los valores de los componentes de color a una cadena para usarla como clave en el mapa
					String colorKey = pixel+"";

					// Incrementar el recuento para el valor actual en el mapa
					countMap.put(colorKey, countMap.getOrDefault(colorKey, 0) + 1);
				}
			}

			// Mostrar los resultados
			for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
				String colorKey = entry.getKey();
				int count = entry.getValue();
				System.out.println("Color: " + colorKey + " - Count: " + count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		/*
		// H
		for(int i = 0; i < ch[].length; i++)
			for (int j = 0; j < 20; j++)
				pxl3[i][j] = 255;


		pxl3[i][j] = valor.nextInt(255);

		Imagen img2 = new Imagen(pxl2, 8);
		img2.escribeImagen("Prueba2.bmp");
		*/

		//APLICAR CODIGOS DE HUFFMAN
		/*AQUI VA TU CODIGO*/

		//Crear archivo escribiendo la cadena con la codificación de Huffman


	}
}


