
package vee.HexWhale.LenDen.Parsers.MessagesFull;

import java.util.List;

public class Response {
    private List<Messages_list> messages_list;
    private boolean show_complete;
    private boolean show_lock;
    private boolean show_unlock;

    public List<Messages_list> getMessages_list() {
        return messages_list;
    }

    public void setMessages_list(List<Messages_list> messages_list) {
        this.messages_list = messages_list;
    }

    public boolean isShow_complete() {
        return show_complete;
    }

    public void setShow_complete(boolean show_complete) {
        this.show_complete = show_complete;
    }

    public boolean isShow_lock() {
        return show_lock;
    }

    public void setShow_lock(boolean show_lock) {
        this.show_lock = show_lock;
    }

    public boolean isShow_unlock() {
        return show_unlock;
    }

    public void setShow_unlock(boolean show_unlock) {
        this.show_unlock = show_unlock;
    }

}
