package com.crio.shorturl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class XUrlImpl implements XUrl {
    private Map<String, String> longToShortMap; // To store mappings from long URL to short URL
    private Map<String, String> shortToLongMap; // To store reverse mappings from short URL to long URL
    private Map<String, Integer> hitCountMap;   // To store the hit count for each long URL

    public XUrlImpl() {
        longToShortMap = new HashMap<>();
        shortToLongMap = new HashMap<>();
        hitCountMap = new HashMap<>();
    }

    @Override
    public String registerNewUrl(String longUrl) {
        // If longUrl already has a corresponding shortUrl, return that shortUrl
        if (longToShortMap.containsKey(longUrl)) {
            return longToShortMap.get(longUrl);
        }

        // Generate a new shortUrl
        String shortUrl = generateShortUrl();

        // Store the mappings in both maps and initialize the hit count to 0
        longToShortMap.put(longUrl, shortUrl);
        shortToLongMap.put(shortUrl, longUrl);
        hitCountMap.put(longUrl, 0);

        return shortUrl;
    }

    @Override
    public String registerNewUrl(String longUrl, String shortUrl) {
        // If shortUrl is already present, return null
        if (shortToLongMap.containsKey(shortUrl)) {
            return null;
        }

        // Store the mappings in both maps and initialize the hit count to 0
        longToShortMap.put(longUrl, shortUrl);
        shortToLongMap.put(shortUrl, longUrl);
        hitCountMap.put(longUrl, 0);

        return shortUrl;
    }

    @Override
    public String getUrl(String shortUrl) {
        // If shortUrl doesn't have a corresponding longUrl, return null
        // Else, return the corresponding longUrl
        String longUrl = shortToLongMap.get(shortUrl);
        if (longUrl != null) {
            // Increment the hit count for this longUrl
            hitCountMap.put(longUrl, hitCountMap.getOrDefault(longUrl, 0) + 1);
        }
        return longUrl;
    }

    @Override
    public Integer getHitCount(String longUrl) {
        // Return the number of times the longUrl has been looked up using getUrl()
        return hitCountMap.getOrDefault(longUrl, 0);
    }

    @Override
    public String delete(String longUrl) {
        // Delete the mapping between this longUrl and its corresponding shortUrl
        // Do not zero the Hit Count for this longUrl
        String shortUrl = longToShortMap.remove(longUrl);
        if (shortUrl != null) {
            shortToLongMap.remove(shortUrl);
            return shortUrl;
        }
        return null;
    }

    private String generateShortUrl() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrlBuilder = new StringBuilder();

        // Generate a 9-character alphanumeric string for the short URL
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            int index = random.nextInt(chars.length());
            shortUrlBuilder.append(chars.charAt(index));
        }

        return "http://short.url/" + shortUrlBuilder.toString();
    }
}
