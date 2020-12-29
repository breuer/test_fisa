package com.fisa.ejercicio;


import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	//private App app = new App();
    private Graph<String, DefaultWeightedEdge> g;
    
    @Before
    public void init() {
    	g = App.loadGraph("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7");
    }
    
    @Test
    public void distance_route_ABC() {
    	Assert.assertEquals("9.0", App.getRouteDistance(g, "A-B-C"));
    }  
    @Test
    public void distance_route_AD() {
    	Assert.assertEquals("5.0", App.getRouteDistance(g, "A-D"));
    }
    @Test
    public void distance_route_ADC() {
    	Assert.assertEquals("13.0", App.getRouteDistance(g, "A-D-C"));
    }
    @Test
    public void distance_route_AEBCD() {
    	Assert.assertEquals("22.0", App.getRouteDistance(g, "A-E-B-C-D"));
    }
    @Test
    public void distance_route_AED() {
    	Assert.assertEquals("NO SUCH ROUTE", App.getRouteDistance(g, "A-E-D"));
    }
    @Test
    public void trips_starting_C_ending_C_maximum_3_stops() {
    	Assert.assertEquals(2, App.countPathsWithMaxNumberOfEdges(g, "C", "C", 3));
    }
    @Test
    public void trips_starting_A_ending_C_exactly_4_stops() {
    	Assert.assertEquals(3, App.countPathsWithExactNumberOfEdges(g, "A", "C", 4));
    }
    @Test
    public void length_shortest_route_from_A_to_C() {
    	Assert.assertEquals("9.0", App.getLengthShortestPath(g, "A", "C")); 
    }
    @Test
    public void length_shortest_route_from_B_to_B() {
    	Assert.assertEquals("9.0", App.getLengthShortestPath(g, "B", "B"));
    }
    @Test
    public void  number_different_routes_from_C_to_C_distance_less_than_30() {
    	Assert.assertEquals(7, App.countPathsWeightLessThan(g, "C", "C", 30));
    }

}
