package nl.indigobeta.james.dispenser.Controllers;

import nl.indigobeta.james.dispenser.Exceptions.EmptySlotException;
import nl.indigobeta.james.dispenser.Exceptions.NoSuchDrankException;
import nl.indigobeta.james.dispenser.Objects.DispenseTemplate;
import nl.indigobeta.james.dispenser.Objects.MixDrank;
import nl.indigobeta.james.dispenser.Objects.Slot;
import nl.indigobeta.james.dispenser.Server;

/**
 * Created by jelle on 02/11/2016.
 */
public class Calculator
{
    private Database db;
    private ArduinoController ac;

    DispenseTemplate template;
    MixDrank mix;
    Slot slotOne;
    Slot slotTwo;
    Slot slotThree;

    /*
     * Creates the mixdrank and sends it to the arduinocontroller to be dispensed.
     * Returns true or false whether this action succeeded or failed.
     */
    public boolean createMix(int identifier) throws Exception
    {
        //Check if the mix exists.
        db = Server.getDatabase();
        mix = db.getMixDrank(identifier);

        //Get the slot info.
        slotOne = db.getSlot(1);
        slotTwo = db.getSlot(2);
        slotThree = db.getSlot(3);

        //create a template containing the milliliters to dispense per slot.
        template = createTemplate();

        //Check if there is enough fluid to make the mix.
        if(!checkContent())
        {
            throw new EmptySlotException();
        }

        //controll the arduino to create the mix.
        ac = Server.getArduinoController();

        if(template.getMlValveOne() != 0)
        {
            ac.openValve(1, mlToMs(template.getMlValveOne()));
            wait((int)mlToMs(template.getMlValveOne()) + 1500);
        }

        if(template.getMlValveTwo() != 0)
        {
            ac.openValve(2, mlToMs(template.getMlValveTwo()));
            wait((int) mlToMs(template.getMlValveTwo()) + 1500);
        }

        if(template.getMlValveThree() != 0)
        {
            ac.openValve(3, mlToMs(template.getMlValveThree()));
            wait((int) mlToMs(template.getMlValveThree()) + 1500);
        }

        //Update the database.
        db.updateInhoud(1, slotOne.getMlDrank() - template.getMlValveOne());
        db.updateInhoud(2, slotTwo.getMlDrank() - template.getMlValveTwo());
        db.updateInhoud(3, slotThree.getMlDrank() - template.getMlValveThree());

        //Delete all temporary data.
        template = null;
        mix = null;
        slotOne = null;
        slotTwo = null;
        slotThree = null;

        return true;
    }

    /*
     * Creates a template in which the order of slots and given mix components are the same.
     * Returns a dispensetemplate containing the right information for each valve.
     */
    private DispenseTemplate createTemplate() throws NoSuchDrankException
    {
        DispenseTemplate template = new DispenseTemplate();

        if(mix.getNaamDrankEen().equals(slotOne.getNaamDrank()))
        {
            template.setMlValveOne(mix.getMlDrankEen());
        }
        else if(mix.getNaamDrankEen().equals(slotTwo.getNaamDrank()))
        {
            template.setMlValveTwo(mix.getMlDrankEen());
        }
        else if(mix.getNaamDrankEen().equals(slotThree.getNaamDrank()))
        {
            template.setMlValveThree(mix.getMlDrankEen());
        }
        else if(mix.getNaamDrankEen().equals("0"))
        {
            template.setMlValveFour(0);
        }
        else
        {
            throw new NoSuchDrankException();
        }

        if(mix.getNaamDrankTwee().equals(slotOne.getNaamDrank()))
        {
            template.setMlValveOne(mix.getMlDrankTwee());
        }
        else if(mix.getNaamDrankTwee().equals(slotTwo.getNaamDrank()))
        {
            template.setMlValveTwo(mix.getMlDrankTwee());
        }
        else if(mix.getNaamDrankTwee().equals(slotThree.getNaamDrank()))
        {
            template.setMlValveThree(mix.getMlDrankTwee());
        }
        else if(mix.getNaamDrankTwee().equals("0"))
        {
            template.setMlValveFour(0);
        }
        else
        {
            throw new NoSuchDrankException();
        }

        if(mix.getNaamDrankDrie().equals(slotOne.getNaamDrank()))
        {
            template.setMlValveOne(mix.getMlDrankDrie());
        }
        else if(mix.getNaamDrankDrie().equals(slotTwo.getNaamDrank()))
        {
            template.setMlValveTwo(mix.getMlDrankDrie());
        }
        else if(mix.getNaamDrankDrie().equals(slotThree.getNaamDrank()))
        {
            template.setMlValveThree(mix.getMlDrankDrie());
        }
        else if(mix.getNaamDrankDrie().equals("0"))
        {
            template.setMlValveFour(0);
        }
        else
        {
            throw new NoSuchDrankException();
        }

        return template;
    }

    /*
     * Checks if the machine contains enough fluid to make the mix.
     * Returns true or false whether the machine contains enough or not.
     */
    private boolean checkContent()
    {
        return template.getMlValveOne() <= slotOne.getMlDrank()
                && template.getMlValveTwo() <= slotTwo.getMlDrank()
                && template.getMlValveThree() <= slotThree.getMlDrank();

    }

    /*
     * Converts the given milliliters to the matching milliseconds which the valve needs to open.
     * Returns the milliseconds which the valve needs to open.
     */
    private double mlToMs(int ml)
    {
        return ml * 26.7;
    }

    /*
     * Delays the program for given time.
     */
    private void wait(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }
        catch(Exception e)
        {

        }
    }
}
