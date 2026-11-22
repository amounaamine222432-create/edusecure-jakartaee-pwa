
package tn.supcom.edusecure.api;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Base64;
import java.util.UUID;

@Path("/documents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DocumentResource {

    private static final DocumentService SERVICE =
            new DocumentService();

    /**
     * Emission d'un document.
     *
     * POST /api/documents
     *
     * Acces reserve au role EMETTEUR.
     */
    @POST
    @RolesAllowed("EMETTEUR")
    public Response issue(JsonObject body) {

        try {
            if (body == null) {
                return error(400, "Corps JSON manquant");
            }

            String filename = body.getString("filename");
            String encoded = body.getString("contentBase64");

            byte[] content = decode(encoded);

            DocumentService.DocumentRecord result =
                    SERVICE.issue(filename, content);

            JsonObject response = Json.createObjectBuilder()
                    .add("id", result.id().toString())
                    .add("filename", result.filename())
                    .add("sha256", result.sha256())
                    .add("size", result.size())
                    .build();

            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();

        } catch (IllegalArgumentException |
                 NullPointerException |
                 ClassCastException exception) {

            return error(400, "Document ou donnees invalides");
        }
    }

    /**
     * Verification de l'integrite d'un document.
     *
     * POST /api/documents/verify
     *
     * Compare le SHA-256 du document fourni
     * avec l'empreinte conservee en memoire.
     */
    @POST
    @Path("/verify")
    @PermitAll
    public Response verify(JsonObject body) {

        try {
            if (body == null) {
                return error(400, "Corps JSON manquant");
            }

            UUID id = UUID.fromString(
                    body.getString("documentId")
            );

            byte[] content = decode(
                    body.getString("contentBase64")
            );

            DocumentService.VerificationStatus result =
                    SERVICE.verify(id, content);

            if (result == DocumentService.VerificationStatus.NOT_FOUND) {
                return error(404, "Document non trouve");
            }

            boolean valid =
                    result == DocumentService.VerificationStatus.VALID;

            JsonObject response = Json.createObjectBuilder()
                    .add("valid", valid)
                    .build();

            return Response.ok(response).build();

        } catch (IllegalArgumentException |
                 NullPointerException |
                 ClassCastException exception) {

            return error(400, "Requete de verification invalide");
        }
    }

    /**
     * Decode un contenu Base64.
     *
     * La taille maximale est controlee avant
     * le decodage pour limiter l'allocation memoire.
     * DocumentService valide ensuite la taille decodee.
     */
    private static byte[] decode(String encoded) {

        if (encoded == null
                || encoded.length() > 14_000_000) {

            throw new IllegalArgumentException(
                    "Contenu Base64 invalide");
        }

        return Base64.getDecoder().decode(encoded);
    }

    /**
     * Construit une reponse JSON d'erreur.
     */
    private static Response error(int status, String message) {

        JsonObject response = Json.createObjectBuilder()
                .add("error", message)
                .build();

        return Response.status(status)
                .entity(response)
                .build();
    }
}
