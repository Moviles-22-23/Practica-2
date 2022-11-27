package es.ucm.stalos.nonogramas.logic.data;

import java.util.ArrayList;
import java.util.List;

public class PackageData {
    public PackageData()
    {
        for(int i = 0; i < 20; i++)
        {
            _levelDataList.add(new LevelData());
        }
    }

    /**
     * List of the data of each level of the package
     */
    public List<LevelData> _levelDataList = new ArrayList<>(20);

    // Cualquier info extra
}
