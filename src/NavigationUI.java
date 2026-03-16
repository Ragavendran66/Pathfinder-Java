import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class NavigationUI extends JFrame {

    private Graph graph;
    private List<String> shortestPath;
    private JComboBox<String> sourceBox, destBox;
    private JLabel resultLabel;
    private MapPanel mapPanel;

    public NavigationUI() {
        graph = new Graph();
        loadCities();
        setupUI();
    }

    private void loadCities() {
        graph.addCity("Delhi",     300, 80);
        graph.addCity("Jaipur",    200, 150);
        graph.addCity("Mumbai",    180, 320);
        graph.addCity("Chennai",   320, 480);
        graph.addCity("Bangalore", 280, 430);
        graph.addCity("Hyderabad", 300, 370);
        graph.addCity("Kolkata",   480, 250);
        graph.addCity("Bhopal",    300, 220);
        graph.addCity("Pune",      200, 360);
        graph.addCity("Ahmedabad", 160, 230);

        graph.addRoad("Delhi",     "Jaipur",    280);
        graph.addRoad("Delhi",     "Bhopal",    780);
        graph.addRoad("Delhi",     "Kolkata",   1500);
        graph.addRoad("Jaipur",    "Ahmedabad", 670);
        graph.addRoad("Jaipur",    "Mumbai",    1150);
        graph.addRoad("Ahmedabad", "Mumbai",    530);
        graph.addRoad("Ahmedabad", "Bhopal",    650);
        graph.addRoad("Mumbai",    "Pune",      150);
        graph.addRoad("Mumbai",    "Hyderabad", 710);
        graph.addRoad("Pune",      "Hyderabad", 560);
        graph.addRoad("Pune",      "Bangalore", 840);
        graph.addRoad("Hyderabad", "Bangalore", 570);
        graph.addRoad("Hyderabad", "Chennai",   630);
        graph.addRoad("Bangalore", "Chennai",   350);
        graph.addRoad("Bhopal",    "Hyderabad", 700);
        graph.addRoad("Bhopal",    "Kolkata",   1100);
        graph.addRoad("Kolkata",   "Chennai",   1650);
    }

    private void setupUI() {
        setTitle("Navigation System — Shortest Path Finder");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // Top panel — title (FIXED: removed setPadding, using setBorder)
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 73, 94));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel("India Navigation System");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // Map panel — center
        mapPanel = new MapPanel();
        add(mapPanel, BorderLayout.CENTER);

        // Bottom panel — controls
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(236, 240, 241));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 8));

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Arial", Font.BOLD, 14));

        sourceBox = new JComboBox<>(graph.getCities().keySet()
                .toArray(new String[0]));
        sourceBox.setFont(new Font("Arial", Font.PLAIN, 13));
        sourceBox.setPreferredSize(new Dimension(130, 30));

        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.BOLD, 14));

        destBox = new JComboBox<>(graph.getCities().keySet()
                .toArray(new String[0]));
        destBox.setFont(new Font("Arial", Font.PLAIN, 13));
        destBox.setPreferredSize(new Dimension(130, 30));
        destBox.setSelectedIndex(2);

        JButton findBtn = new JButton("Find Shortest Path");
        findBtn.setFont(new Font("Arial", Font.BOLD, 13));
        findBtn.setBackground(new Color(46, 204, 113));
        findBtn.setForeground(Color.WHITE);
        findBtn.setFocusPainted(false);
        findBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        findBtn.setPreferredSize(new Dimension(180, 32));

        JButton resetBtn = new JButton("Reset");
        resetBtn.setFont(new Font("Arial", Font.BOLD, 13));
        resetBtn.setBackground(new Color(231, 76, 60));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetBtn.setPreferredSize(new Dimension(90, 32));

        resultLabel = new JLabel("Select source and destination to find path");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        resultLabel.setForeground(new Color(52, 73, 94));

        findBtn.addActionListener(e -> findPath());
        resetBtn.addActionListener(e -> resetPath());

        controlPanel.add(fromLabel);
        controlPanel.add(sourceBox);
        controlPanel.add(toLabel);
        controlPanel.add(destBox);
        controlPanel.add(findBtn);
        controlPanel.add(resetBtn);
        controlPanel.add(resultLabel);

        add(controlPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void findPath() {
        String source = (String) sourceBox.getSelectedItem();
        String dest   = (String) destBox.getSelectedItem();

        if (source.equals(dest)) {
            resultLabel.setText("Source and destination are the same!");
            return;
        }

        shortestPath = Dijkstra.getPath(graph, source, dest);
        int distance = Dijkstra.getDistance(graph, source, dest);

        if (shortestPath.isEmpty()) {
            resultLabel.setText("No path found between " + source + " and " + dest);
        } else {
            String pathStr = String.join(" -> ", shortestPath);
            resultLabel.setText("Path: " + pathStr + "  |  Distance: " + distance + " km");
        }

        mapPanel.setShortestPath(shortestPath);
        mapPanel.repaint();
    }

    private void resetPath() {
        shortestPath = null;
        resultLabel.setText("Select source and destination to find path");
        mapPanel.setShortestPath(null);
        mapPanel.repaint();
    }

    class MapPanel extends JPanel {
        private List<String> path;

        public void setShortestPath(List<String> path) {
            this.path = path;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            setBackground(new Color(232, 244, 248));

            // Draw all roads (gray)
            g2.setColor(new Color(180, 180, 180));
            g2.setStroke(new BasicStroke(1.5f));
            for (Map.Entry<String, java.util.List<Graph.Edge>> entry :
                    graph.getAdjList().entrySet()) {
                City from = graph.getCities().get(entry.getKey());
                for (Graph.Edge edge : entry.getValue()) {
                    City to = graph.getCities().get(edge.destination);
                    g2.drawLine(from.getX(), from.getY(),
                            to.getX(), to.getY());
                    int mx = (from.getX() + to.getX()) / 2;
                    int my = (from.getY() + to.getY()) / 2;


                    g2.setFont(new Font("Arial", Font.PLAIN, 10));
                    FontMetrics fm = g2.getFontMetrics();
                    String label = edge.distance + "km";
                    int labelWidth  = fm.stringWidth(label);
                    int labelHeight = fm.getHeight();

                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.fillRoundRect(mx - 2, my - labelHeight + 2,
                            labelWidth + 6, labelHeight + 2, 4, 4);


                    g2.setColor(new Color(80, 80, 80));
                    g2.drawString(label, mx + 1, my);
                    g2.setColor(new Color(180, 180, 180));
                }
            }

            // Draw shortest path (green)
            if (path != null && path.size() > 1) {
                g2.setColor(new Color(46, 204, 113));
                g2.setStroke(new BasicStroke(4f));
                for (int i = 0; i < path.size() - 1; i++) {
                    City from = graph.getCities().get(path.get(i));
                    City to   = graph.getCities().get(path.get(i + 1));
                    g2.drawLine(from.getX(), from.getY(),
                            to.getX(), to.getY());
                }
            }

            // Draw cities (circles)
            for (Map.Entry<String, City> entry :
                    graph.getCities().entrySet()) {
                City city = entry.getValue();
                boolean isOnPath = path != null &&
                        path.contains(city.getName());

                if (isOnPath) {
                    g2.setColor(new Color(46, 204, 113));
                } else {
                    g2.setColor(new Color(52, 152, 219));
                }

                g2.fillOval(city.getX() - 12, city.getY() - 12, 24, 24);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(city.getX() - 12, city.getY() - 12, 24, 24);

                g2.setColor(new Color(30, 30, 30));
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                g2.drawString(city.getName(),
                        city.getX() - 15, city.getY() - 16);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NavigationUI::new);
    }
}