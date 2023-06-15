import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    String symbol;
    HuffmanNode left, right;

    public int compareTo(HuffmanNode node) {
        return Integer.compare(this.frequency, node.frequency);
    }
}

public class vista extends JFrame {
    private JLabel imageLabel;
    private JButton browseButton;

    private JScrollPane scrollPane;
    private DefaultTableModel model;
    private JTable table;


    public vista() {
        setTitle("Image Input Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel imagen =new JPanel();
        imagen.setLayout(new BoxLayout(imagen, BoxLayout.Y_AXIS));
        imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        browseButton = new JButton("Browse");
        browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel Lista = new JPanel();
        Lista.setLayout(new FlowLayout(FlowLayout.CENTER));

        String[] columnNames = {"Valor", "Frecuencia", "Código"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);


        // Agregar la tabla a un panel de desplazamiento
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(250, 200));


        //scrollPane = new JScrollPane(listView);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(vista.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    loadAndDisplayImage(selectedFile);
                }
            }
        });
        imagen.add(Box.createVerticalStrut(10));
        imagen.add(browseButton);
        imagen.add(Box.createVerticalStrut(10));
        imagen.add(imageLabel);
        imagen.add(Box.createVerticalStrut(10));
        add(imagen);

        Lista.add(scrollPane, BorderLayout.CENTER);
        add(Lista);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        scrollPane.setVisible(true);
    }

    private void loadAndDisplayImage(File file) {
        model.setRowCount(0);
        try {
            BufferedImage image = ImageIO.read(file);
            BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgbImage.createGraphics().drawImage(image, 0, 0, null);

            // Obtener el ancho y alto de la imagen
            int width = rgbImage.getWidth();
            int height = rgbImage.getHeight();

            // Crear la matriz para almacenar los valores de los componentes de color
            int[][] bits = new int[height][width];

            // Recorrer cada píxel de la imagen
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Obtener el valor del píxel en formato RGB
                    int rgb = rgbImage.getRGB(x, y);

                    // Obtener los componentes rojo
                    int red = (rgb >> 16) & 0xFF;

                    // Guardar los valores en la matriz
                    bits[y][x] = red;

                }
            }

            // Imprimir la matriz de valores de los componentes de color
            /*for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    System.out.print(bits[y][x]+"\t");
                }
                System.out.println();
            }*/
            Map<String, Integer> frequencyMap = new HashMap<>();

            // Recorrer la matriz bits
            for (int[] row : bits) {
                for (int pixel : row) {
                    // Convertir los valores de los componentes de color a una cadena para usarla como clave en el mapa
                    String colorKey = pixel + "";

                    // Incrementar el recuento para el valor actual en el mapa
                    frequencyMap.put(colorKey, frequencyMap.getOrDefault(colorKey, 0) + 1);
                }
            }
            HuffmanNode root = buildHuffmanTree(frequencyMap);

            // Construir el mapa de códigos
            Map<String, String> codeMap = new HashMap<>();
            generateHuffmanCodes(root, "", codeMap);

            //System.out.println("Árbol de Huffman:");
            //printHuffmanTree(root, "");

            // Imprimir los códigos de Huffman

            scrollPane.setVisible(true);
            String[][] rowData= new String[codeMap.size()][3];
            int i=0;
            /*for (Map.Entry<String, String> entry : codeMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }*/

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                String colorKey = entry.getKey();
                int count = entry.getValue();
                dataset.addValue(count, "A", colorKey);

            }
            Map<String, Map<String, Object>> characterDataMap = createCharacterDataMap(frequencyMap, codeMap);
            System.out.println("Character Data:");
            for (Map.Entry<String, Map<String, Object>> entry : characterDataMap.entrySet()) {
                String character = entry.getKey();
                Map<String, Object> data = entry.getValue();
                int frequency = (int) data.get("frequency");
                String huffmanCode = (String) data.get("huffmanCode");
                //System.out.println(character + ": " + frequency + ", " + huffmanCode);
                rowData[i][0] = character;
                rowData[i][1] = String.valueOf(frequency);
                rowData[i][2] = huffmanCode;
                i++;
            }
            JFreeChart chart = ChartFactory.createBarChart(
                    "Cantidad de elementos repetidos","",
                    "No. de elementos",
                    dataset, PlotOrientation.VERTICAL,  false,false,true
            );
            ChartFrame frame = new ChartFrame("Cantidad de elementos repetidos", chart);
            frame.pack();
            frame.setVisible(true);
            for (int k = 0; k < rowData.length; k++) {
                String[] finaldata = {rowData[k][0], rowData[k][1], rowData[k][2]};
                model.addRow(finaldata);
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

    private static Map<String, Map<String, Object>> createCharacterDataMap(Map<String, Integer> frequencyMap, Map<String, String> codes) {
        Map<String, Map<String, Object>> characterDataMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String character = entry.getKey();
            int frequency = entry.getValue();
            String huffmanCode = codes.get(character);

            Map<String, Object> data = new HashMap<>();
            data.put("frequency", frequency);
            data.put("huffmanCode", huffmanCode);

            characterDataMap.put(character, data);
        }
        return characterDataMap;
    }

    private static HuffmanNode buildHuffmanTree(Map<String, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        // Crear nodos iniciales para cada símbolo y su frecuencia
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.symbol = entry.getKey();
            node.frequency = entry.getValue();
            priorityQueue.add(node);
        }

        // Combinar nodos hasta que solo quede un nodo en la cola de prioridad
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();

            HuffmanNode parent = new HuffmanNode();
            parent.frequency = left.frequency + right.frequency;
            parent.left = left;
            parent.right = right;

            priorityQueue.add(parent);
        }

        // Devolver el nodo raíz del árbol de Huffman
        return priorityQueue.poll();
    }

    private static void generateHuffmanCodes(HuffmanNode node, String code, Map<String, String> codeMap) {
        if (node == null) {
            return;
        }

        // Si es un nodo hoja, almacenar su código
        if (node.left == null && node.right == null) {
            codeMap.put(node.symbol, code);
        }

        // Recorrer el árbol recursivamente para generar los códigos
        generateHuffmanCodes(node.left, code + "0", codeMap);
        generateHuffmanCodes(node.right, code + "1", codeMap);
    }

    /*private static void printHuffmanTree(HuffmanNode node, String indent) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            System.out.println(indent + "Símbolo: " + node.symbol);
        } else {
            System.out.println(indent + "Frecuencia: " + node.frequency);
            System.out.println(indent + "├─ Izquierda:");
            printHuffmanTree(node.left, indent + "│  ");
            System.out.println(indent + "└─ Derecha:");
            printHuffmanTree(node.right, indent + "   ");
        }
    }*/

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            vista frame = new vista();
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
        });
    }
}
