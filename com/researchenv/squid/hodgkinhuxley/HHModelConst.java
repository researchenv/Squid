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

package com.researchenv.squid.hodgkinhuxley;

public class HHModelConst {
    
    /*constVal|defConst = {gna ,ena ,gk , ek , gleak, eleak ,cm}*/
    private final double[] defConst = new double[]{120., 115., 36., -12., 0.3, 10.16, 1.0}; //default values
    private final double[] constVal = new double[]{120., 115., 36., -12., 0.3, 10.16, 1.0};

    public enum ConstName {
        GNA(0, "gna"),
        ENA(1, "ena"),
        GK(2,   "gk"),
        EK(3,   "ek"),
        GVAZ(4, "gl"),
        EVAZ(5, "el"),
        CM(6,   "cm"); //membrane capacitance

        private ConstName(int i, String name) {
            index = i;
            this.name=name;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString(){
            return name;
        }
        
        private final int index;
        private final String name;
    }

    public HHModelConst() {
        restoreToDef();
    }

    public HHModelConst(double gna, double ena, double gk,
                        double ek, double gl, double el,
                        double cm) {
        setValues(gna, ena, gk, ek, gl, el, cm);
    }

    public final void setValues(double gna, double ena, double gk,
                                double ek, double gl, double el,
                                double cm) {
        constVal[ConstName.GNA.getIndex()] = gna;
        constVal[ConstName.ENA.getIndex()] = ena;
        constVal[ConstName.GK.getIndex()] = gk;
        constVal[ConstName.EK.getIndex()] = ek;
        constVal[ConstName.GVAZ.getIndex()] = gl;
        constVal[ConstName.EVAZ.getIndex()] = el;
        constVal[ConstName.CM.getIndex()] = cm;
    }

    public double getValue(ConstName cn) {
        return constVal[cn.getIndex()];
    }

    public double[] getValues() {
        return constVal;
    }

    public void setValue(ConstName cn, double value) {
        constVal[cn.getIndex()] = value;
    }

    public final void restoreToDef() {
        System.arraycopy(defConst, 0, constVal, 0, constVal.length);
    }
}
