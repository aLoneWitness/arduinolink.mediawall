package arduinolink.websocket.messages;

import lombok.Getter;
import lombok.Setter;
import arduinolink.websocket.messages.enums.TriggerType;

public class TriggerMessage {
    @Setter @Getter
    private TriggerType triggerType;
}
