package com.notarin.pride_craft_network.database.objects;

/**
 * A class that represents a user in the database.
 *
 * @param id The ID of the user
 * @param minecraftUuid The UUID of the user's Minecraft account
 * @param discordId The ID of the user's Discord account
 * @param secret The user's top secret password
 */
public record PrideUser(
        String id,
        String minecraftUuid,
        String discordId,
        String secret
) {
}
