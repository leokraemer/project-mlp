/*        __
 *        \ \
 *   _   _ \ \  ______
 *  | | | | > \(  __  )
 *  | |_| |/ ^ \| || |
 *  | ._,_/_/ \_\_||_|
 *  | |
 *  |_|
 * 
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <rob ∂ CLABS dot CC> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return.
 * ----------------------------------------------------------------------------
 */
package cc.clabs.stratosphere.mlp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

/**
 * 
 * @author rob
 */
public class TexIdentifierExtractor {

	/**
	 * list of false positive identifiers
	 */
	private final static List<String> blacklist = Arrays.asList("sin", "cos",
			"tan", "min", "max", "inf", "lim", "log", "exp", "sup", "lim sup",
			"lim inf", "arg", "dim", "cosh", "arccos", "arcsin", "arctan",
			"rank", "ln", "det", "ker", "sec", "cot", "csc", "tanh", "sinh",
			"coth", "cot", "⋯", ":", "'", "′", "…", "∞", "Λ", "⋮", " ", " ",
			"~", ";", "#", "π", "e", "⋱", "{", "}", "%", "?",

			// ignore identifier that are also english (stop-)words
			"a", "A", "i", "I",

			// ignore special chars
			"$", "\\");

	/**
	 * Returns a list of all identifers within a given formula. The formula is
	 * coded in TeX.
	 * 
	 * @param formula
	 *            TeX representation of a formula
	 * @param augmention
	 * @return list of identifiers
	 */
	public static ArrayList<String> getAll(String formula, boolean augmention) {
		// create vanilla SnuggleEngine and new SnuggleSession
		String xml;
		if (!augmention) {
			SnuggleEngine engine = new SnuggleEngine();
			SnuggleSession session = engine.createSession();
			try {
				SnuggleInput input = new SnuggleInput("$$ "	+ cleanupTexString(formula) + " $$");
				session.parseInput(input);
				xml = session.buildXMLString();
			} catch (Exception e) {
				return new ArrayList<>();
			}
		} else {
			xml = formula;
		}
		return getIdentifiersFrom(xml);
	}

	/**
	 * Returns a list of unique identifiers from a MathML string. This function
	 * searches for all <mi/> or <ci/> tags within the string.
	 * 
	 * @param mathml
	 * @return a list of unique identifiers. When no identifiers were found, an
	 *         empty list will be returned.
	 */
	private static ArrayList<String> getIdentifiersFrom(String mathml) {
		ArrayList<String> list = new ArrayList<>();
		Pattern p = Pattern.compile("<(mi)(.*?)>(.{3,10})</mi>", Pattern.DOTALL);
		Matcher m = p.matcher(mathml);
		while (m.find()) {
			String identifier = m.group(3);
			if (blacklist.contains(identifier))
				continue;
			if (list.contains(identifier))
				continue;
			list.add(identifier);
		}
		return list;
	}

	/**
	 * Returns a cleaned version of the TeX string.
	 * 
	 * @param tex
	 *            the TeX string
	 * @return the cleaned TeX string
	 */
	private static String cleanupTexString(String tex) {
		// strip text blocks
		tex = tex.replaceAll(
				"\\\\(text|math(:?bb|bf|cal|frak|it|sf|tt))\\{.*?\\}", "");
		// strip arbitrary operators
		tex = tex.replaceAll("\\\\operatorname\\{.*?\\}", "");
		// strip dim/log
		tex = tex.replaceAll("\\\\(dim|log)_(\\w+)", "$1");
		// strip "is element of" definitions
		tex = tex.replaceAll("^(.*?)\\\\in", "$1");
		// strip indices
		tex = tex.replaceAll("^([^\\s\\\\\\{\\}])_[^\\s\\\\\\{\\}]$", "$1");
		return tex;
	}

}
