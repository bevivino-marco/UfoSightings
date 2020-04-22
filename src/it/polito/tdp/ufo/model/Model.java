package it.polito.tdp.ufo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.ufo.db.SightingsDAO;


public class Model {
	// creo il grafo
	private SimpleDirectedGraph<Sighting , DefaultEdge> grafo;
	Map <Integer, Sighting> mappaS;
	List <Sighting> listaS;
	SightingsDAO dao ;
	private HashMap backVisit;
	private int MAX =0;
	public Model () {
	dao = new SightingsDAO();
	}
    public void creaGrafo (int anno) {
    	mappaS = new HashMap <Integer, Sighting>();
    	listaS = new LinkedList <Sighting>();
    	grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
    	// creo vertici
    	/**
    	 * i vertici del grafo devono essere stati non sightings , con questa
    	 * query riempio il grafo con il terzo modo
    	 * SELECT  DISTINCT s1.state, s2.state
FROM sighting AS s1, sighting AS s2
WHERE  YEAR(s1.datetime)=2010 AND s1.state!=s2.state AND YEAR(s1.datetime)=YEAR(s2.datetime)
AND s1.datetime> s2.datetime AND s1.country=s2.country AND s1.country='us'
    	 */
    	mappaS= dao.getSightings(anno, mappaS);
    	listaS.addAll(mappaS.values());
    	Graphs.addAllVertices(grafo, listaS);
    	System.out.println(grafo.vertexSet().size());
    	// creo archi
    	for (Sighting s : grafo.vertexSet()) {
    		//List<Integer> l = new LinkedList<>(dao.getSuccessivi(anno,s.getDatetime(), mappaS));
    		for (Sighting s1 : grafo.vertexSet()) {
    			if (s.getDatetime().compareTo(s1.getDatetime())<0) {
    				if (!grafo.containsEdge(s, s1))
    			grafo.addEdge(s, s1);}
    		}
    	}
    	System.out.println(grafo.edgeSet().size());

    }
    public String analizza(String stato) {
    	String str= "la Lista dei precedenti e successivi e' : \n";
    	List<Sighting> l = new LinkedList <Sighting>();
    	for (Sighting s : grafo.vertexSet()) {
    		if (s.getState().compareTo(stato)==0) {
    			
				l.add(s);
    		}
    	}
    	List <String> raggiungibili = new LinkedList <>();
    	List <String> vicini = new LinkedList <String>();
    	for (Sighting s : l) {
    		
    		List <Sighting> l1 = new LinkedList<>(Graphs.neighborListOf(grafo, s));
    		List <Sighting> l2 = new LinkedList<>(raggiungibili(s));
    		
    		for (Sighting s1 : l1) {
    			if (!vicini.contains(s1.getState())) {
    				vicini.add(s1.getState());
    			}
    		}
    		for (Sighting s2 : l2) {
    			if (!raggiungibili.contains(s2.getState())) {
    				raggiungibili.add(s2.getState());
    			}
    		}
    	}
    	str+= vicini.toString()+"\nGli stati raggiungibili sono: \n"+raggiungibili.toString();
    	
		return str;
    	
    }
    
    public List<Sighting> raggiungibili(Sighting source){
    	List <Sighting> result = new ArrayList<Sighting>();
    	backVisit = new HashMap<>();
    	GraphIterator <Sighting, DefaultEdge> it = new DepthFirstIterator<>(this.grafo, source);
    	//GraphIterator <Fermata, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, source);
        backVisit.put(source, null); // la radice non ha un padre
    	//it.addTraversalListener(new Model.EdgeTraversedGraphListener());
    	
    	/*it.addTraversalListener(new TraversalListener<Sighting, DefaultEdge>() {

    		@Override
    		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {
    			Sighting sourceVertex  = grafo.getEdgeSource(ev.getEdge());
    			Sighting targetVertex = grafo.getEdgeTarget(ev.getEdge());
    			/**
    			 * se il grafo è orientato allora il source sara il parente e il target sara il child.
    			 * se non è orientato, potrebbe anche essere il contrario.
    			 *//*
    			if (!backVisit.containsKey(targetVertex) && backVisit.containsKey(sourceVertex)) {
    				backVisit.put(targetVertex, sourceVertex);
    			}else if (!backVisit.containsKey(sourceVertex) && backVisit.containsKey(targetVertex)) {
    				backVisit.put(sourceVertex, targetVertex);
    			}
    			
    			
    		}

    		@Override
    		public void vertexFinished(VertexTraversalEvent<Sighting> arg0) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void vertexTraversed(VertexTraversalEvent<Sighting> arg0) {
    			// TODO Auto-generated method stub
    			
    		}});
    		*/
    	
    	while (it.hasNext()) {
    		result.add(it.next());
    	}
    	//System.out.println(backVisit);
    	return result;
    }
	public List<Annata> getAnni() {
		
		return dao.getAnnate();
	}
	public List <String> getStati() {
		List <String> stati = new LinkedList <String>();
		for (Sighting s : listaS) {
			if (!stati.contains(s.getState())) {
				stati.add(s.getState());
			}
		}
		return stati;
	}
    
    public List <Sighting> trovaSequenza(String stato){
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
	    
		
	}
    
    
    
    
    
    
    
}
