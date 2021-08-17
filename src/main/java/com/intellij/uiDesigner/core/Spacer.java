package com.intellij.uiDesigner.core;

import javax.swing.*;
import java.awt.*;

/**
 * @author Joker
 * @since 2021/08/12
 */
public class Spacer extends JComponent {
    public Dimension getMinimumSize(){
        return new Dimension(0, 0);
    }

    public final Dimension getPreferredSize(){
        return getMinimumSize();
    }
}
