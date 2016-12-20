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

public class SquareInputData {
    
    private final double[] input;
    private double dt;
    boolean stim2;

    public enum Field{
        ON1(0,"on1"),
        OFF1(1,"off1"),
        I1(2,"i1"),
        ON2(3,"on2"),
        OFF2(4,"off2"),
        I2(5,"i2"),
        TIME(6,"Time");

        private Field(int index, String fName){
            this.index=index;
            this.fName=fName;
        }

        public int getIndex(){
            return index;
        }
        
        @Override
        public String toString(){
            return fName;
        }
        
        private final int index;
        private final String fName;
    }
    
    public SquareInputData() {
        //input = {on1,off1,i1,on2,off2,i2,totaltime,dt}
        //i1 current gap overwrites i2 current gap
        input = new double[]{0.0,50.0,10.0,0.0,10.0,-10.0,50.0,0.05};
        stim2 = false;
    }
    
    public SquareInputData(double on1, double off1, double i1, double on2, double off2, double i2, double totaltime, double dt) {
        input = new double[]{on1,off1,i1,on2,off2,i2,totaltime,dt};
        stim2=false;
    }
    
    public double getValue(Field f){
        return input[f.getIndex()];
    }
    
    public double[] getValues(){
        return input;
    }
    
    public double getDeltaTime(){
       return dt;
    }
    
    public void setValue(Field f, double value){
        input[f.getIndex()]=value;
    }
    
    public final void setValues(double on1, double off1, double i1, 
                                double on2, double off2, double i2,
                                double totaltime) {
        
        input[Field.ON1.getIndex()] = on1;
        input[Field.OFF1.getIndex()] = off1;
        input[Field.I1.getIndex()] = i1;
        input[Field.ON2.getIndex()] = on2;
        input[Field.OFF2.getIndex()] = off2;
        input[Field.I2.getIndex()] = i2;
        input[Field.TIME.getIndex()] = totaltime;
    }
    
    public void setDeltaTime(double dt){
        this.dt=dt;
    }
    
    public void setStim2Enable(boolean enable){
        stim2 = enable;
    }

    public boolean isStim2Enable() {
        return stim2;
    }
}
