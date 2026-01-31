import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

public class PrimeWalkerGUI extends JFrame {
    
    // Компоненты интерфейса
    private JLabel primeCountLabel, scaleLabel, imageSizeLabel;
    private JTextField primeCountField, scaleField, imageSizeField;
    private JButton createButton, saveButton;
    private JPanel controlPanel, imagePanel;
    private ImageDisplay imageDisplay;
    
    // Параметры по умолчанию
    private static final int DEFAULT_PRIME_COUNT = 30;
    private static final double DEFAULT_SCALE = 15.0;
    private static final int DEFAULT_IMAGE_SIZE = 777;
    
    public PrimeWalkerGUI() {
        setTitle("Prime Walker - Визуализация простых чисел");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Панель управления
        controlPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        primeCountLabel = new JLabel("Количество простых чисел:");
        primeCountField = new JTextField(String.valueOf(DEFAULT_PRIME_COUNT));
        
        scaleLabel = new JLabel("Масштаб:");
        scaleField = new JTextField(String.valueOf(DEFAULT_SCALE));
        
        imageSizeLabel = new JLabel("Размер изображения:");
        imageSizeField = new JTextField(String.valueOf(DEFAULT_IMAGE_SIZE));
        
        createButton = new JButton("Создать рисунок");
        saveButton = new JButton("Сохранить изображение");
        saveButton.setEnabled(false);
        
        controlPanel.add(primeCountLabel);
        controlPanel.add(primeCountField);
        controlPanel.add(scaleLabel);
        controlPanel.add(scaleField);
        controlPanel.add(imageSizeLabel);
        controlPanel.add(imageSizeField);
        controlPanel.add(createButton);
        controlPanel.add(saveButton);
        
        // Панель для изображения
        imagePanel = new JPanel(new BorderLayout());
        imageDisplay = new ImageDisplay();
        imagePanel.add(new JScrollPane(imageDisplay), BorderLayout.CENTER);
        
        // Добавляем панели в окно
        add(controlPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);
        
        // Обработчики событий
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createImage();
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
        
        // Настройки окна
        setSize(900, 700);
        setLocationRelativeTo(null);
    }
    
    private void createImage() {
        try {
            int primeCount = Integer.parseInt(primeCountField.getText());
            double scale = Double.parseDouble(scaleField.getText());
            int imageSize = Integer.parseInt(imageSizeField.getText());
            
            // Генерация изображения
            BufferedImage image = generateImage(primeCount, scale, imageSize);
            
            // Отображение
            imageDisplay.setImage(image);
            saveButton.setEnabled(true);
            
            showTimedMessage("Изображение создано!", 1450);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Ошибка ввода! Проверьте правильность чисел.", 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Ошибка при создании изображения: " + ex.getMessage(), 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("prime_walk.png"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageIO.write(imageDisplay.getImage(), "PNG", file);
                JOptionPane.showMessageDialog(this, 
                    "Изображение сохранено:\n" + file.getAbsolutePath(),
                    "Сохранено", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Ошибка сохранения: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Вложенный класс для отображения изображения
    class ImageDisplay extends JPanel {
        private BufferedImage image;
        
        public void setImage(BufferedImage image) {
            this.image = image;
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
            revalidate();
            repaint();
        }
        
        public BufferedImage getImage() {
            return image;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, this);
            }
        }
    }
    
    // Метод генерации изображения (взято из предыдущего кода)
    private BufferedImage generateImage(int primeCount, double scale, int imageSize) {
        // 1. Генерируем простые числа начиная с 7
        List<Integer> allPrimes = generatePrimesFrom(7, primeCount);
        
        // 2. Находим всех близнецов
        Set<Integer> twinsSet = findTwinPrimes(allPrimes);
        
        // 3. Удаляем старших близнецов
        List<Integer> filteredPrimes = removeElderTwins(allPrimes);
        
        // 4. Строим ломаную
        List<Segment> segments = new ArrayList<>();
        double currentX = 0, currentY = 0;
        int currentDirection = 0;
        double[][] directionVectors = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        
        for (int i = 0; i < filteredPrimes.size() - 1; i++) {
            int currentPrime = filteredPrimes.get(i);
            int nextPrime = filteredPrimes.get(i + 1);
            int gap = nextPrime - currentPrime;
            
            boolean isYoungerTwin = twinsSet.contains(currentPrime) && 
                                   allPrimes.contains(currentPrime + 2);
            
            Point start = new Point(currentX, currentY);
            currentX += directionVectors[currentDirection][0] * gap;
            currentY += directionVectors[currentDirection][1] * gap;
            Point end = new Point(currentX, currentY);
            
            segments.add(new Segment(start, end, isYoungerTwin));
            currentDirection = (currentDirection + 1) % 4;
        }
        
        // 5. Создаем изображение
        return drawSegments(segments, imageSize, scale);
    }
    
    // Вспомогательные классы и методы
    static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
    }
    
    static class Segment {
        Point start, end;
        boolean fromYoungerTwin;
        Segment(Point start, Point end, boolean fromYoungerTwin) {
            this.start = start;
            this.end = end;
            this.fromYoungerTwin = fromYoungerTwin;
        }
    }
    
    private List<Integer> generatePrimesFrom(int start, int count) {
        List<Integer> primes = new ArrayList<>();
        int num = start;
        while (primes.size() < count) {
            if (isPrime(num)) primes.add(num);
            num++;
        }
        return primes;
    }
    
    private Set<Integer> findTwinPrimes(List<Integer> primes) {
        Set<Integer> twins = new HashSet<>();
        for (int i = 0; i < primes.size() - 1; i++) {
            if (primes.get(i + 1) - primes.get(i) == 2) {
                twins.add(primes.get(i));
                twins.add(primes.get(i + 1));
            }
        }
        return twins;
    }
    
    private List<Integer> removeElderTwins(List<Integer> primes) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < primes.size(); i++) {
            int current = primes.get(i);
            boolean isElderTwin = false;
            if (i > 0 && primes.get(i - 1) == current - 2) {
                isElderTwin = true;
            }
            if (!isElderTwin) result.add(current);
        }
        return result;
    }
    
    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    private BufferedImage drawSegments(List<Segment> segments, int size, double scale) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size, size);
        
        g2d.translate(size / 2, size / 2);
        
        for (Segment segment : segments) {
            if (segment.fromYoungerTwin) {
                g2d.setColor(new Color(0, 180, 0));
                g2d.setStroke(new BasicStroke(3.0f));
            } else {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2.0f));
            }
            
            int x1 = (int)(segment.start.x * scale);
            int y1 = (int)(segment.start.y * scale);
            int x2 = (int)(segment.end.x * scale);
            int y2 = (int)(segment.end.y * scale);
            
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        g2d.setColor(Color.RED);
        g2d.fillOval(-5, -5, 10, 10);
        
        if (!segments.isEmpty()) {
            g2d.setColor(Color.BLUE);
            Point last = segments.get(segments.size() - 1).end;
            int lastX = (int)(last.x * scale);
            int lastY = (int)(last.y * scale);
            g2d.fillOval(lastX - 5, lastY - 5, 10, 10);
        }
        
        g2d.dispose();
        return image;
    }
    
    // Точка входа
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PrimeWalkerGUI().setVisible(true);
            }
        });
    }
    private void showTimedMessage(String message, int delayMs) {
        JDialog dialog = new JDialog(this, "Информация", false);
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        dialog.add(label);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(this);
        
        Timer timer = new Timer(delayMs, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        dialog.setVisible(true);
    }
}