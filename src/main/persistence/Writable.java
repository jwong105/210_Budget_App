package persistence;

import org.json.JSONObject;

// EFFECTS: returns this as JSON object
public interface Writable {
    JSONObject toJson();
}
