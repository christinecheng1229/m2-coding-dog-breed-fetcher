package dogapi;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        Request request = new Request.Builder()
                .url(String.format("https://dog.ceo/api/breed/%s/list", breed))
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.getString("status") == "success") {
                JSONArray subBreedsResults = responseBody.getJSONArray("message");
                List<String> subBreeds = new ArrayList<>();
                for (int i = 0; i < subBreedsResults.length(); i++) {
                    subBreeds.add(subBreedsResults.getString(i));
                }
                return subBreeds;
            }
            else if (responseBody.getString("message").equals("Breed not found (main breed does not exist)")) {
                throw new BreedNotFoundException(breed);
            }
            else {
                // TODO make sure message is a JSON array when the API call is unsuccessful
                throw new RuntimeException(String.valueOf(responseBody.getJSONArray("message")));
            }
        // TODO make a more precise exception catch
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }
}