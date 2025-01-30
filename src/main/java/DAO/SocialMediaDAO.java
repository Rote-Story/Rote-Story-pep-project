package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class SocialMediaDAO {

    /**
     * Inserts new account object into database.
     * 
     * @param account object to be added to database.
     * 
     * @return newly created Account object with generated_account_id,
     *         or null value if SQLException ocurred.
     */
    public Account insertNewAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Create SQL INSERT statement to add new account to database
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            // Set parameters in SQL statement to match account credentials
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();

            // Get generated key to return in new Account object with credentials
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_account_id = (int) pkeyResultSet.getInt(1);
                connection.close();
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    /**
     * Searches through database for mat
     */
    public Account getAccountById(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";

            // Execute SQL query with prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Retrieve query results
            while (resultSet.next()) {
                // Read record into new Account object
                account = new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"));
            }
            connection.close();
            // Return matching account if one is found in the database
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Retrun null if no account was found or an error occurred
        return null;
    }

    /**
     * Retrieves account with matching username.
     * 
     * @param username a String to check for in the database
     * @return a matching Account if the username exists in the database,
     *         null if no matching username can be found or an exception occurs
     */
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            String sql = "SELECT * FROM account WHERE username = ?";

            // Execute SQL query with prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Retrieve query results
            while (resultSet.next()) {
                // Read record into new Account object
                account = new Account(
                        resultSet.getInt("account_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"));
            }
            connection.close();
            // Return matching account if one is found in the database
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Retrun null if no account was found or an error occurred
        return null;
    }

    /**
     * @param message a new Message object, without an existing message_id, to be
     *                inserted into the database.
     * @return the newly inserted message with its generated message_id, if an
     *         exception occurred, then null is returned
     */
    public Message insertNewMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Create SQL INSERT statement to add new message to database
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

            // Set parameters in SQL statement to match message credentials
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();

            // Get generated key to return in new Account object with credentials
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = (int) pkeyResultSet.getInt(1);
                connection.close();
                return new Message(
                        generated_message_id,
                        message.getPosted_by(),
                        message.getMessage_text(),
                        message.getTime_posted_epoch());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Fetches a message that matches the message_id parameter.
     * 
     * @param message_id an integer matching an existing message_id in the database.
     * @return a Message object with a message_id matching the message_id parameter,
     *         if no matching message is found, returns null.
     */
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";

            // Execute SQL query with prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Retrieve query results
            while (resultSet.next()) {
                // Read record into new Message object
                message = new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch"));
            }
            connection.close();
            // Return matching message if one is found in the database
            return message;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Deletes a message with a message_id that matches the message_id parameter.
     * 
     * @param message_id an integer matching an existing message_id in the database.
     * @return a Message object with a message_id matching the message_id parameter,
     *         if no matching message is found, returns null.
     */
    public Message deleteMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";

            // Execute SQL query with prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Retrieve query results
            while (resultSet.next()) {
                // Read record into new Message object
                message = new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch"));
            }
            connection.close();
            // Return matching message if one is found in the database
            return message;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Updates a message with a message_id that matches the message_id parameter.
     * 
     * @param message_id   an integer matching an existing message_id in the
     *                     database.
     * @param message_text text to update the original message text with.
     * 
     */
    public void updateMessageById(int message_id, String message_text) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

            // Execute SQL update with prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message_text);
            preparedStatement.setInt(2, message_id);
            preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Fetches list of all messages.
     * 
     * @return list of all messages, or null if an error occurred.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        Message message;

        try {
            String sql = "SELECT * FROM message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through query results to fill out messages list
            while (resultSet.next()) {
                // Read record into new Message object
                message = new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch"));

                // Add record to list
                messages.add(message);
            }
            connection.close();
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @param account_id the account_id of the message poster; exists in the message
     *                   table as the foreign key "posted_by"
     * @return a list of all messages belonging to a specific user account
     */
    public List<Message> getAllMessagesByAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        Message message;
        try {
            // Looks for account_id values that match posted_by values in the message table
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            // Execute SQL query with prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Retrieve query results
            while (resultSet.next()) {
                // Read record into new Message object
                message = new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
            connection.close();
            // Return matching message if one is found in the database
            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
