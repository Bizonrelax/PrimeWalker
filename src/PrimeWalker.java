import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

public class PrimeWalker {
    
    static class Point {
        double x, y;
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
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
    
    public static void main(String[] args) {
        // Параметры
        int primeCount = 30; // сколько простых чисел использовать
        int imageSize = 777;
        double scale = 15.0;
        
        // 1. Генерируем простые числа начиная с 7
        List<Integer> allPrimes = generatePrimesFrom(7, primeCount);
        System.out.println("Все простые: " + allPrimes);
        
        // 2. Находим всех близнецов (и младших, и старших)
        Set<Integer> twinsSet = findTwinPrimes(allPrimes);
        System.out.println("Все близнецы: " + twinsSet);
        
        // 3. Удаляем старших близнецов
        List<Integer> filteredPrimes = removeElderTwins(allPrimes);
        System.out.println("После удаления старших близнецов: " + filteredPrimes);
        
        // 4. Строим ломаную с информацией о младших близнецах
        List<Segment> segments = new ArrayList<>();
        double currentX = 0, currentY = 0;
        
        // Направления: 0-вправо, 1-вниз, 2-влево, 3-вверх
        int currentDirection = 0;
        double[][] directionVectors = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        
        for (int i = 0; i < filteredPrimes.size() - 1; i++) {
            int currentPrime = filteredPrimes.get(i);
            int nextPrime = filteredPrimes.get(i + 1);
            int gap = nextPrime - currentPrime;
            
            // Проверяем, является ли текущее число младшим близнецом
            boolean isYoungerTwin = twinsSet.contains(currentPrime) && 
                                     allPrimes.contains(currentPrime + 2);
            
            // Стартовая точка отрезка
            Point start = new Point(currentX, currentY);
            
            // Вычисляем конечную точку
            currentX += directionVectors[currentDirection][0] * gap;
            currentY += directionVectors[currentDirection][1] * gap;
            Point end = new Point(currentX, currentY);
            
            // Добавляем отрезок
            segments.add(new Segment(start, end, isYoungerTwin));
            
            // Поворачиваем по часовой стрелке
            currentDirection = (currentDirection + 1) % 4;
        }
        
        // 5. Рисуем
        drawSegments(segments, imageSize, scale, "prime_walk_twins.png");
        
        System.out.println("\nСтатистика:");
        System.out.println("Всего простых чисел: " + allPrimes.size());
        System.out.println("Использовано чисел (после фильтрации): " + filteredPrimes.size());
        System.out.println("Нарисовано отрезков: " + segments.size());
        System.out.println("Конечная точка: (" + currentX + ", " + currentY + ")");
        
        // Анализ фигур
        analyzeShapes(segments);
    }
    
    // Генерация простых чисел начиная с start
    static List<Integer> generatePrimesFrom(int start, int count) {
        List<Integer> primes = new ArrayList<>();
        int num = start;
        while (primes.size() < count) {
            if (isPrime(num)) {
                primes.add(num);
            }
            num++;
        }
        return primes;
    }
    
    // Нахождение всех близнецов
    static Set<Integer> findTwinPrimes(List<Integer> primes) {
        Set<Integer> twins = new HashSet<>();
        for (int i = 0; i < primes.size() - 1; i++) {
            if (primes.get(i + 1) - primes.get(i) == 2) {
                twins.add(primes.get(i));     // младший близнец
                twins.add(primes.get(i + 1)); // старший близнец
            }
        }
        return twins;
    }
    
    // Удаление старших близнецов
    static List<Integer> removeElderTwins(List<Integer> primes) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < primes.size(); i++) {
            int current = primes.get(i);
            boolean isElderTwin = false;
            
            if (i > 0 && primes.get(i - 1) == current - 2) {
                isElderTwin = true;
            }
            
            if (!isElderTwin) {
                result.add(current);
            }
        }
        return result;
    }
    
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    static void drawSegments(List<Segment> segments, int size, double scale, String filename) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size, size);
        
        // Переносим в центр
        g2d.translate(size / 2, size / 2);
        
        for (Segment segment : segments) {
            // Выбираем цвет: зелёный для младших близнецов, чёрный для остальных
            if (segment.fromYoungerTwin) {
                g2d.setColor(new Color(0, 150, 0)); // тёмно-зелёный
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
        
        // Красная начальная точка
        g2d.setColor(Color.RED);
        g2d.fillOval(-5, -5, 10, 10);
        
        // Синяя конечная точка
        if (!segments.isEmpty()) {
            g2d.setColor(Color.BLUE);
            Point last = segments.get(segments.size() - 1).end;
            int lastX = (int)(last.x * scale);
            int lastY = (int)(last.y * scale);
            g2d.fillOval(lastX - 5, lastY - 5, 10, 10);
        }
        
        g2d.dispose();
        
        try {
            ImageIO.write(image, "PNG", new File(filename));
            System.out.println("Изображение сохранено: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Анализ фигур (прямоугольников и квадратов)
    static void analyzeShapes(List<Segment> segments) {
        System.out.println("\nАнализ фигур:");
        
        // Собираем длины отрезков по группам по 4 (одна фигура = 4 отрезка)
        for (int i = 0; i <= segments.size() - 4; i += 4) {
            double[] lengths = new double[4];
            for (int j = 0; j < 4; j++) {
                Segment s = segments.get(i + j);
                double dx = s.end.x - s.start.x;
                double dy = s.end.y - s.start.y;
                lengths[j] = Math.sqrt(dx * dx + dy * dy);
            }
            
            System.out.printf("Фигура %d: [%.1f, %.1f, %.1f, %.1f] ", 
                i/4 + 1, lengths[0], lengths[1], lengths[2], lengths[3]);
            
            // Проверяем, квадрат ли это (все стороны равны)
            boolean isSquare = true;
            for (int j = 1; j < 4; j++) {
                if (Math.abs(lengths[j] - lengths[0]) > 0.01) {
                    isSquare = false;
                    break;
                }
            }
            
            if (isSquare) {
                System.out.println("→ КВАДРАТ");
            } else {
                System.out.println("→ ПРЯМОУГОЛЬНИК");
            }
        }
    }
}