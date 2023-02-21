package com.notarin.pride_craft_network.database.objects;

/**
 * A record that represents a role.
 *
 * @param name The name of the role
 */
public record Role(
        String name,
        Permissions permissions
) {
}
