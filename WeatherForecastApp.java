import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class BackgroundPanel extends JPanel {
    private BufferedImage bgImage;

    public BackgroundPanel(String imagePath) {
        try {
            bgImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            bgImage = null;
        }
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class WeatherForecastApp {

    static JLabel[] tempLabels = new JLabel[5];
    static JLabel[] statusLabels = new JLabel[5];

    public static void updateWeatherForCity(String city) {
        Random rand = new Random();
        String[] statuses = { "Sunny ☀", "Cloudy ☁", "Rainy 🌧", "Storm ⛈", "Windy 🌬" };

        for (int i = 0; i < 5; i++) {
            int temp = 20 + rand.nextInt(15); // 20°C to 35°C
            String status = statuses[rand.nextInt(statuses.length)];
            tempLabels[i].setText(temp + "°C");
            statusLabels[i].setText(status);
        }

        JOptionPane.showMessageDialog(null, "Showing weather for: " + city);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("5-Day Weather Forecast");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null); // Center on screen

        // Background
        BackgroundPanel mainPanel = new BackgroundPanel("background.png");

        // Greeting
        ImageIcon moonIcon = new ImageIcon("moon.png");
        Image scaledMoon = moonIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JLabel greetingLabel = new JLabel("Good Evening!", new ImageIcon(scaledMoon), SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 36));
        greetingLabel.setForeground(new Color(40, 80, 180));
        greetingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        greetingLabel.setIconTextGap(10);
        greetingLabel.setHorizontalTextPosition(SwingConstants.RIGHT);

        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(greetingLabel);

        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        ImageIcon smileIcon = new ImageIcon("smile.png");
        Image smileImg = smileIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JButton emojiButton = new JButton(new ImageIcon(smileImg));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(emojiButton);
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(searchPanel);

        // Weather grid panel
        JPanel weatherPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        weatherPanel.setOpaque(false);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");

        for (int i = 0; i < 5; i++) {
            JPanel dayPanel = new JPanel();
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBackground(Color.WHITE);
            dayPanel.setPreferredSize(new Dimension(120, 150));

            LocalDate date = today.plusDays(i);
            JLabel dayLabel = new JLabel("Day " + (i + 1) + " (" + date.format(formatter) + ")", SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 16));

            tempLabels[i] = new JLabel("27°C", SwingConstants.CENTER);
            statusLabels[i] = new JLabel("Sunny ☀", SwingConstants.CENTER);

            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            tempLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            statusLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);

            dayPanel.add(Box.createVerticalStrut(10));
            dayPanel.add(dayLabel);
            dayPanel.add(tempLabels[i]);
            dayPanel.add(statusLabels[i]);
            weatherPanel.add(dayPanel);
        }

        // Centered wrapper
        JPanel containerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        containerPanel.setOpaque(false);
        containerPanel.add(weatherPanel);

        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(containerPanel);

        // Add action to search button
        searchButton.addActionListener(e -> {
            String city = searchField.getText().trim();
            if (!city.isEmpty()) {
                updateWeatherForCity(city);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a city name.");
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
