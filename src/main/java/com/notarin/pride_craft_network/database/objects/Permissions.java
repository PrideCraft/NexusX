package com.notarin.pride_craft_network.database.objects;

/**
 * A record that represents a role's permissions. These permissions can be
 * used against any user that has a child role of the role that has these.
 * 
 * @param KICK Whether the role can kick users
 */
public record Permissions(
        boolean KICK
        ) {
}
