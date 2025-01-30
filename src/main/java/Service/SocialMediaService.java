package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    /**
     * No args SocialMediaService constructor, instantiates SocialMediaDAO.
     */
    public SocialMediaService() {
        socialMediaDAO = new SocialMediaDAO();
    }

    /**
     * Parameterized constructor for a provided SocialMediaDAO
     * 
     * @param SocialMediaDAO
     */
    public SocialMediaService(SocialMediaDAO socialMediaDAO) {
        this.socialMediaDAO = socialMediaDAO;
    }

    /**
     * Creates new account in the database
     * 
     * @param account - an account object with username and password
     * @return the account object with generated account_id in addition
     *         to username and password if it was successfully added, null otherwise
     */
    public Account addAccount(Account account) {
        return socialMediaDAO.insertNewAccount(account);
    }

    /**
     * Fetches an accoutn matching the given username if it exists in the database.
     * 
     * @param account - an account object with username and password
     * @return the account object with generated account_id in addition
     *         to username and password if it was successfully added, null otherwise
     */
    public Account getAccountByUsername(String username) {
        return socialMediaDAO.getAccountByUsername(username);
    }

    /**
     * Fetches an account matching the given username if it exists in the database.
     * 
     * @param account_id an integer matching an existing account.
     * @return an account object matching the account_id if it exists in the
     *         database, if no matching account exists, then a null value is
     *         returned.
     */
    public Account getAccountById(int account_id) {
        return socialMediaDAO.getAccountById(account_id);
    }

    /**
     * Adds a newly posted message to the database.
     * 
     * @param message a new message submitted by a user.
     * @return a message object matching the object added to the database with
     *         auto_generated message_id, if an exception occurred, then null is
     *         returned.
     */
    public Message postMessage(Message message) {
        return socialMediaDAO.insertNewMessage(message);
    }

    /**
     * Fetches all messages from the database.
     * 
     * @return a list containing all messages in the database; if there are no
     *         messages, then an empty list is returned.
     */
    public List<Message> getAllMessages() {
        return socialMediaDAO.getAllMessages();
    }

    /**
     * Fetches a message matching the given message_id if it exists in the database.
     * 
     * @param message_id an integer matching an existing message_id.
     * @return a message object matching the message_id if it exists in the
     *         database, if no matching message exists, then a null value is
     *         returned.
     */
    public Message getMessageById(int message_id) {
        return socialMediaDAO.getMessageById(message_id);
    }

    /**
     * Deletes a message matching the given message_id if it exists in the database.
     * 
     * @param message_id an integer matching an existing message_id.
     * @return a message object matching the message_id if it exists in the
     *         database, if no matching message exists, then a null value is
     *         returned.
     */
    public Message deleteMessageById(int message_id) {
        return socialMediaDAO.deleteMessageById(message_id);
    }

    /**
     * Updates a message matching the given message_id if it exists in the database.
     * 
     * @param message_id an integer matching an existing message_id.
     * @return a message object matching the message_id if it exists in the
     *         database, if no matching message exists, then a null value is
     *         returned.
     */
    public Message updateMessageById(int message_id, String message_text) {
        socialMediaDAO.updateMessageById(message_id, message_text);
        return socialMediaDAO.getMessageById(message_id);
    }

    /**
     * Fetches all messages matching the given account_id (posted_by) if any exist
     * in the database.
     * 
     * @param account_id an integer matching an existing message_id.
     * @return a message object matching the message_id if it exists in the
     *         database, if no matching message exists, then a null value is
     *         returned.
     */
    public List<Message> getAllMessagesByAccountId(int account_id) {
        return socialMediaDAO.getAllMessagesByAccountId(account_id);
    }

}