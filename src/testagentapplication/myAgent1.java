/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testagentapplication;

import jade.core.Agent;

/**
 *
 * @author Dilukshan Mahendra
 */
public class myAgent1 extends Agent{
    static int val;
    setGui a;
    @Override
    protected void setup(){
    a = new setGui();
    a.setVisible(true);
    System.out.println("set GUI loaded");
    }
}
