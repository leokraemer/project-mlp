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
package cc.clabs.stratosphere.mlp.types;

import eu.stratosphere.pact.common.type.base.PactList;
import eu.stratosphere.pact.common.type.base.PactString;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rob
 */
public class PactSentence extends PactList<PactWord> implements Cloneable {
    
    private static final Log LOG = LogFactory.getLog( PactSentence.class );
    
    /**
     * 
     * @param value
     * @return 
     */
    public boolean containsWord( String value ) {
        boolean found = false;
        String word;
        Iterator<PactWord> it = this.iterator();
        while ( it.hasNext() ) {
            word = it.next().getWord();
            found = found || word.equals( value );
        }
        return found;
    }

    /**
     * 
     * @param word
     * @return 
     */
    public boolean containsWord( PactString word ) {
        return containsWord( word.getValue() );
    }
    
    /**
     * 
     * @param word
     * @return 
     */
    public boolean containsWord( PactWord word ) {
        return containsWord( word.getWord() );
    }
    
    @Override
    public Object clone() {
        PactSentence obj = new PactSentence();
        obj.addAll( this );
        return obj;
    }
    
    @Override
    public String toString() {
        String buffer = "";
        Iterator<PactWord> it = this.iterator();
        while ( it.hasNext() ) buffer += it.next().getWord() + " ";
        return buffer;
    }
    
}