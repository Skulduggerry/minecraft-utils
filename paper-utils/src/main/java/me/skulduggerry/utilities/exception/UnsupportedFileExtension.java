package me.skulduggerry.utilities.exception;

import me.skulduggerry.utilities.manager.config.type.SimpleConfigManager;

import java.nio.file.Path;

/**
 * Exception which is thrown when the plugin detects a not supported file extension,
 * e.g. {@link SimpleConfigManager#getConfigFromFile(Path)}
 *
 * @author Skulduggerry
 * @since 0.1.0
 */
public class UnsupportedFileExtension extends IllegalArgumentException {
    /**
     * Constructor.
     */
    public UnsupportedFileExtension() {
    }

    /**
     * Constructor.
     *
     * @param s Given message
     */
    public UnsupportedFileExtension(String s) {
        super(s);
    }
}
