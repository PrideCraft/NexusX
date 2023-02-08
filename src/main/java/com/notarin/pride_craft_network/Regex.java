package com.notarin.pride_craft_network;

/**
 * This class contains all of the regex statements used in the project.
 */
public class Regex {

    /**
     * This regex statement uses twelve steps to validate the UUID.
     * <ol>
     *     <li>The first eight characters must be a-f, A-F, or 0-9.</li>
     *     <li>The next character must be a dash.</li>
     *     <li>The next four characters must be a-f, A-F, or 0-9.</li>
     *     <li>The next character must be a dash.</li>
     *     <li>The next four characters must be a-f, A-F, or 0-9.</li>
     *     <li>The next character must be a dash.</li>
     *     <li>The next four characters must be a-f, A-F, or 0-9.</li>
     *     <li>The next character must be a dash.</li>
     *     <li>The next four characters must be a-f, A-F, or 0-9.</li>
     *     <li>The next character must be a dash.</li>
     *     <li>The next twelve characters must be a-f, A-F, or 0-9.</li>
     *     <li>The string must end.</li>
     * </ol>
     */
    public static final String uuidValidate = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4" +
            "}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    /**
     * This regex statement uses three steps to validate the Discord ID.
     * <ol>
     *     <li>Checks the first character to see if it is a number.</li>
     *     <li>Repeat the first step 17 more times for a total of 18
     *     characters.</li>
     *     <li>The string must end.</li>
     * </ol>
     */
    public static final String discordIdValidate = "^[0-9]{18}$";
}
