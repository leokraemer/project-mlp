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
package cc.clabs.stratosphere.mlp.contracts;

import cc.clabs.stratosphere.mlp.types.PactRelation;
import eu.stratosphere.nephele.configuration.Configuration;
import eu.stratosphere.pact.common.stubs.Collector;
import eu.stratosphere.pact.common.stubs.ReduceStub;
import eu.stratosphere.pact.common.type.PactRecord;

import java.util.Iterator;

/**
 *
 * @author rob
 */
public class FilterCandidates extends ReduceStub {
        
    
    /**
     * 
     */
    private Double threshold;
    
    /**
     * 
     */
    private final PactRecord target = new PactRecord();

    
    @Override
    public void open(Configuration parameter) throws Exception {
        super.open( parameter );
        threshold = Double.parseDouble( parameter.getString( "THRESHOLD", "0.8" ) );
    }
    
    
    @Override
    public void reduce( Iterator<PactRecord> iterator, Collector<PactRecord> collector ) throws Exception {
        PactRelation relation;
        PactRecord record;
        
        while ( iterator.hasNext() ) {
            record = iterator.next();
            relation = record.getField( 1, PactRelation.class );
            
            // if the score is lesser than our minimum threshold
            // we'll continue with the next word
            if ( relation.getScore().getValue() < threshold ) continue;
            
            // emit
            target.clear();
            target.setField( 0, relation );
            collector.collect( target );
        }
    }

}