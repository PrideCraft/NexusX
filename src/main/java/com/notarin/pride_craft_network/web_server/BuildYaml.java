package com.notarin.pride_craft_network.web_server;

import com.notarin.pride_craft_network.database.objects.PrideUser;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that contains methods to build YAML responses.
 */
public class BuildYaml {

    /**
     * Builds a YAML response for a unsuccessful request.
     *
     * @param message The message to be sent
     * @return The YAML response
     */
    public static String error(final String message) {
        final Map<String, Object> map = new HashMap<>();
        map.put("status", "error");
        map.put("message", message);
        final Yaml yaml = Yaml();
        return yaml.dump(map);
    }


    /**
     * Builds a YAML response for the user object.
     *
     * @param prideUser The user to be sent
     * @return The YAML response
     */
    public static String user(final PrideUser prideUser) {
        final Map<String, Object> map = new HashMap<>();
        final Map<String, Object> data = new HashMap<>();
        map.put("status", "success");
        {
            data.put("id", prideUser.id());
            data.put("minecraftUuid", prideUser.minecraftUuid());
            data.put("discordId", prideUser.discordId());
        }
        map.put("data", data);
        final Yaml yaml = Yaml();
        return yaml.dump(map);
    }

    /**
     * Builds a YAML response for the secret.
     * This is used for the /secret route.
     *
     * @param prideUser The user to return the secret for
     * @return The YAML response
     */
    public static String secret(final PrideUser prideUser) {
        final Map<String, Object> map = new HashMap<>();
        final Map<String, Object> data = new HashMap<>();
        map.put("status", "success");
        {
            data.put("id", prideUser.id());
            data.put("secret", prideUser.secret());
        }
        map.put("data", data);
        final Yaml yaml = Yaml();
        return yaml.dump(map);
    }

    @NotNull
    private static Yaml Yaml() {
        final DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(options);
    }

}
