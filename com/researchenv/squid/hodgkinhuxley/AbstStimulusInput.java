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

public abstract class AbstStimulusInput {
    protected final double deltaTime;
    protected final double time;
    protected double[] current;

    public AbstStimulusInput(double deltaTime, double time) {
        this.deltaTime = deltaTime;
        this.time = time;
        current = new double[(int) (time / deltaTime)+1];
    }
    
    public final double getDeltaTime() {
        return deltaTime;
    }

    public final double getTime() {
        return time;
    }

    public final double[] getCurrent(){
        return current;
    }
    
    protected final void setStmValue(int index, double value){
        if(index<current.length){
            current[index]=value;
        }
    }
}
