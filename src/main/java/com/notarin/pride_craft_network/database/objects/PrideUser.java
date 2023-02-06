package com.notarin.pride_craft_network.database.objects;

/**
 * A class that represents a user in the database.
 *
 * @param id The ID of the user
 * @param minecraftUuid
 */
public record PrideUser(
        String id,
        String minecraftUuid
) {
}
