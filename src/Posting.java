public class Posting {
    private Integer docId;
    private Float weight;

    public Posting (Integer docId, Float weight){
        this.docId = docId;
        this.weight = weight;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }
}
