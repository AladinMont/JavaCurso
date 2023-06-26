/*
Para este proyecto se utilizaron 4 librerias externas:
-jcommon
-jfreechart
-jgrapx
-mxgraph
Las 2 primeras se utilizaron para la creación del grafico de frecuencia
Las ultimas 2 para mostrar la vista del árbol generado por el algoritmo
*/

import org.jfree.data.category.DefaultCategoryDataset;

import java.io.*;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

/*
Primero se crea la clase HuffmanNode que es la estructura que usaremos para los nodos del árbol, contiene el simbolo y la recuencia de este
tiene como variables 2 nodos que indican cuales serian los 2 nodos siguientes de este si es que los hay
La clase implementa el "Comparable", significa que se usara la función "compareTo" para indicar orden de importancia, esto sera de utilidad para la creacion del árbol
 */
class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    String symbol;
    HuffmanNode left, right;

    public int compareTo(HuffmanNode node) {
        return Integer.compare(this.frequency, node.frequency);
    }
}

/*
Tenemos la clase "vista" que es la que contiene las funciones que realizaran el proceso de compresión,
tiene definidos globalmente los elementos visuales básicos (swing) para realizar la vista gráfica
 */
public class view extends JFrame {
    private JLabel imageLabel;
    private JButton browseButton;
    private JScrollPane scrollPane;
    private DefaultTableModel model;
    private JTable table;
    private JLabel bitsiniciales;
    private JLabel bitsf;
    private JLabel porcent;

    /*
    Primero esta la función "vista()", es la que realiza la vista inicial de los elementos, los cuales son:
    -El boton para seleccionar la imagen deseada
    -El imageLabel para poder visualizar la imagen seleccionada
    -La tabla donde se muestran los valores de los bits de la imagen, la frecuencia de aparición de estos y el código de Huffman de dicho valor
    -3 Label para poder indicar los bits iniciales del archivo, los finales y el porcentaje de compresion
     */
    public view() {
        setTitle("Códigos de Huffman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel imagen = new JPanel();
        imagen.setLayout(new BoxLayout(imagen, BoxLayout.Y_AXIS));
        imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        browseButton = new JButton("Explorar");
        browseButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel Lista = new JPanel();
        Lista.setLayout(new BoxLayout(Lista, BoxLayout.Y_AXIS));
        String[] columnNames = {"Valor", "Frecuencia", "Código"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        bitsiniciales = new JLabel();
        bitsiniciales.setAlignmentX(Component.CENTER_ALIGNMENT);
        bitsiniciales.setAlignmentY(Component.CENTER_ALIGNMENT);
        bitsf = new JLabel();
        bitsf.setAlignmentX(Component.CENTER_ALIGNMENT);
        bitsf.setAlignmentY(Component.CENTER_ALIGNMENT);
        porcent = new JLabel();
        porcent.setAlignmentX(Component.CENTER_ALIGNMENT);
        porcent.setAlignmentY(Component.CENTER_ALIGNMENT);

        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(250, 200));

        //En esta parte se le da la capacidad al boton de buscar entre los archivos del sistema para seleccionar la imagen deseada y mandarla a la función "loadAndDisplayImage()"
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(view.this);
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
        Lista.add(Box.createHorizontalGlue());
        Lista.add(scrollPane, BorderLayout.CENTER);
        Lista.add(Box.createHorizontalGlue());
        Lista.add(Box.createVerticalGlue());
        Lista.add(Box.createHorizontalGlue());
        Lista.add(bitsiniciales);
        Lista.add(Box.createHorizontalGlue());
        Lista.add(Box.createVerticalGlue());
        Lista.add(Box.createHorizontalGlue());
        Lista.add(bitsf);
        Lista.add(Box.createHorizontalGlue());
        Lista.add(Box.createVerticalGlue());
        Lista.add(Box.createHorizontalGlue());
        Lista.add(porcent);
        Lista.add(Box.createHorizontalGlue());
        Lista.add(Box.createVerticalGlue());
        add(Lista);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        scrollPane.setVisible(false);
    }

    /*
    Esta función es la principal del proyecto para realizar la compresión, se explicara como funciona dentro del código
     */
    private void loadAndDisplayImage(File file) {
        int bitsfinales = 0;
        model.setRowCount(0);
        try {
            //Primero se obtiene la imagen recibida de la llamada a funcion y se convierte en imagen tipo RGB para su lectura
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

                    // Obtener los valores de los pixel
                    int val = (rgb >> 16) & 0xFF;

                    // Guardar los valores en la matriz
                    bits[y][x] = val;

                }
            }
            //Se crea un Map para guardar la frecuencia de cada valor
            Map<String, Integer> frequencyMap = new HashMap<>();
            // Recorrer la matriz bits
            for (int[] row : bits) {
                for (int pixel : row) {
                    // Convertir los valores a una cadena para usarla como clave en el Map
                    String colorKey = pixel + "";

                    // Incrementar el recuento para el valor actual en el mapa
                    frequencyMap.put(colorKey, frequencyMap.getOrDefault(colorKey, 0) + 1);
                }
            }
            //Aquí se lama a la función "buildHuffmanTree" la cual generará el árbol
            HuffmanNode root = buildHuffmanTree(frequencyMap);
            //Este llamado a funcion es para poder visualizar el árbol de manera gráfica
            drawHuffmanTree(root);

            // Construir el map de códigos con la función "generateHuffmanCodes()"
            Map<String, String> codeMap = new HashMap<>();
            generateHuffmanCodes(root, "", codeMap);
            //Aquí se hace visible la tabla que contendra los valores antes mencionados
            scrollPane.setVisible(true);

            //Esta matriz es la que guardara los valores que se mostraran en la tabla
            String[][] rowData = new String[codeMap.size()][3];
            int i = 0;

            //Esta parte realiza el guardado de informacion del caracter y su frecuencia en un DefaultCategoryDataset (Incluido en las librerias externas) Para poder visualizar la gráfica de frecuencia
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                String colorKey = entry.getKey();
                int count = entry.getValue();
                dataset.addValue(count, "A", colorKey);
            }

            //Este llamado a función regresa un Map donde se unen los Map de frequencyMap y codeMap para poder tener una mejor visualización de los datos y evitar confuciones o errores
            Map<String, Map<String, Object>> characterDataMap = createCharacterDataMap(frequencyMap, codeMap);
            BufferedWriter writer = new BufferedWriter(new FileWriter("Datos.txt"));


            //Aquí se recorre dicho Map para realizar 2 acciones
            for (Map.Entry<String, Map<String, Object>> entry : characterDataMap.entrySet()) {
                String character = entry.getKey();
                Map<String, Object> data = entry.getValue();
                int frequency = (int) data.get("frequency");
                String huffmanCode = (String) data.get("huffmanCode");

                //Se btiene la suma total de bits despues de la compresión
                bitsfinales += frequency * huffmanCode.length();

                String Line = character+","+huffmanCode;
                writer.write(Line);
                writer.newLine();

                //Se asignan los valores de caracter, frecuencia y codigo al arreglo rowData[][] para poder visualizarlo en la tabla
                rowData[i][0] = character;
                rowData[i][1] = String.valueOf(frequency);
                rowData[i][2] = huffmanCode;
                i++;
            }
            writer.write(Integer.toString(width));
            writer.newLine();
            writer.write(Integer.toString(height));
            writer.close();

            //Esta parte realiza el visualizado de la gráfica de frecuencia de cada valor con el dataset antes llenado
            JFreeChart chart = ChartFactory.createBarChart(
                    "Cantidad de elementos repetidos", "",
                    "No. de elementos",
                    dataset, PlotOrientation.VERTICAL, false, false, true
            );

            //Aqui se hace visible dicho gráfico
            ChartFrame frame = new ChartFrame("Cantidad de elementos repetidos", chart);
            frame.pack();
            frame.setVisible(true);

            //este ciclo agrega la informacion antes guardada a la tabla para su visualización
            for (int k = 0; k < rowData.length; k++) {
                String[] finaldata = {rowData[k][0], rowData[k][1], rowData[k][2]};
                model.addRow(finaldata);
            }

            //Esta sera la cadena preeliminar que se formará con los datos ya comprimidos
            String cadena = "";

            //Se obtiene el número de bits originales de la foto y se muestran en la ventana
            int inicial = width * height * 8;
            bitsiniciales.setText(inicial + " bits iniciales");

            //Se muestrabn los bits finales despues de la compresión
            bitsf.setText(bitsfinales + " bits finales");

            //Se obtiene el porcentaje de compresión y se muestra
            float porcentaje = 100 - (((float) bitsfinales * 100) / inicial);
            porcent.setText(porcentaje + "% de reducción");

            //En este ciclo se realiza la creación de la cadena recorriendo la matriz inicial, buscando su codigo creado en el Map codeMap y concatenando el resultado en la variable cadena antes creada
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    String code = codeMap.get(Integer.toString(bits[j][k]));
                    cadena += code;
                }
            }

            //Se manda llamar la función que comprime la imagen en un archivo
            comprimirImagen(cadena);

            //Se manda llamar a la función que muestra la imagen en la ventana
            displayImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    La función recibe la cadena generada con los codigos de huffman
    Comprueba si la cadena es divisible entre 8 (que seran los bits), en caso de no ser exacta agregará 0's para completar todos los octetos
    Después divide la cadena en partes iguales con una longitud de 8
    Se crea el arreglo b[] tipo byte que es el que guardara la información que luego sera guardada en el archivo
    Se convierte los valores de las diferentes cadenas creadas a bytes y se guardan en el arreglo
    Se crea el archivo "Resultado.txt" en la ruta actual y se escribe el arreglo en el archivo
     */
    private void comprimirImagen(String cadena) {
        if (cadena.length() % 8 != 0) {
            while (cadena.length() % 8 != 0) {
                cadena += "0";
            }
        }

        String[] cad = new String[cadena.length() / 8];
        for (int i = 0; i < cadena.length() / 8; i++) {
            int inicio = i * 8;
            int fin = Math.min(inicio + 8, cadena.length());
            cad[i] = cadena.substring(inicio, fin);
        }
        try {
            byte b[] = new byte[cad.length];
            for (int i = 0; i < cad.length; i++) {
                b[i] = (byte) Short.parseShort(cad[i], 2);
            }
            FileOutputStream f2;
            f2 = new FileOutputStream("./Resultado.txt");
            f2.write(b);
            f2.close();
        } catch (IOException e) {
            System.out.println("error al leer o escribir archivo");
        }
    }

    //Recibe la imagen seleccionada anteriormente y la muestra en la ventana
    private void displayImage(BufferedImage image) {
        ImageIcon imageIcon = new ImageIcon(image);
        imageLabel.setIcon(imageIcon);
    }

    /*
    Esta función recibe el nodo raiz del árbol generado
    crea un componente gráfico para mostrar dicho arbol y lo manda a la sig. función para que realice la creación del árbol, si no hubo errores crea una nueva ventana y lo muestra
     */
    private static void drawHuffmanTree(HuffmanNode root) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();

        try {
            drawHuffmanTree(graph, parent, null, root);
        } finally {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.execute(graph.getDefaultParent());

        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(graphComponent);
        frame.pack();
        frame.setSize(1080, 720);
        frame.setVisible(true);
    }

    /*
    Esta función primero crea un vertice en el nodo inicial del árbol
    Como es el nodo inicial no crea ninguna conexión (insertEdge)
    luego si existe un nodo izquierdo hace una llamada recursiva y realiza la misma operación
    (Si esta en la primera llamada recursiva significa que ahora si tiene un vertice anterior, por lo tanto crea la conexión con insertEdge, que es el nodo actual con el anterior)
    Cuando ya no encuentre un nodo izquierdo regresará y realizará lo mismo pero con el nodo derecho
     */
    private static void drawHuffmanTree(mxGraph graph, Object parent, Object previousVertex, HuffmanNode node) {
        Object vertex = graph.insertVertex(parent, null, node.symbol + " (" + node.frequency + ")", 0, 0, 80, 30);

        if (previousVertex != null) {
            graph.insertEdge(parent, null, null, previousVertex, vertex);
        }

        if (node.left != null) {
            drawHuffmanTree(graph, parent, vertex, node.left);
        }

        if (node.right != null) {
            drawHuffmanTree(graph, parent, vertex, node.right);
        }
    }

    /*
    Esta función crea un Map que relaciona el caracter con su frecuencia y su código de huffman
    Recibe los 2 Maps creados anteriormente "frecuencyMap" y "codeMap"
    crea el Map final y entra en un ciclo
    Obtiene el caracter y la frecuencia del mapa de frecuencia, con el valor del caracter se busca en el otro Map el código de huffman
    Se crea un Map auxiliar que es el que guardará la frecuencia y el código
    Finalmente guarda en el Map final el caracter como Key y el Map auxiliar como Value y regresa el Map
     */
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

    /*
    Esta función genera el árbol
    Crea una PriorityQueue que almacena elementos del tipo HuffmanNode (La PriorityQueue funciona como cola de prioridad, ordena los elementos dependiendo, en este caso, de la función compareTo() creada en la clase HuffmanNode, coloca como mayor prioridad los elementos con menor frecuencia)
    En el primero ciclo crea Nodos de la clase HuffmanNode, se le asigna el simbolo y la frecuencia obtenida del Map frecuencyMap y se agrega a la PriorityQueue, estos datos se ordenan automaticamente
    En el segundo ciclo con la función pool() se quitan los 2 elementos con más prioridad (menor frecuencia) y se asignan a las variables left y right, con estos 2 nodos obtenidos se crea un nodo padre sumando la frecuencia y asignando cuales nodos serian los de izquierda y derecha respectivamente
    Luego el nodo padre se vuelve a asignar a la PriorityQueue para ordenar los nodos, esto se realiza hasta que la PriorityQueue sea igual a 1 (Quiere decir que contiene el nodo raiz del árbol)
    Por ultimo regresa el nodo raiz
     */
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

    /*
    Esta función recursiva genera los codigos de Huffman con el árbol antes generado
    Se recibe el nodo inicial, el codigo con el que empieza (al ser la raiz no tiene ninguno) y el Map donde se guardarán los códigos
    Si el nodo no existe reresa al nodo anterior
    Si el nodo actual es un nodo hoja (que no tiene nodos a la izquierda y la derecha) guarda el simbolo como key y el código como Value en el mapa
    Si el nodo actual tiene nodo izquierdo vuelve a llamar la función con el nodo izquierdo, se le suma el num 0 al codigo y se envia, también se envia el Map donde se guardarám los codigos
    Lo mismo ocurre si existe el nodo a la derecha
     */
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

    /*
    La función main genera la ventana de la función view() para mostrarla, se le asigna el tamaño y la posición
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            view frame = new view();
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
        });
    }
}
