package mr.demonid.view;

import java.awt.image.BufferedImage;

public interface IView {

    void setImage(BufferedImage image);
    void setText(String text);
    void appendLog(String message);

}
