package com.ocado.basket;

import java.util.ArrayList;
import java.util.List;

/**
 * SUBSET GENERATOR
 * Class made generating delivery method subsets
 */
public class SubsetGenerator {
    /**
     *
     * @param list - list of all delivery methods
     * @param length - length of the subset
     * @return List containing all subsets of the given length
     */
    public static List<List<String>> getSubsets(List<String> list, int length){
        List<List<String>> subsets = new ArrayList<>();

        generateSubsetsHelper(list, length, 0, new ArrayList<String>(), subsets);

        return subsets;
    }

    /**
     *
     * @param list - list of all delivery methods
     * @param length - length of the subset
     * @param index - current index
     * @param currentSubset - currentSubser
     * @param subsets - list containing all already created subsets
     */
    private static void generateSubsetsHelper(List<String> list, int length, int index, List<String> currentSubset, List<List<String>> subsets) {
        if (currentSubset.size() == length) {
            subsets.add(new ArrayList<>(currentSubset));
            return;
        }
        for (int i = index; i < list.size(); i++) {
            currentSubset.add(list.get(i));
            generateSubsetsHelper(list, length, i + 1, currentSubset, subsets);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }
}
