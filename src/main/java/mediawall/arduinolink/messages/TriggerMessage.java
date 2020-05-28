package mediawall.arduinolink.messages;

import lombok.Getter;
import lombok.Setter;
import mediawall.arduinolink.messages.enums.TriggerType;

public class TriggerMessage {
    @Setter @Getter
    private TriggerType triggerType;
}
