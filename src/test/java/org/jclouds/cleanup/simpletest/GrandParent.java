package org.jclouds.cleanup.simpletest;

import com.google.gson.annotations.SerializedName;
import org.jclouds.javax.annotation.Nullable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Class comment for GrandParent!
 * A few lines....
 * Long
 */
public class GrandParent<T extends GrandParent<T>> {

    /** @return name is required */
    @Nullable
    protected String name;

    /** @return attribute is not null */
    protected String id;

    /** @return this is nullable */
    @Nullable
    protected String description;
    
    @SerializedName("Freddy")
    protected String freddy;

    @SerializedName("Brian")
    @Nullable
    protected String[] brian;

    /** This element is NOT required
     * @return the value of brian
     */    
    public String[] getBrian() {
        return brian;
    }
}
