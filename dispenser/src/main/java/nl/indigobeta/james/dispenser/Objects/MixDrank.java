package nl.indigobeta.james.dispenser.Objects;

/**
 * Created by jelle on 02/11/2016.
 */
public class MixDrank
{
    private String mixDrankNaam;
    private String naamDrankEen;
    private String naamDrankTwee;
    private String naamDrankDrie;

    private int mixDrankId;
    private int mlDrankEen;
    private int mlDrankTwee;
    private int mlDrankDrie;

    public MixDrank()
    {

    }

    public MixDrank(String mixDrankNaam, String naamDrankEen, String naamDrankTwee, String naamDrankDrie, int mixDrankId, int mlDrankEen, int mlDrankTwee, int mlDrankDrie) {
        this.mixDrankNaam = mixDrankNaam;
        this.naamDrankEen = naamDrankEen;
        this.naamDrankTwee = naamDrankTwee;
        this.naamDrankDrie = naamDrankDrie;
        this.mixDrankId = mixDrankId;
        this.mlDrankEen = mlDrankEen;
        this.mlDrankTwee = mlDrankTwee;
        this.mlDrankDrie = mlDrankDrie;
    }

    public String getNaamDrankEen() {
        return naamDrankEen;
    }

    public void setNaamDrankEen(String naamDrankEen) {
        this.naamDrankEen = naamDrankEen;
    }

    public String getNaamDrankTwee() {
        return naamDrankTwee;
    }

    public void setNaamDrankTwee(String naamDrankTwee) {
        this.naamDrankTwee = naamDrankTwee;
    }

    public String getNaamDrankDrie() {
        return naamDrankDrie;
    }

    public void setNaamDrankDrie(String naamDrankDrie) {
        this.naamDrankDrie = naamDrankDrie;
    }

    public int getMlDrankEen() {
        return mlDrankEen;
    }

    public void setMlDrankEen(int mlDrankEen) {
        this.mlDrankEen = mlDrankEen;
    }

    public int getMlDrankTwee() {
        return mlDrankTwee;
    }

    public void setMlDrankTwee(int mlDrankTwee) {
        this.mlDrankTwee = mlDrankTwee;
    }

    public int getMlDrankDrie() {
        return mlDrankDrie;
    }

    public String getMixDrankNaam() {
        return mixDrankNaam;
    }

    public void setMixDrankNaam(String mixDrankNaam) {
        this.mixDrankNaam = mixDrankNaam;
    }

    public int getMixDrankId() {
        return mixDrankId;
    }

    public void setMixDrankId(int mixDrankId) {
        this.mixDrankId = mixDrankId;
    }

    public void setMlDrankDrie(int mlDrankDrie) {
        this.mlDrankDrie = mlDrankDrie;
    }
}
