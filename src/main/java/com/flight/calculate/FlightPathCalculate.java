package com.flight.calculate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//class to rerturn all flight paths possible for given 2 locations
public class FlightPathCalculate{
	
	
	// No. of vertices in graph
	private int v;
	
	private String pathStr = "";
    
	//adjacency list
    private ArrayList<Integer>[] adjList;

    
    public FlightPathCalculate(int vertices){
    	this.v = vertices;
    	
    	
    	// initialise adjacency list
        initAdjList();
    }

    // utility method to initialise
    // adjacency list
    @SuppressWarnings("unchecked")
    private void initAdjList(){
    	adjList = new ArrayList[v];

    	for (int i = 0; i < v; i++) {
    		adjList[i] = new ArrayList<>();
    	}
    }

    // add edge from u to v
    public void addEdge(int u, int v){
    	// Add v to u's list.
    	adjList[u].add(v);
    }

    // Prints all paths from
    // 's' to 'd'
    public void printAllPaths(int s, int d){
    	boolean[] isVisited = new boolean[v];
    	ArrayList<Integer> pathList = new ArrayList<>();

    	// add source to path[]
    	pathList.add(s);

    	// Call recursive utility
    	printAllPathsUtil(s, d, isVisited, pathList);
    }
    
    public String getPathString() {
    	return this.pathStr;
    }

    // A recursive function to print
    // all paths from 'u' to 'd'.
    // isVisited[] keeps track of
    // vertices in current path.
    // localPathList<> stores actual
    // vertices in the current path
    private void printAllPathsUtil(Integer u, Integer d, boolean[] isVisited, List<Integer> localPathList){
    	if (u.equals(d)) {
    		//System.out.println(localPathList);
    		pathStr += localPathList.toString();
    		// if match found then no need to traverse more till depth
    		return;
    	}

    	// Mark the current node
    	isVisited[u] = true;

    	// Recur for all the vertices
    	// adjacent to current vertex
    	for (Integer i : adjList[u]) {
    		if (!isVisited[i]) {
    			// store current node
    			// in path[]
    			localPathList.add(i);
    			printAllPathsUtil(i, d, isVisited, localPathList);

    			// remove current node
    			// in path[]
    			localPathList.remove(i);
    		}
    	}

    	// Mark the current node
    	isVisited[u] = false;
	}
    
    public List<List<Integer>> parsePathString(String pathStr){
    	String[] paths = pathStr.split("[^0-9, ] *");
    	
    	List<List<Integer>> pathsList = new ArrayList<>();
    	
    	for(String ps: paths) {
    		String[] p = ps.split(", ");
    		List<Integer> path = new ArrayList<>();
    		
    		for(String pi: p) {
    			if(!pi.isEmpty())
    				path.add(Integer.parseInt(pi) + 1);
    		}
    		
    		if(!path.isEmpty())
    			pathsList.add(path);
    	}
    	
		return pathsList;
    	
    }
    
	// Driver program
	public static void main(String[] args)
	{
    	// Create a sample graph
    	FlightPathCalculate g = new FlightPathCalculate(4);
    	g.addEdge(0, 1);
    	g.addEdge(0, 2);
    	g.addEdge(0, 3);
    	g.addEdge(2, 0);
    	g.addEdge(2, 1);
    	g.addEdge(1, 3);

    // arbitrary source
    	int s = 2;

    	// arbitrary destination
   		int d = 3;

    	System.out.println(
    		"Following are all different paths from "
        		+ s + " to " + d);
    	g.printAllPaths(s, d);
    	
    	System.out.println("----------saved String------------");
    	String path = g.getPathString();
    	System.out.println(g.parsePathString(path));
	}
}
