package com.fisa.ejercicio;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.EppsteinKShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class App 
{
	private static String NO_SUCH_ROUTE = "NO SUCH ROUTE";
	private static int MAX_PATHS = 1000;
	
	/*
	 * Get sum of vertex list
	 * 
	 * @param	graph
	 * @param	list	list of vertex
	 * @return	sum of edge weight or -1 if an edge not exist	
	 */
	private static double getSumEdgeWeightPath(Graph<String, DefaultWeightedEdge> graph, List<String> list) {
		double sum = 0;
		Iterator<String> i = list.iterator();
		String sVertex = i.next();
		while(i.hasNext()) {
			String tVertex = i.next();
			if(graph.containsEdge(sVertex, tVertex)) {
		    	DefaultWeightedEdge e = graph.getEdge(sVertex, tVertex);
		    	sum += graph.getEdgeWeight(e);
		    } else {
		    	return -1;
		    }
			sVertex = tVertex;
		}
		return sum;
	}
	
	/*
	 * Get string splitted by a token
	 * 
	 * @param	str 	
	 * @param 	token
	 * @return	list of string splitted
	 */
	private static List<String> splitString(String str, String token) {
	    return Arrays.stream(str.split(token))
	    		  .map(String::trim)
	    		  .collect(Collectors.toList());
	}
		
	/*
	 * Get route distance
	 * 
	 * @param	graph	
	 * @param	route	String with route in format A-B-C.. etc
	 * @return	sum of weight of edges or -1 if an edge no exist
	 */
	public static String getRouteDistance(Graph<String, DefaultWeightedEdge> graph, String route) {		
		List<String> listVertex = splitString(route, "-");
		double result = getSumEdgeWeightPath(graph, listVertex);
		if(result < 0) 			
			return NO_SUCH_ROUTE;
		return String.valueOf(result);
	}
	
	/*
	 * Calculate all paths from the source vertex to the target vertex
	 * 
	 * @param	graph
	 * @param 	sVertex 	source vertex
	 * @param 	tVertex		target vertex	 
	 * @param	maxPathLength 	maximum number of edges to allow in a path
	 * @return	list of paths
	 * 
	 * */
	private static List<GraphPath<String, DefaultWeightedEdge>> getAllPahts(Graph<String, DefaultWeightedEdge> graph, String sVertex, String tVertex, int maxPathLength) {
		AllDirectedPaths<String, DefaultWeightedEdge> adp = new AllDirectedPaths<>(graph);
	    return adp.getAllPaths(sVertex, tVertex, false, maxPathLength);
	}
	
	/*
	 * Count paths with a maximum number of edges
	 * @param	graph
	 * @param 	sVertex 	source vertex
	 * @param 	tVertex		target vertex
	 * @returm	sum of paths
	 */
	public static long countPathsWithMaxNumberOfEdges(Graph<String, DefaultWeightedEdge> graph, String sVertex, String tVertex, int maxPathLength) {
	    return getAllPahts(graph, sVertex, tVertex, maxPathLength).stream()
		    	.filter(x -> x.getLength() > 0)
		    	.count();
	}

	/*
	 * Count paths with exact number of edges
	 * @param	graph
	 * @param 	sVertex 	source vertex
	 * @param 	tVertex		target vertex
	 * @returm	sum of paths
	 */
	public static long countPathsWithExactNumberOfEdges(Graph<String, DefaultWeightedEdge> graph, String sVertex, String tVertex, int maxPathLength) {
		return getAllPahts(graph, sVertex, tVertex, maxPathLength).stream()
				.filter(x -> x.getLength() == maxPathLength)
				.count();
	}
	
	/*
	 * Sum weight of shortest path
	 * 
	 * @param	graph
	 * @param 	sVertex 	source vertex
	 * @param 	tVertex		target vertex
	 * @returm	sum of paths
	 */
	public static String getLengthShortestPath(Graph<String, DefaultWeightedEdge> graph, String sVertex, String tVertex) {
		EppsteinKShortestPath<String, DefaultWeightedEdge> e = new EppsteinKShortestPath<>(graph);
		GraphPath<String, DefaultWeightedEdge> path = 
				e.getPaths(sVertex, tVertex, 2).stream()
				.filter(x -> x.getWeight() != 0)
				.min(Comparator.comparingDouble(GraphPath<String, DefaultWeightedEdge>::getWeight))
				.get();	
		return String.valueOf(path.getWeight());
	}
	
	/*
	 * Count paths with weight less than a value
	 * @param	graph
	 * @param 	sVertex 	source vertex
	 * @param 	tVertex		target vertex
	 * @param 	maxWeight	maximum value of weigth
	 * @return	count paths
	 */
	public static long countPathsWeightLessThan(Graph<String, DefaultWeightedEdge> graph, String sVertex, String tVertex, double maxWeight) {
		EppsteinKShortestPath<String, DefaultWeightedEdge> e = new EppsteinKShortestPath<>(graph);
		return  e.getPaths(sVertex, tVertex, MAX_PATHS)
					.stream()
					.filter(x -> x.getWeight() < maxWeight && x.getWeight() != 0)
					.count();
	}
		
	/*
	 * Load a directed graph
	 * 
	 * @param 	input 	String with edges and weights
	 * @return	a directed graph
	 */
	public static Graph<String, DefaultWeightedEdge> loadGraph(String input) {
		List<String> list = splitString(input, ",");
		Set<String> set = new HashSet<>();
		for(String x : list) {
			set.add(x.substring(0, 1));
			set.add(x.substring(1, 2));
		}
		Graph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		set.forEach(x -> g.addVertex(x));
		for(String x : list) {
			DefaultWeightedEdge dwe = g.addEdge(x.substring(0, 1), x.substring(1, 2)); 
			g.setEdgeWeight(dwe, Double.valueOf(x.substring(2)));
		}
		return g;
	}
	
	public static void main(String[] args) {

	     //load graph
	     Graph<String, DefaultWeightedEdge> g = loadGraph("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7");
	     System.out.println(g);
	     //1. The distance of the route A-B-C.
	     //2. The distance of the route A-D.
	     //3. The distance of the route A-D-C.
	     //4. The distance of the route A-E-B-C-D.
	     //5. The distance of the route A-E-D.
	     
	     System.out.println("Output #1: " + getRouteDistance(g, "A-B-C"));
	     System.out.println("Output #2: " + getRouteDistance(g, "A-D"));
	     System.out.println("Output #3: " + getRouteDistance(g, "A-D-C"));
	     System.out.println("Output #4: " + getRouteDistance(g, "A-E-B-C-D"));
	     System.out.println("Output #5: " + getRouteDistance(g, "A-E-D"));
	     
	   
	     //6. The number of trips starting at C and ending at C with a maximum of 3 stops.  
	     //In the sample data below, there are two such trips: C-D-C (2 stops). and C-E-B-C (3 stops).
	     
	     System.out.println("Output #6: " + countPathsWithMaxNumberOfEdges(g, "C", "C", 3));
	     
	     //7. The number of trips starting at A and ending at C with exactly 4 stops.  
	     //In the sample data below, there are three such trips: A to C (via B,C,D); A to C (via D,C,D); and A to C (via D,E,B).
	     
	     System.out.println("Output #7: " + countPathsWithExactNumberOfEdges(g, "A", "C", 4));
	     
	     //8. The length of the shortest route (in terms of distance to travel) from A to C.
	     System.out.println("Output #8: " + getLengthShortestPath(g, "A", "C"));
	     
	     //9. The length of the shortest route (in terms of distance to travel) from B to B.
	     System.out.println("Output #9: " + getLengthShortestPath(g, "B", "B"));
	     
	     //10. The number of different routes from C to C with a distance of less than 30.  
	     //In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC.

	     System.out.println("Output #10: " + countPathsWeightLessThan(g, "C", "C", 30));

	}
}
