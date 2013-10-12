import java.util.*;

public class PostingsBuilder {
    private Map<String, List<Posting>> postings;

    public PostingsBuilder(){
        postings = new HashMap<String, List<Posting>>();
    }

    /**
     * Adds an entry to the postings Map
     * @param term
     * @param docId
     * @param weight
     * @throws IRException
     */
    public void addPosting(String term, Integer docId, Float weight) throws IRException{
        if (containsPosting(term)){ //if we've already added this term before
            postings.get(term).add(new Posting(docId, weight)); //add this docId and weight to the end of the postings list for this term
        }
        else{ //add the posting to the list of postings with a new list
            List<Posting> postList = new LinkedList<Posting>();
            postList.add(new Posting(docId, weight));
            postings.put(term, postList);
        }
    }

    /**
     * Returns the postings for a term
     * @param term
     * @return
     * @throws IRException
     */
    public List<Posting> getPostingList(String term) throws IRException {
        if (!postings.containsKey(term)){
            throw new IRException("postings does not contain a list of postings for the term " + term);
        }
        return postings.get(term);
    }

    /**
     * Returns true if a posting exists for the term.
     * @param term
     * @return
     * @throws IRException
     */
    public boolean containsPosting(String term) throws IRException {
        if (postings.containsKey(term)){
            if(postings.get(term) == null || postings.get(term).isEmpty()){
                throw new IRException("PostingsBuilder contains term but no docId or weights");
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the Map<String, List<Posting>> of postings
     * @return
     */
    public Map<String, List<Posting>> getPostings() {
        return postings;
    }

    public void setPostings(Map<String, List<Posting>> postings) {
        this.postings = postings;
    }
}
