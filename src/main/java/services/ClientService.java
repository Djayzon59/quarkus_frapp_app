package services;

import dto.ClientDto;
import dto.MailDto;
import entities.ClientEntity;
import entities.PaysEntity;
import entities.UtilisateurEntity;
import entities.VilleEntity;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import outils.HateOas;
import outils.SecurityTools;
import outils.Validator;
import repo.ClientRepo;
import repo.PaysRepo;
import repo.UserRepo;
import repo.VilleRepo;
import restClient.MailClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


@Path("/Professionnels/")
@Tag(name = "Professionnels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientService {

    @Inject
    HateOas hateOas;
    @Inject
    ClientRepo clientRepo;
    @Inject
    UserRepo userRepo;
    @Inject
    VilleRepo villeRepo;
    @Inject
    PaysRepo paysRepo;
    @Context
    UriInfo uriInfo;
    @RestClient
    MailClient mailClient;


    @Transactional
    @POST
    @APIResponse(responseCode = "200", description = "OK !")
    @APIResponse(responseCode = "250", description = "Clef Api Invalide !")
    @APIResponse(responseCode = "251", description = "Quota Clef API dépassé !")
    @APIResponse(responseCode = "417", description = "Le login existe déjà !")
    @Operation(summary = "Create pro Account", description = "Create pro account")
    public Response createProAccount(@HeaderParam("login") String login, @HeaderParam("password") String password,
                                     ClientDto clientDto) {

        if (userRepo.findById(login) != null){
            hateOas.setMessage("Le compte existe déjà !");
            return Response.status(417).build();
        }

        if (!Validator.isMailValid(login))
            return Response.status(400, "Adresse mail non valide !").build();
        if (!Validator.isValidPassword(password))
            return Response.ok("MDP Invalide").status(400, "MDP !").build();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();
        String plainTextParameters = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s",
                login,
                BcryptUtil.bcryptHash(password, 31, "MyPersonnalSalt0".getBytes()),
                new SimpleDateFormat("dd-MM-yy-HH:mm:ss").format(expiration),
                clientDto.getDenominationSociale(),
                clientDto.getNumRue(),
                clientDto.getLibelleRue(),
                clientDto.getComplementAdresse(),
                clientDto.getCodePostal(),
                clientDto.getSiren(),
                clientDto.getNumTel(),
                clientDto.getLibelleVille(),
                clientDto.getLibellePays());
        String cryptedParameters = SecurityTools.encrypt(plainTextParameters);

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("Professionnels").path("confirm");
        uriBuilder.queryParam("code", cryptedParameters);

        String bodyResponse = String.format("Veuillez cliquer sur le lien suivant pour confirmer la création de votre compte : %s", uriBuilder.build());
        MailDto mail = new MailDto();
        mail.setSendTo(login);
        mail.setSubject("Confirmation de compte");
        mail.setSendAt(LocalDateTime.now());
        mail.setTexte(bodyResponse);
        Response response = mailClient.sendEmail(mail, "SECRET_KEY");
        if (response.getStatus() == 200)
            return Response.ok("Un mail de confirmation vous a été envoyé !").status(200).build();
        return response;
    }


    @Transactional
    @GET
    @Path("/confirm")
    public Response confirmCreate(@QueryParam("code") String code) {

        String[] params = SecurityTools.decrypt(code).split("\\|");
        try {
            Date expireAt = new SimpleDateFormat("dd-MM-yy-HH:mm:ss").parse(params[2]);
            if (expireAt.before(Calendar.getInstance().getTime()))
                return Response.ok("Le lien n'est plus valide !").status(400, "Le lien n'est plus valide !").build();
        } catch (ParseException e) {
            return Response.ok("Le lien n'est pas valide !").status(400, "Le lien n'est pas valide !").build();
        }

        UtilisateurEntity user = new UtilisateurEntity();
        user.setMail_utilisateur(params[0]);
        user.setPassword(params[1]);
        if (userRepo.findById(user.getMail_utilisateur()) != null)
            return Response.ok("Le lien a déjà été utilisé !").status(400, "Le lien a déjà été utilisé !").build();
        user.setIdRole(2004);
        userRepo.persist(user);

        ClientEntity clientEntity = new ClientEntity(params[3], params[4], params[5], params[6], params[7], params[8], params[9]);
        clientEntity.setVilleEntity(new VilleEntity(params[10], new PaysEntity(params[11])));
        clientEntity.setIdUser(params[0]);
        paysRepo.persist(clientEntity.getVilleEntity().getPaysEntity());
        villeRepo.persist(clientEntity.getVilleEntity());
        clientRepo.persist(clientEntity);

        return Response.status(200).build();
    }

    @RolesAllowed({"super-admin", "client"})
    @Transactional
    @DELETE
    @APIResponse(responseCode = "200", description = "OK !")
    @APIResponse(responseCode = "404", description = "Ce client n'existe pas !")
    @Path("/{id}/")
    public Response deleteById(@PathParam("id") Integer id) {

        ClientEntity clientEntity = clientRepo.findById(id);
        if (clientEntity == null) {
            return Response.status(404).build();
        }
        clientRepo.deleteById(id);
        return Response.status(200).build();
    }




    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK !")
    @APIResponse(responseCode = "404", description = "Ce client n'existe pas !")
    @Operation(summary = "Consulter mes infos (pro)", description = "Consulter mes infos (pro)")
    @Path("/{id}/mon-compte/")
    public Response consulterInfos(@PathParam("id") Integer id){
        ClientEntity clientEntity = clientRepo.findById(id);
        if (clientEntity == null)
            return Response.status(404).build();
        return Response.ok(clientDto).build();
    }

    @Transactional
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK !")
    @APIResponse(responseCode = "404", description = "Ce client n'existe pas !")
    @Operation(summary = "Consulter mes infos (pro)", description = "Consulter mes infos (pro)")
    @Path("/{id}/mon-compte/")
    public Response modifierInfos(@PathParam("id") Integer id){
        ClientEntity clientEntity = clientRepo.findById(id);
        if (clientEntity == null)
            return Response.status(404).build();
        return Response.ok(clientDto).build();
    }*/


}

