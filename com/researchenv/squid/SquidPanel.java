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

import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SquidPanel extends JPanel {

    private final ControlPanel controlPanel;
    private final PlotPanel g;
    private final PlotPanel gi;
    private final PlotPanel gmhn;
    private final Squid squid;
    private SquareStimulusInput si;

    public SquidPanel(final Squid squid) {

        this.squid=squid;
        
        gi = new PlotPanel("Stimulus", "time (ms)", "i (\u00B5A/cm\u00B2)");
        g = new PlotPanel("Spike simulation", "time (ms)", "V (mV)");
        gmhn = new PlotPanel("Activation functions", "time (ms)", "m,h,n (dimensionless)", new String[]{"m", "h", "n"});

        controlPanel = new ControlPanel();

        setLayout(new GridLayout(2, 2));

        add(gi);
        add(g);
        add(controlPanel);
        add(gmhn);

        controlPanel.setActionListener(new ControlListener(this));

        squid.setListenerInterface(new SquidListenerInterface() {
            @Override
            public void calculationDone() {
                g.runPlot(si.getDeltaTime(), squid.getSpike());
                gmhn.runPlot(si.getDeltaTime(), squid.getMAct(), squid.getHAct(), squid.getNAct());
            }
        });
    }

    private class ControlListener implements ControlListenerInterface {
        private final JComponent parent;

        public ControlListener(JComponent parent) {
            this.parent = parent;
        }
        
        @Override
        public void run(ControlData dataFields) {
            si = new SquareStimulusInput(dataFields.getSquareInput());
            si.builStimulus();
            gi.runPlot(si.getDeltaTime(), si.getCurrent());
            squid.runInBackground(si, dataFields.getConstants());
        }

        @Override
        public void clear() {
            gi.clear();
            g.clear();
            gmhn.clear();
        }

        @Override
        public void showHelpDialog() {
            String ver = Squid.SQUID_VERSION;
            JEditorPane editorPanel;
            editorPanel = new JEditorPane("text/html",
                    "<html>"
                    + "</body>"
                    + "<b>Squid " + ver + "</b> - Hodgkin-Huxley neuron action potential model."
                    + "<br>Web site: " + "<a href=\"https://github.com/researchenv/Squid\">https://github.com/researchenv/Squid</a>"
                    + "<br>"
                    + "<br><b>This software is free (open source) and distributed</b>"
                    + "<br><b>under the terms of GNU GPL v3 licence.</b>"
                    + "<br>"
                    + "<br><b>Usage:</b>"
                    + "<br>1.Type stimulus duration (time)."
                    + "<br>1.Select time resolution (dt)."
                    + "<br>2.Set stimulus gaps (on=begin, off=end, I=stimulus)."
                    + "<br>3.Press run button to run calculations."
                    + "<br>"
                    + "<br>Stimulus 2 overwrights stimulus 1."
                    + "<br>Press buttons 1 to 6 for preset stimulus."
                    + "<br>Control model constants in <b>Constants</b> tab."
                    + "<br>Right click on plots for plot options</b>."
                    + "<br>"
                    + "</body>"
                    + "</html>");

            editorPanel.setEditable(false);

            JOptionPane.showMessageDialog(parent, editorPanel, "Squid - Help", JOptionPane.PLAIN_MESSAGE);
        }
    }
}