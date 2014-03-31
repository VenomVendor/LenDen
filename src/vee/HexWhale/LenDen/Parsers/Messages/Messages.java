
package vee.HexWhale.LenDen.Parsers.Messages;

public class Messages {
    private String item_id;
    private String item_title;
    private String last_message;
    private String last_message_date_time;
    private String last_message_first_name;
    private String last_message_last_name;
    private String partner_first_name;
    private String partner_id;
    private String partner_last_name;
    private int unread_messages;

    public String getItem_id() {
        return this.item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_title() {
        return this.item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getLast_message() {
        return this.last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getLast_message_date_time() {
        return this.last_message_date_time;
    }

    public void setLast_message_date_time(String last_message_date_time) {
        this.last_message_date_time = last_message_date_time;
    }

    public String getLast_message_first_name() {
        return this.last_message_first_name;
    }

    public void setLast_message_first_name(String last_message_first_name) {
        this.last_message_first_name = last_message_first_name;
    }

    public String getLast_message_last_name() {
        return this.last_message_last_name;
    }

    public void setLast_message_last_name(String last_message_last_name) {
        this.last_message_last_name = last_message_last_name;
    }

    public String getPartner_first_name() {
        return this.partner_first_name;
    }

    public void setPartner_first_name(String partner_first_name) {
        this.partner_first_name = partner_first_name;
    }

    public String getPartner_id() {
        return this.partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getPartner_last_name() {
        return this.partner_last_name;
    }

    public void setPartner_last_name(String partner_last_name) {
        this.partner_last_name = partner_last_name;
    }

    public int getUnread_messages() {
        return this.unread_messages;
    }

    public void setUnread_messages(int unread_messages) {
        this.unread_messages = unread_messages;
    }
}
