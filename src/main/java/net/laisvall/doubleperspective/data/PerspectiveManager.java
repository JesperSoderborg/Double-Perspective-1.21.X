package net.laisvall.doubleperspective.data;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerspectiveManager {

    private static final PerspectiveManager INSTANCE = new PerspectiveManager();
    public static PerspectiveManager getInstance() { return INSTANCE; }

    private final List<Perspective> perspectives = new ArrayList<>();
    private Perspective activePerspective; // the one currently “switched to”

    private PerspectiveManager() {}

    // Access all perspectives
    public List<Perspective> getAllPerspectives() {
        return Collections.unmodifiableList(perspectives);
    }

    // Add / remove / edit
    public void addPerspective(Perspective p) { perspectives.add(p); savePerspectives(); }
    public void removePerspective(Perspective p) { perspectives.remove(p); savePerspectives(); }
    public void editPerspective(Perspective oldP, Perspective newP) {
        int index = perspectives.indexOf(oldP);
        if (index != -1) perspectives.set(index, newP);
        savePerspectives();
    }

    // Save/load from JSON (simplified)
    private void savePerspectives() {
        // TODO: write perspectives list to config/doubleperspective/perspectives.json
    }

    public void loadPerspectives() {
        // TODO: read JSON and populate `perspectives`
    }

    // Switch active perspective
    public void setActivePerspective(Perspective p) { activePerspective = p; }
    public Perspective getActivePerspective() { return activePerspective; }
}
