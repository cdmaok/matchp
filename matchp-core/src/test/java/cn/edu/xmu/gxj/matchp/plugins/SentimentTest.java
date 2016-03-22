package cn.edu.xmu.gxj.matchp.plugins;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentPipeline;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentTest {
	
	private static final NumberFormat NF = new DecimalFormat("0.0000");
	
	  /**
	   * Sets the labels on the tree to be the indices of the nodes.
	   * Starts counting at the root and does a postorder traversal.
	   */
	  static int setIndexLabels(Tree tree, int index) {
	    if (tree.isLeaf()) {
	      return index;
	    }

	    tree.label().setValue(Integer.toString(index));
	    index++;
	    for (Tree child : tree.children()) {
	      index = setIndexLabels(child, index);
	    }
	    return index;
	  }
	
	  /**
	   * Outputs the scores from the tree.  Counts the tree nodes the
	   * same as setIndexLabels.
	   */
	  static int outputTreeScores(Tree tree, int index) {
	    if (tree.isLeaf()) {
	      return index;
	    }
	    PrintStream out = System.out;
	    out.print("  " + index + ":");
	    SimpleMatrix vector = RNNCoreAnnotations.getPredictions(tree);
	    for (int i = 0; i < vector.getNumElements(); ++i) {
	      out.print("  " + NF.format(vector.get(i)));
	    }
	    out.println();
	    index++;
	    for (Tree child : tree.children()) {
	      index = outputTreeScores(child, index);
	    }
	    return index;
	  }
	  
	public void value(CoreMap sentence){
//	    Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
//        Tree copy = tree.deepCopy();
//        setIndexLabels(copy, 0);
//        outputTreeScores(tree, 0);
		System.out.println(sentence.toShorterString());
        System.out.println("  " + sentence.get(SentimentCoreAnnotations.SentimentClass.class));
//        setSentimentLabels(copy);
//        System.out.println(copy);
//        
//        setIndexLabels(copy, 0);
//        outputTreeVectors(tree, 0);
	}
	
	  /**
	   * Outputs the vectors from the tree.  Counts the tree nodes the
	   * same as setIndexLabels.
	   */
	  static int outputTreeVectors(Tree tree, int index) {
	    if (tree.isLeaf()) {
	      return index;
	    }
	    PrintStream out = System.out;
	    out.print("  " + index + ":");
	    SimpleMatrix vector = RNNCoreAnnotations.getNodeVector(tree);
	    for (int i = 0; i < vector.getNumElements(); ++i) {
	      out.print("  " + NF.format(vector.get(i)));
	    }
	    out.println();
	    index++;
	    for (Tree child : tree.children()) {
	      index = outputTreeVectors(child, index);
	    }
	    return index;
	  }
	
	  /**
	   * Sets the labels on the tree (except the leaves) to be the integer
	   * value of the sentiment prediction.  Makes it easy to print out
	   * with Tree.toString()
	   */
	  static void setSentimentLabels(Tree tree) {
	    if (tree.isLeaf()) {
	      return;
	    }

	    for (Tree child : tree.children()) {
	      setSentimentLabels(child);
	    }

	    Label label = tree.label();
	    if (!(label instanceof CoreLabel)) {
	      throw new IllegalArgumentException("Required a tree with CoreLabels");
	    }
	    CoreLabel cl = (CoreLabel) label;
	    cl.setValue(Integer.toString(RNNCoreAnnotations.getPredictedClass(tree)));
	  }

	@Test
	public void test() {
		getSentiment("wtf, i am sad");
		getSentiment("wtf, i am happy");
		getSentiment("holy shit, i am totally happy");
		getSentiment("怎么办，好开心。");
	}
	
	public void getSentiment(String line){

		Properties pipelineProps = new Properties();
		Properties tokenizerProps = null;
		// pipelineProps.setProperty("sentiment.model", "xxx");
		// pipelineProps.setProperty("parse.model", "xxx");


		pipelineProps.setProperty("annotators", "parse, sentiment");
		pipelineProps.setProperty("enforceRequirements", "false");
		tokenizerProps = new Properties();
		tokenizerProps.setProperty("annotators", "tokenize,ssplit,parse,sentiment");
		tokenizerProps.setProperty(StanfordCoreNLP.NEWLINE_SPLITTER_PROPERTY, "true");
		tokenizerProps.setProperty("customAnnotatorClass.segment","edu.stanford.nlp.pipeline.ChineseSegmenterAnnotator");
		tokenizerProps.setProperty("parse.model","edu/stanford/nlp/models/lexparser/chinesePCFG.ser.gz");
		StanfordCoreNLP tokenizer = (tokenizerProps == null) ? null : new StanfordCoreNLP(tokenizerProps);
		StanfordCoreNLP pipeline = new StanfordCoreNLP(pipelineProps);
		Annotation annotation = tokenizer.process(line);
//		pipeline.annotate(annotation);
		for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			value(sentence);
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            System.out.println(sentiment);
		}
	
	}

}
