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

import com.researchenv.squid.hodgkinhuxley.HHModelConst;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel implements ActionListener, ChangeListener {

    private final JTabbedPane tabbedPane;
    private final ControlData controlData;
    private JSpinner[] constantSpinners;
    private JSpinner[] inputSpinners;
    private JButton[] presetInputButtons;
    private JLabel[] constFieldLabels;
    private JPanel inputPanel;
    private JPanel constPanel;
    private JButton runButton;
    private JButton clearButton;
    private JButton helpButton;
    private JButton defaultButton;
    private JButton quitButton;
    private JCheckBox stim2CheckBox;
    private JComboBox dtComboBox;
    private ControlListenerInterface listenerInterface;

    public ControlPanel() {
        setMinimumSize(new Dimension(350, 300));

        listenerInterface = null;

        this.controlData = new ControlData(new SquareInputData(), new HHModelConst());

        tabbedPane = new JTabbedPane();

        setLayout(new GridBagLayout());

        setButtons();
        setInputPanel();
        setConstPanel();

        tabbedPane.addTab("Input", inputPanel);
        tabbedPane.addTab("Constants", constPanel);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(0, 0, 5,5);

        c.gridwidth = 4;
        c.gridheight = 6;
        c.gridx = 0;
        c.gridy = 0;

        add(tabbedPane, c);
    }

    public void setEnable(boolean enable) {
        super.setEnabled(enable);
        runButton.setEnabled(enable);
        clearButton.setEnabled(enable);
        for (JButton b : presetInputButtons) {
            b.setEnabled(enable);
        }
    }

    private void setInputPanel() {
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(
                5, //top
                5, //left
                5, //bottom
                5)); //right

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 5, 5);

        setInputLabels(c, inputPanel);

        setSpinners(c, inputPanel);
        
        Double[] items= new Double[]{0.01, 0.02, 0.05};
        dtComboBox = new JComboBox<>(items);

        c.gridx = 2;
        c.gridy = 0;
        
        inputPanel.add(dtComboBox, c);
        dtComboBox.addActionListener(this);
        dtComboBox.setSelectedIndex(2);//0.05
        
        stim2CheckBox = new JCheckBox();
        stim2CheckBox.setSelected(true);
        stim2CheckBox.addActionListener(this);

        enableStim2Fields(true);

        c.gridx = 1;
        c.gridy = 4;
        inputPanel.add(stim2CheckBox, c);

        actionPerformed(new ActionEvent(stim2CheckBox, 0, ""));
    }

    private void enableStim2Fields(boolean enable) {
        inputSpinners[SquareInputData.Field.ON2.getIndex()].setEnabled(enable);
        inputSpinners[SquareInputData.Field.OFF2.getIndex()].setEnabled(enable);
        inputSpinners[SquareInputData.Field.I2.getIndex()].setEnabled(enable);
    }

    private void setSpinners(GridBagConstraints c, JPanel inputPanel) {
        int max_time = 10000;
        double max_dt = 1.0;

        double[] values = controlData.getSquareInput().getValues();

        int size = SquareInputData.Field.values().length;

        inputSpinners = new JSpinner[size];
        for (int i = 0; i < inputSpinners.length; i++) {
            inputSpinners[i] = new JSpinner();
            inputSpinners[i].setEditor(new JSpinner.NumberEditor(inputSpinners[i],"#.#"));
        }

        int time = SquareInputData.Field.TIME.getIndex();
        int index = time;
        inputSpinners[index].setModel(new SpinnerNumberModel(values[index], 0, max_time, 1));
        inputSpinners[index].addChangeListener(this);

        index = SquareInputData.Field.ON1.getIndex();
        int off = SquareInputData.Field.OFF1.getIndex();
        inputSpinners[index].setModel(new SpinnerNumberModel(values[index], 0, max_time, 1));
        inputSpinners[index].addChangeListener(this);
        inputSpinners[off].setModel(new SpinnerNumberModel(values[off], 1, max_time, 1));
        inputSpinners[off].addChangeListener(this);

        index = SquareInputData.Field.ON2.getIndex();
        off = SquareInputData.Field.OFF2.getIndex();
        inputSpinners[index].setModel(new SpinnerNumberModel(values[index], 0, max_time, 1));
        inputSpinners[index].addChangeListener(this);
        inputSpinners[off].setModel(new SpinnerNumberModel(values[off], 1, max_time, 1));
        inputSpinners[off].addChangeListener(this);

        index = SquareInputData.Field.I1.getIndex();
        inputSpinners[index].setModel(new SpinnerNumberModel(values[index], -100.0, 100.0, 1));

        index = SquareInputData.Field.I2.getIndex();
        inputSpinners[index].setModel(new SpinnerNumberModel(values[index], -100.0, 100.0, 1));

        int[][] pos = new int[][]{{0,3},{1, 3}, {2, 3}, {0, 5}, {1, 5}, {2, 5}, {1, 0}};

        for (int i = 0; i < inputSpinners.length; i++) {
            c.gridx = pos[i][0];
            c.gridy = pos[i][1];
            inputSpinners[i].setEditor(new JSpinner.NumberEditor(inputSpinners[i],"#.#"));
            inputPanel.add(inputSpinners[i], c);
        }
    }

    private void setInputLabels(GridBagConstraints c, JPanel inputPanel) {
        JLabel stim1label = new JLabel("Stimulus gap 1");
        JLabel stim2label = new JLabel("Stimulus gap 2");
        JLabel Iinjlabel = new JLabel("i (\u00B5A/cm\u00B2)");
        JLabel onlabel = new JLabel("On (ms)");
        JLabel offlabel = new JLabel("Off (ms)");
        JLabel totaltimelabel = new JLabel("Time(ms)\\dt(ms)");

        c.gridwidth = 1;

        c.gridx = 0;
        c.gridy = 0;
        inputPanel.add(totaltimelabel, c);

        c.gridx = 0;
        c.gridy = 1;
        inputPanel.add(stim1label, c);

        c.gridx = 0;
        c.gridy = 2;
        inputPanel.add(onlabel, c);

        c.gridx = 1;
        c.gridy = 2;
        inputPanel.add(offlabel, c);

        c.gridx = 2;
        c.gridy = 2;
        inputPanel.add(Iinjlabel, c);

        c.gridx = 0;
        c.gridy = 4;
        inputPanel.add(stim2label, c);
    }

    private void setButtons() {
        runButton = new JButton("Run");
        clearButton = new JButton("Clear");
        helpButton = new JButton("Help");

        runButton.addActionListener(this);
        clearButton.addActionListener(this);
        helpButton.addActionListener(this);

        presetInputButtons = new JButton[6];

        for (int i = 0; i < 6; i++) {
            presetInputButtons[i] = new JButton(String.valueOf(i + 1));
            presetInputButtons[i].addActionListener(this);
        }

        quitButton = new JButton("Quit");
        quitButton.addActionListener(this);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.insets = new Insets(0, 0, 5, 5);

        c.gridx = 0;
        c.gridy = 6;
        add(runButton, c);

        c.gridx = 1;
        c.gridy = 6;
        add(clearButton, c);

        c.gridx = 2;
        c.gridy = 6;
        add(helpButton, c);

        c.gridx = 3;
        c.gridy = 6;

        add(quitButton, c);
        for (int i = 0; i < 6; i++) {
            c.gridx = 4;
            c.gridy = i;
            add(presetInputButtons[i], c);
        }
    }

    private void setConstPanel() {
        constFieldLabels = new JLabel[7];
        for (HHModelConst.ConstName cn : HHModelConst.ConstName.values()) {
            constFieldLabels[cn.getIndex()] = new JLabel(cn.toString());
        }

        constantSpinners = new JSpinner[7];
        for (HHModelConst.ConstName cn : HHModelConst.ConstName.values()) {
            constantSpinners[cn.getIndex()] = new JSpinner(new SpinnerNumberModel(0, -1000, 1000, 0.1));
            constantSpinners[cn.getIndex()].setEditor(new JSpinner.NumberEditor(constantSpinners[cn.getIndex()],"#.##"));
        }
        
        int index = HHModelConst.ConstName.CM.getIndex();
        constantSpinners[index] = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 0.1));
        constantSpinners[index].setEditor(new JSpinner.NumberEditor(constantSpinners[index],"#.##"));


        updateConstFields();

        defaultButton = new JButton("Default");
        defaultButton.addActionListener(this);

        constPanel = new JPanel(new GridBagLayout());
        constPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 10;
        c.ipady = 10;

        constPanel.setBorder(BorderFactory.createEmptyBorder(
                5, //top
                5, //left
                5, //bottom
                5)); //right

        c.insets = new Insets(0, 0, 5, 5);
        for (int i = 0; i < 6; i = i + 2) {
            c.gridy = i;
            c.gridx = 0;
            constPanel.add(constFieldLabels[i], c);
            c.gridx = 1;
            constPanel.add(constantSpinners[i], c);
            c.gridx = 2;
            constPanel.add(constFieldLabels[i + 1], c);
            c.gridx = 3;
            constPanel.add(constantSpinners[i + 1], c);
        }

        c.gridy = 5;
        c.gridx = 0;
        constPanel.add(constFieldLabels[6], c);
        c.gridx = 1;
        constPanel.add(constantSpinners[6], c);

        c.gridx = 3;
        c.gridy = 5;
        constPanel.add(defaultButton, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object button = e.getSource();
        if (button == runButton) {

            updateData();
            listenerInterface.run(controlData);

        } else if (button == stim2CheckBox) {

            boolean selected = stim2CheckBox.isSelected();

            enableStim2Fields(selected);
            controlData.getSquareInput().setStim2Enable(selected);

        } else if (button == clearButton) {

            listenerInterface.clear();

        } else if (button == quitButton) {

            System.exit(0);

        } else if (button == defaultButton) {

            controlData.getConstants().restoreToDef();
            updateConstFields();

        } else if (button == dtComboBox) {
            double dt = (Double)dtComboBox.getSelectedItem();
            controlData.getSquareInput().setDeltaTime(dt);

        } else if (button == helpButton) {
            listenerInterface.showHelpDialog();
        } else {

            boolean pressed = false;
            for (int i = 0; i < presetInputButtons.length; i++) {
                if (button == presetInputButtons[i]) {
                    loadPreset(i);
                    pressed = true;
                }
            }

            if (pressed) {
                updateData();
                listenerInterface.run(controlData);
            }
        }
    }

    public void setActionListener(ControlListenerInterface listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    private void updateData() {
        HHModelConst cd = controlData.getConstants();
        HHModelConst.ConstName cn[] = HHModelConst.ConstName.values();
        for (HHModelConst.ConstName c : cn) {
            cd.setValue(c, ((Number) constantSpinners[c.getIndex()].getValue()).doubleValue());
        }

        SquareInputData id = controlData.getSquareInput();
        SquareInputData.Field f[] = SquareInputData.Field.values();
        for (SquareInputData.Field field : f) {
            id.setValue(field, ((Number) inputSpinners[field.getIndex()].getValue()).doubleValue());
        }
    }

    private void loadPreset(int i) {
        switch (i) {
            case 0:
                setInputSpinners(0, 50, 10., 0, 10, -10., 50, 0.05);
                break;
            case 1:
                setInputSpinners(2, 3, 10., 7, 8, 10., 50, 0.05);
                break;
            case 2:
                setInputSpinners(2, 3, 10., 25, 26, 10., 50, 0.05);
                break;
            case 3:
                setInputSpinners(0, 15, 2., 15, 50, 6., 50, 0.05);
                break;
            case 4:
                setInputSpinners(0, 15, 2., 15, 50, 10., 50, 0.05);
                break;
            case 5:
                setInputSpinners(0, 20, -10., 20, 50, 0., 50, 0.05);
                break;
        }
    }

    private void setInputSpinners(double on1, double off1, double Iinj1,
            double on2, double off2, double Iinj2,
            double totaltime, double dt) {

        inputSpinners[SquareInputData.Field.ON1.getIndex()].setValue(on1);
        inputSpinners[SquareInputData.Field.OFF1.getIndex()].setValue(off1);
        inputSpinners[SquareInputData.Field.I1.getIndex()].setValue(Iinj1);
        inputSpinners[SquareInputData.Field.ON2.getIndex()].setValue(on2);
        inputSpinners[SquareInputData.Field.OFF2.getIndex()].setValue(off2);
        inputSpinners[SquareInputData.Field.I2.getIndex()].setValue(Iinj2);
        inputSpinners[SquareInputData.Field.TIME.getIndex()].setValue(totaltime);
    }

    private void updateConstFields() {
        double[] val = controlData.getConstants().getValues();

        for (int i = 0; i < constantSpinners.length; i++) {
            constantSpinners[i].setValue(val[i]);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        double on1 = (Double) inputSpinners[SquareInputData.Field.ON1.getIndex()].getValue();
        double on2 = (Double) inputSpinners[SquareInputData.Field.ON2.getIndex()].getValue();
        double off1 = (Double) inputSpinners[SquareInputData.Field.OFF1.getIndex()].getValue();
        double off2 = (Double) inputSpinners[SquareInputData.Field.OFF2.getIndex()].getValue();
        double time = (Double) inputSpinners[SquareInputData.Field.TIME.getIndex()].getValue();

        if (time >= off1 && time >= off2 && on1 < off1 && on2 < off2) {
            setEnable(true);
        } else {
            setEnable(false);
        }
    }

}
