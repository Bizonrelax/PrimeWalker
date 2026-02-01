import java.awt.Color;
import java.io.*;
import java.util.Properties;

public class Settings {
    private static final String SETTINGS_FILE = "primewalker.properties";
    
    private int startNumber = 7;
    private Color mainColor = new Color(0, 180, 0);
    private Color supportColor = Color.BLACK;
    private boolean autoScale = true;
    private int padding = 50;  // Отступ в пикселях
 // Новое поле для максимального размера
    private int maxImageSize = 10000; // по умолчанию 10000
    
    private static Settings instance;
    
    private Settings() {
        loadSettings();
    }
    
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
    
    private void loadSettings() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(SETTINGS_FILE)) {
            props.load(input);
            
            startNumber = Integer.parseInt(props.getProperty("startNumber", "7"));
            
            int mainR = Integer.parseInt(props.getProperty("mainColor.R", "0"));
            int mainG = Integer.parseInt(props.getProperty("mainColor.G", "180"));
            int mainB = Integer.parseInt(props.getProperty("mainColor.B", "0"));
            mainColor = new Color(mainR, mainG, mainB);
            
            int supportR = Integer.parseInt(props.getProperty("supportColor.R", "0"));
            int supportG = Integer.parseInt(props.getProperty("supportColor.G", "0"));
            int supportB = Integer.parseInt(props.getProperty("supportColor.B", "0"));
            supportColor = new Color(supportR, supportG, supportB);
            
            autoScale = Boolean.parseBoolean(props.getProperty("autoScale", "true"));
            padding = Integer.parseInt(props.getProperty("padding", "50"));
            
            // Загружаем максимальный размер
            maxImageSize = Integer.parseInt(props.getProperty("maxImageSize", "10000"));
            
        } catch (IOException | NumberFormatException e) {
            System.out.println("Не удалось загрузить настройки, используются значения по умолчанию");
        }
    }
    
    public void saveSettings() {
        Properties props = new Properties();
        
        props.setProperty("startNumber", String.valueOf(startNumber));
        
        props.setProperty("mainColor.R", String.valueOf(mainColor.getRed()));
        props.setProperty("mainColor.G", String.valueOf(mainColor.getGreen()));
        props.setProperty("mainColor.B", String.valueOf(mainColor.getBlue()));
        
        props.setProperty("supportColor.R", String.valueOf(supportColor.getRed()));
        props.setProperty("supportColor.G", String.valueOf(supportColor.getGreen()));
        props.setProperty("supportColor.B", String.valueOf(supportColor.getBlue()));
        
        props.setProperty("autoScale", String.valueOf(autoScale));
        props.setProperty("padding", String.valueOf(padding));
        
        // Сохраняем максимальный размер
        props.setProperty("maxImageSize", String.valueOf(maxImageSize));
        
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            props.store(output, "PrimeWalker Settings");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения настроек: " + e.getMessage());
        }
    }
    
 // Геттеры и сеттеры
    public int getStartNumber() { return startNumber; }
    public void setStartNumber(int startNumber) { 
        this.startNumber = startNumber; 
        saveSettings(); 
    }
    
    public Color getMainColor() { return mainColor; }
    public void setMainColor(Color mainColor) { 
        this.mainColor = mainColor; 
        saveSettings(); 
    }
    
    public Color getSupportColor() { return supportColor; }
    public void setSupportColor(Color supportColor) { 
        this.supportColor = supportColor; 
        saveSettings(); 
    }
    
    public boolean isAutoScale() { return autoScale; }
    public void setAutoScale(boolean autoScale) { 
        this.autoScale = autoScale; 
        saveSettings(); 
    }
    
    public int getPadding() { return padding; }
    public void setPadding(int padding) { 
        this.padding = padding; 
        saveSettings(); 
    }
    
    // Новые методы для максимального размера
    public int getMaxImageSize() { return maxImageSize; }
    public void setMaxImageSize(int maxImageSize) { 
        this.maxImageSize = maxImageSize; 
        saveSettings(); 
    }
    
    public String getMainColorHex() {
        return String.format("#%02X%02X%02X", mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue());
    }
    
    public String getSupportColorHex() {
        return String.format("#%02X%02X%02X", supportColor.getRed(), supportColor.getGreen(), supportColor.getBlue());
    }
}