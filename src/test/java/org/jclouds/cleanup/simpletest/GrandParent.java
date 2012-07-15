package org.jclouds.cleanup.simpletest;

import com.google.inject.name.Named;
import org.jclouds.javax.annotation.Nullable;

/**
 * Class comment for GrandParent!
 * A few lines....
 * Long
 */
public class GrandParent {

    /** @return name is required */
    @Nullable
    protected String name;

    /** @return attribute is not null */
    protected String id;

    /** @return this is nullable */
    @Nullable
    protected String description;
    
    @Named("Freddy")
    protected String freddy;

    @Named("Brian")
    @Nullable
    protected String[] brian;

    /** This element is NOT required
     * @return the value of brian
     */    
    public String[] getBrian() {
        return brian;
    }
}
