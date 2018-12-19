package dsa.eetac.upc.edu.tracksminimo2;

public class Track {
    //Attributes
    int id;
    String title;
    String singer;

    //Constructor
    public Track(int id, String title, String singer) {
        this.id = id;
        this.title = title;
        this.singer = singer;
    }

    //Gets i Sets
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
