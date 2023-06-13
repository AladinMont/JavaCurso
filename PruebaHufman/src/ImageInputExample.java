import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageInputExample extends JFrame {
    private JLabel imageLabel;
    private JButton browseButton;
    private int[][] colorMatrix;

    public ImageInputExample() {
        setTitle("Image Input Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        imageLabel = new JLabel();
        browseButton = new JButton("Browse");

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(ImageInputExample.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    loadAndDisplayImage(selectedFile);
                }
            }
        });

        add(imageLabel);
        add(browseButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadAndDisplayImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
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

                    // Obtener los componentes rojo
                    int red = (rgb >> 16) & 0xFF;

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
                    String colorKey = pixel + "";

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
            // Contar valores
            displayImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayImage(BufferedImage image) {
        ImageIcon imageIcon = new ImageIcon(image);
        imageLabel.setIcon(imageIcon);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageInputExample();
            }
        });
    }
}
