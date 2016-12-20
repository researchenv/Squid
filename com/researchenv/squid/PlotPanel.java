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

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotPanel extends JPanel {

    private XYSeries series1;
    private XYSeries series2;
    private XYSeries series3;
    private XYSeries series4;
    private final XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    public PlotPanel(String title, String xlabel, String ylabel) {
        dataset = new XYSeriesCollection();
        series1 = new XYSeries(" ");
        dataset.addSeries(series1);
        createChart(title, xlabel, ylabel, false);
    }

    public PlotPanel(String title, String xlabel, String ylabel, String[] legends) {
        dataset = new XYSeriesCollection();
        series2 = new XYSeries(legends[0]);
        series3 = new XYSeries(legends[1]);
        series4 = new XYSeries(legends[2]);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        createChart(title, xlabel, ylabel, true);
    }

    public final void createChart(String title, String xlabel, String ylabel, boolean legendstate) {
        chart = ChartFactory.createXYLineChart(
                title, // chart title
                xlabel, // x axis label
                ylabel, // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                legendstate, // include legend
                true, // tooltips
                false // urls
        );

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        chartPanel = new ChartPanel(chart);
        add(chartPanel);
        chart.setBackgroundPaint(null);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.lightGray);
        chart.getXYPlot().setRangeGridlinePaint(Color.lightGray);
        chart.getXYPlot().setDomainGridlinesVisible(true);
        chart.getXYPlot().setRangeGridlinesVisible(true);
    }

    public void runPlot(double dt, double[] y) {
        dataset.setNotify(false);
        series1.clear();
        for (int i = 0; i < y.length; i++) {
            series1.add((double) i * dt, y[i]);
        }
        dataset.setNotify(true);
    }

    public void runPlot(double dt, double[] m, double[] h, double[] n) {
        dataset.setNotify(false);
        series2.clear();
        series3.clear();
        series4.clear();
        for (int i = 0; i < m.length; i++) {
            series2.add((double) i * dt, m[i]);
        }
        for (int i = 0; i < h.length; i++) {
            series3.add((double) i * dt, h[i]);
        }
        for (int i = 0; i < n.length; i++) {
            series4.add((double) i * dt, n[i]);
        }
        dataset.setNotify(true);
    }

    public void clear() {
        if(series1!=null){
            series1.clear();
        } else {
            series2.clear();
            series3.clear();
            series4.clear();
        }
        chartPanel.restoreAutoBounds();
    }
}
