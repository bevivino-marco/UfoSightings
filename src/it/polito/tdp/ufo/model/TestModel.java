package it.polito.tdp.ufo.model;

public abstract class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		model.creaGrafo(1949);
        //System.out.println(model.analizza("tx"));
        //System.out.println(model.getAnni());
        model.analizza("tx");
        
      // System.out.println("hsrghpkign"+model.trovaSequenza("md")+"\n");
        for (Sighting s : model.trovaSequenza("md")) {
        	System.out.println(s.getState()+"\n");
        }
	}

}
