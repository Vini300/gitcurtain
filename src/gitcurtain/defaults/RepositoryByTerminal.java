package gitcurtain.defaults;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import gitcurtain.extraction.RepoSetupStrategy;

/**
 * Uma implementação padrão de uma extensão para a interface RepoSetupStrategy, que obtém as informações de <i>path</i>, URI e <i>token</i>
 * para acesso ao repositório Git por meio de pedidos de entrada pelo terminal.
 * 
 * @author Vinícius Soares
 *
 */
public class RepositoryByTerminal implements RepoSetupStrategy{

	/**
	 * Obtém o <i>path</i> do repositório a ser extraído por meio de um pedido pelo terminal.
	 */
	public String getRepositoryPath() {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String path = "";
		boolean done = false;
		
		while (!done) {
			try {
				System.out.println("Please enter the repository path (i.e., the folder in which the repository should be downloaded to.):");
	        	path = reader.readLine();
	        	done = true;
			}
	        catch (IOException e) {
	        	System.out.println("An unexpected error occurred while reading data. Retrying...");
				e.printStackTrace();
			}
		}
        
        return path;
	}

	/**
	 * Obtém a URI do repositório a ser extraído por meio de um pedido pelo terminal.
	 */
	public String getRepositoryURI() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String path = "";
		boolean done = false;
		
		while (!done) {
			try {
				System.out.println("Please enter the repository URI (i.e., the repository's Git clone url):");
	        	path = reader.readLine();
	        	done = true;
			}
	        catch (IOException e) {
	        	System.out.println("An unexpected error occurred while reading data. Retrying...");
				e.printStackTrace();
			}
		}
			
        return path;
	}
	
	/**
	 * Obtém o <i>token</i> de acesso ao repositório a ser extraído por meio de um pedido pelo terminal.
	 */
	public String getToken() {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String token = "";
		boolean done = false;

        while (!done) {
			try {
				System.out.println("Please enter your GitHub Personal Access Token:");
	        	token = reader.readLine();
	        	done = true;
			}
	        catch (IOException e) {
	        	System.out.println("An unexpected error occurred while reading data. Retrying...");
				e.printStackTrace();
			}
		}
        
        return token;
	}

	/**
	 * Obtém a <i>branch</i> da qual serão extraídos os dados de commits.
	 */
	public String getBranch() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String branch = "";
		boolean done = false;

        while (!done) {
			try {
				System.out.println("Please enter the name of the branch to extract commit data from:");
	        	branch = reader.readLine();
	        	done = true;
			}
	        catch (IOException e) {
	        	System.out.println("An unexpected error occurred while reading data. Retrying...");
				e.printStackTrace();
			}
		}
        
        return branch;
	}

}
