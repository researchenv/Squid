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
import java.util.ArrayList;

public class SquareStimulusInput extends AbstStimulusInput {

    private final ArrayList<Gap> gaps;
    
    public SquareStimulusInput(SquareInputData id) {
        super(id.getDeltaTime(), id.getValue(SquareInputData.Field.TIME));
        gaps = new ArrayList<>();
        addGap(id.getValue(SquareInputData.Field.ON1), id.getValue(SquareInputData.Field.OFF1), id.getValue(SquareInputData.Field.I1));
        if(id.isStim2Enable()){
            addGap(id.getValue(SquareInputData.Field.ON2), id.getValue(SquareInputData.Field.OFF2), id.getValue(SquareInputData.Field.I2));
        }
    }
    
    private void addGap(double begin, double end, double current) {
        gaps.add(new Gap(begin, end, current));
    }

    public void builStimulus() {
        for (Gap gap : gaps) {
            int begin = (int) (gap.start / deltaTime);
            int end = (int) (gap.end / deltaTime);
            double crr = gap.current;
            for (int i = begin; i <= end; i++) {
                setStmValue(i, crr);
            }
        }
    }

    private class Gap {
        public double start;
        public double end;
        public double current;

        public Gap(double start, double lenght, double current) {
            this.start = start;
            this.end = lenght;
            this.current = current;
        }
    }

}
