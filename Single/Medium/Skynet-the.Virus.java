// Skynet: the Virus
import java.util.*;

enum Node {AGENT, EMPTY, EXIT}                         // Possible state of nodes: Agent, Empty or Exit gateway 

class Player {
	// Global variables used by more methods
	static final boolean DEBUG = false;
	static boolean[][] links;                          // Network links (true:there is a link between nodes [n,m])
	static Node[] nodes;                               // Nodes (1 Agent, 1-3 Exit gateways, others are Empty)
	static int N;                                      // Number of nodes in level, including gateways
	static ArrayList<LinkedList<Integer>> routes =
	    new ArrayList<>();                             // Store routes that lead from an Agent to an Exit
	static HashSet<Integer> exits = new HashSet<>();   // Store Exit nodes
	static HashSet<Integer> visited = new HashSet<>(); // Store visited nodes during obtaining the routes

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		N = in.nextInt();                              // Number of nodes in level, including gateways (3-38)
		int L = in.nextInt();                          // Number of links (2-79)
		int E = in.nextInt();                          // Number of Exit gateways (1-3)
		debug("Nodes:"+N+", Links:"+L+", Exits:"+E+"\n");

		// Initialize link and node arrays
		links = new boolean[N][N];
		for (int i = 0; i < N; ++i) {
			Arrays.fill(links[i], false);
		}
		nodes = new Node[N];
		Arrays.fill(nodes, Node.EMPTY);

		// Store links between nodes to links array
		for (int i = 0; i < L; i++) {
			int N1 = in.nextInt();                      // N1 and N2 defines a link between nodes
			int N2 = in.nextInt();
			addLink(N1, N2);
		}

		// Store Exit gateways in nodes and exits
		for (int i = 0; i < E; i++) {
			int EI = in.nextInt();                      // Index of a gateway node
			nodes[EI] = Node.EXIT;
			exits.add(EI);
		}

		// game loop
		int[] prevLink = {-1, -1};                      // Previously removed (severed) link
		int prevSI = -1;                                // Previous Agent node
		while (true) {
			int SI = in.nextInt();                      // Index of node on which Agent is positioned this turn
			// If NOT first round, prepare the next 
			if ((prevSI = getAgentNode()) != -1) {
			    clearTables(prevLink, prevSI);
			}
		    nodes[SI] = Node.AGENT;
			debug(dispNodes());
			debug(dispLinks());
			getRoute(getAgentNode(),
			    new LinkedList<Integer>());             // Obtain routes from Agent node
            if (routes.size() > 1) {                    // Sort routes if list contains more than one route
                sortRoutes();
            }
		    debug(dispRoutes());
			String severLink = getSeverLink(prevLink);
			// Output message
			System.out.println(severLink);               // Eg: 0 1 are indices of nodes we wish to sever the link between
		}
	}// main()

    // Return the nodes we wish to sever the link between
    static String getSeverLink(int[] prevLink) {
		String severLink = "";    
		LinkedList<Integer> route = routes.get(0);      // Chosen route is first one from list
		prevLink[0] = route.get(route.size() - 2);
		prevLink[1] = route.get(route.size() - 1);
		severLink += prevLink[0] + " " + prevLink[1];
		debug("route choice:" + dispRoute(route) + " ==> out:\"" + severLink + "\"");
		return severLink;
    }// getSeverLink()

    // Sort routes array based on number of nodes it contains: shortest route(s) will be first
    static void sortRoutes() {
        debug("\nsorting routes...");
        Collections.sort(routes,
            new Comparator<LinkedList<Integer>>() {
                public int compare(LinkedList<Integer> nodeA, LinkedList<Integer> nodeB) {
                    int A = nodeA.size();
                    int B = nodeB.size();
                    return (A == B)? 0 : (A < B)? -1 : 1;
                }
            }
        );        
    }// sortRoutes()

    // Obtain routes from current node via recursively walking the tree and store those in routes array
     static void getRoute(int node, LinkedList<Integer> route) {
        debug("getRoute called with node " + node);
        if (exits.contains(node)) {
            debug("node " + node + " found in exits: add to route & routes then return"); 
            if (!route.contains(node)) {
                debug("node " + node + " not in route: added");
                route.add(node);
            }
            routes.add(route);
            return;
        }
        if (visited.contains(node)) {               // Commenting this out only exit routes are stored in routes array 
            debug("node " + node + " found in visited: return");
            //routes.add(route);
            return;           
        } else {
            debug("node " + node + " not in exits or visited: add to route & visited then go links");
            if (!route.contains(node)) {
                debug("node " + node + " not in route: added");
                route.add(node);
            }
            visited.add(node);
            for (int i = 0; i < links.length; ++i) {
                if (links[node][i] == true) {
                    LinkedList<Integer> r = new LinkedList<Integer>(route);
                    if (!r.contains(i)) {
                        debug("node " + i + " not in r: added");
                        r.add(i);
                    }
                    debug("call getRoute with node " + i);
                    getRoute(i, r);
                }
            }
        }
    }// getRoute()

    // Return index of agent in nodes array or -1 if there are no agent
    static int getAgentNode() {
        int ret = -1;
        for (int i = 0; i < nodes.length; ++i) {
            if (nodes[i].equals(Node.AGENT)) {
                ret = i; break;
            }
        }
        debug("Identified agent node:" + ret);
        return ret;
    }// getAgentNode()

	// Add a link between nodes N1 and N2 in links array
	static void addLink(int N1, int N2) {
		links[N1][N2] = true;
		links[N2][N1] = true;
		debug("link added:" + N1 + "-" + N2 + " and " + N2 + "-" + N1);
	}// addLink()

	// Remove (sever) a link between nodes N1 and N2 in links array
	static void removeLink(int N1, int N2) {
		links[N1][N2] = false;
		links[N2][N1] = false;
		debug("link removed:" + N1 + "-" + N2 + " and " + N2 + "-" + N1);
	}// removeLink()

    // Before next round remove severed link, clear previous Agent node, routes & visited tables
    static void clearTables(int[] prevLink, int prevSI) {
	    removeLink(prevLink[0], prevLink[1]);
	    nodes[prevSI] = Node.EMPTY;
	    routes.clear();
	    visited.clear();
    }// clearTables()

    // Return route nodes as string
    static String dispRoute(LinkedList<Integer> route) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < route.size(); ++i) {
            ret.append(route.get(i).toString());
            if (i != route.size() - 1) {
                ret.append(" ");
            }
        }
        return ret.toString();
    }// dispRoute()

    // Return all routes (nodes to exit) as stirng
    static String dispRoutes() {
		StringBuffer buf = new StringBuffer("\nRoutes:\n");
		for (LinkedList<Integer> r : routes) {  // Display all possible routes
		    buf.append(dispRoute(r)).append("\n");
		}
		return buf.toString();
    }// dispRoutes()

	// Display state of all nodes
	static String dispNodes() {
		StringBuffer ret = new StringBuffer("There are " + N + " nodes\n");
		for (int i = 0; i < N; ++i) {
			ret.append("node " + i + ":" + nodes[i] + "\n");
		}
		return ret.toString();
	}// dispNodes()
	
	// Display the links array
	static String dispLinks() {
		int L = links.length;
		StringBuffer ret = new StringBuffer("Links array size: " + L + "x" + L + "\n");
		ret.append("  ");
		for (int i = 0; i < N; ++i) {
			ret.append(i % 10);
		}
		ret.append("\n");
		for (int row = 0; row < L; ++row) {
		    ret.append(row % 10 + " ");
		    for (int col = 0; col < L; ++col) {
		        ret.append(links[row][col] == false? "-":"T");
		    }
			ret.append("\n");
		}
		return ret.toString();	    
	}// dispLinks()

    // Display debug info if DEBUG true	
	static void debug(String msg) {
	    if (DEBUG) {
            System.err.println(msg);
        }
	}// debug()
}