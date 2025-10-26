package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
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
    public List<String> getSubBreeds(String breed) {
        String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "hound/list";
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (!response.isSuccessful()) {
                throw new BreedNotFoundException("cannot find breed for " + breed);
            }
        } catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }

        String responseBody = response.body().toString();
        JSONObject json = new JSONObject(responseBody);

        JSONArray subBreedArray = json.getJSONArray("message");
        List<String> subBreeds = new ArrayList<>();
        for (int i = 0; i < subBreedArray.length(); i++) {
            subBreeds.add(subBreedArray.getJSONObject(i).getString("name"));
        }

        // return statement included so that the starter code can compile and run.
        return subBreeds;
    }
}