package es.ucm.stalos.androidengine;

public interface SerializableSystem {
    /**
     * Save the current data
     *
     * @return true if success
     */
    boolean saveData();

    /**
     * Load the current data
     *
     * @return true if success
     */
    boolean loadData();
}
