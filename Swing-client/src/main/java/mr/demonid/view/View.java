package mr.demonid.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class View extends JFrame implements IView{

    private JTextArea logArea;      // для вывода отладки и прочей фигни
    private JLabel imageLabel;      // картинка с сервера
    private JTextArea textArea;     // текст с сервера

    // из-за жутких тормозов при пересчете картинки в случаях изменения размеров окна,
    // приходится её кешировать и выполнять в отдельном потоке.
    private Image cachedImage;                          // кэшированное изображение
    private Dimension lastSize = new Dimension();       // последний размер области


    public View() {

        initComponents();
        setVisible(true);
        appendLog("Приложение инициализировано.");
    }

    /**
     * Вывод отладочной информации в нижнюю текстовую область.
     */
    @Override
    public void appendLog(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    /**
     * Устанавливает изображение в левую часть и масштабирует его под размеры окна.
     */
    @Override
    public void setImage(BufferedImage image) {
        if (image != null) {
            cachedImage = image;
            resizeImageAsync();
//            imageLabel.repaint();
        }
    }

    /**
     * Устанавливает текст в текстовую область справа.
     */
    @Override
    public void setText(String text) {
        SwingUtilities.invokeLater(() -> textArea.setText(text));
    }

    /**
     * Асинхронное масштабирование изображения при изменении размеров окна.
     */
    private void resizeImageAsync() {
        int width = imageLabel.getWidth();
        int height = imageLabel.getHeight();

        if (width <= 0 || height <= 0 || cachedImage == null || (width == lastSize.width && height == lastSize.height)) {
            return;
        }
        lastSize.setSize(width, height);

        if (cachedImage != null) {
            new Thread(() -> {
                Image scaledImage = cachedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                // графические операции нужно делать в потоке swing
                SwingUtilities.invokeLater(() -> imageLabel.setIcon(new ImageIcon(scaledImage)));
//                imageLabel.repaint();
            }).start();
        }
    }



    private void initComponents() {
        setTitle("Test load files from resource server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // панель для контента
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(1, 2));    // левый столбец для картинки, правый для текста

        // настраиваем левую часть
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        contentPane.add(imageLabel);

        // настраиваем правую часть
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane);

        // настраиваем нижнюю часть
        logArea = new JTextArea(5, 20);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        // осталось добавить панели на окно
        add(contentPane, BorderLayout.CENTER);
        add(logScrollPane, BorderLayout.SOUTH);

        // и добавим слушатель для изменения размеров окна
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImageAsync();      // меняем размер картинки
            }
        });
    }

}
