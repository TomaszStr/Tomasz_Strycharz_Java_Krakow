package com.ocado.basket;

import java.util.*;

/**
 * Class for splitting baskets
 */
public class BasketSplitter {

    public Map<String, List<String>> configuration;

    /**
     *
     * @param absolutePathToConfigFile - path to JSON file containing configuration
     */
    public BasketSplitter(String absolutePathToConfigFile) {
        //read configuration from file
        configuration = ConfigReader.readConfig(absolutePathToConfigFile);
    }

    /**
     * Splits given basket
     * @param items - all items in the basket
     * @return map: delivery -> list of items, Optimal basket split
     */
    public Map<String, List<String>> split(List<String> items) {

        List<String> uniqueProducts = new ArrayList<>(new HashSet<>(items));

        HashMap<String, List<String>> deliveryToProduct = MapDeliveryToProduct(uniqueProducts,configuration);

        List<String> deliveries = new ArrayList<>(deliveryToProduct.keySet());

        //sort deliveries by the number of products
        deliveries.sort(Comparator.comparingInt(delivery -> deliveryToProduct.get(delivery).size()).reversed());

        List<String> optimalSubset = GetOptimalSubset(deliveryToProduct,deliveries,uniqueProducts);

        Map<String, List<String>> solution = AssignProductsToDeliveries(optimalSubset, items, deliveryToProduct);
        return solution;
    }

    /**
     *
     * @param uniqueProducts - list containing unique products from the basket
     * @param configuration - saved configuration
     * @return Map: Delivery -> list of products
     */
    private HashMap<String, List<String>> MapDeliveryToProduct(List<String> uniqueProducts,Map<String, List<String>> configuration){
        HashMap<String, List<String>> deliveryToProduct = new HashMap<>();
        for (String product : uniqueProducts) {
            //update each delivery's count
            for (String delivery : configuration.get(product)) {
                deliveryToProduct.putIfAbsent(delivery, new ArrayList<>());
                deliveryToProduct.get(delivery).add(product);
            }
        }
        return deliveryToProduct;
    }

    /**
     *
     * @param deliveryToProduct - Map: Delivery -> list of products
     * @param deliveries - list of deliveries
     * @param uniqueProducts - list containing unique products from the basket
     * @return Optimal subset of deliveries for splitting basket
     */
    private List<String> GetOptimalSubset(Map<String, List<String>> deliveryToProduct, List<String> deliveries, List<String> uniqueProducts){
        List<String> optimalSubset = new ArrayList<>();
        SubsetChecker checker = new SubsetChecker(uniqueProducts,deliveries,deliveryToProduct);
        //if already found solution stop searching for a bigger one
        for(int i=1; i <= deliveries.size() && optimalSubset.isEmpty(); i++){
            // Heuristic for optimization:
            // If the number of products for the best deliveries is smaller than the number of products in the basket,
            // then it's not possible for those methods to fill all the products.
            int possible_sum=0;
            for(int j=0; j<i; j++)
                possible_sum += deliveryToProduct.get(deliveries.get(j)).size();

            //check whether it's even possible to fill all products -> avoid checking unnecessary subsets
            if(possible_sum >= uniqueProducts.size()){
                //System.out.println("Subset length "+i);
                List<List<String>> subsets = SubsetGenerator.getSubsets(deliveries,i);
                for(List<String> subset : subsets){
                    //check if subset fill all products
                    if(checker.checkSubset(subset)){
                        //first feasible subset
                        if(optimalSubset.isEmpty()) {
                            optimalSubset = subset;
                            optimalSubset.sort(Comparator.comparingInt(delivery -> deliveryToProduct.get(delivery).size()).reversed());
                        }
                        //compare two subset choose the one with bigger product group
                        else {
                            subset.sort(Comparator.comparingInt(delivery -> deliveryToProduct.get(delivery).size()).reversed());
                            if(deliveryToProduct.get(optimalSubset.get(0)).size() < deliveryToProduct.get(subset.get(0)).size())
                                optimalSubset = subset;
                        }
                    }
                }
            }
        }
        return optimalSubset;
    }

    /**
     *
     * @param optimalSubset - Optimal subset of deliveries for splitting basket
     * @param products - list containg all products from the basket(even duplicates)
     * @param deliveryToProduct - Map: Delivery -> list of products
     * @return Map: Delivery -> list of products, Optimal split for given list of products
     */
    private Map<String,List<String >> AssignProductsToDeliveries(List<String> optimalSubset, List<String> products, Map<String,List<String>> deliveryToProduct){
        Map<String,List<String>> solution = new HashMap<>();
        // Products which still lack delivery method
        List<String> unassigned =  new ArrayList<>(new HashSet<>(products));
        for(String method : optimalSubset){
            solution.put(method,new ArrayList<>());
            for(String product : products) {
                // Check if product already has delivery method
                if (unassigned.contains(product) && deliveryToProduct.get(method).contains(product))
                    solution.get(method).add(product);
                // Save which products have delivery method
                unassigned.removeAll(solution.get(method));
            }
        }
        return solution;
    }

    @Deprecated
    /**
     * Greedy method which doesn't always find optimal solution
     */
    public Map<String, List<String>> greedySplit(List<String> items) {
        //products still without delivery method
        List<String> unassigned = new ArrayList<>(List.copyOf(items));

        //our final result
        Map<String, List<String>> solution = new HashMap<>();

        while(!unassigned.isEmpty()) {
            Map<String, Integer> deliveryCount = new HashMap<>();
            //check deliveries
            for (String product : unassigned) {
                //update each delivery's count
                for (String delivery : configuration.get(product)) {
                    deliveryCount.putIfAbsent(delivery, 0);
                    int count = deliveryCount.get(delivery);
                    deliveryCount.put(delivery, ++count);
                }
            }

            unassigned.sort(
                    (String product1, String product2)->{
                        int deliCount1 = configuration.get(product1).size();
                        int deliCount2 = configuration.get(product2).size();
                        return Integer.compare(deliCount1,deliCount2);
                    }
            );

            //take products with the least possible deliveries
            int leastDeliveries = configuration.get(unassigned.get(0)).size();
            String added = unassigned.get(0);
            //take delivery method with the highest count
            configuration.get(added).sort((String s1, String s2)->{return -Integer.compare(deliveryCount.get(s1),deliveryCount.get(s2));});
            String method = configuration.get(added).get(0);

            System.out.println(added+" deliveries:"+configuration.get(added).size() +" method: "+ method +" count: "+ deliveryCount.get(method));

            List<String> products = new ArrayList<>();
            for (String item : unassigned) {
                //if delivery method is feasible for the remaining product
                if(configuration.get(item).contains(method))
                    products.add(item);
            }
            //remove products which already have a delivery method chosen
            unassigned.removeAll(products);
            //save result to map
            solution.put(method,products);
            System.out.println(method + products);
        }
        return solution;
    }

}
