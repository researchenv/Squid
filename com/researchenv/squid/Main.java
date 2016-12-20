/*
 *   Squid 1.0 - Hodgkin-Huxley neuron action potential model
 *   Web site: https://github.com/researchenv/Squid
 *
 *   Copyright (C) 2016,  Erbe Pandini Rodrigues
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.researchenv.squid;

import java.awt.*;
import javax.swing.*;

public class Main {

    public Main() {
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        SquidPanel squidPanel = new SquidPanel(new Squid());
        JFrame frame  = new JFrame("Squid - Hodgkin-Huxley action potential model");
        frame.getContentPane().add(squidPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(800, 600));
        frame.setMinimumSize(new Dimension(800,600));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}