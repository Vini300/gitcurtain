package gitcurtain.defaults;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gitcurtain.exceptions.InvalidEnumIDException;
import gitcurtain.metrics.MetricStrategy;
import gitcurtain.utils.Commit;
import gitcurtain.utils.CommitVariables;
import gitcurtain.utils.MetricResult;

/**
 * Uma implementação padrão de uma extensão da interface MetricStrategy. Essa implementação se utiliza de um sistema com base em palavras
 * chave para detectar a presença de <i>self-affirmed refactorings</i> nas mensagens dos commits analisados. O sistema obtém a lista de
 * palavras chave por meio de um arquivo formatado linha a linha com as palavras chave, onde o usuário final inclui o nome de tal arquivo.
 * 
 * @author Vinícius Soares
 *
 */
public class SelfAffirmedRefactoringMetric implements MetricStrategy {
	
	/**
	 * A lista de palavras chave que serão utilizadas para a detecção de SARs.
	 */
	private ArrayList<String> keywordList;
	private String keywordPath;
	
	/**
	 * Constrói uma instância da métrica, preparando o ArrayList.
	 */
	public SelfAffirmedRefactoringMetric() {
		
		keywordList = new ArrayList<String>();
		keywordPath = null;
		
	}
	
	public SelfAffirmedRefactoringMetric(String path) {
		
		keywordList = new ArrayList<String>();
		keywordPath = path;
		
	}
	
	/**
	 * Pede o nome do arquivo que contém as palavras chave e, então, com o nome do arquivo, obtém a lista de palavras chave para detecção
	 * de SARs, e as carrega para a lista de palavras chave. Este método deve ser chamado antes da execução das métricas.
	 * 
	 * @throws IOException Quando ocorre um erro de I/O
	 */
	public void loadKeywordList() throws IOException {
		
		if (keywordPath == null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Please enter the filename from which to extract the SAR keywords from"
					+ " (File format: each line contains a single keyword - or phrase - without any additional characters."
					+ " Accepts Regex patterns.):");
			
			keywordPath = reader.readLine();
			
		}
		
		keywordList = keywordsFromFile(keywordPath);       
	}
	
	/**
	 * Faz o carregamento das palavras-chave com base no nome do arquivo provido como parâmetro.
	 * 
	 * @param path O <i>path</i> do arquivo que contém a lista de palavras chave.
	 * 
	 * @return Um ArrayList de Strings que contém a lista de palavras chave final já carregada do arquivo.
	 * 
	 * @throws IOException É lançado quando ocorre um erro durante a leitura do arquivo.
	 * @throws FileNotFoundException É lançado quando o <i>path</i> não leva a um arquivo válido.
	 */
	private ArrayList<String> keywordsFromFile(String path) throws IOException, FileNotFoundException {
		
		ArrayList<String> keywords = new ArrayList<String>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line = br.readLine();

		    while (line != null) {
		        keywords.add(line);
		        line = br.readLine();
		    }
		}
		
		return keywords;
	}

	/**
	 * Executa a métrica, ou seja, usa a lista de palavras chave para detectar a presença de SARs nas mensagens de commit dos commits
	 * extraídos.
	 */
	public MetricResult executeMetric(ArrayList<Commit> commits) {
		
		System.out.println("Executing metric - Self-Affirmed Refactoring detection...");
		
		HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
		
		hashMap.put("SAR", new ArrayList<String>());
		hashMap.put("Non SAR", new ArrayList<String>());
		
		String message = "";
		String hash = "";
		boolean found;
		
		for (Commit commit : commits) {
			found = false;
			for (String keyword : keywordList) {
				try {
					message = (String) commit.getVariableByEnum(CommitVariables.MESSAGE);
					hash = (String) commit.getVariableByEnum(CommitVariables.HASH);
					
					Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(message);
					
					if (matcher.find()) {
						found = true;
					}
					
				}
				catch (InvalidEnumIDException e) {
					System.out.println("An unexpected error ocurred while accessing commit data. Please try again later, and double check"
							+ " the Enum variables used for any access to commit data.");
					e.printStackTrace();
				}
			}
			if (found) {
				if (!hashMap.get("SAR").contains(hash)) {
					hashMap.get("SAR").add(hash);
				}
			}
			else {
				if (!hashMap.get("Non SAR").contains(hash)) {
					hashMap.get("Non SAR").add(hash);
				}
			}
		}
		
		SelfAffirmedRefactoringSet keyset = new SelfAffirmedRefactoringSet(hashMap);
		
		return keyset;
	}

}
