/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorytmy_0402;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author skrzy
 */
public class Algorytmy_0402 {
    
    
    static int lines_counter(File file) throws FileNotFoundException{
        LineNumberReader lineCounter = new LineNumberReader(
            new InputStreamReader(new FileInputStream(file)));
        String nextLine = null;
 
        try {
            while ((nextLine = lineCounter.readLine()) != null) {
                if (nextLine == null)
                    break;
                System.out.println(nextLine);
            }
            System.out.println("Total number of line in this file " + lineCounter.getLineNumber());
        }catch (Exception done) {
        done.printStackTrace();
    }
        return lineCounter.getLineNumber();
 }


    static boolean BellmanFord(Graph graph,int src) throws FileNotFoundException
    {
        int V = graph.V, E = graph.E;
        int dist[] = new int[V];
        File file = new File("sigma.txt");
        PrintWriter out = new PrintWriter(file);

        for (int i=0; i<V; ++i)
            dist[i] = Integer.MAX_VALUE;
        dist[src] = 0;

        for (int i=1; i<V; i++)
        {
            for (int j=0; j<E; j++)
            {
                int u = graph.edge[j].src;
                int v = graph.edge[j].dest;
                int weight = graph.edge[j].weight;
                if (dist[u]!=Integer.MAX_VALUE && dist[u]+weight<dist[v])
                    dist[v]=dist[u]+weight;
            }
        }
 

        for (int j=0; j<E; ++j)
        {
            int u = graph.edge[j].src;
            int v = graph.edge[j].dest;
            int weight = graph.edge[j].weight;
            if (dist[u] != Integer.MAX_VALUE && dist[u]+weight < dist[v])
              return false;
        }
        for (int i=0; i<V; ++i)
            out.print(dist[i]+" ");
        out.println();
        out.close();
        return true;
    }
    
    static void Dijkstry(Graph G, PrintWriter save, int s, int n){
        int[] fin = new int[n+1];
        int[] dist = new int[n+1];
        int[] pred = new int[n+1];
        int[][] A = new int[n+1][n+1];
        int inf=Integer.MAX_VALUE;
        int k = G.V-1;
        for(int i=1;i<=n;i++){
            for(int j=1;j<=n;j++){
                if(k<G.E && G.edge[k].dest == j && G.edge[k].src == i){
                    A[i][j] = G.edge[k].weight;
                    k++;
                }
                else
                    A[i][j] = 3333;
            }
        }
        for(int v=1; v<=n; v++){
            dist[v]=inf; 
            fin[v]=0; 
            pred[v]=-1;
        }
        dist[s]=0; 
        fin[s]=1; 
        int last=s;
        for(int i=1; i<n; i++){
            for(int v=1; v<=n; v++){
                if((A[last][v]<inf)&&(fin[v]==0)){
                    if(dist[last]+A[last][v]<dist[v]){
                    dist[v]=dist[last]+A[last][v]; 
                    pred[v]=last;
                    }
                }
            }
            int y=0, temp=inf;
            for(int u=1; u<=n; u++){
                if((fin[u]==0)&&(dist[u]<temp)){
                    y=u;
                    temp=dist[u];
                }
            }
            if(temp<inf){
                fin[y]=1; 
                last=y;
            }
        }
        int i;
        for(i=1;i<=n;i++){
                save.print(dist[i]+" ");
        }
        save.println();
    }

    static void Johnson(Graph G, int src) throws FileNotFoundException{
        File file = new File("Out0502.txt");
        File file2 = new File("sigma.txt");
        File file3 = new File("Dijkstra.txt");
        Scanner in = new Scanner(file);
        Scanner in2 = new Scanner(file2);
        Scanner in3 = new Scanner(file3);
        PrintWriter out = new PrintWriter(file);
        PrintWriter out2 = new PrintWriter(file3);
        Graph Gprim = new Graph(G.V,G.E);
        int hu,hv,wuv,j=0;
        int[] Deltauv = new int[G.V];
        int[] duv = new int[G.V];
        int[] sigma = new int[G.V];
        if(!BellmanFord(G,src))
          System.out.println("Graf wejściowy zawiera cykl o ujemnej wadze.");  
        else{            
            for(int i=0;i<G.V;i++){
                sigma[i] = in2.nextInt();
                out.print(sigma[i]+" ");
            }
            out.println();
            while(j<G.E){
                int s = G.edge[j].src;
                out.print("["+s+"] ");
                hu = sigma[s];
                
                while(j<G.E && G.edge[j].src==s){
                    hv = sigma[G.edge[j].dest];
                    wuv = G.edge[j].weight;
                    Gprim.edge[j].src = s;
                    Gprim.edge[j].dest = G.edge[j].dest;
                    Gprim.edge[j].weight = wuv + hu - hv;
                    if(Gprim.edge[j].dest != Gprim.edge[j].src)
                        out.print(Gprim.edge[j].dest+"("+Gprim.edge[j].weight+") ");
                    j++;
                }
                out.println();
            }
            
        }
    
            
        for(int k = 1 ; k<Gprim.V ; k++){
            Dijkstry(Gprim, out2, k ,Gprim.V-1);
        }
        out2.close();
        for(int m = 1 ; m<Gprim.V ; m++){
            out.print("Delta^"+m+"][");
            for(int l = 1 ; l<Gprim.V ; l++){
                Deltauv[l] = in3.nextInt();
                hv = sigma[l];
                hu = sigma[m];                  
                duv[l] = Deltauv[l] + hv - hu;
                }
            for(int l = 1 ; l<Gprim.V ; l++){
                if(Deltauv[l] == 3333)
                    out.print("∞ ");
                else
                    out.print(Deltauv[l]+" ");
            }
            out.print("], D["+m+"][");
            for(int l = 1 ; l<Gprim.V ; l++){
                if(duv[l] > 2000)
                    out.print("∞ ");
                else
                    out.print(duv[l]+" ");
            }
            out.println("]");
        }

        out.close();
           
    }

    

    public static void main(String[] args) throws FileNotFoundException {
        try {
	   Scanner scanner = new Scanner(new File("In0502.txt"));
	   Scanner sc = scanner.useDelimiter("[\\p{Ps}\\p{Pe}]\\s*");
	        int V = Integer.parseInt(sc.nextLine());
	        int E = (V*(V-1))/2;
	        int src = 0,j = 0,k,l;
	        Graph graph = new Graph(V, E);
	        sc.nextLine();
	        src = sc.nextInt();
	            while(sc.hasNext()){
	                if(sc.hasNextInt()){
	                    graph.edge[j].src = src;
	                    graph.edge[j].dest = sc.nextInt();
	                    graph.edge[j].weight = sc.nextInt();
	                    j++;
	               }
	               else{
	            	    sc.next();
	                    src = sc.nextInt();
	                    graph.edge[j].src = src;
	                    if(!sc.hasNextInt())
	                        j++;     
	                    }
	                }scanner.close();
	            sc.close();
	        V=V+1;
	        Graph graph2 = new Graph(V,j+V-1);
	        for( k=0, l=1;k<V-1;k++,l++){
	            graph2.edge[k].src = 0;
	            graph2.edge[k].dest = l;
	            graph2.edge[k].weight = 0;
	        }
	        l=0;
	        while(k<j+V-1){
	               graph2.edge[k].src = graph.edge[l].src;
	               graph2.edge[k].dest = graph.edge[l].dest;
	               graph2.edge[k].weight = graph.edge[l].weight;
	               k++;
	               l++;
	            }
	        for(l=0;l<j+V-1;l++){
	            if(graph2.edge[l].dest==0){
	                graph2.edge[l].dest = graph2.edge[l].src;
	                graph2.edge[l].weight = 0;
	            }}
	        Johnson(graph2,0);
        } catch (Exception e) {
        System.out.print(e.getMessage());
        }
    }
}
