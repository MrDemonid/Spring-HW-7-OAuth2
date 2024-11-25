package mr.demonid;

import mr.demonid.controller.Controller;
import mr.demonid.view.View;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Controller(new View()));
    }
}