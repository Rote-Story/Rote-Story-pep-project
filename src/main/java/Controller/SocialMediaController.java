package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    SocialMediaService socialMediaService;

    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postRegisterAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountId);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);

        return app;
    }

    /**
     * POST handler to register a new account.
     * 
     * @param context object to handle request information and create a response.
     * @throws JsonProcessingException if an issue is encountered when converting
     *                                 json to an Account object.
     */
    private void postRegisterAccountHandler(Context context) throws JsonProcessingException {
        // Convert JSON in POST request to Account object
        ObjectMapper objectMapper = new ObjectMapper();
        Account newAccount = objectMapper.readValue(context.body(), Account.class);

        String username = newAccount.getUsername();

        // Verifying password is longer than 4 characters
        if (newAccount.getPassword().length() < 4) {
            context.status(400);
        }
        // Verifying that username is not blank
        else if (newAccount.getUsername().isBlank()) {
            context.status(400);
        }
        // Verifying that username is not taken
        else if (socialMediaService.getAccountByUsername(username) != null) {
            context.status(400);
        } else {
            newAccount = socialMediaService.addAccount(newAccount);
            context.json(newAccount).status(200);
        }
    }

    /**
     * POST handler to login to an existing account.
     * 
     * @param context object to handle request information and create a response.
     * @throws JsonProcessingException if an issue is encountered when converting
     *                                 json to an Account object.
     */
    private void postLoginHandler(Context context) throws JsonProcessingException {
        // Convert JSON in POST request to Account object
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(context.body(), Account.class);

        // Searching for matching account in database by username
        Account matchingAccount = socialMediaService.getAccountByUsername(account.getUsername());

        // Verifying that username exists in database
        if (matchingAccount == null) {
            context.status(401);
        }
        // Verifying password matches username
        else if (!matchingAccount.getPassword().equals(account.getPassword())) {
            context.status(401);
        } else {
            context.json(matchingAccount).status(200);
        }
    }

    /**
     * POST handler for creating new messages.
     * 
     * @param context object to handle request information and create a response.
     * @throws JsonProcessingException if an issue is encountered when converting
     *                                 json to an Message object.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        // Convert JSON in POST request to Message object
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(context.body(), Message.class);

        // Fetching matching user account for account verification
        Account matchingAccount = socialMediaService.getAccountById(message.getPosted_by());

        // Verifying that the message is posted by an existing user
        if (matchingAccount == null) {
            context.status(400);
        }
        // Verifying that message is not blank
        else if (message.getMessage_text().isBlank()) {
            context.status(400);
        }
        // Verifying that message is less than 255 characters
        else if (message.getMessage_text().length() > 255) {
            context.status(400);
        } else {
            message = socialMediaService.postMessage(message);
            context.json(message).status(200);
        }
    }

    /**
     * GET handler for fetching all messages from the database.
     * 
     * @param context object to handle request information and create a response.
     */
    private void getAllMessages(Context context) {
        // Retrieve list of all messages in database
        List<Message> messages = socialMediaService.getAllMessages();
        context.json(messages).status(200);
    }

    /**
     * GET handler for fetching the message with the matching message_id from the
     * database.
     * 
     * @param context object to handle request information and create a response.
     */
    private void getMessageByIdHandler(Context context) {
        // Get message_id from path parameter
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message message = socialMediaService.getMessageById(message_id);
        if (message == null) {
            context.status(200);
        } else {
            context.json(message).status(200);
        }
    }

    /**
     * DELETE handler for deleting the message with the matching message_id from the
     * database.
     * 
     * @param context object to handle request information and create a response.
     */
    private void deleteMessageByIdHandler(Context context) {
        // Get message_id from path parameter
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message message = socialMediaService.getMessageById(message_id);
        if (message == null) {
            context.status(200);
        } else {
            context.json(message).status(200);
        }
    }

    /**
     * PATCH handler for updating the message with the matching message_id from the
     * database.
     * 
     * @param context object to handle request information and create a response.
     * @throws JsonProcessingException if an issue is encountered when converting
     *                                 json to an Message object.
     */
    private void patchMessageByIdHandler(Context context) throws JsonProcessingException {
        // Convert JSON in POST request to Message object
        ObjectMapper objectMapper = new ObjectMapper();
        Message updatedMessage = objectMapper.readValue(context.body(), Message.class);

        // Get message_text from request body
        String message_text = updatedMessage.getMessage_text();
        // Get message_id from path parameter
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message originalMessage = socialMediaService.getMessageById(message_id);
        // Verifying message with matching message_id was found in database
        if (originalMessage == null) {
            context.status(400);
        }
        // Verifying that message_text is not blank
        else if (message_text.isBlank()) {
            context.status(400);
        }
        // Verifying message length is less than 255 characters
        else if (message_text.length() > 255) {
            context.status(400);
        } else {
            updatedMessage = socialMediaService.updateMessageById(message_id, message_text);
            context.json(updatedMessage).status(200);
        }
    }

    /**
     * GET handler for fetching all messages from the database belonging to the
     * account that has a matching account_id.
     * 
     * @param context object to handle request information and create a response.
     */
    private void getAllMessagesByAccountId(Context context) {

        // Get account_id value from the path parameter
        int account_id = Integer.parseInt(context.pathParam("account_id"));

        // Retrieve list of all messages with a posted_by value matching the account_id
        List<Message> messages = socialMediaService.getAllMessagesByAccountId(account_id);

        // Verifying that messages for the specified account exist
        if (messages == null) {
            context.status(200);
        } else {
            context.json(messages).status(200);
        }
    }

}