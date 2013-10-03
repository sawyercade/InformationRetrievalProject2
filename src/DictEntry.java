import java.util.List;

public class DictEntry {
    //assume each unique term occurs on average in 9,999,999 documents yields
    //20_char_term 9digit_docId
    //public static final int DICT_ENTRY_BYTE_SIZE = 40+2+18+2+
    private String term;
    private Integer numDocs;
    private List<Posting> postingList;
    private Long postingLocation;

    public DictEntry(String term, Integer numDocs, List<Posting> postingList){
        this.term = term;
        this.numDocs = numDocs;
        this.postingList = postingList;
    }

    //GETTERS AND SETTERS
    public List<Posting> getPostingList() {
        return postingList;
    }

    public void setPostingList(List<Posting> postingList) {
        this.postingList = postingList;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getNumDocs() {
        return numDocs;
    }

    public void setNumDocs(Integer numDocs) {
        this.numDocs = numDocs;
    }

    public Long getPostingLocation() {
        return postingLocation;
    }

    public void setPostingLocation(Long postingLocation) {
        this.postingLocation = postingLocation;
    }
}
