import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DictBuilder {
    private Map<String, DictEntry> dictEntries;
    private PostingsBuilder postingsBuilder;

    public DictBuilder(PostingsBuilder postingsBuilder){
        dictEntries = new TreeMap<String, DictEntry>(); //TreeMap automatically sorts by key
        this.postingsBuilder = postingsBuilder;
    }

    /**
     * Adds or increments a dict entry for this term. Always add to postings before adding to Dict.
     * @param term
     * @throws IRException
     */
    public void addToDictEntry(String term) throws IRException{
        if(dictEntries.containsKey(term)){
            if(dictEntries.get(term).getPostingList()==null){
                throw new IRException("Dict contains an entry with no postings pointer");
            }
            dictEntries.get(term).setNumDocs(dictEntries.get(term).getNumDocs()+1); //increment the numDocs of this term by one
        }
        else{
            dictEntries.put(term, new DictEntry(term, 1, postingsBuilder.getPostingList(term))); //create a new dict entry for this term
        }
    }

    /**
     * Adds a posting location to a dict entry.
     * @param term
     * @param location
     * @throws IRException
     */
    public void addPostingLocation(String term, Long location) throws IRException{
        if(!dictEntries.containsKey(term)){
            throw new IRException("Cannot add a location to a term that doesn't exist in dictEntries");
        }
        dictEntries.get(term).setPostingLocation(location);
    }

    //GETTERS AND SETTERS
    public Map<String, DictEntry> getDictEntries() {
        return dictEntries;
    }

    public void setDictEntries(Map<String, DictEntry> dictEntries) {
        this.dictEntries = dictEntries;
    }

    public PostingsBuilder getPostingsBuilder() {
        return postingsBuilder;
    }

    public void setPostingsBuilder(PostingsBuilder postingsBuilder) {
        this.postingsBuilder = postingsBuilder;
    }
}
