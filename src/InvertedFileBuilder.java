import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InvertedFileBuilder {
    public static final int POSTING_CHAR_LENGTH = 35; //total posting record length
    public static final int DICT_CHAR_LENGTH = 56; //total dict record length
    public static final int TERM_CHAR_LENGTH = 25; //truncate terms after 25 chars
    public static final int NUMDOCS_CHAR_LENGTH = 9; //up to 999,999,999 documents
    public static final int POSTINGS_LOCATION_CHAR_LENGTH = 19; //long has a max value of 9,223,372,036,854,775,807, and postingLocation is a long

    private DictBuilder dictBuilder;
    private PostingsBuilder postingsBuilder;

    public InvertedFileBuilder(){
        postingsBuilder = new PostingsBuilder();
        dictBuilder = new DictBuilder(postingsBuilder);
    }

    public void addEntry(String term, Integer docId, Float weight) throws IRException{
        postingsBuilder.addPosting(term, docId, weight); //always add to postings first.
        dictBuilder.addToDictEntry(term);
    }

    public void printToFiles(String dictPath, String postingsPath) throws IOException, IRException {
        File dictFile = new File(dictPath);
        File postFile = new File(postingsPath);

        if(!dictFile.exists()){
            dictFile.createNewFile();
        }
        if(!postFile.exists()){
            postFile.createNewFile();
        }

        FileOutputStream dictOutStream = new FileOutputStream(dictFile);
        FileOutputStream postOutStream = new FileOutputStream(postFile);

        Long postingsCounter = new Long(0);

        for (Map.Entry<String, List<Posting>> entry : postingsBuilder.getPostings().entrySet()){ //for each posting
            String term = entry.getKey();
            List<Posting> postings = entry.getValue();
            dictBuilder.addPostingLocation(term, (postingsCounter* POSTING_CHAR_LENGTH));
            //write to postings file
            for (Posting post : postings){
                writePosting(post, postOutStream);
            }
            postingsCounter++;
        }

        for(Map.Entry<String, DictEntry> entry : dictBuilder.getDictEntries().entrySet()){ //for each dict entry
            writeDictEntry(entry.getValue(), dictOutStream); //write to file
        }
    }

    private void writeDictEntry(DictEntry dictEntry, FileOutputStream dictOutStream) throws IOException, IRException {
        String term = dictEntry.getTerm();
        if (term.length() > TERM_CHAR_LENGTH){
            term = term.substring(0, TERM_CHAR_LENGTH);
        }
        while (term.length()<TERM_CHAR_LENGTH){
            term = term.concat(" ");
        }

        String numDocs = dictEntry.getNumDocs().toString();
        if (numDocs.length() > NUMDOCS_CHAR_LENGTH){
            numDocs = numDocs.substring(0, NUMDOCS_CHAR_LENGTH);
        }
        while (term.length()<NUMDOCS_CHAR_LENGTH){
            numDocs = "0" + numDocs; //pad with 0s on the left
        }

        String postingsLocation = dictEntry.getPostingLocation().toString();
        String record = term + " " + numDocs + " " + postingsLocation;
        while(record.length() < DICT_CHAR_LENGTH-1){//pad with spaces, if necessary
            record = record.concat(" ");
        }
        record = record.concat("\n");

        if(record.length()!=DICT_CHAR_LENGTH){
            throw new IRException("Failed to properly format dict entry for file write");
        }

        dictOutStream.write(record.getBytes());
    }

    private void writePosting(Posting post, FileOutputStream postOutStream) throws IOException, IRException{
        String postStr = post.getDocId().toString() + " " + post.getWeight();
        if (postStr.length() > POSTING_CHAR_LENGTH-1){ //truncate the weight, if necessary
            postStr = postStr.substring(0, POSTING_CHAR_LENGTH);
        }
        while(postStr.length()<POSTING_CHAR_LENGTH-1){//pad with spaces, if necessary
            postStr = postStr.concat(" ");
        }
        postStr = postStr.concat("\n");

        if (postStr.length()!=POSTING_CHAR_LENGTH){
            throw new IRException("Failed to properly format posting for file write");
        }
        postOutStream.write(postStr.getBytes());
    }
}
