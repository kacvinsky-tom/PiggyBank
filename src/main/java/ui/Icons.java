package ui;

import javax.swing.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Icons {

    static final Icon DELETE_ICON = createIcon("delete-icon.png");
    static final Icon EDIT_ICON = createIcon("edit-icon.png");
    static final Icon ADD_ICON = createIcon("add-icon.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    static <E extends Enum<E>> Map<E, Icon> createEnumIcons(Class<E> clazz, int width) {
        return Stream.of(clazz.getEnumConstants())
                .collect(Collectors.toUnmodifiableMap(
                        e -> e,
                        e -> createIcon(clazz.getSimpleName() + "." + e.name() + "-" + width + ".png")));
    }

    private static ImageIcon createIcon(String name) {
        return new ImageIcon(MainWindow.class.getResource(name));
    }
}
