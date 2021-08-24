import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;

public class JPHInteractor {
    private static final String USERS_URL = "https://jsonplaceholder.typicode.com/users";
    private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";

    private String getResponse(HttpURLConnection connection) throws IOException {
        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public void create(String path) throws IOException {
        URL url = new URL(USERS_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStream os = connection.getOutputStream();
        os.write(Files.readAllBytes(new File(path).toPath()));
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        System.out.println("POST response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            String response = this.getResponse(connection);
            System.out.println(response);
        } else {
            System.out.println("POST request not worked");
        }
    }

    public void update (String path, int id) throws IOException {
        URL url = new URL(USERS_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStream os = connection.getOutputStream();
        os.write(Files.readAllBytes(new File(path).toPath()));
        os.flush();
        os.close();

        int responseCode = connection.getResponseCode();
        System.out.println("PUT response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = this.getResponse(connection);
            System.out.println(response);
        } else {
            System.out.println("PUT request not worked");
        }
    }

    public void delete (int id) throws IOException {
        URL url = new URL(USERS_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);
        connection.connect();

        int responseCode = connection.getResponseCode();
        System.out.println("DELETE response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("DELETE request worked");
        } else {
            System.out.println("DELETE request not worked");
        }
    }

    public String getUsers() throws IOException {
        URL url = new URL(USERS_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = this.getResponse(connection);
            return response;
        } else {
            System.out.println("GET request not worked");
            return null;
        }
    }

    public String getUser(int id) throws IOException {
        URL url = new URL(USERS_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = this.getResponse(connection);
            return response;
        } else {
            System.out.println("GET request not worked");
            return null;
        }
    }

    public String getUser(String username) throws IOException {
        URL url = new URL(USERS_URL + "?username=" + username);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = this.getResponse(connection);
            return response;
        } else {
            System.out.println("GET request not worked");
            return null;
        }
    }

    public void getPostComments (int id) throws IOException {
        URL url = new URL(USERS_URL + "/" + id + "/posts");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GET posts response code: " + responseCode);
        String postId = "";
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = this.getResponse(connection);
            Properties[] data = new Gson().fromJson(response, Properties[].class);
            Properties lastPost = data[data.length - 1];
            postId = lastPost.getProperty("id");
        } else {
            System.out.println("GET request not worked");
            return;
        }

        url = new URL(POSTS_URL + "/" + postId + "/comments");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        responseCode = connection.getResponseCode();
        System.out.println("GET comments response code: " + responseCode);
        String comments = "";
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = this.getResponse(connection);
            comments = response;
            JsonElement je = JsonParser.parseString(comments);
            comments = new GsonBuilder().setPrettyPrinting().create().toJson(je);
            System.out.println(comments);
        } else {
            System.out.println("GET request not worked");
            return;
        }

        String fileName = "user-" + id + "-post-" + postId + "-comments.json";
        try (FileWriter writer = new FileWriter(fileName)){
            writer.write(comments);
            writer.flush();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void printRelevantTasks(int id) throws IOException {
        URL url = new URL(USERS_URL + "/" + id + "/todos");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = this.getResponse(connection);
            Properties[] data = new Gson().fromJson(response, Properties[].class);
            for (int i = 0; i < data.length; i++)
            {
                if (data[i].getProperty("completed").equals("false"))
                {
                    System.out.println(data[i]);
                }
            }
        } else {
            System.out.println("GET request not worked");
        }
    }
}
