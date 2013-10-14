import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Project2 {

    final static String tokensSortedByTermFilename = "sortedByToken.txt";
    final static String tokensSortedByFreqFilename = "sortedByFreq.txt";

    public static void main(String[] args) throws IOException, IRException{
       run(args);
    }

    public static void run(String[] args) throws IOException, IRException{
        //timers
        long startTimeMs = System.currentTimeMillis();
        long endTimeMs, buildStartTime, buildEndTime, writeStartTime,writeEndTime;

        //files
        File inDir = new File(args[0]);
        File[] inFiles = inDir.listFiles();
        int numFiles = inFiles.length;
        File outDir = new File(args[1]);


        TokenCollector collector = new TokenCollector(); //stores tokens and localHashTable across documents
        Map<Integer, String> fileNames = new HashMap<Integer, String>(); //Maps docId to filename

        InvertedFileBuilder invertedFileBuilder = new InvertedFileBuilder(); //Builds the inverted file

        buildStartTime = System.currentTimeMillis();
        //Process each file
        int fileCounter = 0;
        for(File inFile : inFiles){
            fileNames.put(fileCounter, inFile.getName());
            DocumentAnalyzer analyzer = new DocumentAnalyzer(); //processes and tokenizes the document
            Map<String, Integer> freqs = analyzer.tokenize(inFile.getAbsolutePath()).getFrequencies(); //tokenize the doc
            collector.addTokens(freqs);

            for (Map.Entry<String, Integer> entry : freqs.entrySet()){
                invertedFileBuilder.addEntry(entry.getKey(), fileCounter, (float)entry.getValue());
            }
            fileCounter++;
        }
        buildEndTime = System.currentTimeMillis();

        writeStartTime = System.currentTimeMillis();
        invertedFileBuilder.printToFiles(outDir.getAbsolutePath().concat("\\dict.txt"), outDir.getAbsolutePath().concat("\\post.txt")); //write dict and post
        writeMapToFile(fileNames, outDir.getAbsolutePath().concat("\\names.txt")); //write docId-names file
        writeEndTime = System.currentTimeMillis();

        System.out.println("Number of files: " + numFiles);
        System.out.println("Number of unique tokens: " + collector.getUniqueTokens());
        System.out.println("Number of non-unique tokens: " + collector.getNonuniqueTokens());
        System.out.println("Runtime of tokenizing and building in memory: " + (buildEndTime-buildStartTime)+"ms");
        System.out.println("Runtime of writing dict, post, and names to file: " + (writeEndTime-writeStartTime)+"ms");
        System.out.println("Total runtime: " + (System.currentTimeMillis() - startTimeMs)+"ms");
    }

    /**
     * Similar to run, but samples with replacement between trials. Used for timing on varying number of documents.
     * @param args
     * @throws IOException
     * @throws IRException
     */
    public static void runTrials(String[] args)throws  IOException, IRException{
        //timers
        long startTimeMs = System.currentTimeMillis();
        long endTimeMs, buildingStartTime, buildEndTime, writeStartTime,writeEndTime;
        List<Long> runtimes = new ArrayList<Long>();

        //files
        File inDir = new File(args[0]);
        File[] inFiles = inDir.listFiles();
        int numFiles = inFiles.length;
        File outDir = new File(args[1]);

        //stores tokens and localHashTable across documents
        TokenCollector collector = new TokenCollector();
        InvertedFileBuilder invertedFileBuilder = new InvertedFileBuilder();

        Random random = new Random(System.currentTimeMillis());
        //Process each file
        for(int i = 0; i < 50; i++){
            buildingStartTime = System.currentTimeMillis();
            List<File> sampleFiles = new ArrayList<File>();
            for (int j = 0; j < (i+1)*10; j++){
                sampleFiles.add(inFiles[random.nextInt(504)]);
            }
            int fileCounter = 0;
            for(File inFile : sampleFiles){
                DocumentAnalyzer analyzer = new DocumentAnalyzer(); //processes the document
                Map<String, Integer> freqs = analyzer.tokenize(inFile.getAbsolutePath()).getFrequencies();
                collector.addTokens(freqs);

                for (Map.Entry<String, Integer> entry : freqs.entrySet()){
                    invertedFileBuilder.addEntry(entry.getKey(), fileCounter, (float)entry.getValue());
                }
                fileCounter++;
            }
            buildEndTime = System.currentTimeMillis();
            runtimes.add(buildEndTime-buildingStartTime);
        }

//        writeStartTime = System.currentTimeMillis();
//        invertedFileBuilder.printToFiles(outDir.getAbsolutePath().concat("\\dict.txt"), outDir.getAbsolutePath().concat("\\post.txt"));
//        writeEndTime = System.currentTimeMillis();

        System.out.println("Number of files: " + numFiles);
        System.out.println("Number of unique tokens: " + collector.getUniqueTokens());
        System.out.println("Number of non-unique tokens: " + collector.getNonuniqueTokens());
        //System.out.println("Runtime of tokenizing and building in memory: " + (buildEndTime-buildingStartTime)+"ms");
//        System.out.println("Runtime of writing dict and post to file: " + (writeEndTime-writeStartTime)+"ms");
        System.out.println("Total runtime: " + (System.currentTimeMillis() - startTimeMs)+"ms");
    }

    public static void writeSortedTokens(List<TermFreq> termFreqs, String outputFilepath) throws IOException{
        File outFile = new File(outputFilepath);
        if(outFile.exists()){
            outFile.delete();
        }
        outFile.createNewFile();

        FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for(TermFreq t : termFreqs){
            bw.write(String.format("%s %d\n", t.getTerm(), t.getFreq()));
        }
        bw.close();
        fw.close();
    }

    public static void writeTokensToFile(Map<String, Integer> tokens, String outputFilepath)throws IOException{
        File outFile = new File(outputFilepath);
        if(outFile.exists()){
            outFile.delete();
        }
        outFile.createNewFile();

        FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for(Map.Entry<String, Integer> entry : tokens.entrySet())
        {
            for(int i = 0; i < entry.getValue(); i++){
                bw.write(String.format("%s ", entry.getKey()));
            }

        }
        bw.close();
        fw.close();
    }

    public static void writeMapToFile(Map<Integer, String> map, String filepath) throws IOException{
        File outfile = new File(filepath);
        outfile.createNewFile();
        FileWriter writer = new FileWriter(outfile);
        for (Map.Entry<Integer, String> entry : map.entrySet()){
            writer.write(entry.getKey() + "\t\t" + entry.getValue() +"\n");
        }
        writer.close();
    }
}
