import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;

/*class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    char symbol;
    HuffmanNode left, right;

    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}

public class HuffmanCoding {
    public static void main(String[] args) {
        String message = "ACAAGATGCCATTGTCCCCCGGCCTCCTGCTGCTGCTGCTCTCCGGGGCCACGGCCACCGCTGCCCTGCCCCTGGAGGGTGGCCCCACCGGCCGAGACAGCGAGCATATGCAGGAAGCGGCAGGAATAAGGAAAAGCAGCCTCCTGACTTTCCTCGCTTGGTGGTTTGAGTGGACCTCCCAGGCCAGTGCCGGGCCCCTCATAGGAGAGGAAGCTCGGGAGGTGGCCAGGCGGCAGGAAGGCGCACCCCCCCAGCAATCCGCGCGCCGGGACAGAATGCCCTGCAGGAACTTCTTCTGGAAGACCTTCTCCTCCTGCAAATAAAACCTCACCCATGAATGCTCACGCAAGTTTAATTACAGACCTGAAACAAGATGCCATTGTCCCCCGGCCTCCTGCTGCTGCTGCTCTCCGGGGCCACGGCCACCGCTGCCCTGCCCCTGGAGGGTGGCCCCACCGGCCGAGACAGCGAGCATATGCAGGAAGCGGCAGGAATAAGGAAAAGCAGCCTCCTGACTTTCCTCGCTTGGTGGTTTGAGTGGACCTCCCAGGCCAGTGCCGGGCCCCTCATAGGAGAGGAAGCTCGGGAGGTGGCCAGGCGGCAGGAAGGCGCACCCCCCCAGCAATCCGCGCGCCGGGACAGAATGCCCTGCAGGAACTTCTTCTGGAAGACCTTCTCCTCCTGCAAATAAAACCTCACCCATGAATGCTCACGCAAGTTTAATTACAGACCTGAAGACTTTTTTTTTTTTTCCTTTGGATTACAGACCTGAA";
        Map<Character, Integer> frequencyMap = new HashMap<>();

        // Calcular la frecuencia de aparición de cada símbolo en el mensaje
        for (char c : message.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Construir el árbol de Huffman
        HuffmanNode root = buildHuffmanTree(frequencyMap);

        // Construir el mapa de códigos
        Map<Character, String> codeMap = new HashMap<>();
        generateHuffmanCodes(root, "", codeMap);

        // Imprimir los códigos de Huffman
        for (Map.Entry<Character, String> entry : codeMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        // Crear nodos iniciales para cada símbolo y su frecuencia
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
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

    private static void generateHuffmanCodes(HuffmanNode node, String code, Map<Character, String> codeMap) {
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
}*/
