package nl.indigobeta.james.dispenser.Controllers;

import nl.indigobeta.james.dispenser.Objects.MixDrank;
import nl.indigobeta.james.dispenser.Objects.Slot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by jelle on 12/10/2016.
 */
public class Database
{
    private Connection con;
    private String host = "jdbc:mysql://localhost:3306/james";
    private String uName = "root";
    private String uPass = "password123";
    private ResultSet rs = null;

    public Database()
    {
        this.connect();
    }

    /*
     * Creates the database connection.
     */
    private void connect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(host, uName, uPass);
            System.out.println("Database connection established.");
        }
        catch(Exception e)
        {
            System.out.println("Database connection failed. "+ e);
        }
    }

    /*
     * Collects all the info about the given slot from the database.
     * Returns a slot object containing the collected info.
     */
    public Slot getSlot(int slotId)
    {
        Slot slot = new Slot();
        slot.setSlotId(slotId);

        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * " +
                                                        "FROM james.dispenser " +
                                                        "WHERE slotId = ?");

            ps.setInt(1, slotId);

            rs = ps.executeQuery();
            rs.next();

            slot.setNaamDrank(rs.getString("naamDrank"));
            slot.setMlDrank(rs.getInt("mlDrank"));
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong when getting the slot." + e);
        }

        return slot;
    }
    /*
     * Collects all the info about the given mixdrank from the database.
     * Returns a mixdrank object containing the collected info.
     */
    public MixDrank getMixDrank(int identifier)
    {
        MixDrank mixDrank = new MixDrank();

        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * " +
                                                        "FROM james.mixDranken " +
                                                        "WHERE mixDrankId = ? ");

            ps.setInt(1, identifier);

            rs = ps.executeQuery();
            rs.next();

            mixDrank.setMixDrankNaam(rs.getString("mixDrankNaam"));
            mixDrank.setNaamDrankEen(rs.getString("naamDrankEen"));
            mixDrank.setNaamDrankTwee(rs.getString("naamDrankTwee"));
            mixDrank.setNaamDrankDrie(rs.getString("naamDrankDrie"));

            mixDrank.setMixDrankId(rs.getInt("mixDrankId"));
            mixDrank.setMlDrankEen(rs.getInt("mlDrankEen"));
            mixDrank.setMlDrankTwee(rs.getInt("mlDrankTwee"));
            mixDrank.setMlDrankDrie(rs.getInt("mlDrankDrie"));

        }
        catch(Exception e)
        {
            System.out.println("Something went wrong when getting the ingredients");
        }

        return mixDrank;
    }

    /*
     * Updates the capacity from the given slot in the database.
     * Returns true or false whether the update succeeded or failed.
     */
    public boolean updateInhoud(int slot, int inhoud)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("UPDATE james.dispenser SET mlDrank=? WHERE slotId=?");
            ps.setInt(1, inhoud);
            ps.setInt(2, slot);

            ps.executeUpdate();
            return true;
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong when updating the contents. " + e);
        }

        return false;
    }

    /*
     * Updates the given slot in the database.
     * Returns true or false whether the update succeeded or failed.
     */
    public boolean updateSlot(Slot slot)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("UPDATE james.dispenser SET mlDrank = ?, naamDrank = ? WHERE slotId = ?");
            ps.setInt(1, slot.getMlDrank());
            ps.setString(2, slot.getNaamDrank());
            ps.setInt(3, slot.getSlotId());

            ps.executeUpdate();
            return true;
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong when updating the slot. " + e);
        }

        return false;
    }

    /*
     * Updates the given mixDrank in the database.
     * Returns true or false whether the update succeeded or failed.
     */
    public boolean updateMixDrank(MixDrank mixDrank)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("UPDATE james.mixDranken SET mixDrankNaam=?, "
                                                                                    + "naamDrankEen=?, "
                                                                                    + "mlDrankEen=?, "
                                                                                    + "naamDrankTwee=?, "
                                                                                    + "mlDrankTwee=?, "
                                                                                    + "naamDrankDrie=?, "
                                                                                    + "mlDrankDrie=? "
                                                                                    + "WHERE mixDrankId=?");
            ps.setString(1, mixDrank.getMixDrankNaam());
            ps.setString(2, mixDrank.getNaamDrankEen());
            ps.setInt(3, mixDrank.getMlDrankEen());
            ps.setString(4, mixDrank.getNaamDrankTwee());
            ps.setInt(5, mixDrank.getMlDrankTwee());
            ps.setString(6, mixDrank.getNaamDrankDrie());
            ps.setInt(7, mixDrank.getMlDrankDrie());
            ps.setInt(8, mixDrank.getMixDrankId());

            ps.executeUpdate();
            return true;
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong when updating the mixDrank. " + e);
        }

        return false;
    }

    /*
     * Inserts a new mixdrank in the database.
     * Returns true or false whether the insert succeeded or failed.
     */
    public boolean insertMixDrank(MixDrank mixDrank)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT MAX(mixDrankId) FROM james.mixDranken");

            rs = ps.executeQuery();
            rs.next();

            mixDrank.setMixDrankId(rs.getInt("MAX(mixDrankId)") + 1);
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong when selecting the highest mix identifier. " + e);
            return false;
        }

        try
        {
            PreparedStatement ps = con.prepareStatement("INSERT INTO `james`.`mixDranken` (`mixDrankId`, `mixDrankNaam`, `naamDrankEen`, `mlDrankEen`, `naamDrankTwee`, `mlDrankTwee`, `naamDrankDrie`, `mlDrankDrie`) "
                                                        + "VALUES (?,?,?,?,?,?,?,?)");
            ps.setInt(1, mixDrank.getMixDrankId());
            ps.setString(2, mixDrank.getMixDrankNaam());
            ps.setString(3, mixDrank.getNaamDrankEen());
            ps.setInt(4, mixDrank.getMlDrankEen());
            ps.setString(5, mixDrank.getNaamDrankTwee());
            ps.setInt(6, mixDrank.getMlDrankTwee());
            ps.setString(7, mixDrank.getNaamDrankDrie());
            ps.setInt(8, mixDrank.getMlDrankDrie());

            ps.executeUpdate();
            return true;
        }
        catch(Exception e)
        {
            System.out.println("something went wrong when inserting the new mixDrank. " + e);
        }

        return false;
    }

    /*
     * Deletes a given mixdrank form the database.
     * Returns true or false whether the action succeeded or failed.
     */
    public boolean deleteMixDrank(int identifier)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM james.mixDranken WHERE mixDrankId = ?");
            ps.setInt(1, identifier);
            ps.execute();

            return true;
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong when deleting a mixdrank. " + e);
        }

        return false;
    }

    /*
     * Collects all the mixes possible with the current container setup.
     * returns an array containing the ID's of all possible mixes.
     */
    public int[] getPossibleMixes()
    {
        int[] possiblemixes = null;

        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT mixDrankId " +
                                                        "FROM mixDranken " +
                                                        "WHERE naamDrankEen IN (SELECT naamDrank FROM dispenser) " +
                                                        "AND naamDrankTwee IN (SELECT naamDrank FROM dispenser) " +
                                                        "AND naamDrankDrie IN (SELECT naamDrank FROM dispenser);");
            rs = ps.executeQuery();
            ArrayList<Integer> ids = new ArrayList<>();

            while(rs.next())
            {
                ids.add(rs.getInt("mixDrankId"));
            }

            possiblemixes = new int[ids.size()];
            for (int i=0; i < possiblemixes.length; i++)
            {
                    possiblemixes[i] = ids.get(i).intValue();
            }
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong when getting the possible mixes" + e);
        }

        return possiblemixes;
    }
}
