package com.unbxd.ngram.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.dictionary.Dictionary;
import edu.cmu.lti.jawjaw.JAWJAW;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.ws4j.WS4J;

public class NGramEngine {

	private static Map<String,IndexWordSet> posMap = new HashMap<String,IndexWordSet>();
	private static Dictionary dictionary;
	private static Set<String> validWordSet = new HashSet<String>();
	private static Set<String> inValidWordSet = new HashSet<String>();
	static{
		try {
			dictionary = Dictionary.getDefaultResourceInstance();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeDictionary(){
		dictionary.close();
	}
	
	public static void processString(String input) throws JWNLException{
		
		List<List<String>> results = new ArrayList<List<String>>();
		long time = System.currentTimeMillis();
		search(input, new Stack<String>(), results);

		time = System.currentTimeMillis() - time;

		// list the results found
		for (List<String> result : results) {
			for (String word : result) {
				IndexWordSet wordSet = posMap.get(word);
				Set<String> set = new HashSet<String>();
				if(wordSet != null && wordSet.size() > 0){
					
            		for(IndexWord indexWord : wordSet.getIndexWordCollection()){
            			set.addAll(JAWJAW.findSynonyms(word, POS.valueOf(indexWord.getPOS().getKey())));
            		}
				}
				Set<String> relatedSet = new HashSet<String>();
				for(String str : set){
					double val = WS4J.runWUP(word, str);
					if(val > 0.5){
						relatedSet.add(str);
					}
				}
				System.out.print(word + relatedSet + " ");
				//System.out.print(word + " ");
			}
			System.out.println("(" + result.size() + " words)");
		}
		System.out.println();
		System.out.println("Took " + time + "ms");
	}
	
	

	public static void search(String input, Stack<String> words,List<List<String>> results) throws JWNLException {

		for (int i = 2; i < input.length(); i++) {
			String substring = input.substring(0, i + 1);
			IndexWordSet wordSet = null;
			if(!inValidWordSet.contains(substring)){
				if(!validWordSet.contains(substring)){
					wordSet = dictionary.lookupAllIndexWords(substring);
				}
				if (wordSet == null || wordSet.size() > 0) {
					validWordSet.add(substring);				
					words.push(substring);
					if(!posMap.containsKey(substring)){
						posMap.put(substring, wordSet);
					}
					
					if (i == input.length() - 1) {
						results.add(new ArrayList<String>(words));
					} else {
						search(input.substring(i + 1), words, results);
					}
					words.pop();
				}else{
					inValidWordSet.add(substring);
				}
			}
			
			
		}
	}

}
