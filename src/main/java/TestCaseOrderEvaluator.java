import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class TestCaseOrderEvaluator {

    static double fitnessFunction(Map<String, boolean[]> testCases, String[] candidate) {
        int numberOfFaults = testCases.values().iterator().next().length;
        int position = 1;
        Map<Integer, Integer> faultFound = new HashMap<>();
        for (String test : candidate) {
            boolean[] faults = testCases.get(test);
            for (int i = 0; i < numberOfFaults; i++) { // For each fault tracks in which position it was found e.g. fault "1" fas found after "4" tests
                if (!faultFound.containsKey(i) && faults[i]) {
                    faultFound.put(i, position);
                }
            }
            position++;
        }
        return calculateAPFD(faultFound.values(), numberOfFaults, candidate.length, numberOfFaults - faultFound.size());
    }

    // 1 - ((TF1+TF2+TF3+ ... +TFn) / (number of tests * number of faults))) + 1 / (2 * number of tests)
    private static double calculateAPFD(Collection<Integer> faultFoundOrder, int numberOfFaults, int subsetSize, int faultsNotFound) {
        double x = faultsNotFound * (subsetSize + 1); // if fault is not found it treats as it was found in the last test + 1
        for (Integer i : faultFoundOrder) { // TF1+TF2+TF3+ ... +TFn
            x += i;
        }
        return 1.0 - (x / (subsetSize * numberOfFaults)) + (1.0 / (2 * subsetSize)); // some of these values could be pre-calculated constants. This would speed up a little bit the calculation but decrease readability
    }

}
