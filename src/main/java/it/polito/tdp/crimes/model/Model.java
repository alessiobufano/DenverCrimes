package it.polito.tdp.crimes.model;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Map<Long, Event> eIdMap;
	private List<MonthYear> months;
	private List<String> categories;
	private List<CrimesCouple> crimesCouple;
	private Graph<String, DefaultWeightedEdge> graph;
	private LinkedList<String> bestPath;
	
	public Model() {
		this.dao = new EventsDao();
		
		this.categories = new LinkedList<>(this.dao.getCategories());
		this.months = new LinkedList<>(this.dao.getMonths());
		
		this.eIdMap = new HashMap<>();
		this.dao.setAllEvents(eIdMap);
	}
	
	public List<MonthYear> getMonths() {
		return months;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setGraph(String category, int month, int year) {
		
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.graph, this.dao.setVertices(eIdMap, category, year, month));
		
		this.crimesCouple = new LinkedList<>(this.dao.setEdges(category, year, month));
		for(CrimesCouple cc : this.crimesCouple)
			Graphs.addEdge(this.graph, cc.getCrime1(), cc.getCrime2(), cc.getNeighborhoodNumber());
		
	}
	
	public int verticesNumber() {
		return this.graph.vertexSet().size();
	}
	
	public int edgesNumber() {
		return this.graph.edgeSet().size();
	}
	
	public List<CrimesCouple> getOverWeightedEdgesList() {
		
		double averageWeight = 0.0;
		for(DefaultWeightedEdge e : this.graph.edgeSet()) {
			averageWeight += this.graph.getEdgeWeight(e);
		}
		averageWeight = averageWeight/this.graph.edgeSet().size();
		
		List<CrimesCouple> list = new LinkedList<>();		
		for(CrimesCouple cc : this.crimesCouple)
			if(cc.getNeighborhoodNumber()>=averageWeight)
				list.add(cc);
		Collections.sort(list);

		return list;
	}
	
	public String printOverWeightedEdgesList() {
		
		double averageWeight = 0.0;
		for(DefaultWeightedEdge e : this.graph.edgeSet()) {
			averageWeight += this.graph.getEdgeWeight(e);
		}
		averageWeight = averageWeight/this.graph.edgeSet().size();
		
		String list = "The crimes with more than "+averageWeight+" common neighborhoodes, are:\n";
		for(CrimesCouple cc : this.getOverWeightedEdgesList())
			list += ""+cc.toString()+", common neighborhoodes number = "+cc.getNeighborhoodNumber()+"\n";
		
		return list;
	}
	
	public List<String> getLongestPath(String source, String destination) {
		
		List<String> partial = new LinkedList<>();
		this.bestPath = new LinkedList<>();
		
		partial.add(source);
		recursiveFunction(destination, partial, 0);
		
		return this.bestPath;
	}

	private void recursiveFunction(String destination, List<String> partial, int level) {

		//terminal case --> when last vertex == destination
		if(partial.get(partial.size() - 1).equals(destination))
		{
			if(partial.size()>this.bestPath.size())
				this.bestPath = new LinkedList<>(partial);
			return;
		}
		
		//intermediate case --> reading neighbors of last vertex
		for(String neighbor : Graphs.neighborListOf(this.graph, partial.get(partial.size() -1))) 
		{
			//path is acyclic -> check if the vertex isn't already in the partial solution
			if(!partial.contains(neighbor)) 
			{
				partial.add(neighbor);
				this.recursiveFunction(destination, partial, level+1);
				partial.remove(partial.size() -1);
			}
		}
	}
}
