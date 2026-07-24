package businesslogic;

import datamodels.dealer;
import utility.ManualSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDealerSelector {

    private final Random random;

    public RandomDealerSelector() {
        this.random = new Random();
    }

    public RandomDealerSelector(Random random) {
        this.random = random;
    }

    public List<dealer> selectFourUniqueSortedByLocation(List<dealer> allDealers) {
        if (allDealers == null || allDealers.size() < 4) {
            throw new IllegalArgumentException("need at least 4 dealers");
        }

        List<dealer> chosen = new ArrayList<>();
        List<Integer> usedIndexes = new ArrayList<>();

        while (chosen.size() < 4) {
            int index = random.nextInt(allDealers.size());
            if (!usedIndexes.contains(index)) {
                usedIndexes.add(index);
                chosen.add(allDealers.get(index));
            }
        }

        return ManualSort.sortByLocation(chosen);
    }
}