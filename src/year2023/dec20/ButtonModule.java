package year2023.dec20;

import java.util.List;

public class ButtonModule extends Module {
    private final Module broadcaster;

    public ButtonModule(Module broadcaster) {
        super("aptly");
        this.broadcaster = broadcaster;
    }

    @Override
    public List<Module> sendPulse() {
        broadcaster.update(Pulse.LOW, this);
        super.updates.clear();
        return List.of(broadcaster);
    }
}
