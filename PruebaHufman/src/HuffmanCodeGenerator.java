import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import java.util.*;

public class HuffmanCodeGenerator {
    private static class HuffmanNode {
        char symbol;
        int frequency;
        HuffmanNode left, right;

        HuffmanNode(char data, int frequency) {
            this.symbol = data;
            this.frequency = frequency;
        }
    }

    private static class HuffmanComparator implements Comparator<HuffmanNode> {
        public int compare(HuffmanNode node1, HuffmanNode node2) {
            return node1.frequency - node2.frequency;
        }
    }

    public static void main(String[] args) {
        String text = "ACAAGATGCCATTGTCCCCCGGCCTCCTGCTGCTGCTGCTCTCCGGGGCCACGGCCACCGCTGCCCTGCCCCTGGAGGGTGGCCCCACCGGCCGAGACAGCGAGCATATGCAGGAAGCGGCAGGAATAAGGAAAAGCAGCCTCCTGACTTTCCTCGCTTGGTGGTTTGAGTGGACCTCCCAGGCCAGTGCCGGGCCCCTCATAGGAGAGGAAGCTCGGGAGGTGGCCAGGCGGCAGGAAGGCGCACCCCCCCAGCAATCCGCGCGCCGGGACAGAATGCCCTGCAGGAACTTCTTCTGGAAGACCTTCTCCTCCTGCAAATAAAACCTCACCCATGAATGCTCACGCAAGTTTAATTACAGACCTGAAACAAGATGCCATTGTCCCCCGGCCTCCTGCTGCTGCTGCTCTCCGGGGCCACGGCCACCGCTGCCCTGCCCCTGGAGGGTGGCCCCACCGGCCGAGACAGCGAGCATATGCAGGAAGCGGCAGGAATAAGGAAAAGCAGCCTCCTGACTTTCCTCGCTTGGTGGTTTGAGTGGACCTCCCAGGCCAGTGCCGGGCCCCTCATAGGAGAGGAAGCTCGGGAGGTGGCCAGGCGGCAGGAAGGCGCACCCCCCCAGCAATCCGCGCGCCGGGACAGAATGCCCTGCAGGAACTTCTTCTGGAAGACCTTCTCCTCCTGCAAATAAAACCTCACCCATGAATGCTCACGCAAGTTTAATTACAGACCTGAAGACTTTTTTTTTTTTTCCTTTGGATTACAGACCTGAA";

        Map<Character, Integer> frequencyMap = getFrequencyMap(text);
        HuffmanNode root = buildHuffmanTree(frequencyMap);

        Map<Character, String> codes = generateHuffmanCodes(root);
        System.out.println("Huffman Codes:");
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        drawHuffmanTree(root);
    }

    private static Map<Character, Integer> getFrequencyMap(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    private static HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(new HuffmanComparator());

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();

            HuffmanNode newNode = new HuffmanNode('\0', left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;

            priorityQueue.offer(newNode);
        }

        return priorityQueue.poll();
    }

    private static Map<Character, String> generateHuffmanCodes(HuffmanNode root) {
        Map<Character, String> codes = new HashMap<>();
        generateHuffmanCodes(root, "", codes);
        return codes;
    }

    private static void generateHuffmanCodes(HuffmanNode node, String code, Map<Character, String> codes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            codes.put(node.symbol, code);
        }

        generateHuffmanCodes(node.left, code + "0", codes);
        generateHuffmanCodes(node.right, code + "1", codes);
    }

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
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(graphComponent);
        frame.pack();
        //frame.setSize(800,600);
        frame.setVisible(true);
    }

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
}
