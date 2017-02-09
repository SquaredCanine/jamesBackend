package nl.indigobeta.james.dispenser;

import nl.indigobeta.james.api.*;
import nl.indigobeta.james.dispenser.Controllers.*;
import nl.indigobeta.james.dispenser.Exceptions.*;
import nl.indigobeta.james.dispenser.Objects.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Jelle van Hengel on 06/10/2016.
 */
@Path("/")
public class DispenserEndpoint
{

    /*
     * Test the backend connection.
     * Returns the appropriate response depending on a successful database connection.
     */
    @GET
    @Path ("/test/connection")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response testConnection()
    {
        Response response = new Response();
        System.out.println("Someone connected using the test endpoint");
        if(Server.getDatabase() != null)
        {
            response.setMessage("OK");
            response.setStatus(200);
        }
        else
        {
            response.setMessage("INTERNAL_SERVER_FAULT");
            response.setStatus(500);
        }

        return response;
    }

    /*
     * Tests the connection to the valve by opening the given valve number.
     * Returns the appropriate response depending on a successful valve connection.
     */
    @GET
    @Path ("/test/valve/{identifier}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response testValve(@PathParam("identifier") int identifier)
    {
        Response response = new Response();
        System.out.println("Someone connected using the testValve endpoint");
        System.out.println("Trying to open the valve for 10 seconds");

        try
        {
            ArduinoController controller = Server.getArduinoController();
            controller.openValve(identifier, 10000);
            System.out.println("Valve opened succesfully.");
            response.setMessage("OK");
            response.setStatus(200);
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong in the testvalve endpoint.");
            response.setMessage("INTERNAL_SERVER_FAULT");
            response.setStatus(500);
        }

        return response;
    }

    /*
     * Creates the mixdrank belonging to the given identifier.
     * Returns the response depending on the success of creating the mixdrank.
     */
    @GET
    @Path ("/dispenser/drink/{identifier}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response createMixDrank(@PathParam("identifier") int identifier)
    {
        Response response = new Response();

        if(identifier == 0)
        {
            response.setStatus(204);
            response.setMessage("INVALID_REQUEST");
            return response;
        }

        try
        {
            Calculator calculator = Server.getCalculator();
            calculator.createMix(identifier);
            response.setStatus(200);
            response.setMessage("OK");

        }
        catch(EmptySlotException e)
        {
            System.out.println("An exception was thrown because one of the needed containers is empty.");
            response.setStatus(400);
            response.setMessage("EMPTY_SLOT");
        }
        catch(NoSuchDrankException n)
        {
            System.out.println("An exception was thrown because one of the ingredients does not exist.");
            response.setStatus(400);
            response.setMessage("NO_SUCH_DRANK");
        }
        catch(Exception x)
        {
            System.out.println("Something really went wrong in the /dispenser/drink endpoint. " + x);
            response.setStatus(500);
            response.setMessage("INTERNAL_SERVER_FAULT");
        }

        return response;
    }

    /*
     * Collects the info about the given slot.
     * Retuns a slotresponse containing the info belonging to the given slot.
     */
    @GET
    @Path("/database/get/slot/{identifier}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public SlotResponse getSlot(@PathParam("identifier") int identifier)
    {
        SlotResponse response = new SlotResponse();
        response.setSlotId(identifier);

        if(identifier < 1 || identifier > 3)
        {
            response.setStatus(204);
            response.setMessage("INVALID_REQUEST");
            return response;
        }

        try
        {
            Database db = Server.getDatabase();
            Slot slot = db.getSlot(identifier);

            response.setDrankNaam(slot.getNaamDrank());
            response.setMlDrank(slot.getMlDrank());
            response.setMessage("OK");
            response.setStatus(200);
        }
        catch(Exception x)
        {
            System.out.println("Something went wrong in the /database/get/slot endpoint. " + x);
            response.setStatus(500);
            response.setMessage("INTERNAL_SERVER_FAULT");
        }

        return response;
    }

    /*
     * Collect the info about the given mixDrank.
     * Returns a mixdrankresponse containing the info belonging to the given mixdrank.
     */
    @GET
    @Path("/database/get/mixdrank/{identifier}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public MixDrankResponse getMixDrank(@PathParam("identifier") int identifier)
    {
        MixDrankResponse response = new MixDrankResponse();
        response.setMixDrankId(identifier);

        if(identifier < 1)
        {
            response.setStatus(204);
            response.setMessage("INVALID_REQUEST");
            return response;
        }

        try
        {
            Database db = Server.getDatabase();
            MixDrank mix = db.getMixDrank(identifier);

            response.setMixDrankId(mix.getMixDrankId());
            response.setMixDrankNaam(mix.getMixDrankNaam());
            response.setNaamDrankEen(mix.getNaamDrankEen());
            response.setMlDrankEen(mix.getMlDrankEen());
            response.setNaamDrankTwee(mix.getNaamDrankTwee());
            response.setMlDrankTwee(mix.getMlDrankTwee());
            response.setNaamDrankDrie(mix.getNaamDrankDrie());
            response.setMlDrankDrie(mix.getMlDrankDrie());

            response.setStatus(200);
            response.setMessage("OK");
        }
        catch(Exception x)
        {
            System.out.println("Something went wrong in the /database/get/mixdrank endpoint. " + x);
            response.setStatus(500);
            response.setMessage("INTERNAL_SERVER_FAULT");
        }

        return response;
    }

    /*
     * Collects the identifier of all possible mixes.
     * Returns a possiblemixesresponse containing the ID of all possible mixes.
     */
    @GET
    @Path("/database/get/possiblemixes")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public PossibleMixesResponse getPossibleMixes()
    {
        PossibleMixesResponse response = new PossibleMixesResponse();

        try
        {
            Database db = Server.getDatabase();
            int[] possiblemixes = db.getPossibleMixes();

            response.setPossibleMixes(possiblemixes);
            response.setStatus(200);
            response.setMessage("OK");
        }
        catch(Exception x)
        {
            System.out.println("Something went wrong in the /database/get/possiblemixes endpoint. " + x);
            response.setStatus(500);
            response.setMessage("INTERNAL_SERVER_FAULT");
        }

        return response;
    }

    /*
     * Gives the backend the needed info to change the given slot.
     * Returns the appropriate response depending on the succes for changing the slot.
     */
    @GET
    @Path("/database/set/slot/{identifier}/{naamDrank}/{mlDrank}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response setSlot(@PathParam("identifier") int identifier,
                            @PathParam("naamDrank") String naamDrank,
                            @PathParam("mlDrank") int mlDrank)
    {
        Response response = new Response();

        if(identifier < 1 || identifier > 3)
        {
            response.setStatus(204);
            response.setMessage("INVALID_REQUEST");
            return response;
        }

        Slot slot = new Slot(identifier, naamDrank, mlDrank);

        try
        {
            Database db = Server.getDatabase();
            if(db.updateSlot(slot))
            {
                response.setStatus(200);
                response.setMessage("OK");
            }
            else
            {
                response.setStatus(500);
                response.setMessage("INTERNAL_SERVER_FAULT");
            }

        }
        catch(Exception x)
        {
            System.out.println("Something went wrong in the /database/set/slot endpoint. " + x);
            response.setStatus(500);
            response.setMessage("INTERNAL_SERVER_FAULT");
        }

        return response;
    }

    /*
     * Gives the backend the needed info to change the given mixdrank.
     * Returns the appropriate response depending on the success for changing the mixdrank.
     */
    @GET
    @Path("/database/set/mixdrank/{identifier}/{mixDrankNaam}/{naamDrankEen}/{mlDrankEen}/{naamDrankTwee}/{mlDrankTwee}/{naamDrankDrie}/{mlDrankDrie}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response setMixDrank(@PathParam("identifier") int identifier,
                                @PathParam("mixDrankNaam") String mixDrankNaam,
                                @PathParam("naamDrankEen") String naamDrankEen,
                                @PathParam("mlDrankEen") int mlDrankEen,
                                @PathParam("naamDrankTwee") String naamDrankTwee,
                                @PathParam("mlDrankTwee") int mlDrankTwee,
                                @PathParam("naamDrankDrie") String naamDrankDrie,
                                @PathParam("mlDrankDrie") int mlDrankDrie)
    {
        Response response = new Response();

        if(identifier < 1)
        {
            response.setStatus(204);
            response.setMessage("INVALID_REQUEST");
            return response;
        }

        MixDrank mix = new MixDrank(mixDrankNaam,
                                    naamDrankEen,
                                    naamDrankTwee,
                                    naamDrankDrie,
                                    identifier,
                                    mlDrankEen,
                                    mlDrankTwee,
                                    mlDrankDrie);

        try
        {
            Database db = Server.getDatabase();
            if(db.updateMixDrank(mix))
            {
                response.setStatus(200);
                response.setMessage("OK");
            }
            else
            {
                response.setStatus(500);
                response.setMessage("INTERNAL_SERVER_FAULT");
            }

        }
        catch(Exception x)
        {
            System.out.println("Something went wrong in the /database/set/mixDrank endpoint. " + x);
            response.setStatus(500);
            response.setMessage("INTERNAL_SERVER_FAULT");
        }

        return response;
    }

    /*
     * Passes the given info to the backend to be inserted into the database.
     * Returns the appropriate response depending on the success for inserting the mixdrank.
     */
    @GET
    @Path("/database/insert/mixdrank/{mixDrankNaam}/{naamDrankEen}/{mlDrankEen}/{naamDrankTwee}/{mlDrankTwee}/{naamDrankDrie}/{mlDrankDrie}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response insertMixDrankInfo(@PathParam("mixDrankNaam") String mixDrankNaam,
                                   @PathParam("naamDrankEen") String naamDrankEen,
                                   @PathParam("mlDrankEen") int mlDrankEen,
                                   @PathParam("naamDrankTwee") String naamDrankTwee,
                                   @PathParam("mlDrankTwee") int mlDrankTwee,
                                   @PathParam("naamDrankDrie") String naamDrankDrie,
                                   @PathParam("mlDrankDrie") int mlDrankDrie)
    {
        Response response = new Response();
        MixDrank mix = new MixDrank(mixDrankNaam,
                                    naamDrankEen,
                                    naamDrankTwee,
                                    naamDrankDrie,
                                    0,
                                    mlDrankEen,
                                    mlDrankTwee,
                                    mlDrankDrie);

        try
        {
            Database db = Server.getDatabase();
            if(db.insertMixDrank(mix))
            {
                response.setStatus(200);
                response.setMessage("OK");
            }
            else
            {
                response.setStatus(500);
                response.setMessage("INTERNAL_SERVER_FAULT");
            }

        }
        catch(Exception x)
        {
            System.out.println("Something went wrong in the /database/set/mixDrank endpoint. " + x);
            response.setStatus(500);
            response.setMessage("INTERNAL_SERVER_FAULT");
        }

        return response;
    }

    /*
     * Gives the backend the note to delete the mixdrank corresponding to the given ID.
     * Returns the appropriate response depending on the success for deleting the mixdrank.
     */
    @GET
    @Path("/database/delete/mixdrank/{identifier}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response deleteMixDrank(@PathParam("identifier") int identifier)
    {
        Response response = new Response();

        if(identifier < 1)
        {
            response.setStatus(204);
            response.setMessage("INVALID_REQUEST");
            return response;
        }

        try
        {
            Database db = Server.getDatabase();
            if(db.deleteMixDrank(identifier))
            {
                response.setStatus(200);
                response.setMessage("OK");
            }
            else
            {
                response.setStatus(500);
                response.setMessage("INTERNAL_SERVER_FAULT");
            }
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong in the /database/delete/mixdrank endpoint. " + e);
        }

        return response;
    }
}
