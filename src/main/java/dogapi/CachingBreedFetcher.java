package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private BreedFetcher fetcher;
    private int callsMade;
    private Map<String, List<String>> cache;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
        this.callsMade = 0;
        this.cache = new HashMap<>();

    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (!cache.containsKey(breed)) {
            // the specified breed hasn't been cached
            try {
                cache.put(breed, fetcher.getSubBreeds(breed));
                callsMade ++;
            } catch (BreedNotFoundException e) {
                callsMade ++;
                throw new BreedNotFoundException(breed);
            }
        }
        return cache.get(breed);
    }

    public int getCallsMade() {
        return callsMade;
    }
}