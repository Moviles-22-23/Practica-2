package es.ucm.stalos.androidengine;

public interface SerializableSystem {
    /**
     * Save the current data
     *
     * @return true if success
     */
    void saveData();

    /**
     * Load the current data
     *
     * @return true if success
     */
    void loadData();
}
