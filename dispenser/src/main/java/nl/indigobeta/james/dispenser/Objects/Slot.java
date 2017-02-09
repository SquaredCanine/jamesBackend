package nl.indigobeta.james.dispenser.Objects;

/**
 * Created by jelle on 02/11/2016.
 */
public class Slot
{
    private int slotId;
    private String naamDrank;
    private int mlDrank;

    public Slot()
    {

    }

    public Slot(int slotId, String naamDrank, int mlDrank) {
        this.slotId = slotId;
        this.naamDrank = naamDrank;
        this.mlDrank = mlDrank;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getNaamDrank() {
        return naamDrank;
    }

    public void setNaamDrank(String naamDrank) {
        this.naamDrank = naamDrank;
    }

    public int getMlDrank() {
        return mlDrank;
    }

    public void setMlDrank(int mlDrank) {
        this.mlDrank = mlDrank;
    }
}
