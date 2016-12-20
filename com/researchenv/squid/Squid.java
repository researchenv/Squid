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

import com.researchenv.squid.hodgkinhuxley.AbstStimulusInput;
import com.researchenv.squid.hodgkinhuxley.HHModelConst;
import com.researchenv.squid.hodgkinhuxley.HodgkinHuxley;
import javax.swing.SwingWorker;

public class Squid {

    static final String SQUID_VERSION = "1.0";
    private final HodgkinHuxley hh;
    private SquidListenerInterface listenerInterface;
    
    public Squid(){
        hh = new HodgkinHuxley();
    }

    public void setListenerInterface(SquidListenerInterface listenerInterface) {
        this.listenerInterface = listenerInterface;
    }
    
    public void runInBackground(AbstStimulusInput si, HHModelConst hhc)
    {
        new Exec(si,hhc).execute();
    }
    
    public double[] getSpike(){
        return hh.getV();
    }
    
    public double[] getMAct(){
        return hh.getM();
    }
    
    public double[] getNAct(){
        return hh.getN();
    }
    public double[] getHAct(){
        return hh.getH();
    }
    
    private class Exec extends SwingWorker<Object, Object> {

        private final AbstStimulusInput si;
        private final HHModelConst hhc;

        public Exec(AbstStimulusInput si, HHModelConst hhc) {
            this.si=si;
            this.hhc=hhc;
        }

        @Override
        protected Object doInBackground() throws Exception {
            
            //set HodgkinHuxley model constants
            hh.setModelConstants(hhc);

            //run HodgkinHuxley model calculations
            hh.run(si);

            if(listenerInterface!=null){
                listenerInterface.calculationDone();
            }
            
            return null;
        }
    }    
}
