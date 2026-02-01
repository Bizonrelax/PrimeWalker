import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

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
    
    private JLabel primeCountLabel, scaleLabel, imageSizeLabel;
    private JTextField primeCountField, scaleField, imageSizeField;
    private JButton createButton, saveButton, settingsButton;
    private JPanel controlPanel, imagePanel, buttonPanel;
    private ImageDisplay imageDisplay;
    
    private static final int DEFAULT_PRIME_COUNT = 30;
    private static final double DEFAULT_SCALE = 15.0;
    private static final int DEFAULT_IMAGE_SIZE = 777;
    
    public PrimeWalkerGUI() {
        setTitle("PrimeWalker - Визуализация простых чисел");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Панель управления
        controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Кнопка настроек
        settingsButton = new JButton("Настройки");
        settingsButton.setToolTipText("Открыть настройки программы");
        settingsButton.setPreferredSize(new Dimension(90, 25));
        settingsButton.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        controlPanel.add(settingsButton, gbc);
        
        // Поле "Чисел"
        primeCountLabel = new JLabel("Чисел:");
        primeCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        controlPanel.add(primeCountLabel, gbc);
        
        primeCountField = new JTextField(String.valueOf(DEFAULT_PRIME_COUNT));
        primeCountField.setColumns(6);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        controlPanel.add(primeCountField, gbc);
        
        // Поле "Масштаб"
        scaleLabel = new JLabel("Масштаб:");
        scaleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        controlPanel.add(scaleLabel, gbc);
        
        scaleField = new JTextField(String.valueOf(DEFAULT_SCALE));
        scaleField.setColumns(6);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        controlPanel.add(scaleField, gbc);
        gbc.gridwidth = 1;
        
        // Поле "Размер"
        imageSizeLabel = new JLabel("Размер:");
        imageSizeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        controlPanel.add(imageSizeLabel, gbc);
        
        imageSizeField = new JTextField(String.valueOf(DEFAULT_IMAGE_SIZE));
        imageSizeField.setColumns(6);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        controlPanel.add(imageSizeField, gbc);
        gbc.gridwidth = 1;
        
        // Кнопки "Создать" и "Сохранить"
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        createButton = new JButton("Создать рисунок");
        createButton.setPreferredSize(new Dimension(140, 30));
        buttonPanel.add(createButton);
        
        saveButton = new JButton("Сохранить");
        saveButton.setPreferredSize(new Dimension(140, 30));
        saveButton.setEnabled(false);
        buttonPanel.add(saveButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        controlPanel.add(buttonPanel, gbc);
        
        // Панель для изображения
        imagePanel = new JPanel(new BorderLayout());
        imageDisplay = new ImageDisplay();
        imagePanel.add(new JScrollPane(imageDisplay), BorderLayout.CENTER);
        
        add(controlPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);
        
        // Обработчики событий
        createButton.addActionListener(e -> createImage());
        saveButton.addActionListener(e -> saveImage());
        settingsButton.addActionListener(e -> showSettingsDialog());
        
        setSize(900, 700);
        setLocationRelativeTo(null);
    }
    
    private void showSettingsDialog() {
        SettingsDialog dialog = new SettingsDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            JOptionPane.showMessageDialog(this,
                "Настройки сохранены и будут применены при создании следующего рисунка.",
                "Настройки обновлены",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void createImage() {
        try {
            int primeCount = Integer.parseInt(primeCountField.getText());
            double scale = Double.parseDouble(scaleField.getText());
            int imageSize = Integer.parseInt(imageSizeField.getText());
            
            System.out.println("=== СОЗДАНИЕ ИЗОБРАЖЕНИЯ ===");
            System.out.println("Параметры: чисел=" + primeCount + ", масштаб=" + scale + ", размер=" + imageSize);
            
            BufferedImage image = generateImage(primeCount, scale, imageSize);
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
                ImageIO.write((BufferedImage) imageDisplay.getImage(), "PNG", file);
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
    
   private BufferedImage generateImage(int primeCount, double scale, int imageSize) {
    Settings settings = Settings.getInstance();
    int startNumber = settings.getStartNumber();
    
    List<Integer> allPrimes = generatePrimesFrom(startNumber, primeCount);
    Set<Integer> twinsSet = findTwinPrimes(allPrimes);
    List<Integer> filteredPrimes = removeElderTwins(allPrimes);
    
    if (filteredPrimes.size() < 2) {
        throw new IllegalArgumentException("После фильтрации осталось слишком мало чисел. Попробуйте увеличить количество или изменить стартовое число.");
    }
    
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
    
    // АВТОМАТИЧЕСКИЙ РАСЧЁТ РАЗМЕРА СО СДВИГОМ
    int finalImageSize = imageSize;
    double shiftX = 0, shiftY = 0;
    
    if (settings.isAutoScale() && !segments.isEmpty()) {
        AutoSizeResult autoSize = calculateAutoSize(segments, scale, settings.getPadding());
        finalImageSize = autoSize.size;
        shiftX = autoSize.shiftX;
        shiftY = autoSize.shiftY;
        
        // Создаем финальные копии для лямбды
        final int sizeForLambda = finalImageSize;
        final double shiftXForLambda = shiftX;
        final double shiftYForLambda = shiftY;
        
        // Обновляем поле размера
        SwingUtilities.invokeLater(() -> {
            imageSizeField.setText(String.valueOf(sizeForLambda));
        });
        
        System.out.println("  Используемый сдвиг: (" + shiftX + ", " + shiftY + ")");
    }
    
    return drawSegments(segments, finalImageSize, scale, shiftX, shiftY);
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
    
    private BufferedImage drawSegments(List<Segment> segments, int size, double scale, 
                                   double shiftX, double shiftY) {
    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, size, size);
    
    // Центрируем изображение
    g2d.translate(size / 2, size / 2);
    
    // Получаем цвета из настроек
    Settings settings = Settings.getInstance();
    Color mainColor = settings.getMainColor();
    Color supportColor = settings.getSupportColor();
    
    // Рисуем отрезки со сдвигом
    for (Segment segment : segments) {
        if (segment.fromYoungerTwin) {
            g2d.setColor(mainColor);
            g2d.setStroke(new BasicStroke(3.0f));
        } else {
            g2d.setColor(supportColor);
            g2d.setStroke(new BasicStroke(2.0f));
        }
        
        // Применяем масштаб И сдвиг
        int x1 = (int)((segment.start.x * scale) + shiftX);
        int y1 = (int)((segment.start.y * scale) + shiftY);
        int x2 = (int)((segment.end.x * scale) + shiftX);
        int y2 = (int)((segment.end.y * scale) + shiftY);
        
        g2d.drawLine(x1, y1, x2, y2);
    }
    
    // Красная начальная точка (также со сдвигом)
    g2d.setColor(Color.RED);
    int startX = (int)((0 * scale) + shiftX);
    int startY = (int)((0 * scale) + shiftY);
    g2d.fillOval(startX - 5, startY - 5, 10, 10);
    
    if (!segments.isEmpty()) {
        // Синяя конечная точка
        g2d.setColor(Color.BLUE);
        Point last = segments.get(segments.size() - 1).end;
        int lastX = (int)((last.x * scale) + shiftX);
        int lastY = (int)((last.y * scale) + shiftY);
        g2d.fillOval(lastX - 5, lastY - 5, 10, 10);
    }
    
    g2d.dispose();
    return image;
}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Используем стандартный вид
            }
            
            new PrimeWalkerGUI().setVisible(true);
        });
    }
  private AutoSizeResult calculateAutoSize(List<Segment> segments, double scale, int padding) {
    // Собираем все точки (включая начальную)
    List<Point> allPoints = new ArrayList<>();
    allPoints.add(new Point(0, 0)); // Начальная точка
    
    for (Segment segment : segments) {
        allPoints.add(segment.start);
        allPoints.add(segment.end);
    }
    
    // Находим реальные границы в масштабированных координатах
    double minX = Double.MAX_VALUE;
    double maxX = -Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    double maxY = -Double.MAX_VALUE;
    
    for (Point point : allPoints) {
        double scaledX = point.x * scale;
        double scaledY = point.y * scale;
        
        minX = Math.min(minX, scaledX);
        maxX = Math.max(maxX, scaledX);
        minY = Math.min(minY, scaledY);
        maxY = Math.max(maxY, scaledY);
    }
    
    // Вычисляем центр рисунка
    double centerX = (minX + maxX) / 2;
    double centerY = (minY + maxY) / 2;
    
    // Вычисляем необходимый сдвиг, чтобы центр рисунка был в центре изображения
    // После применения g2d.translate(size/2, size/2), центр изображения будет в (0,0)
    // Поэтому нам нужно сдвинуть рисунок на -centerX и -centerY
    double shiftX = -centerX;
    double shiftY = -centerY;
    
    // После сдвига, находим новые границы
    double shiftedMinX = minX + shiftX;
    double shiftedMaxX = maxX + shiftX;
    double shiftedMinY = minY + shiftY;
    double shiftedMaxY = maxY + shiftY;
    
    // Находим максимальное абсолютное отклонение после сдвига
    double maxAbsX = Math.max(Math.abs(shiftedMinX), Math.abs(shiftedMaxX));
    double maxAbsY = Math.max(Math.abs(shiftedMinY), Math.abs(shiftedMaxY));
    double maxAbs = Math.max(maxAbsX, maxAbsY);
    
 // Требуемый размер: 2 * (максимальное отклонение + отступ)
    int requiredSize = (int) Math.ceil(2 * (maxAbs + padding));
    
    // Ограничения
    requiredSize = Math.max(requiredSize, 100);   // Минимум 100 пикселей
    
    // Используем максимальный размер из настроек
    Settings settings = Settings.getInstance();
    int maxSize = settings.getMaxImageSize();
    requiredSize = Math.min(requiredSize, maxSize);
    
    // Делаем четным для точного центрирования
    if (requiredSize % 2 != 0) {
        requiredSize++;
    }
    
    System.out.println("Авторасчёт размера:");
    System.out.println("  maxAbsX=" + maxAbsX + ", maxAbsY=" + maxAbsY);
    System.out.println("  maxAbs=" + maxAbs + ", padding=" + padding);
    System.out.println("  requiredSize=" + requiredSize + " (макс. лимит: " + maxSize + ")");
    
    return new AutoSizeResult(requiredSize, shiftX, shiftY);
}
}