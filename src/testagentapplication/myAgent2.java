/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testagentapplication;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author Dilukshan Mahendra
 */
public class myAgent2 extends Agent{
    getGui b;
    @Override
     protected void setup(){
    b = new getGui();    
    b.setVisible(true);
    System.out.println("get GUI loaded");
    addBehaviour(new MyValue());
    }
    
    public class MyValue extends Behaviour{

        @Override
        public void action() {
            getGui.lbl_value.setText(Integer.toString(myAgent1.val));
        }

        @Override
        public boolean done() {
           return false;
        }
    
    }
}
