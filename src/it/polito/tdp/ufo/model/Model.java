package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import it.polito.tdp.ufo.db.SightingsDAO;


public class Model {
	// creo il grafo
	private SimpleDirectedGraph<String , DefaultEdge> grafo;
	Map <String, String> mappaS;
	List <String> listaS;
	SightingsDAO dao ;
	private HashMap backVisit;
	private int MAX =0;
	private List <String>finale;
	public Model () {
	dao = new SightingsDAO();
	}
    public void creaGrafo (int anno) {
    	mappaS = new HashMap <String, String>();
    	
    	grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
    	
    	/**
    	 * i vertici del grafo devono essere stati non sightings , con questa
    	 * query riempio il grafo con il terzo modo
    	 * SELECT  DISTINCT s1.state, s2.state
FROM sighting AS s1, sighting AS s2
WHERE  YEAR(s1.datetime)=2010 AND s1.state!=s2.state AND YEAR(s1.datetime)=YEAR(s2.datetime)
AND s1.datetime> s2.datetime AND s1.country=s2.country AND s1.country='us'
    	 */
    	mappaS = dao.getStatiV(anno, mappaS);
    	listaS = new LinkedList <String>(mappaS.values());
    	// creo vertici
    	Graphs.addAllVertices(this.grafo, this.listaS);
    	// creo gli archi
    	for (String s1 : grafo.vertexSet()) {
    		for (String s2 : grafo.vertexSet()) {
    			if (!s1.equals(s2)&&dao.esisteArco(s1, s2, anno)) {
    				grafo.addEdge(s1, s2);
    			}
    		}
    	}
    	System.out.println("vertici : "+ grafo.vertexSet().size()
    			+"\n archi : "+ grafo.edgeSet().size());
    }
    public String analizza (String stato) {
    	String str ="";
    	str+= "I predecessori : \n"+getPredecessori(stato)+"\nI successori :\n"+getSuccessori(stato).toString()+"\nRaggiungibili\n"+getRaggiungibili(stato);
		return str;
    	
    }
   private String getRaggiungibili(String stato) {
	   List <String> result = new ArrayList<String>();
		backVisit = new HashMap<>();
		GraphIterator <String, DefaultEdge> it = new DepthFirstIterator<>(this.grafo, stato);
		//GraphIterator <String, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, stato);
	    it.next();
		while (it.hasNext()) {
			result.add(it.next());
		}
		
		return result.toString();
	}
private List<String> getSuccessori(String stato) {
	
		return Graphs.successorListOf(grafo, stato);
	}
   public List <String> trovaSequenza (String stato){
	   List <String> parziale = new LinkedList<>();
      finale = new LinkedList<>();
      parziale.add(stato);
   	cerca (parziale, stato);
	return finale;
	   
   }
   private void cerca (List <String>parziale, String stato) {
	   if (parziale.size()>finale.size()) {
		   finale = new LinkedList<String> (parziale);
	   }
	   List <String> vicini = new LinkedList <String>(getSuccessori(parziale.get(parziale.size()-1)));
	   if (vicini.size()==0)
		   return;
	   for (String s : vicini) {
		   if (!parziale.contains(s)) {
			   parziale.add(s);
			   cerca (parziale, stato);
			   parziale.remove(s);
		   }
	   }
	   
   } 
/* public List <Sighting> trovaSequenza(String stato){
    	List <Sighting> parziale = new LinkedList<>();
    	List <Sighting> finale = new LinkedList<>();
    	List <String> stati = new LinkedList <>(getStati());
    	List <String> passati = new LinkedList<>();
    	List <Sighting> iniziale = new LinkedList<>();
    	for (Sighting s : listaS) {
    		if (s.getState().compareTo(stato)==0) {
    			iniziale.add(s);
    		}
    	}
		cerca (parziale,iniziale, finale, stato, passati, 0);
		return finale;
    	
    }
	private void cerca(List<Sighting> parziale, List<Sighting>iniziale, List<Sighting> finale, String stato, List<String> passati, int livello) {
		
		List <Sighting> vicini = new LinkedList<>();
	    if (livello == 0) {
	    	vicini.addAll(iniziale);
	    }else {
	    	vicini.addAll(Graphs.neighborListOf(grafo,
				parziale.get(parziale.size()-1)));
	    }
	   
	    	if (livello >MAX ) { 
	    		MAX = livello;
	    		finale.clear();
	    		finale.addAll(parziale);
	    		System.out.println(finale);
	    		}
	    		
	    
	    
	    for (Sighting s : vicini) {
	    	if (!passati.contains(s.getState()) && Graphs.neighborListOf(grafo,s).size()>0) {
	    		passati.add(s.getState());
	    		parziale.add(s);
	    		
	    		cerca(parziale, iniziale, finale,stato , passati, livello+1);
	    		parziale.remove(s);
	    	}
	    }
	    
		
	}*/
	private String getPredecessori(String stato) {
		
		return Graphs.predecessorListOf(this.grafo, stato).toString();
		
	}
	public List<Annata>getAnni() {
		// TODO Auto-generated method stub
		return dao.getAnnate();
	}
	public List<String> getStati() {
		
		return listaS;
	}
	public String parametriGrafo() {
	String str = "N.Vertici : \n"+grafo.vertexSet().size()+"\nN.Archi :\n"+grafo.edgeSet().size()+";";
		return str;
	}
    
    
    
    
    
    
    
}
