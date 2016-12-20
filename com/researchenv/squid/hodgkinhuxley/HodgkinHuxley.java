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

public class HodgkinHuxley {
    
    private double[] v,m,h,n; // v menbrane potential; m,n,h activation functions.
    private double[] ctes;    // model constants, see HHModelConst.

    public HodgkinHuxley() {
        ctes = new HHModelConst().getValues();
    }

    public HodgkinHuxley(HHModelConst hhmc) {
        ctes = hhmc.getValues();
    }

    
    public final void setModelConstants(HHModelConst hhmc){
        ctes = hhmc.getValues();
    }
    
    public double[] getV() {
        return v;
    }

    public double[] getM() {
        return m;
    }

    public double[] getH() {
        return h;
    }

    public double[] getN() {
        return n;
    }
    
    public void run(AbstStimulusInput is) {
        
        int size = is.getCurrent().length;
        
        v = new double[size+1];
        m = new double[size+1];
        h = new double[size+1];
        n = new double[size+1];
        
        v[0]=0;
        m[0]=0.0529325;
        h[0]=0.59612075;
        n[0]=0.31767676;
        
        evalRungeKutta4(is.getCurrent(), is.getDeltaTime());
    }
    
    private void evalRungeKutta4(double[] current, double dt) {
        double vx,mx,hx,nx;
        
        double[] k1 = new double[4];
        double[] k2 = new double[4];
        double[] k3 = new double[4];
        double[] k4 = new double[4];
        
        int itNum = current.length;
        
        for(int i=0; i<itNum; i++) {

            double crr = current[i];
        
            k1[0]=v_tax(v[i],m[i],h[i],n[i],crr, ctes)*dt;
            k1[1]=m_tax(v[i],m[i])*dt;
            k1[2]=h_tax(v[i],h[i])*dt;
            k1[3]=n_tax(v[i],n[i])*dt;

            vx=v[i]+.5*k1[0]; 
            mx=m[i]+.5*k1[1]; 
            hx=h[i]+.5*k1[2]; 
            nx=n[i]+.5*k1[3];

            k2[0]=v_tax(vx,mx,hx,nx,crr, ctes)*dt;
            k2[1]=m_tax(vx,mx)*dt;
            k2[2]=h_tax(vx,hx)*dt;
            k2[3]=n_tax(vx,nx)*dt;

            vx=v[i]+.5*k2[0]; 
            mx=m[i]+.5*k2[1]; 
            hx=h[i]+.5*k2[2]; 
            nx=n[i]+.5*k2[3];

            k3[0]=v_tax(vx,mx,hx,nx,crr, ctes)*dt;
            k3[1]=m_tax(vx,mx)*dt;
            k3[2]=h_tax(vx,hx)*dt;
            k3[3]=n_tax(vx,nx)*dt;

            vx=v[i]+k3[0]; 
            mx=m[i]+k2[1]; 
            hx=h[i]+k2[2]; 
            nx=n[i]+k2[3];

            k4[0]=v_tax(vx,mx,hx,nx,crr, ctes)*dt;
            k4[1]=m_tax(vx,mx)*dt;
            k4[2]=h_tax(vx,hx)*dt;
            k4[3]=n_tax(vx,nx)*dt;

            v[i+1]=v[i]+(k1[0]+2*(k2[0]+k3[0])+k4[0])/6.0;
            m[i+1]=m[i]+(k1[1]+2*(k2[1]+k3[1])+k4[1])/6.0;
            h[i+1]=h[i]+(k1[2]+2*(k2[2]+k3[2])+k4[2])/6.0;
            n[i+1]=n[i]+(k1[3]+2*(k2[3]+k3[3])+k4[3])/6.0;
        }
    }

    private double v_tax(double v, double m, double h, double n, double crr, double[] ctes) {
        double v_out;
        v_out=(1./ctes[6])*(ctes[0]*Math.pow(m,3.)*h*(-v+ctes[1])
        + ctes[2]*Math.pow(n,4.)*(-v+ctes[3])
        + ctes[4]*(-v+ctes[5])
        + crr);
        return v_out;
    }

    private double m_tax(double v, double m) {
        double Am,Bm;
        if(v==25.) {
            Am=1.0;
        } else {
            Am=0.1*(25.-v)/(Math.exp(.1*(25.-v))-1.);
        }
        Bm=4.*Math.exp(-v/18.);
        return (Am*(1.-m)-Bm*m);
    }

    private double h_tax(double v, double h) {
        double Ah,Bh;
        Ah=0.07*Math.exp(-v/20.);
        Bh=1./(Math.exp(.1*(30.-v))+1.);
        return (Ah*(1.-h)-Bh*h);
    }

    private double n_tax(double v, double n) {
        double An,Bn;
        if(v==10.) {
            An=.1;
        } else {
            An=0.01*(10.-v)/(Math.exp(.1*(10.-v))-1.);
        }
        Bn=0.125*Math.exp(-v/80.);
        return (An*(1.-n)-Bn*n);
    }
}