import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsDialog extends JDialog {
    private JTextField startNumberField;
    private JTextField paddingField;
    private JTextField maxSizeField; // ДОБАВЛЕНО: объявление поля
    private JButton mainColorButton;
    private JButton supportColorButton;
    private JCheckBox autoScaleCheck;
    private boolean confirmed = false;
    
    private Color mainColor;
    private Color supportColor;
    
    public SettingsDialog(JFrame parent) {
        super(parent, "Настройки PrimeWalker", true);
        
        Settings settings = Settings.getInstance();
        
        setLayout(new BorderLayout());
        setSize(450, 350);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // === Строка 1: Стартовое число ===
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Стартовое число:"), gbc);
        
        startNumberField = new JTextField(String.valueOf(settings.getStartNumber()));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        mainPanel.add(startNumberField, gbc);
        
        // === Строка 2: Основной цвет ===
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        mainPanel.add(new JLabel("Основной цвет:"), gbc);
        
        mainColor = settings.getMainColor();
        mainColorButton = new JButton("Выбрать цвет");
        mainColorButton.setBackground(mainColor);
        mainColorButton.setForeground(getContrastColor(mainColor));
        mainColorButton.addActionListener(e -> chooseMainColor());
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(mainColorButton, gbc);
        
        // === Строка 3: Несущий цвет ===
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Несущий цвет:"), gbc);
        
        supportColor = settings.getSupportColor();
        supportColorButton = new JButton("Выбрать цвет");
        supportColorButton.setBackground(supportColor);
        supportColorButton.setForeground(getContrastColor(supportColor));
        supportColorButton.addActionListener(e -> chooseSupportColor());
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(supportColorButton, gbc);
        
        // === Строка 4: Отступ ===
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Отступ (пикс):"), gbc);
        
        paddingField = new JTextField(String.valueOf(settings.getPadding()));
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(paddingField, gbc);
        
        // === Строка 5: Максимальный размер карты ===
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Макс. размер:"), gbc);
        
        maxSizeField = new JTextField(String.valueOf(settings.getMaxImageSize())); // Инициализация
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(maxSizeField, gbc);
        
        // === Строка 6: Автомасштабирование ===
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        autoScaleCheck = new JCheckBox("Автоматический подбор размера рисунка", 
                                      settings.isAutoScale());
        mainPanel.add(autoScaleCheck, gbc);
        gbc.gridwidth = 1;
        
        // === Кнопки ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Применить");
        JButton cancelButton = new JButton("Отмена");
        
        okButton.addActionListener(e -> {
            saveSettings();
            confirmed = true;
            dispose();
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void chooseMainColor() {
        Color newColor = JColorChooser.showDialog(
            this, 
            "Выберите основной цвет (зелёные линии)", 
            mainColor
        );
        if (newColor != null) {
            mainColor = newColor;
            mainColorButton.setBackground(mainColor);
            mainColorButton.setForeground(getContrastColor(mainColor));
        }
    }
    
    private void chooseSupportColor() {
        Color newColor = JColorChooser.showDialog(
            this, 
            "Выберите несущий цвет (чёрные линии)", 
            supportColor
        );
        if (newColor != null) {
            supportColor = newColor;
            supportColorButton.setBackground(supportColor);
            supportColorButton.setForeground(getContrastColor(supportColor));
        }
    }
    
    private void saveSettings() {
        Settings settings = Settings.getInstance();
        
        // Стартовое число
        try {
            String text = startNumberField.getText().trim();
            if (text.isEmpty()) {
                settings.setStartNumber(7);
            } else {
                double value = Double.parseDouble(text);
                int startNum = (int) Math.floor(value);
                settings.setStartNumber(Math.max(startNum, 2));
            }
        } catch (NumberFormatException e) {
            settings.setStartNumber(7);
        }
        
        // Отступ
        try {
            int padding = Integer.parseInt(paddingField.getText().trim());
            settings.setPadding(Math.max(padding, 5)); // Минимум 5 пикселей
        } catch (NumberFormatException e) {
            settings.setPadding(50);
        }
        
        // Максимальный размер
        try {
            int maxSize = Integer.parseInt(maxSizeField.getText().trim());
            settings.setMaxImageSize(Math.max(maxSize, 100)); // Минимум 100 пикселей
        } catch (NumberFormatException e) {
            settings.setMaxImageSize(10000);
        }
        
        // Цвета и авторазмер
        settings.setMainColor(mainColor);
        settings.setSupportColor(supportColor);
        settings.setAutoScale(autoScaleCheck.isSelected());
        
        System.out.println("Настройки сохранены (диалог):");
        System.out.println("  Основной цвет: " + mainColor);
        System.out.println("  Несущий цвет: " + supportColor);
        System.out.println("  Отступ: " + settings.getPadding());
        System.out.println("  Макс. размер: " + settings.getMaxImageSize());
    }
    
    private Color getContrastColor(Color color) {
        double luminance = (0.299 * color.getRed() + 
                          0.587 * color.getGreen() + 
                          0.114 * color.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}