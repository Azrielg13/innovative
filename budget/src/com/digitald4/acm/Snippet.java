package com.digitald4.acm;

public class Snippet {
	public static void main(String[] args) {
		int length = 2;
		// Charter Guy Jerrod Madison 1-855-867-1979 
		// 117.93 137.93 159.93
		System.out.println(dfs(length-1,0,true));
  }
	
  /**
   * perform a depth first search on the graph, culling out any trees that contain more than 3 repeated elements.
   * first and last positions must be black
   * @return the number of permutations in the (sub) graph
   */
  private static int dfs(int length, int repeat, boolean black) {
    if(repeat >= 3) { return 0; }
    if(length == 0) { return 1; }

    int tot = dfs(length - 1, (black ? repeat + 1 : 0), true);
    if (length > 1) {
        tot += dfs(length - 1, (!black ? repeat + 1 : 0), false);
    }

    return tot;
  }
}

